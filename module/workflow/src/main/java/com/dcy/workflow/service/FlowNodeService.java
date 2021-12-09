package com.dcy.workflow.service;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.dcy.common.enums.FlowTypeEnum;
import com.dcy.db.base.service.BaseService;
import com.dcy.workflow.mapper.FlowNodeDeptMapper;
import com.dcy.workflow.mapper.FlowNodeMapper;
import com.dcy.workflow.mapper.FlowNodeRoleMapper;
import com.dcy.workflow.mapper.FlowNodeUserMapper;
import com.dcy.workflow.model.FlowNode;
import com.dcy.workflow.model.FlowNodeDept;
import com.dcy.workflow.model.FlowNodeRole;
import com.dcy.workflow.model.FlowNodeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 流程节点表 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2021-06-08
 */
@Service
public class FlowNodeService extends BaseService<FlowNodeMapper, FlowNode> {

    @Autowired
    private FlowNodeUserMapper flowNodeUserMapper;
    @Autowired
    private FlowNodeRoleMapper flowNodeRoleMapper;
    @Autowired
    private FlowNodeDeptMapper flowNodeDeptMapper;

    /**
     * 保存流程和审批节点关联
     *
     * @param flowNodes
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveFlowNode(List<FlowNode> flowNodes) {
        boolean success = saveOrUpdateBatch(flowNodes);
        if (success) {
            // 添加关联表
            flowNodes.forEach(flowNode -> {
                FlowTypeEnum flowType = FlowTypeEnum.getByCode(flowNode.getFlowType());
                switch (flowType) {
                    case USER:
                        flowNodeUserMapper.delete(Wrappers.<FlowNodeUser>lambdaQuery().eq(FlowNodeUser::getFlowNodeId, flowNode.getId()));
                        List<FlowNodeUser> flowNodeUsers = new ArrayList<>();
                        flowNode.getIds().forEach(s -> flowNodeUsers.add(new FlowNodeUser().setFlowNodeId(flowNode.getId()).setUserId(s)));
                        SqlHelper.executeBatch(FlowNodeUser.class, this.log, flowNodeUsers, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
                            String sqlStatement = SqlHelper.getSqlStatement(FlowNodeUserMapper.class, SqlMethod.INSERT_ONE);
                            sqlSession.insert(sqlStatement, entity);
                        });
                        break;
                    case ROLE:
                        flowNodeRoleMapper.delete(Wrappers.<FlowNodeRole>lambdaQuery().eq(FlowNodeRole::getFlowNodeId, flowNode.getId()));
                        List<FlowNodeRole> flowNodeRoles = new ArrayList<>();
                        flowNode.getIds().forEach(s -> flowNodeRoles.add(new FlowNodeRole().setFlowNodeId(flowNode.getId()).setRoleId(s)));
                        SqlHelper.executeBatch(FlowNodeRole.class, this.log, flowNodeRoles, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
                            String sqlStatement = SqlHelper.getSqlStatement(FlowNodeRoleMapper.class, SqlMethod.INSERT_ONE);
                            sqlSession.insert(sqlStatement, entity);
                        });
                        break;
                    case DEPT:
                        flowNodeDeptMapper.delete(Wrappers.<FlowNodeDept>lambdaQuery().eq(FlowNodeDept::getFlowNodeId, flowNode.getId()));
                        List<FlowNodeDept> flowNodeDepts = new ArrayList<>();
                        flowNode.getIds().forEach(s -> flowNodeDepts.add(new FlowNodeDept().setFlowNodeId(flowNode.getId()).setDeptId(s)));
                        SqlHelper.executeBatch(FlowNodeDept.class, this.log, flowNodeDepts, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
                            String sqlStatement = SqlHelper.getSqlStatement(FlowNodeDeptMapper.class, SqlMethod.INSERT_ONE);
                            sqlSession.insert(sqlStatement, entity);
                        });
                        break;
                    default:
                }
            });
        }
        return success;
    }

    /**
     * 根据flowNode 获取审批 ids
     *
     * @param flowNode
     * @return
     */
    public List<String> getIds(FlowNode flowNode) {
        List<String> ids = new ArrayList<>();
        FlowTypeEnum flowType = FlowTypeEnum.getByCode(flowNode.getFlowType());
        switch (flowType) {
            case USER:
                ids = flowNodeUserMapper.selectList(Wrappers.<FlowNodeUser>lambdaQuery().select(FlowNodeUser::getUserId).eq(FlowNodeUser::getFlowNodeId, flowNode.getId()))
                        .stream().map(FlowNodeUser::getUserId).collect(Collectors.toList());
                break;
            case ROLE:
                ids = flowNodeRoleMapper.selectList(Wrappers.<FlowNodeRole>lambdaQuery().select(FlowNodeRole::getRoleId).eq(FlowNodeRole::getFlowNodeId, flowNode.getId()))
                        .stream().map(FlowNodeRole::getRoleId).collect(Collectors.toList());
                break;
            case DEPT:
                ids = flowNodeDeptMapper.selectList(Wrappers.<FlowNodeDept>lambdaQuery().select(FlowNodeDept::getDeptId).eq(FlowNodeDept::getFlowNodeId, flowNode.getId()))
                        .stream().map(FlowNodeDept::getDeptId).collect(Collectors.toList());
                break;
            default:
        }
        return ids;
    }

}
