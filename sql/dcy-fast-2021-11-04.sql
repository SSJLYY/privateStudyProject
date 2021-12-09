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

 Date: 04/11/2021 08:43:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数id',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数键名',
  `config_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统内置',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '参数配置表';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
BEGIN;
INSERT INTO `sys_config` VALUES ('1377930337229471746', '测试配置', 'test_config_key', 'userinfo', '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门id',
  `parent_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '祖级列表',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `dept_sort` decimal(20, 0) NULL DEFAULT 0 COMMENT '显示顺序',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `dept_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '部门状态（0、正常；1、停用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` VALUES ('1', '0', '0', '总公司', 0, NULL, NULL, '0'), ('1371702174283571201', '3', '0,1,3', '财务部门', 5, NULL, NULL, '0'), ('1377935436483919873', '2', '0,1,2', '研发部门', 1, NULL, NULL, '0'), ('1377935480696078337', '2', '0,1,2', '测试部门', 3, NULL, NULL, '0'), ('1377935517522067457', '2', '0,1,2', '产品部门', 5, NULL, NULL, '0'), ('1377935562556309505', '2', '0,1,2', '运维部门', 7, NULL, NULL, '0'), ('1377935601739497474', '2', '0,1,2', '市场部门', 8, NULL, NULL, '0'), ('1377936163021258753', '3', '0,1,3', '市场部门', 3, NULL, NULL, '0'), ('2', '1', '0,1', '深圳总公司', 1, NULL, NULL, '0'), ('3', '1', '0,1', '长沙分公司', 2, '22323', '11111', '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典id',
  `parent_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级id',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `dict_sort` int(11) NULL DEFAULT 0 COMMENT '字典排序',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `dict_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据表';

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_data` VALUES ('1372709863595810818', NULL, '男', '1', 'sex', 1, NULL, 'primary', '0'), ('1372709863595810819', NULL, '女', '2', 'sex', 2, NULL, 'success', '0'), ('1372710732080979960', NULL, '正常', '0', 'dict_status', 1, NULL, 'success', '0'), ('1372710732080979962', NULL, '正常', '0', 'role_status', 1, NULL, 'success', '0'), ('1372710732080979966', NULL, '正常', '0', 'dept_status', 1, NULL, 'success', '0'), ('1372710732080979967', NULL, '正常', '0', 'post_status', 1, NULL, 'success', '0'), ('1372710732080979969', NULL, '正常', '0', 'user_status', 1, NULL, 'success', '0'), ('1372710732080979980', NULL, '正常', '0', 'res_status', 1, NULL, 'success', '0'), ('1372710783326986241', NULL, '禁用', '1', 'user_status', 2, NULL, 'danger', '0'), ('1372710783326986242', NULL, '禁用', '1', 'dict_status', 2, NULL, 'danger', '0'), ('1372710783326986243', NULL, '禁用', '1', 'role_status', 2, NULL, 'danger', '0'), ('1372710783326986245', NULL, '禁用', '1', 'dept_status', 2, NULL, 'danger', '0'), ('1372710783326986248', NULL, '禁用', '1', 'post_status', 2, NULL, 'danger', '0'), ('1372710783326986281', NULL, '禁用', '1', 'res_status', 2, NULL, 'danger', '0'), ('1372711039930310657', NULL, '管理员', '0', 'user_type', 1, NULL, NULL, '0'), ('1372711039930310673', NULL, '目录', '1', 'resource_type', 1, NULL, NULL, '0'), ('1372711076852768769', NULL, '普通用户', '1', 'user_type', 2, NULL, NULL, '0'), ('1372711076852768774', NULL, '菜单', '2', 'resource_type', 2, NULL, NULL, '0'), ('1372711076852768776', NULL, 'GET', 'GET', 'http_method', 2, NULL, NULL, '0'), ('1372711076852768777', NULL, 'POST', 'POST', 'http_method', 3, NULL, NULL, '0'), ('1376409914658590721', NULL, '全部数据权限', '1', 'data_scope', 1, NULL, 'primary', '0'), ('1376409951539105794', NULL, '自定数据权限', '2', 'data_scope', 2, NULL, 'success', '0'), ('1376409996724342786', NULL, '本部门数据权限', '3', 'data_scope', 3, NULL, 'danger', '0'), ('1376410032313012226', NULL, '本部门及以下数据权限', '4', 'data_scope', 4, NULL, 'error', '0'), ('1378139151967047682', NULL, '是', '0', 'config_type', 0, NULL, 'success', '0'), ('1378139197374582785', NULL, '否', '1', 'config_type', 1, NULL, 'danger', '0'), ('1379234283315372034', NULL, '立即执行', '1', 'misfire_policy_type', 1, NULL, 'primary', '0'), ('1379234361887268865', NULL, '执行一次', '2', 'misfire_policy_type', 2, NULL, 'success', '0'), ('1379234431768567809', NULL, '放弃执行', '3', 'misfire_policy_type', 3, NULL, 'danger', '0'), ('1379234652439289857', NULL, '允许', '0', 'concurrent_type', 0, NULL, 'primary', '0'), ('1379234729853558786', NULL, '禁止', '1', 'concurrent_type', 1, NULL, 'error', '0'), ('1379234884933754882', NULL, '正常', '0', 'job_status', 0, NULL, 'primary', '0'), ('1379234956018819074', NULL, '暂停', '1', 'job_status', 1, NULL, 'danger', '0'), ('1379240904900845569', NULL, '默认', 'DEFAULT', 'job_group', 1, NULL, 'primary', '0'), ('1379240969895780354', NULL, '系统', 'SYSTEM', 'job_group', 2, NULL, 'info', '0'), ('1402133703081861122', NULL, '用户', '1', 'flow_type', 1, NULL, 'primary', '0'), ('1402133733775777794', NULL, '角色', '2', 'flow_type', 2, NULL, 'info', '0'), ('1402133777295876097', NULL, '部门', '3', 'flow_type', 3, NULL, 'danger', '0'), ('1404971356122898433', NULL, '事假', '1', 'leave_type', 1, '', 'primary', '0'), ('1404971356122898434', NULL, '病假', '2', 'leave_type', 2, '', 'success', '0'), ('1404971356122898435', NULL, '婚假', '3', 'leave_type', 3, '', 'info', '0'), ('1404971356122898436', NULL, '丧假', '4', 'leave_type', 4, '', 'warning', '0'), ('1404971356122898437', NULL, '产假', '5', 'leave_type', 5, '', 'danger', '0'), ('1404971356122898438', NULL, '陪产假', '6', 'leave_type', 6, '', '', '0'), ('1455847279071858690', NULL, '按钮', '3', 'resource_type', 3, NULL, NULL, '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `dict_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表';

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_type` VALUES ('1372709749485576194', '性别', 'sex', '0'), ('1372710292954128385', '用户类型', 'user_type', '0'), ('1372710383374934017', '用户状态', 'user_status', '0'), ('1372710383374934018', '字典状态', 'dict_status', '0'), ('1372714386955558914', '角色状态', 'role_status', '0'), ('1372714386955558915', '部门状态', 'dept_status', '0'), ('1372714386955558916', '岗位状态', 'post_status', '0'), ('1372720409946746881', '模块类型', 'resource_type', '0'), ('1372720457925390338', '请求方式', 'http_method', '0'), ('1372742181504978945', '资源状态', 'res_status', '0'), ('1376409832819331073', '数据范围', 'data_scope', '0'), ('1378139043670118402', '参数配置系统内置选项', 'config_type', '0'), ('1379234181460893697', '任务计划执行错误策略', 'misfire_policy_type', '0'), ('1379234566711910401', '任务是否并发执行', 'concurrent_type', '0'), ('1379234800548552706', '任务状态', 'job_status', '0'), ('1379240777616302082', '任务分组', 'job_group', '0'), ('1402133618805710849', '工作流审批类型', 'flow_type', '0'), ('1404971228288901121', '请假类型', 'leave_type', '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_file_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_info`;
CREATE TABLE `sys_file_info`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件md5',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名称',
  `content_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `file_size` int(11) NOT NULL COMMENT '文件大小',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物理路径',
  `url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'url地址',
  `source` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '来源',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件表';

-- ----------------------------
-- Records of sys_file_info
-- ----------------------------
BEGIN;
INSERT INTO `sys_file_info` VALUES ('d9c81d9c4a45fc58520f14602b5c1687', '旭旭宝宝头像.jpg', 'image/jpeg', 33072, 'group1/M00/00/00/wKgDLF-skuuAEEWsAACBMET2Odo216.jpg', 'http://192.168.3.44:8888/group1/M00/00/00/wKgDLF-skuuAEEWsAACBMET2Odo216.jpg', 'fastdfs', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `job_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  PRIMARY KEY (`id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度表';

-- ----------------------------
-- Records of sys_job
-- ----------------------------
BEGIN;
INSERT INTO `sys_job` VALUES ('1', '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1'), ('100', 'test', 'DEFAULT', 'parsingXMLTask.parsingXml', '0/10 * * * * ?', '1', '1', '1'), ('2', '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1'), ('3', '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1');
COMMIT;

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务日志ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志信息',
  `job_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '异常信息',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度日志表';

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------
BEGIN;
INSERT INTO `sys_job_log` VALUES ('1379316292951748609', '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '系统默认（无参） 总共耗时：-16毫秒', '0', '', '2021-04-06 14:13:25');
COMMIT;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `oper_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人员',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求地址',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `business_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务模块名称',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法名',
  `result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '返回结果',
  `log_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '错误信息',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日志表';

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '岗位名称',
  `post_sort` decimal(10, 2) NULL DEFAULT NULL COMMENT '显示顺序',
  `post_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '岗位状态（0、正常；1、停用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '岗位表';

-- ----------------------------
-- Records of sys_post
-- ----------------------------
BEGIN;
INSERT INTO `sys_post` VALUES ('1372012058585690114', 'ceo', '董事长', 1.00, '0'), ('1372053095446319106', 'xxx', '研发经理', 2.00, '0'), ('1372053161728905217', 'test', '测试经理', 3.00, '0'), ('1372053279836311554', 'hr', '人力资源', 4.00, '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `parent_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级id',
  `parent_ids` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级ids',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题（目录名称、菜单名称、按钮名称）',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型（1、目录；2、菜单；3、按钮）',
  `permission` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识（菜单和按钮）',
  `res_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '后端url路径地址（菜单和按钮）',
  `http_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方式（GET或者POST等等）',
  `route_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由地址（目录和菜单）',
  `component_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单组件名称',
  `component_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单组件地址',
  `res_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态（0、正常；1、禁用）',
  `res_sort` decimal(10, 2) NULL DEFAULT NULL COMMENT '排序',
  `menu_ext_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '外链菜单（1：是；2：否）',
  `menu_cache_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单缓存（1：是；2：否）',
  `menu_hidden_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单和目录可见（1：是；2：否）',
  `menu_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '资源表';

-- ----------------------------
-- Records of sys_resource
-- ----------------------------
BEGIN;
INSERT INTO `sys_resource` VALUES ('1173787141281456130', '1455703600814428162', '0,1455496504403607553,1455703600814428162', '角色添加', '3', 'role:save', '/system/role/save', 'POST', NULL, NULL, NULL, '0', 1002.10, NULL, NULL, NULL, NULL, NULL), ('1173787273578192898', '1455703600814428162', '0,1455496504403607553,1455703600814428162', '角色修改', '3', 'role:update', '/system/role/update', 'POST', NULL, NULL, NULL, '0', 1002.15, NULL, NULL, NULL, NULL, NULL), ('1173787371326447617', '1455703600814428162', '0,1455496504403607553,1455703600814428162', '角色删除', '3', 'role:delete', '/system/role/delete', 'POST', NULL, NULL, NULL, '0', 1002.20, NULL, NULL, NULL, NULL, NULL), ('1173787686142517250', '1455703600814428162', '0,1455496504403607553,1455703600814428162', '授权', '3', 'role:auth:resource', '/system/role/saveAuthResource', 'POST', NULL, NULL, NULL, '0', 1002.25, NULL, NULL, NULL, NULL, NULL), ('1173793141136859137', '1455496906708664322', '0,1455496504403607553,1455496906708664322', '资源添加', '3', 'resource:add', '/system/resource/save', 'POST', NULL, NULL, NULL, '0', 1003.10, NULL, NULL, NULL, NULL, NULL), ('1173793218580488194', '1455496906708664322', '0,1455496504403607553,1455496906708664322', '资源修改', '3', 'resource:update', '/system/resource/update', 'POST', NULL, NULL, NULL, '0', 1003.14, NULL, NULL, NULL, NULL, NULL), ('1173793287136387073', '1455496906708664322', '0,1455496504403607553,1455496906708664322', '资源删除', '3', 'resource:delete', '/system/resource/delete', 'POST', NULL, NULL, NULL, '0', 1003.18, NULL, NULL, NULL, NULL, NULL), ('1208938659106000810', '1455777270018715649', '0,1455496504403607553,1455777270018715649', '字典修改', '3', 'dict:update', '/system/dict-type/update', 'POST', NULL, NULL, NULL, '0', 1006.04, NULL, NULL, NULL, NULL, NULL), ('1208938659106000811', '1455777270018715649', '0,1455496504403607553,1455777270018715649', '字典删除', '3', 'dict:delete', '/system/dict-type/delete', 'POST', NULL, NULL, NULL, '0', 1006.09, NULL, NULL, NULL, NULL, NULL), ('1208938659106000898', '1455777270018715649', '0,1455496504403607553,1455777270018715649', '字典添加', '3', 'dict:save', '/system/dict-type/save', 'POST', NULL, NULL, NULL, '0', 1006.14, NULL, NULL, NULL, NULL, NULL), ('1377928808594419714', '1455776367157030914', '0,1455496504403607553,1455776367157030914', '部门添加', '3', 'dept:save', '/system/dept/save', 'POST', NULL, NULL, NULL, '0', 1004.02, NULL, NULL, NULL, NULL, NULL), ('1377928808594419715', '1455776367157030914', '0,1455496504403607553,1455776367157030914', '部门修改', '3', 'dept:update', '/system/dept/update', 'POST', NULL, NULL, NULL, '0', 1004.04, NULL, NULL, NULL, NULL, NULL), ('1377928808594419716', '1455776367157030914', '0,1455496504403607553,1455776367157030914', '部门删除', '3', 'dept:delete', '/system/dept/delete', 'POST', NULL, NULL, NULL, '0', 1004.12, NULL, NULL, NULL, NULL, NULL), ('1377929323734642691', '1455776928103247874', '0,1455496504403607553,1455776928103247874', '岗位添加', '3', 'post:save', '/system/post/save', 'POST', NULL, NULL, NULL, '0', 1005.04, NULL, NULL, NULL, NULL, NULL), ('1377929323734642692', '1455776928103247874', '0,1455496504403607553,1455776928103247874', '岗位修改', '3', 'post:update', '/system/post/update', 'POST', NULL, NULL, NULL, '0', 1005.08, NULL, NULL, NULL, NULL, NULL), ('1377929323734642693', '1455776928103247874', '0,1455496504403607553,1455776928103247874', '岗位删除', '3', 'post:delete', '/system/post/delete', 'POST', NULL, NULL, NULL, '0', 1005.14, NULL, NULL, NULL, NULL, NULL), ('1377930633724821506', '1455777407700938753', '0,1455496504403607553,1455777407700938753', '配置添加', '3', 'config:save', '/system/config/save', 'POST', NULL, NULL, NULL, '0', 1007.02, NULL, NULL, NULL, NULL, NULL), ('1377930633724821507', '1455777407700938753', '0,1455496504403607553,1455777407700938753', '配置修改', '3', 'config:update', '/system/config/update', 'POST', NULL, NULL, NULL, '0', 1007.07, NULL, NULL, NULL, NULL, NULL), ('1377930633724821508', '1455777407700938753', '0,1455496504403607553,1455777407700938753', '配置删除', '3', 'config:delete', '/system/config/delete', 'POST', NULL, NULL, NULL, '0', 1007.14, NULL, NULL, NULL, NULL, NULL), ('1377930633724821509', '1455777407700938753', '0,1455496504403607553,1455777407700938753', '配置删除（批量）', '3', 'config:batch:delete', '/system/config/deleteBatch', 'POST', NULL, NULL, NULL, '0', 1007.16, NULL, NULL, NULL, NULL, NULL), ('1379320248616734723', '1455777761641476098', '0,1455496504403607553,1455777761641476098', '任务添加', '3', 'job:save', '/monitor/job/save', 'POST', NULL, NULL, NULL, '0', 1009.02, NULL, NULL, NULL, NULL, NULL), ('1379320248616734724', '1455777761641476098', '0,1455496504403607553,1455777761641476098', '任务修改', '3', 'job:update', '/monitor/job/update', 'POST', NULL, NULL, NULL, '0', 1009.05, NULL, NULL, NULL, NULL, NULL), ('1379320248616734725', '1455777761641476098', '0,1455496504403607553,1455777761641476098', '任务删除', '3', 'job:delete', '/monitor/job/delete', 'POST', NULL, NULL, NULL, '0', 1009.06, NULL, NULL, NULL, NULL, NULL), ('1379320248616734728', '1455777761641476098', '0,1455496504403607553,1455777761641476098', '执行一次', '3', 'job:run', '/monitor/job/run', 'POST', NULL, NULL, NULL, '0', 1009.16, NULL, NULL, NULL, NULL, NULL), ('1379320248616734729', '1455777761641476098', '0,1455496504403607553,1455777761641476098', '调度日志', '3', 'job:task:log', '/monitor/jobLog/page', 'GET', NULL, NULL, NULL, '0', 1009.20, NULL, NULL, NULL, NULL, NULL), ('1455496504403607553', '0', '0', '系统管理', '1', NULL, NULL, '', '/admin', NULL, NULL, '0', 1000.00, '2', '2', '1', 'system', '2021-11-02 19:26:23'), ('1455496906708664321', '1455496504403607553', '0,1455496504403607553', '用户管理', '2', 'user:list', '/system/user/page', 'GET', 'user-manage', 'user-manage', 'admin/user/user-manage', '0', 1001.00, '2', '2', '1', 'user', '2021-11-02 19:27:59'), ('1455496906708664322', '1455496504403607553', '0,1455496504403607553', '资源管理', '2', 'resource:list', '/system/resource/page', 'GET', 'resource-manage', 'resource-manage', 'admin/resource/resource-manage', '0', 1003.00, '2', '2', '1', 'people', '2021-11-02 19:27:59'), ('1455703600814428162', '1455496504403607553', '0,1455496504403607553', '角色管理', '2', 'role:list', '/system/role/list', 'GET', 'role-manage', 'role-manage', 'admin/role/role-manage', '0', 1002.00, '2', '2', '1', 'role', '2021-11-03 09:09:19'), ('1455710855135870977', '0', '0', '嵌套菜单', '1', NULL, NULL, NULL, 'nested', NULL, NULL, '0', 1200.00, '2', '2', '1', 'nested', '2021-11-03 09:38:08'), ('1455711672903516161', '1455710855135870977', '0,1455710855135870977', '菜单1', '1', NULL, NULL, NULL, '/menu1', NULL, NULL, '0', 500.00, '2', '2', '1', NULL, '2021-11-03 09:41:23'), ('1455711998473781250', '1455711672903516161', '0,1455710855135870977,1455711672903516161', '菜单1-1', '2', NULL, NULL, NULL, 'menu1-1', 'menu1-1', 'nested/menu1/menu1-1/index', '0', 500.00, '2', '2', '1', NULL, '2021-11-03 09:42:41'), ('1455712265919381505', '1455711672903516161', '0,1455710855135870977,1455711672903516161', '菜单1-2', '1', NULL, NULL, NULL, 'menu1-2', NULL, NULL, '0', 502.00, '2', '2', '1', NULL, '2021-11-03 09:43:45'), ('1455712369480941569', '1455712265919381505', '0,1455710855135870977,1455711672903516161,1455712265919381505', '菜单1-2-1', '2', NULL, NULL, NULL, 'menu1-2-1', 'menu1-2-1', 'nested/menu1/menu1-2/menu1-2-1/index', '0', 500.00, '2', '2', '1', NULL, '2021-11-03 09:44:09'), ('1455712458534404098', '1455712265919381505', '0,1455710855135870977,1455711672903516161,1455712265919381505', '菜单1-2-2', '2', NULL, NULL, NULL, 'menu1-2-2', 'menu1-2-2', 'nested/menu1/menu1-2/menu1-2-2/index', '0', 502.00, '2', '2', '1', NULL, '2021-11-03 09:44:31'), ('1455712586129326081', '1455711672903516161', '0,1455710855135870977,1455711672903516161', '菜单1-3', '2', NULL, NULL, NULL, 'menu1-3', 'menu1-3', 'nested/menu1/menu1-3/index', '0', 504.00, '2', '2', '1', NULL, '2021-11-03 09:45:01'), ('1455712684905185281', '1455710855135870977', '0,1455710855135870977', '菜单2', '2', NULL, NULL, NULL, '/menu2', 'menu2', 'nested/menu2/index', '0', 502.00, '2', '2', '1', NULL, '2021-11-03 09:45:25'), ('1455774928233910274', '1455496504403607553', '0,1455496504403607553', '接口文档', '2', NULL, NULL, NULL, 'http://localhost:8999/doc.html', NULL, NULL, '0', 1011.00, '1', '2', '1', 'nested', '2021-11-03 13:52:45'), ('1455776367157030914', '1455496504403607553', '0,1455496504403607553', '部门管理', '2', 'dept:list', '/system/dept/getDeptTreeList', 'GET', 'dept-manage', 'dept-manage', 'admin/dept/dept-manage', '0', 1004.00, '2', '2', '1', 'nested', '2021-11-03 13:58:28'), ('1455776928103247874', '1455496504403607553', '0,1455496504403607553', '岗位管理', '2', 'post:list', '/system/post/page', 'GET', 'post-manage', 'post-manage', 'admin/post/post-manage', '0', 1005.00, '2', '2', '1', 'nested', '2021-11-03 14:00:41'), ('1455777270018715649', '1455496504403607553', '0,1455496504403607553', '字典管理', '2', 'dict:list', '/system/dict-type/page', 'GET', 'dict-manage', 'dict-manage', 'admin/dict/dict-type-manage', '0', 1006.00, '2', '2', '1', 'nested', '2021-11-03 14:02:03'), ('1455777407700938753', '1455496504403607553', '0,1455496504403607553', '配置管理', '2', 'config:list', '/system/config/page', 'GET', 'config-manage', 'config-manage', 'admin/config/config-manage', '0', 1007.00, '2', '2', '1', 'nested', '2021-11-03 14:02:36'), ('1455777591243681793', '1455496504403607553', '0,1455496504403607553', '日志管理', '2', 'log:list', '/system/log/page', 'GET', 'log-manage', 'log-manage', 'admin/log/log-manage', '0', 1008.00, '2', '2', '1', 'nested', '2021-11-03 14:03:19'), ('1455777761641476098', '1455496504403607553', '0,1455496504403607553', '任务管理', '2', 'job:list', '/monitor/job/page', 'GET', 'job-manage', 'job-manage', 'admin/job/job-manage', '0', 1009.00, '2', '2', '1', 'nested', '2021-11-03 14:04:00'), ('1455777952633303042', '1455496504403607553', '0,1455496504403607553', '数据监控', '2', NULL, NULL, NULL, 'http://localhost:8999/druid/login.html', NULL, NULL, '0', 1010.00, '1', '2', '1', 'nested', '2021-11-03 14:04:46'), ('1455778042701787138', '0', '0', '协同管理', '1', NULL, NULL, NULL, '/flow', NULL, NULL, '0', 1100.00, '2', '2', '1', 'nested', '2021-11-03 14:05:07'), ('1455778473704271874', '1455778042701787138', '0,1455778042701787138', '模型管理', '2', 'model:list', '/flow/model/page', 'GET', 'model-manage', 'model-manage', 'flow/model/model-manage', '0', 1110.00, '2', '2', '1', 'nested', '2021-11-03 14:06:50'), ('1455778622019055618', '1455778042701787138', '0,1455778042701787138', '流程管理', '2', 'process:list', '/flow/process/page', 'GET', 'process-manage', 'process-manage', 'flow/process/process-manage', '0', 1120.00, '2', '2', '1', 'nested', '2021-11-03 14:07:25'), ('1455801448314703873', '1455496906708664321', '0,1455496504403607553,1455496906708664321', '用户添加', '3', 'user:save', '/system/user/save', 'POST', NULL, NULL, NULL, '0', 1001.20, NULL, NULL, NULL, NULL, '2021-11-03 15:38:07'), ('1455836405246464001', '1455496906708664321', '0,1455496504403607553,1455496906708664321', '用户修改', '3', 'user:update', '/system/user/update', 'POST', NULL, NULL, NULL, '0', 1001.31, NULL, NULL, NULL, NULL, '2021-11-03 17:57:02'), ('1455836405246464002', '1455496906708664321', '0,1455496504403607553,1455496906708664321', '用户删除', '3', 'user:delete', '/system/user/delete', 'POST', NULL, NULL, NULL, '0', 1001.35, NULL, NULL, NULL, NULL, '2021-11-03 17:57:02'), ('1455836405246464004', '1455496906708664321', '0,1455496504403607553,1455496906708664321', '重置密码', '3', 'user:reset:pass', '/system/user/resetPassword', 'POST', NULL, NULL, NULL, '0', 1001.45, NULL, NULL, NULL, NULL, '2021-11-03 17:57:02'), ('1455836405246464005', '1455496906708664321', '0,1455496504403607553,1455496906708664321', '授权角色', '3', 'user:auth:role', '/system/user/saveAuthRole', 'POST', NULL, NULL, NULL, '0', 1001.50, NULL, NULL, NULL, NULL, '2021-11-03 17:57:02'), ('1455836405246464010', '1455778473704271874', '0,1455778042701787138,1455778473704271874', '模型添加', '3', 'model:save', '/flow/model/create', 'POST', NULL, NULL, NULL, '0', 1110.02, NULL, NULL, NULL, NULL, '2021-11-03 15:38:07'), ('1455836405246464011', '1455778473704271874', '0,1455778042701787138,1455778473704271874', '模型修改', '3', 'model:update', '/flow/model/update', 'POST', NULL, NULL, NULL, '0', 1110.05, NULL, NULL, NULL, NULL, '2021-11-03 17:57:02'), ('1455836405246464012', '1455778473704271874', '0,1455778042701787138,1455778473704271874', '模型删除', '3', 'model:delete', '/flow/model/delete', 'POST', NULL, NULL, NULL, '0', 1110.14, NULL, NULL, NULL, NULL, '2021-11-03 17:57:02'), ('1455836405246464013', '1455778473704271874', '0,1455778042701787138,1455778473704271874', '模型部署', '3', 'model:deploy', '/flow/model/deploy', 'POST', NULL, NULL, NULL, '0', 1110.20, NULL, NULL, NULL, NULL, '2021-11-03 17:57:02'), ('1455836405246464020', '1455778622019055618', '0,1455778042701787138,1455778622019055618', '流程设置审批', '3', 'process:approve', '/flow/flow-node/getActivityListByProDefId', 'GET', NULL, NULL, NULL, '0', 1120.03, NULL, NULL, NULL, NULL, '2021-11-03 15:38:07'), ('1455836405246464021', '1455778622019055618', '0,1455778042701787138,1455778622019055618', '流程图', '3', 'process:chart', '/flow/diagram/getPicByProcessDefinitionId', 'GET', NULL, NULL, NULL, '0', 1120.06, NULL, NULL, NULL, NULL, '2021-11-03 17:57:02'), ('1455836405246464022', '1455778622019055618', '0,1455778042701787138,1455778622019055618', '激活或者挂起', '3', 'process:hang:change', '/flow/process/deletehangChange', 'POST', NULL, NULL, NULL, '0', 1120.10, NULL, NULL, NULL, NULL, '2021-11-03 17:57:02'), ('1455836405246464023', '1455778622019055618', '0,1455778042701787138,1455778622019055618', '删除', '3', 'process:delete', '/flow/process/delete', 'POST', NULL, NULL, NULL, '0', 1120.16, NULL, NULL, NULL, NULL, '2021-11-03 17:57:02'), ('1456050068800679937', '0', '0', '在线办公', '1', NULL, NULL, NULL, '/leave', NULL, NULL, '0', 1150.00, NULL, NULL, '1', 'nested', '2021-11-04 08:06:03'), ('1456050203655942145', '1456050068800679937', '0,1456050068800679937', '请假管理', '1', NULL, NULL, NULL, 'apply', NULL, NULL, '0', 1151.00, NULL, NULL, '1', 'nested', '2021-11-04 08:06:35'), ('1456050419205419009', '1456050068800679937', '0,1456050068800679937', '我的任务', '1', NULL, NULL, NULL, 'task', NULL, NULL, '0', 1152.00, NULL, NULL, '1', 'nested', '2021-11-04 08:07:27'), ('1456050688999829505', '1456050203655942145', '0,1456050068800679937,1456050203655942145', '请假申请', '2', NULL, NULL, NULL, 'leave-apply', 'leave-apply', 'office/leave/leave-apply', '0', 1151.02, '2', '2', '1', 'nested', '2021-11-04 08:08:31'), ('1456050814514376706', '1456050203655942145', '0,1456050068800679937,1456050203655942145', '我的申请', '2', NULL, NULL, NULL, 'me-apply', 'me-apply', 'office/leave/me-leave-apply', '0', 1151.05, '2', '2', '1', 'nested', '2021-11-04 08:09:01'), ('1456050986438897665', '1456050419205419009', '0,1456050068800679937,1456050419205419009', '代办任务', '2', NULL, NULL, NULL, 'run-task', 'run-task', 'office/task/run-task-manager', '0', 1152.02, '2', '2', '1', 'nested', '2021-11-04 08:09:42'), ('1456051083985825793', '1456050419205419009', '0,1456050068800679937,1456050419205419009', '已办任务', '2', NULL, NULL, NULL, 'his-task', 'his-task', 'office/task/his-task-manager', '0', 1152.05, '2', '2', '1', 'nested', '2021-11-04 08:10:05');
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色id',
  `role_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_key` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色权限字符串',
  `role_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色状态（0、正常；1、禁用）',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '数据范围（1、全部数据权限；2、自定数据权限；3、本部门数据权限；4、本部门及以下数据权限）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES ('1171709223680184321', '管理员', 'ROLE_ADMIN', '0', '1'), ('1171953892250918913', '开发组长', 'ROLE_DEVELOP', '0', '4'), ('1171953965877731330', '测试组长', 'ROLE_TEST', '0', '4'), ('1171954063797952514', '项目经理', 'ROLE_MANAGE', '1', '1'), ('1339373635128303617', 'test2', 'ROLE_AAD', '1', '2');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色id',
  `dept_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门id',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_dept` VALUES ('1339373635128303617', '1377935436483919873'), ('1339373635128303617', '1377935480696078337'), ('1339373635128303617', '1377935517522067457'), ('1339373635128303617', '1377935562556309505'), ('1339373635128303617', '1377935601739497474'), ('1339373635128303617', '2');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_res
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_res`;
CREATE TABLE `sys_role_res`  (
  `role_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色id',
  `res_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源id',
  PRIMARY KEY (`role_id`, `res_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和资源关联表';

-- ----------------------------
-- Records of sys_role_res
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_res` VALUES ('1171709223680184321', '1173787141281456130'), ('1171709223680184321', '1173787273578192898'), ('1171709223680184321', '1173787371326447617'), ('1171709223680184321', '1173787686142517250'), ('1171709223680184321', '1173793141136859137'), ('1171709223680184321', '1173793218580488194'), ('1171709223680184321', '1173793287136387073'), ('1171709223680184321', '1208938659106000810'), ('1171709223680184321', '1208938659106000811'), ('1171709223680184321', '1208938659106000898'), ('1171709223680184321', '1377928808594419714'), ('1171709223680184321', '1377928808594419715'), ('1171709223680184321', '1377928808594419716'), ('1171709223680184321', '1377929323734642691'), ('1171709223680184321', '1377929323734642692'), ('1171709223680184321', '1377929323734642693'), ('1171709223680184321', '1377930633724821506'), ('1171709223680184321', '1377930633724821507'), ('1171709223680184321', '1377930633724821508'), ('1171709223680184321', '1377930633724821509'), ('1171709223680184321', '1379320248616734723'), ('1171709223680184321', '1379320248616734724'), ('1171709223680184321', '1379320248616734725'), ('1171709223680184321', '1379320248616734728'), ('1171709223680184321', '1379320248616734729'), ('1171709223680184321', '1455496504403607553'), ('1171709223680184321', '1455496906708664321'), ('1171709223680184321', '1455496906708664322'), ('1171709223680184321', '1455703600814428162'), ('1171709223680184321', '1455710855135870977'), ('1171709223680184321', '1455711672903516161'), ('1171709223680184321', '1455711998473781250'), ('1171709223680184321', '1455712265919381505'), ('1171709223680184321', '1455712369480941569'), ('1171709223680184321', '1455712458534404098'), ('1171709223680184321', '1455712586129326081'), ('1171709223680184321', '1455712684905185281'), ('1171709223680184321', '1455774928233910274'), ('1171709223680184321', '1455776367157030914'), ('1171709223680184321', '1455776928103247874'), ('1171709223680184321', '1455777270018715649'), ('1171709223680184321', '1455777407700938753'), ('1171709223680184321', '1455777591243681793'), ('1171709223680184321', '1455777761641476098'), ('1171709223680184321', '1455777952633303042'), ('1171709223680184321', '1455778042701787138'), ('1171709223680184321', '1455778473704271874'), ('1171709223680184321', '1455778622019055618'), ('1171709223680184321', '1455801448314703873'), ('1171709223680184321', '1455836405246464001'), ('1171709223680184321', '1455836405246464002'), ('1171709223680184321', '1455836405246464004'), ('1171709223680184321', '1455836405246464005'), ('1171709223680184321', '1455836405246464010'), ('1171709223680184321', '1455836405246464011'), ('1171709223680184321', '1455836405246464012'), ('1171709223680184321', '1455836405246464013'), ('1171709223680184321', '1455836405246464020'), ('1171709223680184321', '1455836405246464021'), ('1171709223680184321', '1455836405246464022'), ('1171709223680184321', '1455836405246464023'), ('1171709223680184321', '1456050068800679937'), ('1171709223680184321', '1456050203655942145'), ('1171709223680184321', '1456050419205419009'), ('1171709223680184321', '1456050688999829505'), ('1171709223680184321', '1456050814514376706'), ('1171709223680184321', '1456050986438897665'), ('1171709223680184321', '1456051083985825793');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_info`;
CREATE TABLE `sys_user_info`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `dept_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门id',
  `username` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `user_type` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '用户类型（0、管理员；1、普通用户）',
  `email` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `phone_number` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别（0、男；1、女）',
  `avatar_path` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `user_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '帐号状态（0、正常；1、禁用）',
  `create_by` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标识',
  `remark` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表';

-- ----------------------------
-- Records of sys_user_info
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_info` VALUES ('1170896100656156674', '1', 'admin', '$2a$10$I3nz8bGJfgpKcZbUSJnc8.PQxAYXdJP6r.eLHzdsfBLsCOx8JSB76', '管理员', '0', '13223423@qq.com', '15988888885', '1', NULL, '0', NULL, '2021-04-03 09:19:01', '1170896100656156674', '2021-04-03 09:25:21', '0', '管理员'), ('1171948965562806274', '2', '1234567', '$2a$10$O2YavjwTheFjryKJSrZGv.aixXnZ1K6GgtCAxEMg5KXSC6gvCDKSy', 'dd', '1', 'dsfa', '112312', '1', NULL, '0', '1170896100656156674', NULL, '1170896100656156674', NULL, '0', 'dd'), ('1298494547157360642', '3', '121212121', '$2a$10$XryvgFQCGnLn88wV13lm.ezWYcxlPd8VsRWdYa98lqCLwLCya4Aaa', '千千万万1', '1', 'ddasfsda@qq', '154123123213', '1', NULL, '0', NULL, '2020-08-25 13:36:40', '1170896100656156674', '2021-03-17 13:49:10', '0', 'ddd'), ('1339368705646702593', '3', '12345676', '$2a$10$U1GwFyVo1.xXknTYIbU72OlHox2S8OCkzSd0pAmIZV.MYLtuMhzwe', '1116', '1', '11116', '123123412346', '2', NULL, '1', NULL, NULL, '1170896100656156674', '2021-03-17 13:49:05', '0', NULL), ('1455798474943238145', '2', 'zhangsan', '$2a$10$/b13zSPVji6vOL84LgaOyunSlxEKYLQEb1bejOo5vdboAzYME6OJ2', '张三', '1', NULL, '123123123', '1', NULL, '0', '1170896100656156674', '2021-11-03 15:26:19', '1170896100656156674', '2021-11-03 15:26:43', '0', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `post_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位id',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户与岗位关联表';

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_post` VALUES ('1298494547157360642', '1372012058585690114'), ('1298494547157360642', '1372053095446319106'), ('1298494547157360642', '1372053161728905217'), ('1298494547157360642', '1372053279836311554'), ('1339368705646702593', '1372012058585690114');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `role_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色id',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES ('1170896100656156674', '1171709223680184321'), ('1171948965562806274', '1171953965877731330');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
