/*
 Navicat Premium Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : localhost:3306
 Source Schema         : dcy-fast

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 17/06/2021 11:19:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for act_flow_node
-- ----------------------------
DROP TABLE IF EXISTS `act_flow_node`;
CREATE TABLE `act_flow_node`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `proc_def_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程定义id',
  `flow_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程节点id',
  `flow_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程节点名称',
  `flow_type` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程审批类型（1：用户；2：角色；3：部门）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_flow_node
-- ----------------------------
INSERT INTO `act_flow_node` VALUES ('1402815813451091970', 'leave:2:71909566-ce6d-11eb-82ac-005056c00008', 'Activity_1q7mri7', '校长审批', '2');
INSERT INTO `act_flow_node` VALUES ('1402815813451091971', 'leave:2:71909566-ce6d-11eb-82ac-005056c00008', 'Activity_0hgofl1', '班长审批', '2');
INSERT INTO `act_flow_node` VALUES ('1402815813463674882', 'leave:2:71909566-ce6d-11eb-82ac-005056c00008', 'Activity_0e5ryeg', '老师审批', '2');

-- ----------------------------
-- Table structure for act_flow_node_dept
-- ----------------------------
DROP TABLE IF EXISTS `act_flow_node_dept`;
CREATE TABLE `act_flow_node_dept`  (
   `flow_node_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点类型id',
   `dept_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门id',
   PRIMARY KEY (`flow_node_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务节点关联部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for act_flow_node_role
-- ----------------------------
DROP TABLE IF EXISTS `act_flow_node_role`;
CREATE TABLE `act_flow_node_role`  (
   `flow_node_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点类型id',
   `role_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色id',
   PRIMARY KEY (`flow_node_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务节点关联角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_flow_node_role
-- ----------------------------
INSERT INTO `act_flow_node_role` VALUES ('1402815813451091970', '1171709223680184321');
INSERT INTO `act_flow_node_role` VALUES ('1402815813451091971', '1171709223680184321');
INSERT INTO `act_flow_node_role` VALUES ('1402815813463674882', '1171709223680184321');

-- ----------------------------
-- Table structure for act_flow_node_user
-- ----------------------------
DROP TABLE IF EXISTS `act_flow_node_user`;
CREATE TABLE `act_flow_node_user`  (
   `flow_node_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点类型id',
   `user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
   PRIMARY KEY (`flow_node_id`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务节点关联用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oa_leave
-- ----------------------------
DROP TABLE IF EXISTS `oa_leave`;
CREATE TABLE `oa_leave`  (
     `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
     `process_instance_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程实例id',
     `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请人',
     `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请人姓名',
     `leave_date` datetime(0) NULL DEFAULT NULL COMMENT '请假申请时间',
     `begin_date` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
     `end_date` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
     `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请假类型',
     `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请假事由',
     `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态（1：未开始；2：进行中；3已完成；4：驳回）',
     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;



SET FOREIGN_KEY_CHECKS = 1;
