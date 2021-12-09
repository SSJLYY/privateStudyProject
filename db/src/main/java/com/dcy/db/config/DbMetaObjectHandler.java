package com.dcy.db.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.dcy.common.context.BaseContextHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author：dcy
 * @Description: 自动填充
 * @Date: 2019/9/6 10:44
 */
@Component
public class DbMetaObjectHandler implements MetaObjectHandler {

    private static final String CREATE_BY_FIELD = "createBy";
    private static final String CREATE_DATE_BY_FIELD = "createDate";
    private static final String UPDATE_BY_BY_FIELD = "updateBy";
    private static final String UPDATE_DATE_BY_FIELD = "updateDate";

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, CREATE_BY_FIELD, BaseContextHandler::getUserID, String.class);
        this.strictInsertFill(metaObject, CREATE_DATE_BY_FIELD, Date::new, Date.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, UPDATE_BY_BY_FIELD, BaseContextHandler::getUserID, String.class);
        this.strictUpdateFill(metaObject, UPDATE_DATE_BY_FIELD, Date::new, Date.class);
    }
}
