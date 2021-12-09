drop table if exists act_flow_node;

drop table if exists act_flow_node_dept;

drop table if exists act_flow_node_role;


drop table if exists act_flow_node_user;

/*==============================================================*/
/* Table: act_flow_node                                           */
/*==============================================================*/
create table act_flow_node
(
    id         varchar(36) not null comment '主键id',
    proc_def_id          varchar(255) comment '流程定义id',
    flow_id              varchar(255) comment '流程节点id',
    flow_name            varchar(255) comment '流程节点名称',
    flow_type            varchar(11) comment '流程审批类型（1：用户；2：角色；3：部门）',
    primary key (id)
);



/*==============================================================*/
/* Table: act_task_node_depa                                      */
/*==============================================================*/
create table act_flow_node_dept
(
    flow_node_id         varchar(36) comment '节点类型id',
    dept_id              varchar(36) comment '部门id'
);

alter table act_flow_node_dept comment '任务节点关联部门表';

/*==============================================================*/
/* Table: act_task_node_role                                      */
/*==============================================================*/
create table act_flow_node_role
(
    flow_node_id         varchar(36) comment '节点类型id',
    role_id              varchar(36) comment '角色id'
);

alter table act_flow_node_role comment '任务节点关联角色表';



/*==============================================================*/
/* Table: act_task_node_user                                      */
/*==============================================================*/
create table act_flow_node_user
(
    flow_node_id         varchar(36) comment '节点类型id',
    user_id              varchar(36) comment '用户id'
);

alter table act_flow_node_user comment '任务节点关联用户表';