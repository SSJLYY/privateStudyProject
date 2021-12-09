package com.dcy.db.base.interceptor;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.dcy.common.context.BaseContextHandler;
import com.dcy.db.base.annotation.DataColumn;
import com.dcy.db.base.annotation.DataScope;
import com.dcy.db.base.model.DataScopeRoleModel;
import com.dcy.db.base.service.IDataScopeService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author：dcy
 * @Description: 数据权限mybatis拦截器
 * @Date: 2021/9/8 14:01
 */
@Slf4j
@Component
public class DataPermissionInterceptor extends JsqlParserSupport implements InnerInterceptor {

    /**
     * {@link Executor#query(MappedStatement, Object, RowBounds, ResultHandler, CacheKey, BoundSql)} 操作前置处理
     * <p>
     * 改改sql啥的
     *
     * @param executor      Executor(可能是代理对象)
     * @param ms            MappedStatement
     * @param parameter     parameter
     * @param rowBounds     rowBounds
     * @param resultHandler resultHandler
     * @param boundSql      boundSql
     */
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        mpBs.sql(parserSingle(mpBs.sql(), ms));
    }

    /**
     * 查询
     *
     * @param select
     * @param index
     * @param sql
     * @param obj
     */
    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        MappedStatement ms = PluginUtils.realTarget(obj);
        // 先判断是不是SELECT操作 不是直接过滤
        if (!SqlCommandType.SELECT.equals(ms.getSqlCommandType())) {
            return;
        }
        final String userId = BaseContextHandler.getUserID();
        if (StrUtil.isBlank(userId)) {
            return;
        }
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        authHandler(userId, ms, plainSelect);
    }

    /**
     * 鉴权处理
     *
     * @param userId
     * @param ms
     * @param plainSelect
     */
    private void authHandler(String userId, MappedStatement ms, PlainSelect plainSelect) {
        log.info("userID  {} ", userId);
        //获取执行方法的位置
        String namespace = ms.getId();
        log.info("namespace  {} ", namespace);
        //获取mapper名称
        String className = StrUtil.subBefore(namespace, ".", true);
        log.info("className  {} ", className);
        //获取方法名
        String methodName = StrUtil.subAfter(namespace, ".", true);
        log.info("methodName  {} ", methodName);
        try {
            final Method[] methods = Class.forName(className).getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    //获取注解  来判断是不是要储存sql
                    DataScope dataScope = method.getAnnotation(DataScope.class);
                    if (dataScope != null) {
                        dataPermissionHandler(userId, dataScope, plainSelect);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            log.error("找不到文件 {}", e.toString());
        }
    }

    /**
     * 数据权限处理
     *
     * @param userId      用户id
     * @param dataScope   权限注解
     * @param plainSelect sql
     */
    private void dataPermissionHandler(String userId, DataScope dataScope, PlainSelect plainSelect) {
        if (StrUtil.equals(dataScope.type(), "dev")) {
            final DataColumn[] value = dataScope.value();
            for (DataColumn dataColumn : value) {
                if (StrUtil.equals(dataColumn.name(), "dept_id")) {
                    final IDataScopeService iDataScopeService = SpringUtil.getBean(IDataScopeService.class);
                    final Map<String, Object> allDataScopeFlagAndData = iDataScopeService.getAllDataScopeFlagAndDataByUserId(userId);
                    final Boolean allDataScope = MapUtil.getBool(allDataScopeFlagAndData, IDataScopeService.ALL_DATA_SCOPE_FLAG);
                    if (!allDataScope) {
                        // 有数据范围
                        List<DataScopeRoleModel> roleList = MapUtil.get(allDataScopeFlagAndData, IDataScopeService.ROLE_LIST, new TypeReference<List<DataScopeRoleModel>>() {
                        });
                        Set<String> dataScopeList = iDataScopeService.getDateScopeDeptIds(userId, roleList);
                        ItemsList itemsList = new ExpressionList(dataScopeList.stream().map(StringValue::new).collect(Collectors.toList()));
                        String column = getAliasColumn(dataColumn);
                        InExpression inExpression = new InExpression(new Column(column), itemsList);
                        if (null == plainSelect.getWhere()) {
                            // 不存在 where 条件
                            plainSelect.setWhere(new Parenthesis(inExpression));
                        } else {
                            // 存在 where 条件 and 处理
                            plainSelect.setWhere(new AndExpression(plainSelect.getWhere(), inExpression));
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取字段名称
     *
     * @param dataColumn
     * @return
     */
    private String getAliasColumn(DataColumn dataColumn) {
        String column = dataColumn.name();
        if (StrUtil.isNotBlank(dataColumn.alias())) {
            column = StrUtil.builder().append(dataColumn.alias()).append(StringPool.DOT).append(dataColumn.name()).toString();
        }
        return column;
    }

}
