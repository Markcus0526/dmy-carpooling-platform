/*
Navicat MySQL Data Transfer

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2015-03-26 16:09:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `abb_record`
-- ----------------------------
DROP TABLE IF EXISTS `abb_record`;
CREATE TABLE `abb_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL COMMENT '做非正常行为的用户ID',
  `abb_type` int(11) NOT NULL DEFAULT '0' COMMENT '1 : 乘客一天内X次取消订单\r\n2 : 车主迟到X分钟\r\n3 : 乘客不去\r\n4 : 车主不去',
  `desc` varchar(500) NOT NULL DEFAULT '' COMMENT '非正常行为的说明',
  `order_exec_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '非正常行为跟订单有相关的情况下，就指定那个订单的ID\r\n跟order_exec_cs表格的id有相关',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '1 : 未处理\r\n2 : 已警告\r\n3 : 已扣点\r\n4 : 加入黑名单\r\n5 : 已解除黑名单',
  `remark` varchar(500) NOT NULL DEFAULT '' COMMENT '备注',
  `auditor` bigint(20) NOT NULL DEFAULT '0' COMMENT '认定非正常行为的管理员ID\r\n跟administrator表格的id字段有相关',
  `reviewer` bigint(20) NOT NULL DEFAULT '0' COMMENT '行为处理的管理员ID.\r\n跟administrator表格的id字段有相关',
  `limit_days` int(11) NOT NULL DEFAULT '-1' COMMENT '-1 : 永远加入黑名单\r\n正数 : 加入黑名单的天数',
  `limit_days_begin` date DEFAULT NULL COMMENT '加入黑名单的开始日期。\r\n如果处理方式不是加入黑名单的话这个字段就是NULL',
  `abb_time` date DEFAULT NULL COMMENT '违约时间',
  `balance_ts` bigint(20) NOT NULL DEFAULT '0' COMMENT '如果该违约行为被处罚扣点的话，肯定有个交易记录。\r\n该字段就是那个交易记录的 id字段。\r\n默认是 0',
  `cancel_number` int(11) DEFAULT NULL COMMENT '违约次数   Null : 违约类型不是“一天内取消X条订单”',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 : 未删除    1 : 已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='非正常行为记录表格';

-- ----------------------------
-- Table structure for `activities`
-- ----------------------------
DROP TABLE IF EXISTS `activities`;
CREATE TABLE `activities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `active_code` varchar(50) NOT NULL DEFAULT '' COMMENT '活动编号',
  `limit_count` int(11) NOT NULL DEFAULT '0' COMMENT '点券限制数量',
  `syscoupon_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '跟sys_coupon表格的id字段有相关',
  `at_date` datetime NOT NULL COMMENT '活动开始日期',
  `isenabled` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否停止    0：已停止    1：未停止',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除    1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8 COMMENT='点券(代金券)活动表格';


-- ----------------------------
-- Table structure for `administrator`
-- ----------------------------
DROP TABLE IF EXISTS `administrator`;
CREATE TABLE `administrator` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `usercode` varchar(50) NOT NULL COMMENT '管理员登录名',
  `password` varchar(255) NOT NULL DEFAULT '' COMMENT '管理员密码',
  `username` varchar(255) NOT NULL COMMENT '管理员姓名',
  `unit` int(11) NOT NULL COMMENT '管理员属于单位',
  `phoneNum` char(20) NOT NULL COMMENT '管理员手机号码',
  `sex` varchar(3) NOT NULL COMMENT '性别',
  `phoneNum2` char(20) NOT NULL DEFAULT '' COMMENT '联系电话',
  `qq` char(50) NOT NULL DEFAULT '' COMMENT '管理员的QQ编号',
  `email` char(255) NOT NULL DEFAULT '' COMMENT '管理员Email',
  `level` int(11) NOT NULL DEFAULT '0' COMMENT '管理员等级',
  `note` longtext,
  `deleted` tinyint(11) NOT NULL DEFAULT '0' COMMENT '是否删除  0:未删除   1:已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COMMENT='管理员表格。或者可能说后台用户表格';

-- ----------------------------
-- Table structure for `administrator_datarole_rel_zyl`
-- ----------------------------
DROP TABLE IF EXISTS `administrator_datarole_rel_zyl`;
CREATE TABLE `administrator_datarole_rel_zyl` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `administrator_code` varchar(11) NOT NULL COMMENT '管理员登录名',
  `role_data_id` int(11) DEFAULT NULL COMMENT '数据角色表主键',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=72 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户和数据角色的关系表';


-- ----------------------------
-- Table structure for `administrator_operrole_rel_zyl`
-- ----------------------------
DROP TABLE IF EXISTS `administrator_operrole_rel_zyl`;
CREATE TABLE `administrator_operrole_rel_zyl` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `administrator_code` varchar(11) NOT NULL COMMENT '管理员登录名',
  `role_oper_id` int(11) DEFAULT NULL COMMENT '角色表主键',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=105 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户和操作角色的关系表';

-- ----------------------------
-- Table structure for `announcement`
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(256) NOT NULL DEFAULT '' COMMENT '标题',
  `content` varchar(1000) NOT NULL DEFAULT '' COMMENT '公告内容',
  `ps_date` datetime NOT NULL COMMENT '公告添加时间',
  `publisher` bigint(20) NOT NULL DEFAULT '0' COMMENT '公告发布的管理员ID',
  `ps_city` varchar(100) NOT NULL DEFAULT '' COMMENT '发布公告的城市.\r\n\r\n空 : 所有的城市\r\n非空 : 发布的城市名',
  `range` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 : 全部\r\n1 : 车主',
  `validate` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '显示公告的有效日期\r\nNULL : 永远显示',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 : 未删除   1 : 已删除\r\n\r\n删除等于撤销',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='公告表格';


-- ----------------------------
-- Table structure for `app`
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_code` varchar(100) NOT NULL DEFAULT '' COMMENT 'app识别代码',
  `app_name` varchar(50) NOT NULL DEFAULT '' COMMENT 'app名称。 比如"拼车","代驾"',
  `pack_name` varchar(100) NOT NULL DEFAULT '' COMMENT '安卓包名。可能为空',
  `bundle_id` varchar(100) NOT NULL DEFAULT '' COMMENT '苹果包名.  可能为空',
  `url_scheme` varchar(100) NOT NULL DEFAULT '' COMMENT '自定义URLScheme. 要是没有苹果版本就空',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 : 未删除    1 : 已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8 COMMENT='软件管理表格';

-- ----------------------------
-- Table structure for `app_download`
-- ----------------------------
DROP TABLE IF EXISTS `app_download`;
CREATE TABLE `app_download` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_version_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '跟app_version表格的id字段有相关',
  `userid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'app下载的用户id.\r\n有的时候可能0. 因为未注册的用户也可以下载。',
  `ip` varchar(30) NOT NULL DEFAULT '' COMMENT '下载手机端的IP地址',
  `ps_date` datetime NOT NULL COMMENT '下载日期/时间',
  `is_completed` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0 : 下载未成功      1 : 下载成功',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 : 未删除      1 : 已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='app下载记录表格';

-- ----------------------------
-- Table structure for `app_spread_unit`
-- ----------------------------
DROP TABLE IF EXISTS `app_spread_unit`;
CREATE TABLE `app_spread_unit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `unit_id` varchar(50) NOT NULL DEFAULT '' COMMENT '合作单位编码',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '合作单位名称',
  `create_date` datetime NOT NULL COMMENT '合作单位添加的日期/时间',
  `linkname` varchar(50) NOT NULL DEFAULT '' COMMENT '联系人名称',
  `linkphone` varchar(30) NOT NULL DEFAULT '' COMMENT '联系电话号码',
  `group_property` varchar(30) NOT NULL DEFAULT '' COMMENT '合作单位性质',
  `contract_no` varchar(100) NOT NULL DEFAULT '' COMMENT '合同编号',
  `fix_phone` varchar(30) NOT NULL DEFAULT '' COMMENT '固定电话',
  `email` varchar(30) NOT NULL DEFAULT '' COMMENT 'Email',
  `fax` varchar(30) NOT NULL DEFAULT '' COMMENT '传真号码',
  `group_address` varchar(100) NOT NULL DEFAULT '' COMMENT '所在地址',
  `invite_code` varchar(20) NOT NULL DEFAULT '' COMMENT '邀请编号',
  `balance_ts` bigint(20) NOT NULL DEFAULT '0' COMMENT '交易ID',
  `ratio_as_passenger_self` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '本合作单位邀请一个用户的时候给设置这个用户的信息的参数。\r\n\r\n乘客方返利值(百分比方式)',
  `integer_as_passenger_self` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '乘客方返利值(固定方式)',
  `active_as_passenger_self` tinyint(4) NOT NULL DEFAULT '0' COMMENT '乘客方返利方式\r\n\r\n0 : 百分比方式\r\n1 : 固定值方式',
  `ratio_as_driver_self` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '车主方返利(百分比方式)',
  `integer_as_driver_self` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '车主方返利(固定值方式)',
  `active_as_driver_self` tinyint(4) NOT NULL DEFAULT '0' COMMENT '车主方返利方式   0:百分比方式  1:固定值方式',
  `limit_way` tinyint(4) NOT NULL DEFAULT '3' COMMENT '合作单位邀请别的用户的时候设置给他provide_profitsharing_way的字段\r\n\r\n1：按时间\r\n2：按次数\r\n3：都考虑',
  `limit_month_as_passenger_self` int(11) NOT NULL DEFAULT '0' COMMENT '乘客方返利期间(单位 : 月)',
  `limit_month_as_driver_self` int(11) NOT NULL DEFAULT '0' COMMENT '车主方返利期间(单位 : 月)',
  `limit_count_as_passenger_self` int(11) NOT NULL DEFAULT '0' COMMENT '乘客方返利次数(单位 : 次)',
  `limit_count_as_driver_self` int(11) NOT NULL DEFAULT '0' COMMENT '车主方返利次数(单位 : 次)',
  `goods` varchar(20) NOT NULL DEFAULT '' COMMENT '合作单位所有的产品名称',
  `remark` varchar(200) NOT NULL DEFAULT '' COMMENT '备注',
  `bankcard` varchar(100) NOT NULL DEFAULT '' COMMENT '银行卡信息',
  `bankname` varchar(50) NOT NULL DEFAULT '' COMMENT '银行名称',
  `subbranch` varchar(100) NOT NULL DEFAULT '' COMMENT '开户之行名称',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 : 未删除      1 : 已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='合作单位表格';


-- ----------------------------
-- Table structure for `app_version`
-- ----------------------------
DROP TABLE IF EXISTS `app_version`;
CREATE TABLE `app_version` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_code` varchar(20) NOT NULL DEFAULT '' COMMENT '软件识别代码',
  `url` varchar(400) NOT NULL DEFAULT '' COMMENT '软件下载相对路径\r\n不包括"http://..."等等前面部分。\r\n只是包括相对的路径',
  `version` varchar(20) NOT NULL DEFAULT '' COMMENT '软件版本',
  `version_code` int(11) NOT NULL DEFAULT '1' COMMENT '软件版本代码',
  `upload_time` datetime NOT NULL COMMENT '软件上传时间',
  `upload_user` bigint(20) NOT NULL DEFAULT '0' COMMENT '上传版本的管理员ID',
  `remark` varchar(500) NOT NULL DEFAULT '' COMMENT '备注',
  `qrcode_path` varchar(500) NOT NULL DEFAULT '' COMMENT '二维码保存路径',
  `size` bigint(20) NOT NULL DEFAULT '0' COMMENT '文件的大小',
  `icon_path` varchar(500) NOT NULL DEFAULT '' COMMENT '图标的相对路径',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除   1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8 COMMENT='软件版本管理表格';


-- ----------------------------
-- Table structure for `car_type`
-- ----------------------------
DROP TABLE IF EXISTS `car_type`;
CREATE TABLE `car_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `brand` varchar(50) NOT NULL DEFAULT '' COMMENT '车品牌。 比如"一汽大众","TOYODA"',
  `car_style` varchar(50) NOT NULL DEFAULT '' COMMENT '车类型。 比如"Santana","Lavida"',
  `type` int(11) NOT NULL DEFAULT '1' COMMENT '1 : 经济型\r\n2 : 舒适性\r\n3 : 豪华型\r\n4 : 商业型',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除   1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=661 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='车类型表格';

-- ----------------------------
-- Table structure for `cds`
-- ----------------------------
DROP TABLE IF EXISTS `cds`;
CREATE TABLE `cds` (
  `titel` varchar(200) COLLATE latin1_general_ci DEFAULT NULL,
  `interpret` varchar(200) COLLATE latin1_general_ci DEFAULT NULL,
  `jahr` int(11) DEFAULT NULL,
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

-- ----------------------------
-- Table structure for `charge_logs`
-- ----------------------------
DROP TABLE IF EXISTS `charge_logs`;
CREATE TABLE `charge_logs` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(200) NOT NULL DEFAULT '' COMMENT '订单编号',
  `log_time` datetime NOT NULL COMMENT '记录时间',
  `userid` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  PRIMARY KEY (`uid`)
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `city`
-- ----------------------------
DROP TABLE IF EXISTS `city`;
CREATE TABLE `city` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(30) NOT NULL DEFAULT '' COMMENT '城市代码',
  `prov` varchar(30) NOT NULL DEFAULT '' COMMENT '省名称',
  `name` varchar(30) NOT NULL DEFAULT '' COMMENT '城市名称',
  `level` int(11) NOT NULL DEFAULT '1' COMMENT '城市等级',
  `platform` bigint(20) NOT NULL DEFAULT '0' COMMENT '暂时不用',
  `ratio` decimal(8,2) NOT NULL DEFAULT '-1.00' COMMENT '该城市内完成拼车动作以后，计算平台信息费的百分比值',
  `integer_` decimal(8,2) NOT NULL DEFAULT '-1.00' COMMENT '该城市内完成拼车动作以后，计算平台信息费的固定值',
  `active` int(11) NOT NULL DEFAULT '-1' COMMENT '计算平台信息费的方式\r\n\r\n0 ： 百分比\r\n1 ： 固定值',
  `branch` int(11) NOT NULL DEFAULT '1' COMMENT '固定值1\r\n暂时不用',
  `turn1_time` int(11) NOT NULL DEFAULT '-1' COMMENT '乘客发布订单之后，向周围的车主通知。\r\n但是不能同时发送。\r\n首先按照距离分开三个范围，向一个范围的车主通知，过一段时间之后再想下一个范围的车主通知\r\n\r\n这个字段就是通知第一个范围和第二个范围的时间间隔。',
  `turn2_time` int(11) NOT NULL DEFAULT '-1' COMMENT '通知第二个范围和第三个范围的时间间隔。',
  `total_time` int(11) NOT NULL DEFAULT '-1' COMMENT '乘客一旦发布单次订单之后能够等待的时间\\r\\n(单位:秒)',
  `driver_lock_time` int(11) NOT NULL DEFAULT '-1' COMMENT '车主一旦发枪弹单次订单之后能够等到的时间\r\n(单位:秒)',
  `range1` decimal(8,2) NOT NULL DEFAULT '-1.00' COMMENT '分开范围的第一个距离',
  `range2` decimal(8,2) NOT NULL DEFAULT '-1.00' COMMENT '分开范围的第二个距离',
  `range3` decimal(8,0) NOT NULL DEFAULT '-1' COMMENT '分开范围的第三个距离',
  `points_per_add_ratio` int(11) NOT NULL DEFAULT '-1' COMMENT '乘客端发布之后没有任何车主接单，乘客就能再高价格之后发布。\r\n\r\n该字段就是价格再高的百分比',
  `points_per_add_integer` int(11) NOT NULL DEFAULT '-1' COMMENT '价格再高的固定值',
  `points_per_add_active` int(11) NOT NULL DEFAULT '-1' COMMENT '价格再高的方式\r\n0 ： 百分比       1 ： 固定值',
  `price_limit_ratio` int(11) NOT NULL DEFAULT '-1' COMMENT '发布订单的时候能设置的最低价格(百分比)',
  `price_limit_integer` int(11) NOT NULL DEFAULT '-1' COMMENT '发布订单的时候能设置的最低价格(固定值)',
  `price_limit_active` int(11) NOT NULL DEFAULT '-1' COMMENT '发布订单的时候能设置的最低价格方式\r\n0 ： 百分比       1 ： 固定值',
  `add_price_time1` int(11) NOT NULL DEFAULT '240' COMMENT '加价1次发布时间',
  `add_price_time2` int(11) NOT NULL DEFAULT '240' COMMENT '加价2次发布时间',
  `add_price_time3` int(11) NOT NULL DEFAULT '240' COMMENT '加价3次发布时间',
  `add_price_time4` int(11) NOT NULL DEFAULT '240' COMMENT '加价4次发布时间',
  `add_price_time5` int(11) NOT NULL DEFAULT '240' COMMENT '加价5次发布时间',
  `same_price_time1` int(11) NOT NULL DEFAULT '240' COMMENT '等价1次发布时间',
  `same_price_time2` int(11) NOT NULL DEFAULT '240' COMMENT '等价2次发布时间',
  `same_price_time3` int(11) NOT NULL DEFAULT '240' COMMENT '等价3次发布时间',
  `same_price_time4` int(11) NOT NULL DEFAULT '240' COMMENT '等价4次发布时间',
  `same_price_time5` int(11) NOT NULL DEFAULT '240' COMMENT '等价5次发布时间',
  `a1` decimal(8,2) NOT NULL DEFAULT '-1.00',
  `a2` decimal(8,2) NOT NULL DEFAULT '-1.00',
  `b1` decimal(8,2) NOT NULL DEFAULT '-1.00',
  `b2` decimal(8,2) NOT NULL DEFAULT '-1.00',
  `b4` decimal(8,2) NOT NULL DEFAULT '-1.00',
  `c1` decimal(8,2) NOT NULL DEFAULT '-1.00',
  `c2` decimal(8,2) NOT NULL DEFAULT '-1.00',
  `t1` time NOT NULL DEFAULT '00:00:00',
  `t2` time NOT NULL DEFAULT '00:00:00',
  `d1` decimal(8,2) NOT NULL DEFAULT '-1.00',
  `d2` decimal(8,2) NOT NULL DEFAULT '-1.00',
  `e1` decimal(8,2) NOT NULL DEFAULT '-1.00',
  `e2` decimal(8,2) NOT NULL DEFAULT '-1.00',
  `e4` decimal(8,2) NOT NULL DEFAULT '-1.00',
  `f1` decimal(8,2) NOT NULL DEFAULT '-1.00',
  `f2` decimal(8,2) NOT NULL DEFAULT '-1.00',
  `g1` decimal(8,2) NOT NULL DEFAULT '-1.00',
  `g2` decimal(8,2) NOT NULL DEFAULT '-1.00',
  `g3` decimal(8,2) NOT NULL DEFAULT '-1.00',
  `g4` decimal(8,2) NOT NULL DEFAULT '-1.00',
  `g5` decimal(8,2) NOT NULL DEFAULT '-1.00',
  `register_syscoupon_id` varchar(50) NOT NULL DEFAULT '' COMMENT '用户注册之后自动发送给那个用户的实物点券id',
  `verified_syscoupon_id` varchar(50) NOT NULL DEFAULT '' COMMENT '个人身份认证之后自动发送给那个用户的实物点券',
  `num_order_finished` int(11) NOT NULL DEFAULT '-1' COMMENT '订单完成限制',
  `finishorders_syscoupon_id` varchar(50) NOT NULL DEFAULT '' COMMENT 'x个订单完成之后自动发送给那个用户的实物点券id',
  `num_registermonth` int(11) NOT NULL DEFAULT '-1' COMMENT '注册之后过去的期间(单位:月)',
  `registermonth_syscoupon_id` varchar(50) NOT NULL DEFAULT '' COMMENT '注册之后过X个月的时候自动发给用户的实物点券ID',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 ： 未删除       1 ： 已删除',
  `insu_fee` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '保险费',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=402 DEFAULT CHARSET=utf8 COMMENT='城市表格';


-- ----------------------------
-- Table structure for `color`
-- ----------------------------
DROP TABLE IF EXISTS `color`;
CREATE TABLE `color` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(50) NOT NULL DEFAULT '' COMMENT '颜色代码',
  `color_desc` varchar(50) NOT NULL DEFAULT '' COMMENT '颜色说明',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除    0：未删除   1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8 COMMENT='颜色表格';

-- ----------------------------
-- Table structure for `coupon_send_log`
-- ----------------------------
DROP TABLE IF EXISTS `coupon_send_log`;
CREATE TABLE `coupon_send_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `coupon_code` varchar(50) NOT NULL DEFAULT '' COMMENT '点券代码',
  `operator` bigint(20) NOT NULL DEFAULT '0' COMMENT '手动发放的情况下发放管理员的ID',
  `send_type` int(11) NOT NULL DEFAULT '1' COMMENT '发放方式\r\n1 : 注册的时候发放\r\n2 : 完成x个订单的时候自动发放\r\n3 : 个人身份认证时自动发放\r\n4 : 注册x个月之后自动发放\r\n5 : 手动发放',
  `num` int(11) NOT NULL DEFAULT '1' COMMENT '发放的用户数量',
  `sendtime` datetime NOT NULL COMMENT '发放日期/时间',
  `msg` varchar(256) NOT NULL DEFAULT '' COMMENT '发放关联的通知内容',
  `remark` varchar(256) NOT NULL DEFAULT '' COMMENT '备注',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除     1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8 COMMENT='点券发放记录';


-- ----------------------------
-- Table structure for `cs_lotteryactivity_syscoupon_rel`
-- ----------------------------
DROP TABLE IF EXISTS `cs_lotteryactivity_syscoupon_rel`;
CREATE TABLE `cs_lotteryactivity_syscoupon_rel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_id` bigint(20) NOT NULL COMMENT '活动主键',
  `sys_coupon_id` bigint(20) NOT NULL COMMENT '点卷主键',
  `used_num` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `cs_lotteryactivity_syscoupon_rel_ibfk_1` FOREIGN KEY (`activity_id`) REFERENCES `cs_lottery_activity` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='活动点卷关系表';


-- ----------------------------
-- Table structure for `cs_lotteryactivity_syscoupon_rule`
-- ----------------------------
DROP TABLE IF EXISTS `cs_lotteryactivity_syscoupon_rule`;
CREATE TABLE `cs_lotteryactivity_syscoupon_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `end_num` int(11) NOT NULL,
  `start_num` int(11) NOT NULL,
  `activity_coupon_id` bigint(20) NOT NULL COMMENT '活动主键',
  PRIMARY KEY (`id`),
  KEY `cs_lotteryactivity_syscoupon_rule_ibfk_1` (`activity_coupon_id`),
  CONSTRAINT `cs_lotteryactivity_syscoupon_rule_ibfk_1` FOREIGN KEY (`activity_coupon_id`) REFERENCES `cs_lotteryactivity_syscoupon_rel` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='活动点卷规则表';

-- ----------------------------
-- Table structure for `cs_lottery_activity`
-- ----------------------------
DROP TABLE IF EXISTS `cs_lottery_activity`;
CREATE TABLE `cs_lottery_activity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '活动名称',
  `start_time` datetime NOT NULL COMMENT '活动开始时间',
  `end_time` datetime NOT NULL COMMENT '发布日期',
  `share_time` int(11) DEFAULT '0' COMMENT '活动分享时间-小时',
  `share_people` int(11) DEFAULT '0' COMMENT '活动分享人数',
  `prize_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '奖品设置类型0：点卷。1：第三方奖品',
  `lottery_name` varchar(50) DEFAULT '' COMMENT '彩票名称',
  `join_people` int(11) DEFAULT '0' COMMENT '参加人数',
  `give_gift_num` int(11) DEFAULT '0' COMMENT '发放奖品数',
  `status` tinyint(4) DEFAULT '0' COMMENT '活动状态默认0：活动中。1：停止',
  `userd_people` int(11) DEFAULT '0' COMMENT '活动已分享人数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='活动表';

-- ----------------------------
-- Table structure for `cs_lottery_activity_faqi_user`
-- ----------------------------
DROP TABLE IF EXISTS `cs_lottery_activity_faqi_user`;
CREATE TABLE `cs_lottery_activity_faqi_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `faqi_userid` bigint(20) NOT NULL COMMENT '发起活动用户的主键',
  `activity_id` bigint(20) NOT NULL COMMENT '活动主键ID',
  `type` tinyint(4) NOT NULL COMMENT '类型 0:app 1:威信',
  `status` tinyint(4) NOT NULL COMMENT '子活动状态。0：活动中，1停止',
  `add_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `faqi_userid` (`faqi_userid`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `cs_lottery_activity_faqi_user_ibfk_2` FOREIGN KEY (`activity_id`) REFERENCES `cs_lottery_activity` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COMMENT='活动用户获奖表';

-- ----------------------------
-- Table structure for `cs_lottery_activity_huojiang_user`
-- ----------------------------
DROP TABLE IF EXISTS `cs_lottery_activity_huojiang_user`;
CREATE TABLE `cs_lottery_activity_huojiang_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `faqi_id` bigint(20) NOT NULL COMMENT '发起活动主键',
  `huojiang_userid` bigint(20) NOT NULL COMMENT '获奖用户的主键',
  `style` tinyint(4) NOT NULL COMMENT '类型 0:奖券 1:',
  `gift_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COMMENT='活动用户获奖表';


-- ----------------------------
-- Table structure for `cs_lottery_activity_tip`
-- ----------------------------
DROP TABLE IF EXISTS `cs_lottery_activity_tip`;
CREATE TABLE `cs_lottery_activity_tip` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `tip_name` varchar(255) DEFAULT NULL,
  `is_deleted` int(11) DEFAULT '0',
  `type` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `cs_lottery_activity_tip_user`
-- ----------------------------
DROP TABLE IF EXISTS `cs_lottery_activity_tip_user`;
CREATE TABLE `cs_lottery_activity_tip_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activity_id` bigint(20) NOT NULL COMMENT '活动主键',
  `faqi_id` bigint(20) NOT NULL COMMENT '用户主键',
  `huojiang_id` bigint(20) NOT NULL COMMENT '用户主键',
  `type` tinyint(4) DEFAULT '0' COMMENT '渠道：0：威信。1：APP',
  `add_time` date DEFAULT NULL COMMENT '获奖日期',
  PRIMARY KEY (`id`),
  KEY `activity_id` (`activity_id`),
  KEY `faqi_id` (`faqi_id`),
  CONSTRAINT `cs_lottery_activity_tip_user_ibfk_1` FOREIGN KEY (`activity_id`) REFERENCES `cs_lottery_activity` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `cs_lottery_activity_tip_user_ibfk_2` FOREIGN KEY (`faqi_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='活动彩票用户表';


-- ----------------------------
-- Table structure for `daemon_logs`
-- ----------------------------
DROP TABLE IF EXISTS `daemon_logs`;
CREATE TABLE `daemon_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `oper_no` text NOT NULL COMMENT '操作编号',
  `table_names` text NOT NULL COMMENT '操作表格名称',
  `oper_contents` text NOT NULL COMMENT '操作内容',
  `remark` text NOT NULL COMMENT '备注',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除   0：未删除   1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of daemon_logs
-- ----------------------------

-- ----------------------------
-- Table structure for `driver_online_stat`
-- ----------------------------
DROP TABLE IF EXISTS `driver_online_stat`;
CREATE TABLE `driver_online_stat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL DEFAULT '0' COMMENT '车主的ID（user表格的ID）',
  `count` int(11) NOT NULL DEFAULT '0' COMMENT '上线心跳数',
  `hour` int(11) NOT NULL DEFAULT '0' COMMENT '统计的时间(点)',
  `ps_date` date NOT NULL COMMENT '统计的日期',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除   0：未删除  1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1989 DEFAULT CHARSET=utf8 COMMENT='车主上线记录';


-- ----------------------------
-- Table structure for `environment`
-- ----------------------------
DROP TABLE IF EXISTS `environment`;
CREATE TABLE `environment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(100) NOT NULL DEFAULT '' COMMENT '变量代码',
  `value` varchar(500) NOT NULL DEFAULT '' COMMENT '变量值',
  `remark` varchar(500) NOT NULL DEFAULT '' COMMENT '备注',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除   0：未删除   1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8 COMMENT='全局的环境变量表格';


-- ----------------------------
-- Table structure for `evaluation_cs`
-- ----------------------------
DROP TABLE IF EXISTS `evaluation_cs`;
CREATE TABLE `evaluation_cs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `from_userid` bigint(20) NOT NULL DEFAULT '0' COMMENT '评价人',
  `to_userid` bigint(20) NOT NULL DEFAULT '0' COMMENT '被评价的用户编号',
  `level` int(11) NOT NULL DEFAULT '1' COMMENT '1 : 好评    2 : 中评    3 : 差评',
  `msg` varchar(256) NOT NULL DEFAULT '' COMMENT '评价内容',
  `ps_date` datetime NOT NULL COMMENT '评价日期/时间',
  `usertype` int(11) NOT NULL DEFAULT '1' COMMENT '评价的用户类型    1 : 乘客     2 : 车主',
  `order_cs_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '相关订单编号(ID)',
  `blocked` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否屏蔽   0：未屏蔽    1：已屏蔽',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除      1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=179 DEFAULT CHARSET=utf8 COMMENT='订单评价表格';


-- ----------------------------
-- Table structure for `feedback`
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `title` varchar(256) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '标题。  暂时空',
  `content` varchar(500) COLLATE utf8_unicode_ci NOT NULL COMMENT '内容',
  `application` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '来源名称',
  `ps_date` datetime NOT NULL COMMENT '反馈日期/时间',
  `sys_msg` varchar(500) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '处理意见',
  `date_sys` date DEFAULT NULL COMMENT '处理日期/时间',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除      1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='用户意见反馈表格';

-- ----------------------------
-- Table structure for `freeze_points`
-- ----------------------------
DROP TABLE IF EXISTS `freeze_points`;
CREATE TABLE `freeze_points` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL DEFAULT '0' COMMENT '相关用户ID',
  `source` tinyint(4) NOT NULL DEFAULT '1' COMMENT '来源\r\n1：Main软件\r\n2：拼车软件\r\n3：其他',
  `adminid` bigint(20) NOT NULL DEFAULT '0' COMMENT '冻结的管理员。 如果是自动冻结，解冻的话该字段就是0',
  `balance` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '冻结或者解冻的金额',
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '状态\r\n0：冻结\r\n1：解冻\r\n2：用掉了',
  `freeze_ts_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '冻结的交易记录id',
  `release_ts_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '解冻的交易记录ID',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除     1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=828 DEFAULT CHARSET=utf8 COMMENT='绿点冻结，解冻记录表格';


-- ----------------------------
-- Table structure for `func_point_zyl`
-- ----------------------------
DROP TABLE IF EXISTS `func_point_zyl`;
CREATE TABLE `func_point_zyl` (
  `id` int(11) NOT NULL DEFAULT '0',
  `code` varchar(11) NOT NULL,
  `func_name` varchar(255) DEFAULT NULL,
  `url` char(255) DEFAULT NULL,
  `parent` varchar(11) DEFAULT NULL,
  `type_` varchar(50) DEFAULT NULL COMMENT '1菜单组；2菜单项；3页面链接；4页面按钮',
  `icon` varchar(50) DEFAULT NULL,
  `rank` int(10) DEFAULT NULL COMMENT '菜单排序',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='访问功能点';


-- ----------------------------
-- Table structure for `global_params`
-- ----------------------------
DROP TABLE IF EXISTS `global_params`;
CREATE TABLE `global_params` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(30) NOT NULL DEFAULT '' COMMENT 'reserved',
  `name` varchar(30) NOT NULL DEFAULT '' COMMENT 'reserved',
  `level` int(11) NOT NULL DEFAULT '0' COMMENT 'reserved',
  `platform` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reserved',
  `ratio` decimal(8,2) NOT NULL DEFAULT '0.00',
  `integer_` decimal(8,2) NOT NULL DEFAULT '0.00',
  `active` int(11) NOT NULL DEFAULT '0',
  `branch` int(11) NOT NULL DEFAULT '1',
  `turn1_time` int(11) NOT NULL DEFAULT '30',
  `turn2_time` int(11) NOT NULL DEFAULT '30',
  `total_time` int(11) NOT NULL DEFAULT '240',
  `driver_lock_time` int(11) NOT NULL DEFAULT '240',
  `range1` decimal(8,2) NOT NULL DEFAULT '1.00',
  `range2` decimal(8,2) NOT NULL DEFAULT '3.00',
  `range3` decimal(8,2) NOT NULL DEFAULT '5.00',
  `points_per_add_ratio` int(11) NOT NULL DEFAULT '3',
  `points_per_add_integer` int(11) NOT NULL DEFAULT '10',
  `points_per_add_active` int(11) NOT NULL DEFAULT '0',
  `price_limit_ratio` int(11) NOT NULL DEFAULT '50',
  `price_limit_integer` int(11) NOT NULL DEFAULT '10',
  `price_limit_active` int(11) NOT NULL DEFAULT '0',
  `add_price_time1` int(11) NOT NULL DEFAULT '240',
  `add_price_time2` int(11) NOT NULL DEFAULT '240',
  `add_price_time3` int(11) NOT NULL DEFAULT '240',
  `add_price_time4` int(11) NOT NULL DEFAULT '240',
  `add_price_time5` int(11) NOT NULL DEFAULT '240',
  `same_price_time1` int(11) NOT NULL DEFAULT '240',
  `same_price_time2` int(11) NOT NULL DEFAULT '240',
  `same_price_time3` int(11) NOT NULL DEFAULT '240',
  `same_price_time4` int(11) NOT NULL DEFAULT '240',
  `same_price_time5` int(11) NOT NULL DEFAULT '240',
  `a1` decimal(8,2) NOT NULL DEFAULT '0.00',
  `a2` decimal(8,2) NOT NULL DEFAULT '0.00',
  `b1` decimal(8,2) NOT NULL DEFAULT '0.00',
  `b2` decimal(8,2) NOT NULL DEFAULT '0.00',
  `b4` decimal(8,2) NOT NULL DEFAULT '0.00',
  `c1` decimal(8,2) NOT NULL DEFAULT '0.00',
  `c2` decimal(8,2) NOT NULL DEFAULT '0.00',
  `t1` time NOT NULL DEFAULT '00:00:00',
  `t2` time NOT NULL DEFAULT '00:00:00',
  `d1` decimal(8,2) NOT NULL DEFAULT '0.00',
  `d2` decimal(8,2) NOT NULL DEFAULT '0.00',
  `e1` decimal(8,2) NOT NULL DEFAULT '0.00',
  `e2` decimal(8,2) NOT NULL DEFAULT '0.00',
  `e4` decimal(8,2) NOT NULL DEFAULT '0.00',
  `f1` decimal(8,2) NOT NULL DEFAULT '0.00',
  `f2` decimal(8,2) NOT NULL DEFAULT '0.00',
  `g1` decimal(8,2) NOT NULL DEFAULT '0.00',
  `g2` decimal(8,2) NOT NULL DEFAULT '0.00',
  `g3` decimal(8,2) NOT NULL DEFAULT '0.00',
  `g4` decimal(8,2) NOT NULL DEFAULT '0.00',
  `g5` decimal(8,2) NOT NULL DEFAULT '0.00',
  `register_syscoupon_id` varchar(50) NOT NULL DEFAULT '0',
  `verified_syscoupon_id` varchar(50) NOT NULL DEFAULT '1',
  `num_order_finished` int(11) NOT NULL DEFAULT '0',
  `finishorders_syscoupon_id` varchar(50) NOT NULL DEFAULT '',
  `num_registermonth` int(11) NOT NULL DEFAULT '0',
  `registermonth_syscoupon_id` varchar(50) NOT NULL DEFAULT '',
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  `single_sms_price` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '单条短信费用',
  `insu_fee` decimal(8,2) NOT NULL DEFAULT '0.50' COMMENT '保险费',
  `waittime_when_charging` int(4) DEFAULT '60',
  `polling_time` int(4) DEFAULT NULL COMMENT '车主端程序在后台运行和锁屏时的轮询时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='全局的分成关联参数。\r\n每个字段就跟city表格一样';


-- ----------------------------
-- Table structure for `group_`
-- ----------------------------
DROP TABLE IF EXISTS `group_`;
CREATE TABLE `group_` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `groupid` varchar(50) NOT NULL DEFAULT '' COMMENT '集团客户代码',
  `group_name` varchar(256) NOT NULL DEFAULT '' COMMENT '名称',
  `create_date` datetime NOT NULL COMMENT '创造日期',
  `linkname` varchar(50) NOT NULL DEFAULT '' COMMENT '联系人名称',
  `linkphone` varchar(50) NOT NULL DEFAULT '' COMMENT '联系人手机号码',
  `group_property` varchar(50) NOT NULL DEFAULT '' COMMENT '集团客户性质',
  `contract_no` varchar(100) NOT NULL DEFAULT '' COMMENT '合同编号',
  `fix_phone` varchar(50) NOT NULL DEFAULT '' COMMENT '固定电话',
  `email` varchar(50) NOT NULL DEFAULT '' COMMENT 'Email',
  `fax` varchar(50) NOT NULL DEFAULT '' COMMENT '传真号码',
  `group_address` varchar(100) NOT NULL DEFAULT '' COMMENT '所在城市',
  `sign_time` datetime NOT NULL COMMENT '签合同的时间',
  `invitecode_self` varchar(50) NOT NULL DEFAULT '' COMMENT '邀请码',
  `balance_ts` bigint(20) NOT NULL DEFAULT '0' COMMENT '最新交易记录ID',
  `ratio_as_passenger_self` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '乘客方返利(百分比)',
  `integer_as_passenger_self` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '乘客方返利(固定值)',
  `active_as_passenger_self` int(11) NOT NULL DEFAULT '0' COMMENT '乘客方返利方式   0 : 百分比   1：固定值',
  `ratio_as_driver_self` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '车主方返利(百分比)',
  `integer_as_driver_self` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '车主方返利（固定值）',
  `active_as_driver_self` int(11) NOT NULL DEFAULT '0' COMMENT '车主方返利方式   0：百分比   1：固定值',
  `limit_way` tinyint(4) NOT NULL DEFAULT '3' COMMENT '集团客户邀请用户的时候设置给他provide_profitsharing_way的字段\r\n\r\n1：按时间\r\n2：按次数\r\n3：都考虑',
  `limit_month_as_passenger_self` int(11) NOT NULL DEFAULT '0' COMMENT '乘客方返利有效期间(单位：月）',
  `limit_month_as_driver_self` int(11) NOT NULL DEFAULT '0' COMMENT '车主方返利有效期间(单位：月）',
  `limit_count_as_passenger_self` int(11) NOT NULL DEFAULT '0' COMMENT '乘客方返利有效次数',
  `limit_count_as_driver_self` int(11) NOT NULL DEFAULT '0' COMMENT '车主方返利有效次数',
  `remark` varchar(500) NOT NULL DEFAULT '' COMMENT '备注',
  `bankcard` varchar(100) NOT NULL DEFAULT '' COMMENT '银行卡信息',
  `bankname` varchar(50) NOT NULL DEFAULT '' COMMENT '银行名称',
  `subbranch` varchar(100) NOT NULL DEFAULT '' COMMENT '开户之行名称',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除    1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='集团客户表格';


-- ----------------------------
-- Table structure for `group_association`
-- ----------------------------
DROP TABLE IF EXISTS `group_association`;
CREATE TABLE `group_association` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ga_code` varchar(50) NOT NULL DEFAULT '' COMMENT '集团联盟代码',
  `ga_name` varchar(50) NOT NULL DEFAULT '' COMMENT '名称',
  `linkname` varchar(50) NOT NULL DEFAULT '' COMMENT '联系人名称',
  `linkphone` varchar(50) NOT NULL DEFAULT '' COMMENT '联系电话',
  `group_property` varchar(50) NOT NULL DEFAULT '' COMMENT '性质',
  `contract_no` varchar(50) NOT NULL DEFAULT '' COMMENT '合同编号',
  `fix_phone` varchar(50) NOT NULL DEFAULT '' COMMENT '固定电话',
  `email` varchar(50) NOT NULL DEFAULT '' COMMENT 'Email',
  `fax` varchar(50) NOT NULL DEFAULT '' COMMENT '传真号码',
  `group_address` varchar(256) NOT NULL DEFAULT '' COMMENT '所在地址',
  `sign_time` datetime NOT NULL COMMENT '签合同时间',
  `desc_` varchar(256) NOT NULL DEFAULT '' COMMENT '备注',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除   0：未删除   1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='集团联盟表格';


-- ----------------------------
-- Table structure for `group_association_details`
-- ----------------------------
DROP TABLE IF EXISTS `group_association_details`;
CREATE TABLE `group_association_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `associd` bigint(20) NOT NULL DEFAULT '0' COMMENT '集团联盟ID',
  `groupid` bigint(20) NOT NULL DEFAULT '0' COMMENT '集团客户ID',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除   0：未删除         1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='集团联盟和集团客户的关系表格';

-- ----------------------------
-- Table structure for `group_details`
-- ----------------------------
DROP TABLE IF EXISTS `group_details`;
CREATE TABLE `group_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `groupid` varchar(50) NOT NULL DEFAULT '' COMMENT '集团客户ID',
  `userid` bigint(20) NOT NULL DEFAULT '0' COMMENT '手机端用户ID',
  `group_type` int(11) NOT NULL DEFAULT '0' COMMENT '集团客户类型(比如：出租车公司)',
  `application` varchar(20) NOT NULL DEFAULT '' COMMENT '来源',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除   1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COMMENT='集团客户和手机端用户的关系表格';

-- ----------------------------
-- Table structure for `insurance`
-- ----------------------------
DROP TABLE IF EXISTS `insurance`;
CREATE TABLE `insurance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `REQUEST_TYPE` varchar(20) DEFAULT NULL COMMENT '请求类型',
  `USER` varchar(20) DEFAULT NULL COMMENT '用户名',
  `PASSWORD` varchar(30) DEFAULT NULL COMMENT '密码',
  `REQUSET_TIME` datetime DEFAULT NULL COMMENT '交易时间，格式：yyyy-mm-dd hh:MM:ss',
  `REQUEST_TRANSNO` varchar(30) DEFAULT NULL COMMENT '交易流水号，合作方自行定义（不可重复）',
  `APPL_NO` varchar(30) DEFAULT NULL COMMENT '投保单号,默认不填，由接口返回',
  `APPL_TIME` datetime DEFAULT NULL COMMENT '投保时间，格式：yyyy-mm-dd hh:MM:ss',
  `PROD_TYPE` varchar(20) DEFAULT NULL COMMENT '承保产品类型',
  `PROD_CODE` varchar(20) DEFAULT NULL COMMENT '承保产品代码',
  `EFFECT_TIME` datetime DEFAULT NULL COMMENT '保单生效时间',
  `INSEXPR_DATE` datetime DEFAULT NULL COMMENT '保单终止时间',
  `SALE_CHANNEL` varchar(20) DEFAULT NULL COMMENT '销售渠道',
  `BUS_BRANCH` varchar(25) DEFAULT NULL COMMENT '业务受理机构',
  `COUNTER_CODE` varchar(25) DEFAULT NULL COMMENT '销售网点代码',
  `TOTAL_AMOUNT` decimal(12,2) DEFAULT NULL COMMENT '合计保额',
  `TOTAL_PREMINUM` decimal(12,2) DEFAULT NULL COMMENT '合计保费',
  `APPL_NAME` varchar(25) DEFAULT NULL COMMENT '投保人姓名',
  `APPL_ID` bigint(20) DEFAULT '0' COMMENT '投保人id，即车主id',
  `APPL_SEX` varchar(20) DEFAULT NULL COMMENT '投保人性别',
  `APPL_BIRTHDAY` date DEFAULT NULL COMMENT '投保人出生日期',
  `CERT_TYPE` varchar(20) DEFAULT NULL COMMENT '证件类型',
  `CERT_CODE` varchar(20) DEFAULT NULL COMMENT '证件号码',
  `ISD_COUNT` int(4) DEFAULT NULL COMMENT '被保险人合计人数',
  `ISD_SERIAL` int(4) DEFAULT NULL COMMENT '被保人序号。同一投保单内被保人的唯一标识，从1开始，不得重复',
  `ISD_TYPE` varchar(25) DEFAULT NULL COMMENT '被保人类型',
  `APPL_RELATION` varchar(25) DEFAULT NULL COMMENT '被保人与投保人关系',
  `ISD_NAME` varchar(25) DEFAULT NULL COMMENT '被保险人姓名',
  `ISD_ID` bigint(20) DEFAULT '0' COMMENT '被保险人id',
  `ISD_SEX` varchar(19) DEFAULT NULL COMMENT '被保险人性别',
  `ISD_BIRTHDAY` date DEFAULT NULL COMMENT '被保险人出生日期',
  `ISD_CERT_TYPE` varchar(25) DEFAULT NULL COMMENT '被保险人证件类型',
  `ISD_CERT_CODE` varchar(25) DEFAULT NULL COMMENT '被保险人证件号码',
  `BENEF_ORDER` int(4) DEFAULT NULL COMMENT '受益顺序',
  `INSU_STATUS` int(4) DEFAULT '0' COMMENT '保单状态，0有效，1失效',
  `ORDER_EXEC_ID` bigint(20) DEFAULT NULL COMMENT '执行订单id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=607 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `insurance_dtd`
-- ----------------------------
DROP TABLE IF EXISTS `insurance_dtd`;
CREATE TABLE `insurance_dtd` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `insu_id` bigint(20) NOT NULL COMMENT '保单表主键，对应一条保单表记录',
  `appl_no` varchar(20) NOT NULL COMMENT '投保单号，唯一，用户可以通过投保单号去保险公司网站查询保单信息',
  `oper_type` int(4) NOT NULL COMMENT '操作类型：0，投保；1，撤保；',
  `oper_time` datetime NOT NULL,
  `insu_sum` decimal(8,2) NOT NULL COMMENT '保险金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=847 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `insu_error_msg`
-- ----------------------------
DROP TABLE IF EXISTS `insu_error_msg`;
CREATE TABLE `insu_error_msg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `insu_id` bigint(20) DEFAULT NULL COMMENT '保单表主键',
  `appl_no` varchar(255) DEFAULT NULL COMMENT '投保单号',
  `error_msg` varchar(255) DEFAULT NULL COMMENT '错误信息',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=60546 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `menu`
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `url` char(255) DEFAULT NULL,
  `menucode` int(11) DEFAULT NULL,
  `submenu` int(11) DEFAULT NULL,
  `parent` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=371 DEFAULT CHARSET=utf8 COMMENT='管理后台左边的菜单表格';


-- ----------------------------
-- Table structure for `midpoints`
-- ----------------------------
DROP TABLE IF EXISTS `midpoints`;
CREATE TABLE `midpoints` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_type` int(11) NOT NULL DEFAULT '0' COMMENT '订单类型    0：单词拼车   1：上下班拼车',
  `orderid` bigint(20) NOT NULL DEFAULT '0' COMMENT '相关订单ID',
  `point_index` int(11) NOT NULL DEFAULT '0' COMMENT '中途点编号  0~3',
  `lat` decimal(10,6) NOT NULL DEFAULT '0.000000' COMMENT '纬度',
  `lng` decimal(10,6) NOT NULL DEFAULT '0.000000' COMMENT '经度',
  `addr` varchar(256) NOT NULL DEFAULT '' COMMENT '地址',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除   0：未删除   1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=389 DEFAULT CHARSET=utf8 COMMENT='中途点管理表';


-- ----------------------------
-- Table structure for `notify_order`
-- ----------------------------
DROP TABLE IF EXISTS `notify_order`;
CREATE TABLE `notify_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `notification_type` int(11) NOT NULL DEFAULT '1' COMMENT '1：车主枪弹啦\r\n2：车主到达啦\r\n3：服务已经结束啦\r\n4：支付完成啦。一次性的执行订单完成啦\r\n5：其他',
  `ps_date` datetime NOT NULL COMMENT '通知时间',
  `order_cs_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '相关订单ID',
  `receiver` bigint(20) NOT NULL DEFAULT '0' COMMENT '针对的用户ID',
  `msg` varchar(500) NOT NULL DEFAULT '' COMMENT '消息内容',
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '标题',
  `has_read` varchar(255) NOT NULL DEFAULT '0' COMMENT '0：手机端还没读取    1：手机端已经读取',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除     1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=852 DEFAULT CHARSET=utf8 COMMENT='订单消息表格';


-- ----------------------------
-- Table structure for `notify_person`
-- ----------------------------
DROP TABLE IF EXISTS `notify_person`;
CREATE TABLE `notify_person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) NOT NULL DEFAULT '' COMMENT '标题',
  `msg` varchar(500) NOT NULL DEFAULT '' COMMENT '内容',
  `msg_type` int(11) NOT NULL DEFAULT '0' COMMENT '暂时不用',
  `ps_date` datetime NOT NULL COMMENT '上传日期',
  `couponid` bigint(20) NOT NULL DEFAULT '0' COMMENT '点券ID   如果通知跟点券无关就0',
  `receiver` bigint(20) NOT NULL DEFAULT '0' COMMENT '针对的用户ID',
  `has_read` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：手机端还没读取     1：手机端已经读取',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除    1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2195 DEFAULT CHARSET=utf8 COMMENT='私人通知表格';


-- ----------------------------
-- Table structure for `oper_log`
-- ----------------------------
DROP TABLE IF EXISTS `oper_log`;
CREATE TABLE `oper_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `operuser_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作的管理员',
  `oper_time` datetime NOT NULL COMMENT '操作时间',
  `t_name` char(250) NOT NULL DEFAULT '' COMMENT '变更的表格名称。如果有好几个表格的话用“；”来分开',
  `action_url` char(250) NOT NULL DEFAULT '' COMMENT '操作的Action地址',
  `desc` varchar(1000) NOT NULL COMMENT '操作内容',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除  0：为身处  1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='后台操作记录表格';


-- ----------------------------
-- Table structure for `order_exec_cs`
-- ----------------------------
DROP TABLE IF EXISTS `order_exec_cs`;
CREATE TABLE `order_exec_cs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_type` int(11) NOT NULL DEFAULT '1' COMMENT '1,临时订单，2上下班拆分成的临时订单，3上下班订单，4长途订单',
  `passenger` bigint(20) NOT NULL DEFAULT '0' COMMENT '乘客ID',
  `driver` bigint(20) NOT NULL DEFAULT '0' COMMENT '车主ID',
  `from_` varchar(256) NOT NULL DEFAULT '' COMMENT '出发地点',
  `to_` varchar(256) NOT NULL DEFAULT '' COMMENT '目的地点',
  `average_price_platform` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '当时的平台平均价',
  `price` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '价格',
  `pre_time` datetime NOT NULL COMMENT '出发时间',
  `remark` varchar(256) NOT NULL DEFAULT '' COMMENT '备注',
  `cr_date` datetime NOT NULL COMMENT '订单添加时间',
  `ti_accept_order` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '接单时间',
  `begin_exec_time` datetime DEFAULT NULL COMMENT '开始执行时间(车主前往出发地)',
  `driverarrival_time` datetime DEFAULT NULL COMMENT '标记到达时间',
  `beginservice_time` datetime DEFAULT NULL COMMENT '上车验票，开始服务时间',
  `stopservice_time` datetime DEFAULT NULL COMMENT '结束服务时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付完成时间',
  `pass_cancel_time` datetime DEFAULT NULL COMMENT '乘客取消时间',
  `driver_cancel_time` datetime DEFAULT NULL COMMENT '车主取消时间',
  `city_from` varchar(50) NOT NULL DEFAULT '' COMMENT '出发城市',
  `city_to` varchar(50) NOT NULL DEFAULT '' COMMENT '目的城市',
  `has_evaluation_passenger` tinyint(4) DEFAULT '0' COMMENT '0：乘客还没评价     1：乘客已经评价',
  `has_evaluation_driver` tinyint(4) DEFAULT '0' COMMENT '0：车主还没评价     1：车主已经评价',
  `password` varchar(20) DEFAULT '' COMMENT '电子车票密码',
  `begin_lat` decimal(10,6) DEFAULT '0.000000' COMMENT '出发纬度',
  `begin_lng` decimal(10,6) DEFAULT '0.000000' COMMENT '出发经度',
  `end_lat` decimal(10,6) DEFAULT '0.000000' COMMENT '目的纬度',
  `end_lng` decimal(10,6) DEFAULT NULL COMMENT '目的经度',
  `freeze_points` decimal(8,2) DEFAULT '0.00' COMMENT '冻结的绿点',
  `total_distance` decimal(8,2) DEFAULT '0.00' COMMENT '总里程',
  `order_city` varchar(50) DEFAULT '' COMMENT '订单发布城市',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '订单执行状态\\r\\n\\r\\n2：成交状态\\r\\n3：开始执行状态\\r\\n4：车主到达状态\\r\\n5：乘客上车状态\\r\\n6：执行结束状态\\r\\n7：结算完成状态\\r\\n8：取消状态',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未评价    1：已评价',
  `insu_id_driver` bigint(20) NOT NULL DEFAULT '0' COMMENT '车主保单id',
  `insu_id_pass` bigint(20) DEFAULT NULL COMMENT '乘客保单id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=755 DEFAULT CHARSET=utf8 COMMENT='执行订单表格';

-- ----------------------------
-- Table structure for `order_exec_modify_log`
-- ----------------------------
DROP TABLE IF EXISTS `order_exec_modify_log`;
CREATE TABLE `order_exec_modify_log` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `modifier` int(10) DEFAULT NULL COMMENT '修改人',
  `md_time` datetime DEFAULT NULL COMMENT '修改时间',
  `md_column` varchar(50) DEFAULT NULL COMMENT '修改字段',
  `old_value` varchar(50) DEFAULT NULL COMMENT '原内容',
  `new_value` varchar(50) DEFAULT NULL COMMENT '新内容',
  `order_exec_id` int(11) NOT NULL COMMENT '订单执行表主键',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除       1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='执行订单修改记录表';

-- ----------------------------
-- Table structure for `order_longdistance_details`
-- ----------------------------
DROP TABLE IF EXISTS `order_longdistance_details`;
CREATE TABLE `order_longdistance_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_num` varchar(50) NOT NULL DEFAULT '' COMMENT '长途订单代码',
  `pre_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '出发时间',
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '每个座的价格',
  `start_city` varchar(100) NOT NULL DEFAULT '' COMMENT '出发城市',
  `end_city` varchar(100) NOT NULL DEFAULT '' COMMENT '目的城市',
  `start_addr` varchar(100) NOT NULL DEFAULT '' COMMENT '出发地址',
  `end_addr` varchar(100) NOT NULL DEFAULT '' COMMENT '目的地址',
  `start_lat` decimal(10,6) NOT NULL DEFAULT '0.000000' COMMENT '出发地点纬度',
  `start_lng` decimal(10,6) NOT NULL DEFAULT '0.000000' COMMENT '出发地点经度',
  `end_lat` decimal(10,6) NOT NULL DEFAULT '0.000000' COMMENT '目的地点纬度',
  `end_lng` decimal(10,6) NOT NULL DEFAULT '0.000000' COMMENT '目的地点经度',
  `seat_num` int(11) NOT NULL DEFAULT '4' COMMENT '座位数',
  `publisher` bigint(20) NOT NULL DEFAULT '0' COMMENT '发布人ID',
  `ps_time` datetime NOT NULL COMMENT '出发时间',
  `ti_accept_order` datetime DEFAULT NULL COMMENT '第一个乘客接单的时间',
  `begin_exec_time` datetime DEFAULT NULL COMMENT '开始执行时间',
  `driverarrival_time` datetime DEFAULT NULL COMMENT '车主到达时间',
  `beginservice_time` datetime DEFAULT NULL COMMENT '开始服务时间。乘客上车时间',
  `endservice_time` datetime DEFAULT NULL COMMENT '结束服务时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `pass_cancel_time` datetime DEFAULT NULL COMMENT '乘客取消时间',
  `driver_cancel_time` datetime DEFAULT NULL COMMENT '车主取消时间',
  `occupied_num` int(11) NOT NULL DEFAULT '0' COMMENT '抢座数',
  `remark` varchar(200) NOT NULL DEFAULT '' COMMENT '备注',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '订单状态\r\n\r\n1 : 发布状态\r\n2 : 成交状态\r\n3 : 开始执行状态\r\n4 : 车主到达状态\r\n5 : 乘客上车状态\r\n6 : 执行结束\r\n7 : 已结算\r\n8 : 已关闭',
  `source` tinyint(11) NOT NULL DEFAULT '1' COMMENT '来源  1：手机端   2：用户网站',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除        1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=270 DEFAULT CHARSET=utf8 COMMENT='长途订单表格';

-- ----------------------------
-- Table structure for `order_longdistance_users_cs`
-- ----------------------------
DROP TABLE IF EXISTS `order_longdistance_users_cs`;
CREATE TABLE `order_longdistance_users_cs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `orderdriverlongdistance_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '长途订单ID',
  `userid` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户编号（乘客编号）',
  `order_exec_cs_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '订单执行表ID',
  `seat_num` int(11) NOT NULL DEFAULT '1' COMMENT '座位数',
  `ps_date` datetime NOT NULL COMMENT '上传日期/时间',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除       1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=270 DEFAULT CHARSET=utf8 COMMENT='长途订单枪弹的乘客表格';


-- ----------------------------
-- Table structure for `order_modify_log`
-- ----------------------------
DROP TABLE IF EXISTS `order_modify_log`;
CREATE TABLE `order_modify_log` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `modifier` int(10) DEFAULT NULL COMMENT '修改人',
  `md_time` datetime DEFAULT NULL COMMENT '修改时间',
  `md_column` varchar(50) DEFAULT NULL COMMENT '修改字段',
  `old_value` varchar(50) DEFAULT NULL COMMENT '原内容',
  `new_value` varchar(50) DEFAULT NULL COMMENT '新内容',
  `order_id` int(11) NOT NULL COMMENT '订单表主键',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除       1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='订单修改记录表';


-- ----------------------------
-- Table structure for `order_onoffduty_details`
-- ----------------------------
DROP TABLE IF EXISTS `order_onoffduty_details`;
CREATE TABLE `order_onoffduty_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_num` varchar(50) NOT NULL DEFAULT '' COMMENT '上下班订单编号',
  `alldays_beaccepted` int(11) NOT NULL DEFAULT '1' COMMENT '是否全部枪弹了\r\n0 : 不\r\n1 : 是',
  `from_date` date NOT NULL COMMENT '开始日期',
  `price` decimal(10,2) NOT NULL COMMENT '价格（单次）',
  `start_lat` decimal(10,6) NOT NULL DEFAULT '0.000000' COMMENT '出发地点纬度',
  `start_lng` decimal(10,6) NOT NULL DEFAULT '0.000000' COMMENT '出发地点经度',
  `start_addr` varchar(100) NOT NULL DEFAULT '' COMMENT '出发地址',
  `end_lat` decimal(10,6) NOT NULL DEFAULT '0.000000' COMMENT '目的纬度',
  `end_lng` decimal(10,6) NOT NULL DEFAULT '0.000000' COMMENT '目的经度',
  `end_addr` varchar(100) NOT NULL DEFAULT '' COMMENT '目的地址',
  `order_city` varchar(100) NOT NULL DEFAULT '' COMMENT '订单发布城市',
  `leftdays` varchar(255) NOT NULL COMMENT '剩下的日期',
  `pre_time` datetime NOT NULL COMMENT '预定出发时间',
  `publisher` bigint(20) NOT NULL DEFAULT '0' COMMENT '发布的用户ID，就是车主ID',
  `publish_date` datetime NOT NULL COMMENT '发布时间',
  `ti_accept_order` datetime DEFAULT NULL COMMENT '接单时间   成交时间',
  `endservice_time` datetime DEFAULT NULL COMMENT '终止服务时间',
  `reqcarstyle` int(11) NOT NULL DEFAULT '1' COMMENT '1 : 经济型\r\n2 : 舒适型\r\n3 : 豪华型\r\n4 : 商务型',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '订单状态\r\n\r\n1 : 发布状态\r\n2 : 成交状态\r\n3 : 结束服务状态\r\n4 : 暂停出行',
  `remark` varchar(200) NOT NULL DEFAULT '' COMMENT '备注',
  `source` tinyint(4) NOT NULL DEFAULT '1' COMMENT '来源   1：手机端   2：用户网站',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除       1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='上下边整体订单表格';

-- ----------------------------
-- Records of order_onoffduty_details
-- ----------------------------

-- ----------------------------
-- Table structure for `order_onoffduty_divide`
-- ----------------------------
DROP TABLE IF EXISTS `order_onoffduty_divide`;
CREATE TABLE `order_onoffduty_divide` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `which_days` varchar(50) NOT NULL DEFAULT '' COMMENT '接单日.用斑点来分开',
  `orderdetails_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '上下班整体订单ID',
  `driver_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '车主ID',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除       1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='上下班订单接单的车主表格';

-- ----------------------------
-- Records of order_onoffduty_divide
-- ----------------------------
INSERT INTO `order_onoffduty_divide` VALUES ('1', '1,2,3,4,5,6,0,', '1', '6', '0');

-- ----------------------------
-- Table structure for `order_onoffduty_exec_details`
-- ----------------------------
DROP TABLE IF EXISTS `order_onoffduty_exec_details`;
CREATE TABLE `order_onoffduty_exec_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `onoffduty_divide_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'order_onoffduty_divide表格的ID',
  `order_cs_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '执行记录表格',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除       1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_onoffduty_exec_details
-- ----------------------------
INSERT INTO `order_onoffduty_exec_details` VALUES ('1', '1', '1', '0');

-- ----------------------------
-- Table structure for `order_onoffduty_grab`
-- ----------------------------
DROP TABLE IF EXISTS `order_onoffduty_grab`;
CREATE TABLE `order_onoffduty_grab` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `grab_time` datetime NOT NULL COMMENT '接单时间',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未处理   1：成功   2：失败',
  `driverid` bigint(20) NOT NULL DEFAULT '0' COMMENT '接单的车主ID',
  `days` varchar(50) NOT NULL DEFAULT '' COMMENT '接受的出行日',
  `order_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '相关订单ID',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除   0：未删除   1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='上下班订单接单记录表';

-- ----------------------------
-- Records of order_onoffduty_grab
-- ----------------------------

-- ----------------------------
-- Table structure for `order_temp_details`
-- ----------------------------
DROP TABLE IF EXISTS `order_temp_details`;
CREATE TABLE `order_temp_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_num` varchar(50) NOT NULL DEFAULT '' COMMENT '订单编号',
  `seats_num` int(11) NOT NULL DEFAULT '4' COMMENT '座位数',
  `is_broadcast_stop` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否停止通知',
  `turn_num` int(11) NOT NULL DEFAULT '0' COMMENT '枪弹的车主在哪个范围内的编号',
  `ps_date` datetime NOT NULL COMMENT '记录时间    发布时间',
  `accept_time` datetime DEFAULT NULL COMMENT '成交时间',
  `begin_exec_time` datetime DEFAULT NULL COMMENT '开始服务时间',
  `driverarrival_time` datetime DEFAULT NULL COMMENT '车主到达时间',
  `beginservice_time` datetime DEFAULT NULL COMMENT '开始服务时间',
  `endservice_time` datetime DEFAULT NULL COMMENT '结束服务时间',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `pass_cancel_time` datetime DEFAULT NULL COMMENT '乘客取消时间',
  `driver_cancel_time` datetime DEFAULT NULL COMMENT '车主取消时间',
  `start_lat` decimal(10,6) NOT NULL DEFAULT '0.000000' COMMENT '起点纬度',
  `start_lng` decimal(10,6) NOT NULL DEFAULT '0.000000' COMMENT '起点经度',
  `start_addr` varchar(100) NOT NULL COMMENT '起点地址',
  `end_lat` decimal(10,6) NOT NULL DEFAULT '0.000000' COMMENT '终点纬度',
  `end_lng` decimal(10,6) NOT NULL DEFAULT '0.000000' COMMENT '终点经度',
  `end_addr` varchar(100) NOT NULL DEFAULT '' COMMENT '终点地址',
  `mileage` double(255,1) DEFAULT NULL COMMENT '订单总里程',
  `order_city` varchar(100) NOT NULL DEFAULT '',
  `pre_time` datetime NOT NULL COMMENT '出发时间',
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '价格',
  `wait_time` int(11) NOT NULL DEFAULT '240' COMMENT '发布订单之后等待的时间',
  `order_cs_id` bigint(20) DEFAULT '0' COMMENT '执行记录编号',
  `is_from_onoffdutyorder` int(11) NOT NULL DEFAULT '0' COMMENT '是否上下班订单的临时订单',
  `orderonoffduty_id` bigint(20) DEFAULT '0' COMMENT '上下班订单整体订单ID。  可能为0',
  `publisher` bigint(20) NOT NULL DEFAULT '0' COMMENT '发布用户ID',
  `reqcarstyle` int(11) NOT NULL DEFAULT '1' COMMENT '1 : 경제형(经济型)\r\n2 : 편안형(舒适型)\r\n3 : 호화형(豪华型)\r\n4 : 사무형(商务型)',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '订单状态\r\n\r\n1 : 发布状态\r\n2 : 成交状态\r\n3 : 开支执行状态\r\n4 : 车主到达状态\r\n5 : 乘客上车状态\r\n6 : 执行结束\r\n7 : 已结算\r\n8 : 已关闭',
  `remark` varchar(200) NOT NULL DEFAULT '' COMMENT '备注',
  `source` tinyint(4) NOT NULL DEFAULT '1' COMMENT '来源    1：手机端    2：用户网站',
  `has_clickedchargingbtn` int(4) DEFAULT '0',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除       1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4718 DEFAULT CHARSET=utf8 COMMENT='单次（临时）订单表格';

-- ----------------------------
-- Table structure for `order_temp_grab`
-- ----------------------------
DROP TABLE IF EXISTS `order_temp_grab`;
CREATE TABLE `order_temp_grab` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `grab_time` datetime NOT NULL COMMENT '记录时间',
  `distance` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '当前距离',
  `status` tinyint(11) NOT NULL DEFAULT '0' COMMENT '0：未处理   1：成功    2：失败',
  `driverid` bigint(20) NOT NULL DEFAULT '0' COMMENT '接单的车主ID',
  `order_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '相关订单编号. order_temp_details.id',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除     1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=669 DEFAULT CHARSET=utf8 COMMENT='关于通知发布单次订单，用该表格，记录接单的车主信息';


-- ----------------------------
-- Table structure for `order_temp_notify`
-- ----------------------------
DROP TABLE IF EXISTS `order_temp_notify`;
CREATE TABLE `order_temp_notify` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `turn` bigint(20) NOT NULL DEFAULT '0' COMMENT '范围编号    0,1,2,3 其中0指播报过程当中，车主上线的情况',
  `order_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '相关订单编号. order_temp_details.id',
  `push_time` datetime NOT NULL COMMENT '创建时间',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除     1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7970 DEFAULT CHARSET=utf8 COMMENT='关于通知发布单次订单，用该表格，记录需要通知的车主。只是为了开发方便。临时性的表格。\r\n没有其他用处';


-- ----------------------------
-- Table structure for `pma_bookmark`
-- ----------------------------
DROP TABLE IF EXISTS `pma_bookmark`;
CREATE TABLE `pma_bookmark` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dbase` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `user` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `label` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `query` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Bookmarks';

-- ----------------------------
-- Records of pma_bookmark
-- ----------------------------

-- ----------------------------
-- Table structure for `pma_column_info`
-- ----------------------------
DROP TABLE IF EXISTS `pma_column_info`;
CREATE TABLE `pma_column_info` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `db_name` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `table_name` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `column_name` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `comment` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `mimetype` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `transformation` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `transformation_options` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `db_name` (`db_name`,`table_name`,`column_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Column information for phpMyAdmin';

-- ----------------------------
-- Records of pma_column_info
-- ----------------------------

-- ----------------------------
-- Table structure for `pma_designer_coords`
-- ----------------------------
DROP TABLE IF EXISTS `pma_designer_coords`;
CREATE TABLE `pma_designer_coords` (
  `db_name` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `table_name` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `x` int(11) DEFAULT NULL,
  `y` int(11) DEFAULT NULL,
  `v` tinyint(4) DEFAULT NULL,
  `h` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`db_name`,`table_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Table coordinates for Designer';

-- ----------------------------
-- Records of pma_designer_coords
-- ----------------------------

-- ----------------------------
-- Table structure for `pma_history`
-- ----------------------------
DROP TABLE IF EXISTS `pma_history`;
CREATE TABLE `pma_history` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `db` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `table` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `timevalue` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `sqlquery` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  KEY `username` (`username`,`db`,`table`,`timevalue`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='SQL history for phpMyAdmin';

-- ----------------------------
-- Records of pma_history
-- ----------------------------

-- ----------------------------
-- Table structure for `pma_pdf_pages`
-- ----------------------------
DROP TABLE IF EXISTS `pma_pdf_pages`;
CREATE TABLE `pma_pdf_pages` (
  `db_name` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `page_nr` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `page_descr` varchar(50) CHARACTER SET utf8 NOT NULL DEFAULT '',
  PRIMARY KEY (`page_nr`),
  KEY `db_name` (`db_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='PDF relation pages for phpMyAdmin';

-- ----------------------------
-- Records of pma_pdf_pages
-- ----------------------------

-- ----------------------------
-- Table structure for `pma_recent`
-- ----------------------------
DROP TABLE IF EXISTS `pma_recent`;
CREATE TABLE `pma_recent` (
  `username` varchar(64) COLLATE utf8_bin NOT NULL,
  `tables` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Recently accessed tables';

-- ----------------------------
-- Table structure for `pma_relation`
-- ----------------------------
DROP TABLE IF EXISTS `pma_relation`;
CREATE TABLE `pma_relation` (
  `master_db` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `master_table` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `master_field` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `foreign_db` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `foreign_table` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `foreign_field` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`master_db`,`master_table`,`master_field`),
  KEY `foreign_field` (`foreign_db`,`foreign_table`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Relation table';

-- ----------------------------
-- Records of pma_relation
-- ----------------------------

-- ----------------------------
-- Table structure for `pma_table_coords`
-- ----------------------------
DROP TABLE IF EXISTS `pma_table_coords`;
CREATE TABLE `pma_table_coords` (
  `db_name` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `table_name` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `pdf_page_number` int(11) NOT NULL DEFAULT '0',
  `x` float unsigned NOT NULL DEFAULT '0',
  `y` float unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`db_name`,`table_name`,`pdf_page_number`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Table coordinates for phpMyAdmin PDF output';

-- ----------------------------
-- Records of pma_table_coords
-- ----------------------------

-- ----------------------------
-- Table structure for `pma_table_info`
-- ----------------------------
DROP TABLE IF EXISTS `pma_table_info`;
CREATE TABLE `pma_table_info` (
  `db_name` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `table_name` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `display_field` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`db_name`,`table_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Table information for phpMyAdmin';

-- ----------------------------
-- Records of pma_table_info
-- ----------------------------

-- ----------------------------
-- Table structure for `pma_table_uiprefs`
-- ----------------------------
DROP TABLE IF EXISTS `pma_table_uiprefs`;
CREATE TABLE `pma_table_uiprefs` (
  `username` varchar(64) COLLATE utf8_bin NOT NULL,
  `db_name` varchar(64) COLLATE utf8_bin NOT NULL,
  `table_name` varchar(64) COLLATE utf8_bin NOT NULL,
  `prefs` text COLLATE utf8_bin NOT NULL,
  `last_update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`username`,`db_name`,`table_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Tables'' UI preferences';

-- ----------------------------
-- Records of pma_table_uiprefs
-- ----------------------------

-- ----------------------------
-- Table structure for `pma_tracking`
-- ----------------------------
DROP TABLE IF EXISTS `pma_tracking`;
CREATE TABLE `pma_tracking` (
  `db_name` varchar(64) COLLATE utf8_bin NOT NULL,
  `table_name` varchar(64) COLLATE utf8_bin NOT NULL,
  `version` int(10) unsigned NOT NULL,
  `date_created` datetime NOT NULL,
  `date_updated` datetime NOT NULL,
  `schema_snapshot` text COLLATE utf8_bin NOT NULL,
  `schema_sql` text COLLATE utf8_bin,
  `data_sql` longtext COLLATE utf8_bin,
  `tracking` set('UPDATE','REPLACE','INSERT','DELETE','TRUNCATE','CREATE DATABASE','ALTER DATABASE','DROP DATABASE','CREATE TABLE','ALTER TABLE','RENAME TABLE','DROP TABLE','CREATE INDEX','DROP INDEX','CREATE VIEW','ALTER VIEW','DROP VIEW') COLLATE utf8_bin DEFAULT NULL,
  `tracking_active` int(1) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`db_name`,`table_name`,`version`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT COMMENT='Database changes tracking for phpMyAdmin';

-- ----------------------------
-- Records of pma_tracking
-- ----------------------------

-- ----------------------------
-- Table structure for `pma_userconfig`
-- ----------------------------
DROP TABLE IF EXISTS `pma_userconfig`;
CREATE TABLE `pma_userconfig` (
  `username` varchar(64) COLLATE utf8_bin NOT NULL,
  `timevalue` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `config_data` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='User preferences storage for phpMyAdmin';

-- ----------------------------
-- Records of pma_userconfig
-- ----------------------------
INSERT INTO `pma_userconfig` VALUES ('root', '2014-08-16 11:15:23', 0x7B22636F6C6C6174696F6E5F636F6E6E656374696F6E223A22757466386D62345F67656E6572616C5F6369227D);

-- ----------------------------
-- Table structure for `profitsharing_config_person`
-- ----------------------------
DROP TABLE IF EXISTS `profitsharing_config_person`;
CREATE TABLE `profitsharing_config_person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ratio_as_passenger` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '乘客方返利值(百分比)',
  `integer_as_passenger` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '乘客方返利值(固定值)',
  `active_as_passenger` int(11) NOT NULL DEFAULT '0' COMMENT '乘客方返利方式   0：百分比   1：固定值',
  `ratio_as_driver` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '车主方返利值(百分比)',
  `integer_as_driver` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '车主方返利值(固定值)',
  `active_as_driver` int(8) NOT NULL DEFAULT '0' COMMENT '车主方返利方式',
  `limit_way` tinyint(4) NOT NULL DEFAULT '3' COMMENT '用户邀请别的用户的时候设置给他provide_profitsharing_way的字段\r\n\r\n1：按时间\r\n2：按次数\r\n3：都考虑',
  `limit_month_as_passenger` int(11) NOT NULL DEFAULT '0' COMMENT '乘客方返利有效期间(单位：月)',
  `limit_month_as_driver` int(11) NOT NULL DEFAULT '0' COMMENT '车主方返利有效期间(单位：月)',
  `limit_count_as_passenger` int(11) NOT NULL DEFAULT '0' COMMENT '乘客方返利有效次数',
  `limit_count_as_driver` int(11) NOT NULL DEFAULT '0' COMMENT '车主方返利有效次数',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除      1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='全局的分成参数设置表格';

-- ----------------------------
-- Records of profitsharing_config_person
-- ----------------------------
INSERT INTO `profitsharing_config_person` VALUES ('1', '1.00', '11.00', '2', '2.00', '22.00', '2', '3', '12', '12', '111', '111', '0');

-- ----------------------------
-- Table structure for `profitsharing_tree`
-- ----------------------------
DROP TABLE IF EXISTS `profitsharing_tree`;
CREATE TABLE `profitsharing_tree` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_cs_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '相关订单编号',
  `branch_no` varchar(256) NOT NULL DEFAULT '' COMMENT '分成编号',
  `node1` varchar(20) NOT NULL DEFAULT '' COMMENT '分成给方。user,group_,app_spread_unit表格的ID',
  `node2` varchar(20) NOT NULL DEFAULT '' COMMENT '分成收方。user,group_,app_spread_unit表格的ID',
  `ps_value` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '分成比率或者固定值',
  `ps_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '分成方式\r\n0：百分比      1：固定值',
  `value` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '实际的分成值',
  `node1_source` varchar(20) NOT NULL DEFAULT '' COMMENT 'node1的类型\r\n1：个人客户\r\n2：集团客户\r\n3：合作单位',
  `node2_source` varchar(20) NOT NULL DEFAULT '' COMMENT 'node2的类型\r\n1：个人客户\r\n2：集团客户\r\n3：合作单位',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除\r\n0：未删除      1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=507 DEFAULT CHARSET=utf8 COMMENT='分成详细记录表格';


-- ----------------------------
-- Table structure for `read_announcements`
-- ----------------------------
DROP TABLE IF EXISTS `read_announcements`;
CREATE TABLE `read_announcements` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `announceid` bigint(20) NOT NULL DEFAULT '0' COMMENT '公告ID',
  `readtime` datetime NOT NULL COMMENT '读取时间',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除     0:未删除   1:已删除',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=307 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `req_client_account`
-- ----------------------------
DROP TABLE IF EXISTS `req_client_account`;
CREATE TABLE `req_client_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `req_num` varchar(50) NOT NULL DEFAULT '' COMMENT '邀请编号',
  `account` varchar(50) NOT NULL DEFAULT '' COMMENT '用户银行卡账户.\r\n提现的时候利用\r\n',
  `account_type` int(11) NOT NULL DEFAULT '1' COMMENT '针对用户类型\r\n1 : 个人客户\r\n2 : 集团客户\r\n3 : 合作单位',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '针对的客户ID.\r\n按照account_type字段的内容，\r\n可能成为用户的id，集团客户的id，合作单位的id',
  `sum` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '操作金额',
  `oper` int(11) NOT NULL DEFAULT '0' COMMENT '操作类型\r\n1：转出\r\n2：转入',
  `req_date` datetime NOT NULL COMMENT '邀请日期/时间',
  `audit_date` datetime DEFAULT NULL COMMENT '审核日期/时间',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态\r\n\r\n1：待审核\r\n2：已审核\r\n3：已驳回\r\n4：已撤销\r\n5：处理中',
  `oper_type` int(11) NOT NULL DEFAULT '1' COMMENT '操作类型\r\n1 : 提现\r\n2 : 充值\r\n',
  `channel` varchar(50) NOT NULL DEFAULT '' COMMENT '渠道. 提现的情况下就是银行名称。 充值的情况系可能是支付宝或者什么的',
  `req_cause` varchar(50) NOT NULL DEFAULT '' COMMENT '申请原因',
  `order_cs_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '顶管订单编号',
  `reject_cause` varchar(200) NOT NULL DEFAULT '' COMMENT '驳回理由',
  `ts_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '交易ID',
  `req_user` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作申请的管理员ID。可能为0',
  `auditor` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作审核的管理员ID。可能为0',
  `req_source` int(11) NOT NULL DEFAULT '1' COMMENT '1：手机端\r\n2：用户网站\r\n3：管理后台',
  `remark` varchar(100) NOT NULL DEFAULT '' COMMENT '详细说明。 备注',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除      1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8 COMMENT='手机端的充值提现这个表格里记录。';

-- ----------------------------
-- Table structure for `req_myform`
-- ----------------------------
DROP TABLE IF EXISTS `req_myform`;
CREATE TABLE `req_myform` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `req_num` varchar(50) NOT NULL DEFAULT '' COMMENT '邀请编号',
  `account` varchar(50) NOT NULL DEFAULT '' COMMENT '用户银行卡账户.\\r\\n提现的时候利用\\r\\n',
  `account_type` int(11) NOT NULL DEFAULT '1' COMMENT '针对用户类型\\r\\n1 : 个人客户\\r\\n2 : 集团客户\\r\\n3 : 合作单位',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '针对的客户ID.\\r\\n按照account_type字段的内容，\\r\\n可能成为用户的id，集团客户的id，合作单位的id',
  `sum` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '操作金额',
  `oper` int(11) NOT NULL DEFAULT '0' COMMENT '操作类型\\r\\n0：转出\\r\\n1：转入',
  `req_date` datetime NOT NULL COMMENT '邀请日期/时间',
  `audit_date` datetime DEFAULT NULL COMMENT '审核日期/时间',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态\\r\\n\\r\\n1：待审核\\r\\n2：已审核\\r\\n3：已驳回\\r\\n4：已撤销\\r\\n5：处理中',
  `oper_type` int(11) NOT NULL DEFAULT '1' COMMENT '操作类型\\r\\n1 : 提现\\r\\n2 : 充值\\r\\n3 : 扣点',
  `channel` varchar(50) NOT NULL DEFAULT '' COMMENT '渠道. 提现的情况下就是银行名称。 充值的情况系可能是支付宝或者什么的',
  `req_cause` varchar(50) NOT NULL DEFAULT '' COMMENT '申请原因',
  `order_cs_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '顶管订单编号',
  `reject_cause` varchar(200) NOT NULL DEFAULT '' COMMENT '驳回理由',
  `ts_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '交易ID',
  `req_user` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作申请的管理员ID。可能为0',
  `auditor` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作审核的管理员ID。可能为0',
  `req_source` int(11) NOT NULL DEFAULT '1' COMMENT '1：手机端\\r\\n2：用户网站\\r\\n3：管理后台',
  `remark` varchar(100) NOT NULL DEFAULT '' COMMENT '详细说明。 备注',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除      1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='在后台做出来的提现，充值，扣点等等操作需要审核';

-- ----------------------------
-- Records of req_myform
-- ----------------------------

-- ----------------------------
-- Table structure for `req_oper_account`
-- ----------------------------
DROP TABLE IF EXISTS `req_oper_account`;
CREATE TABLE `req_oper_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `req_num` varchar(50) NOT NULL DEFAULT '' COMMENT '邀请编号',
  `account` varchar(50) NOT NULL DEFAULT '' COMMENT '用户银行卡账户.\r\n提现的时候利用\r\n',
  `account_type` int(11) NOT NULL DEFAULT '1' COMMENT '针对用户类型\r\n1 : 个人客户\r\n2 : 集团客户\r\n3 : 合作单位',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '针对的客户ID.\r\n按照account_type字段的内容，\r\n可能成为用户的id，集团客户的id，合作单位的id',
  `sum` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '操作金额',
  `oper` int(11) NOT NULL DEFAULT '0' COMMENT '操作类型\r\n1：转出\r\n2：转入',
  `req_date` datetime NOT NULL COMMENT '邀请日期/时间',
  `audit_date` datetime DEFAULT NULL COMMENT '审核日期/时间',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态\r\n\r\n1：待审核\r\n2：已审核\r\n3：已驳回\r\n4：已撤销\r\n5：处理中',
  `oper_type` int(11) NOT NULL DEFAULT '1' COMMENT '操作类型\r\n1 : 提现\r\n2 : 充值\r\n3 : 扣点',
  `channel` varchar(50) NOT NULL DEFAULT '' COMMENT '渠道. 提现的情况下就是银行名称。 充值的情况系可能是支付宝或者什么的',
  `req_cause` varchar(50) NOT NULL DEFAULT '' COMMENT '申请原因',
  `order_cs_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '顶管订单编号',
  `reject_cause` varchar(200) NOT NULL DEFAULT '' COMMENT '驳回理由',
  `ts_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '交易ID',
  `req_user` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作申请的管理员ID。可能为0',
  `auditor` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作审核的管理员ID。可能为0',
  `req_source` int(11) NOT NULL DEFAULT '1' COMMENT '1：手机端\r\n2：用户网站\r\n3：管理后台',
  `remark` varchar(100) NOT NULL DEFAULT '' COMMENT '详细说明。 备注',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除      1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8 COMMENT='手机端的充值提现也在这个表格里记录。';


-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `remark` text,
  `type` tinyint(4) DEFAULT NULL,
  `deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=76 DEFAULT CHARSET=utf8;



-- ----------------------------
-- Table structure for `roleitem`
-- ----------------------------
DROP TABLE IF EXISTS `roleitem`;
CREATE TABLE `roleitem` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `mid` varchar(255) DEFAULT NULL,
  `rid` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `parent` int(11) DEFAULT NULL,
  `chk` tinyint(4) DEFAULT NULL,
  `associd` int(11) DEFAULT NULL,
  `cid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1887 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `role_data_org_rel_zyl`
-- ----------------------------
DROP TABLE IF EXISTS `role_data_org_rel_zyl`;
CREATE TABLE `role_data_org_rel_zyl` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_data_id` int(11) NOT NULL COMMENT '数据角色主键',
  `group_id` bigint(11) DEFAULT NULL COMMENT '群组表主键',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=216 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='数据角色和机构的关系表';


-- ----------------------------
-- Table structure for `role_data_zyl`
-- ----------------------------
DROP TABLE IF EXISTS `role_data_zyl`;
CREATE TABLE `role_data_zyl` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '数据角色名称',
  `remark` text COMMENT '注释',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '是否被删除',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=77 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='数据角色表';



-- ----------------------------
-- Table structure for `role_oper_func_rel_zyl`
-- ----------------------------
DROP TABLE IF EXISTS `role_oper_func_rel_zyl`;
CREATE TABLE `role_oper_func_rel_zyl` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_oper_id` int(11) NOT NULL COMMENT '操作角色主键',
  `func_code` varchar(11) DEFAULT NULL COMMENT '菜单表code',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2402 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='操作角色和系统功能点的关系表';


-- ----------------------------
-- Table structure for `role_oper_zyl`
-- ----------------------------
DROP TABLE IF EXISTS `role_oper_zyl`;
CREATE TABLE `role_oper_zyl` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '操作角色名称，不能重复',
  `remark` text COMMENT '注释',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '是否被删除',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=95 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='操作角色表';

-- ----------------------------
-- Table structure for `routes_settings`
-- ----------------------------
DROP TABLE IF EXISTS `routes_settings`;
CREATE TABLE `routes_settings` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `startcity` varchar(256) NOT NULL DEFAULT '' COMMENT '出发城市',
  `endcity` varchar(256) NOT NULL DEFAULT '' COMMENT '目的城市',
  `from_` varchar(256) NOT NULL DEFAULT '' COMMENT '起点地址',
  `to_` varchar(256) NOT NULL DEFAULT '' COMMENT '终点地址',
  `city` varchar(256) NOT NULL DEFAULT '' COMMENT '出发城市',
  `lat_from` decimal(10,6) NOT NULL DEFAULT '0.000000' COMMENT '起点纬度',
  `lng_from` decimal(10,6) NOT NULL COMMENT '起点经度',
  `lat_to` decimal(10,6) NOT NULL COMMENT '终点纬度',
  `lng_to` decimal(10,6) NOT NULL COMMENT '终点经度',
  `ps_date` datetime NOT NULL COMMENT '添加日期/时间',
  `start_time` datetime NOT NULL COMMENT '预定出发时间',
  `type` tinyint(4) NOT NULL DEFAULT '2' COMMENT '类型\r\n1 : 长途\r\n2 : 市内',
  `whichdays` varchar(20) NOT NULL DEFAULT '' COMMENT '出行日\r\n周一：0\r\n周二：1\r\n。。。\r\n周日：6\r\n\r\n用斑点来分开',
  `userid` bigint(11) NOT NULL DEFAULT '0' COMMENT '用户编号ID',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除      1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='日常路线表格';


-- ----------------------------
-- Table structure for `single_coupon`
-- ----------------------------
DROP TABLE IF EXISTS `single_coupon`;
CREATE TABLE `single_coupon` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `coupon_code` varchar(50) NOT NULL DEFAULT '' COMMENT '点券编号',
  `sum` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '点券价格。实物点券的情况下该字段就0',
  `date_expired` date DEFAULT NULL COMMENT '有效日期',
  `isused` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未用过\r\n1：已用过',
  `date_used` datetime DEFAULT NULL COMMENT '使用日期',
  `password` varchar(20) NOT NULL DEFAULT '' COMMENT '暂时不用',
  `ps_date` datetime NOT NULL COMMENT '添加/更新日期',
  `remark` varchar(256) NOT NULL DEFAULT '' COMMENT '备注',
  `userid` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `order_cs_id` bigint(50) NOT NULL DEFAULT '0' COMMENT '相关订单编号ID',
  `isenabled` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1：能用\r\n0：不能用',
  `syscoupon_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'sys_coupon表格ID字段',
  `active_code` varchar(50) NOT NULL DEFAULT '' COMMENT '如果该点券是从活动获取的话该字段就是活动编号',
  `is_generated_by_active` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否从活动生成的\r\n0：不         1：是',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除      1：已删除',
  `type` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1044 DEFAULT CHARSET=utf8 COMMENT='用户已经得到的点券表格';


-- ----------------------------
-- Table structure for `sms_log`
-- ----------------------------
DROP TABLE IF EXISTS `sms_log`;
CREATE TABLE `sms_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sms_plan_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '短信发送计划ID',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `price` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '1次发送费用',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1：成功     2：失败',
  `time` datetime NOT NULL COMMENT '发送时间',
  `phonenum` varchar(50) NOT NULL DEFAULT '' COMMENT '非系统客户手机号码',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除          1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8 COMMENT='短信息发送记录';


-- ----------------------------
-- Table structure for `sms_plan`
-- ----------------------------
DROP TABLE IF EXISTS `sms_plan`;
CREATE TABLE `sms_plan` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `plan_code` varchar(50) NOT NULL DEFAULT '' COMMENT '计划编号',
  `client_num` int(11) NOT NULL DEFAULT '1' COMMENT '针对的客户数量',
  `msg` varchar(1000) NOT NULL DEFAULT '' COMMENT '内容',
  `price` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '一次费用',
  `send_mode` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1：一次发送\r\n2：周期的发送',
  `single_time` datetime DEFAULT NULL COMMENT '单次发送的时候发送时间',
  `has_send_times` int(11) NOT NULL DEFAULT '0' COMMENT '已发送数量',
  `limit_times` int(11) NOT NULL DEFAULT '1' COMMENT '限制数量',
  `regular_send_mode` int(11) NOT NULL DEFAULT '1' COMMENT '定期发送方式\r\n1：每天\r\n2：每周\r\n3：每月',
  `time1` int(11) NOT NULL DEFAULT '0' COMMENT '定期发送方式： 发送时间的第一个参数',
  `time2` int(11) NOT NULL DEFAULT '0' COMMENT '定期发送方式： 发送时间的第二个参数\r\ntime2 = hh * 3600 + mm * 60 + ss (hh:mm:ss, hh=0~23, mm=0~59, ss=0~59)',
  `isenabled` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否能用\r\n1：能\r\n0：不能',
  `remark` varchar(500) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创造时间',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除           1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COMMENT='短信发送计划表';

-- ----------------------------
-- Table structure for `sms_users`
-- ----------------------------
DROP TABLE IF EXISTS `sms_users`;
CREATE TABLE `sms_users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sms_plan_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '短信息发送计划ID. sms_plan表格的ID',
  `userid` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `phone` varchar(20) NOT NULL DEFAULT '' COMMENT '手机号码',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除   0：未删除  1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=utf8 COMMENT='短信息针对的用户表格';



-- ----------------------------
-- Table structure for `sys_coupon`
-- ----------------------------
DROP TABLE IF EXISTS `sys_coupon`;
CREATE TABLE `sys_coupon` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `syscoupon_code` varchar(50) NOT NULL DEFAULT '' COMMENT '点券类型识别代码',
  `password` varchar(20) NOT NULL DEFAULT '' COMMENT '暂时不用',
  `sc_date` datetime NOT NULL COMMENT '发布日期/时间',
  `remark` varchar(256) NOT NULL DEFAULT '' COMMENT '备注',
  `release_channel` int(11) NOT NULL DEFAULT '1' COMMENT '发行渠道\r\n1 : 本平台\r\n2：合作单位。一般实物点券',
  `app_spread_unit_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '合作单位ID。 实物点券利用',
  `goods_or_cash` int(11) NOT NULL DEFAULT '1' COMMENT '点券类型\r\n\r\n1：现金点券\r\n2：实物点券',
  `sum` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '价格',
  `goods` varchar(100) NOT NULL DEFAULT '' COMMENT '实物名称',
  `apply_source` int(11) NOT NULL DEFAULT '0' COMMENT '使用范围\r\n\r\n0：全部\r\n1：拼车\r\n2：代驾',
  `coupon_type` int(11) NOT NULL DEFAULT '0' COMMENT '使用条件\r\n0：无条件\r\n1：订单金额超过X元就可以用一张\r\n2：不能与其他点券并用\r\n3：每个订单只能用一张',
  `limit_val` int(11) NOT NULL DEFAULT '0' COMMENT 'coupon_type字段为1的时候，该字段就指定X元',
  `valid_period_unit` int(11) NOT NULL DEFAULT '0' COMMENT '有效期的单位\r\n1：日\r\n2：周\r\n3：月\r\n4：年',
  `valid_period` int(11) NOT NULL DEFAULT '0' COMMENT '有效期间. valid_period_unit字段的数量',
  `limit_count` int(11) NOT NULL DEFAULT '0' COMMENT '限制数量',
  `isenabled` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否能用\r\n1 ： 能\r\n0 ： 不能',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除        1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8 COMMENT='系统点券类型表格';


-- ----------------------------
-- Table structure for `ts`
-- ----------------------------
DROP TABLE IF EXISTS `ts`;
CREATE TABLE `ts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_cs_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '相关执行订单编号',
  `order_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '相关订单编号',
  `order_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '相关订单类型   1：单次订单   2：上下班订单   3：长途订单',
  `oper` int(11) NOT NULL DEFAULT '1' COMMENT '操作类型\r\n1：转出\r\n2：转入',
  `ts_way` int(11) NOT NULL DEFAULT '1' COMMENT '1：支付宝\r\n2：银联\r\n3：百度钱包\r\n其他：其他',
  `balance` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '余额金额',
  `ts_date` datetime NOT NULL COMMENT '交易日期/时间',
  `userid` bigint(20) NOT NULL DEFAULT '0' COMMENT '针对用户ID',
  `groupid` bigint(20) NOT NULL DEFAULT '0' COMMENT '集团客户ID',
  `unitid` bigint(20) NOT NULL DEFAULT '0' COMMENT '合作单位ID',
  `remark` varchar(256) NOT NULL DEFAULT '' COMMENT '备注',
  `account` varchar(50) NOT NULL DEFAULT '' COMMENT '在手机端提现的时候输入的银行卡信息',
  `account_type` int(11) NOT NULL DEFAULT '1' COMMENT '针对的客户的类型\r\n\r\n1：个人用户\r\n2：集团客户\r\n3：合作单位',
  `application` varchar(20) NOT NULL DEFAULT 'pinche' COMMENT '来源。''mainapp'',''pinche'',''daijia'',''sysmanage'',''website'',''other''',
  `ts_type` varchar(50) NOT NULL COMMENT '交易类型。 ts_type表格的tx_code字段',
  `sum` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '操作金额',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除     1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2239 DEFAULT CHARSET=utf8 COMMENT='交易记录表格';


-- ----------------------------
-- Table structure for `ts_type`
-- ----------------------------
DROP TABLE IF EXISTS `ts_type`;
CREATE TABLE `ts_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tx_code` varchar(50) NOT NULL DEFAULT '' COMMENT '交易类型代码',
  `comment` varchar(50) NOT NULL DEFAULT '' COMMENT '说明',
  `comment_mobile` varchar(50) NOT NULL DEFAULT '' COMMENT '在手机端现实的说明。 要比短一点',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除       1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='交易类型表格';

-- ----------------------------
-- Table structure for `unit`
-- ----------------------------
DROP TABLE IF EXISTS `unit`;
CREATE TABLE `unit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `unit_type` int(11) NOT NULL DEFAULT '0',
  `name` varchar(100) NOT NULL,
  `entityid` bigint(20) NOT NULL DEFAULT '0',
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of unit
-- ----------------------------
INSERT INTO `unit` VALUES ('1', '0', '', '0', '0');
INSERT INTO `unit` VALUES ('2', '0', '', '0', '0');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `usercode` varchar(50) NOT NULL DEFAULT '' COMMENT '登录名',
  `username` varchar(50) NOT NULL DEFAULT '' COMMENT '实名',
  `nickname` varchar(50) NOT NULL DEFAULT '' COMMENT '昵称',
  `password` varchar(50) NOT NULL DEFAULT '' COMMENT '登录密码',
  `phone` varchar(20) NOT NULL DEFAULT '' COMMENT '手机号码',
  `balance_ts` bigint(20) NOT NULL DEFAULT '0' COMMENT '最新交易记录ID',
  `invitecode_self` varchar(50) NOT NULL DEFAULT '' COMMENT '自体邀请码',
  `invitecode_regist` varchar(50) NOT NULL DEFAULT '' COMMENT '邀请我的邀请码',
  `reg_date` datetime NOT NULL COMMENT '注册时间',
  `last_login_time` datetime NOT NULL COMMENT '最终登录日期/时间',
  `app_register` int(11) NOT NULL DEFAULT '1' COMMENT '1 : 在MainApp注册\r\n2 : 在PincheApp注册\r\n3 : 其他',
  `bankcard` varchar(100) NOT NULL DEFAULT '' COMMENT '银行卡信息',
  `bankname` varchar(50) NOT NULL DEFAULT '' COMMENT '银行名称',
  `subbranch` varchar(100) NOT NULL DEFAULT '' COMMENT '开户账户',
  `sex` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 : 男     1 : 女',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `id_card1` varchar(2000) NOT NULL DEFAULT '' COMMENT '身份证正面图地址',
  `driver_license1` varchar(2000) NOT NULL DEFAULT '' COMMENT '驾驶证正面图地址',
  `driving_license1` varchar(2000) NOT NULL DEFAULT '' COMMENT '行驶证正面图地址',
  `id_card2` varchar(2000) NOT NULL DEFAULT '' COMMENT '身份证反面图地址',
  `driver_license2` varchar(2000) NOT NULL DEFAULT '' COMMENT '驾驶证反面图地址',
  `driving_license2` varchar(2000) NOT NULL DEFAULT '' COMMENT '行驶证反面图地址',
  `img` varchar(2000) NOT NULL DEFAULT '' COMMENT '用户头像地址',
  `person_verified` tinyint(4) NOT NULL DEFAULT '0' COMMENT '个人身份认证状况\r\n0 : 未认证\r\n1 : 已认证\r\n2 : 已申请. 待认证',
  `driver_verified` tinyint(4) NOT NULL DEFAULT '0' COMMENT '车主身份认证状况\r\n0 : 未认证\r\n1 : 已认证\r\n2 : 已申请. 待认证',
  `provide_profitsharing_way` tinyint(4) NOT NULL DEFAULT '3' COMMENT '用户参与拼车的时候给邀请方分成的限制方式\r\n\r\n1：按时间\r\n2：按次数\r\n3：都考虑',
  `provide_profitsharing_time_as_passenger` int(11) NOT NULL DEFAULT '0' COMMENT '贡献分成方式按时间的话，是多长时间，此记录一个时间段。此变量在用户注册的时候从全局配置表里读取',
  `provide_profitsharing_count_as_passenger` int(11) NOT NULL DEFAULT '0' COMMENT '用户作为乘客的时候分成给的有效次数',
  `provide_profitsharing_time_as_driver` int(11) NOT NULL DEFAULT '0' COMMENT '用户作为车主的时候分成给的有效期间(单位：月)',
  `provide_profitsharing_count_as_driver` int(11) NOT NULL DEFAULT '0' COMMENT '用户作为车主的时候分成给的有效次数',
  `city_register` varchar(50) NOT NULL DEFAULT '' COMMENT '注册城市',
  `city_cur` varchar(50) NOT NULL DEFAULT '' COMMENT '最后登录城市',
  `ratio_as_passenger` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '乘客方返利(百分比)',
  `integer_as_passenger` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '乘客方返利(固定值)',
  `activeway_as_passenger` int(11) NOT NULL DEFAULT '0' COMMENT '乘客方返利方式\r\n0：百分比      1：固定值',
  `ratio_as_driver` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '车主方返利(百分比)',
  `integer_as_driver` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '车主方返利(固定值)',
  `activeway_as_driver` int(11) NOT NULL DEFAULT '0' COMMENT '车主方返利方式\r\n0：百分比      1：固定值',
  `inviter_type` int(11) NOT NULL DEFAULT '0' COMMENT '邀请方类型\r\n1 : 个人用户\r\n2 : 集团客户\r\n3 : 合作单位\r\n0 : 没有邀请人',
  `is_platform` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 : 不是平台用户\r\n，1 :是平台用户',
  `ratio_as_passenger_self` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '邀请乘客的时候给他设置的乘客方返利百分值',
  `integer_as_passenger_self` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '邀请乘客的时候给他设置的乘客方返利固定值',
  `active_as_passenger_self` int(11) NOT NULL DEFAULT '0' COMMENT '0 : 用百分比方式\r\n1 : 用固定值方式',
  `ratio_as_driver_self` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '邀请乘客的时候给他设置的车主方返利百分值',
  `integer_as_driver_self` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '邀请乘客的时候给他设置的车主方返利固定值',
  `active_as_driver_self` int(11) NOT NULL DEFAULT '0' COMMENT '0 : 用百分比方式\r\n1 : 用固定值方式',
  `limit_way` tinyint(4) NOT NULL DEFAULT '3' COMMENT '用户邀请别的用户的时候设置给他provide_profitsharing_way的字段\r\n\r\n1：按时间\r\n2：按次数\r\n3：都考虑\r\n',
  `limit_month_as_passenger_self` int(11) NOT NULL DEFAULT '0' COMMENT '邀请乘客的时候给他设置的乘客方返利有效期间(单位：月)',
  `limit_month_as_driver_self` int(11) NOT NULL DEFAULT '0' COMMENT '邀请乘客的时候给他设置的车主方返利有效期间(单位：月)',
  `limit_count_as_passenger_self` int(11) NOT NULL DEFAULT '0' COMMENT '邀请乘客的时候给他设置的乘客方返利有效次数',
  `limit_count_as_driver_self` int(11) NOT NULL DEFAULT '0' COMMENT '邀请乘客的时候给他设置的车主方返利有效次数',
  `phone_system` tinyint(4) NOT NULL DEFAULT '0' COMMENT '最后登录的手机系统\r\n\r\n1 : iOS\r\n2 : Android',
  `device_token` varchar(200) NOT NULL DEFAULT '' COMMENT '设备识别代码.',
  `nation` varchar(20) NOT NULL DEFAULT '' COMMENT '民族',
  `id_card_num` varchar(50) NOT NULL DEFAULT '' COMMENT '身份证号',
  `address` varchar(100) NOT NULL DEFAULT '' COMMENT '住址',
  `drivinglicence_num` varchar(50) NOT NULL DEFAULT '' COMMENT '驾照号码',
  `drlicence_ti` varchar(50) NOT NULL DEFAULT '' COMMENT '驾照获取时间',
  `remark` text COMMENT '备注',
  `got_reg_coupon` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否注册的时候收到点券\r\n0：未收到\r\n1：已收到',
  `got_verify_coupon` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否在个人身份认证的时候收到点券\r\n0：未收到\r\n1：已收到',
  `got_order_coupon` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否在完成X个订单的时候收到点券\r\n0：未收到\r\n1：已收到',
  `got_afterreg_coupon` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否在注册后X个月过的时候收到点券\r\n0：未收到\r\n1：已收到',
  `loggedout` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否退出登录\r\n0:未退出\r\n1:已退出',
  `channel_flag` varchar(255) DEFAULT NULL COMMENT '渠道标识',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 : 未删除\r\n1 : 已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=171 DEFAULT CHARSET=utf8 COMMENT='手机端用户表格';

-- ----------------------------
-- Table structure for `user_car`
-- ----------------------------
DROP TABLE IF EXISTS `user_car`;
CREATE TABLE `user_car` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `vin` varchar(50) NOT NULL DEFAULT '' COMMENT '车辆的VIN码',
  `brand` varchar(50) NOT NULL DEFAULT '' COMMENT '车辆品牌',
  `style` varchar(50) NOT NULL DEFAULT '' COMMENT '车辆类型',
  `color` varchar(50) NOT NULL DEFAULT '' COMMENT '车辆颜色',
  `eno` varchar(50) NOT NULL DEFAULT '',
  `car_img` varchar(2000) NOT NULL DEFAULT '' COMMENT '车辆图片路径',
  `plate_num` varchar(20) NOT NULL DEFAULT '' COMMENT '车牌号',
  `plate_num_last3` varchar(3) NOT NULL DEFAULT '' COMMENT '车牌号最后三位',
  `is_oper_vehicle` int(11) NOT NULL DEFAULT '2' COMMENT '是否是营运车辆:1是，2否',
  `vehicle_owner` varchar(50) NOT NULL DEFAULT '' COMMENT '车主姓名',
  `car_type_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '车类型ID. car_type表格的ID字段',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除   0：未删除   1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8 COMMENT='用户车辆管理表格';

-- ----------------------------
-- Table structure for `user_copy`
-- ----------------------------
DROP TABLE IF EXISTS `user_copy`;
CREATE TABLE `user_copy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `usercode` varchar(50) NOT NULL DEFAULT '' COMMENT '登录名',
  `username` varchar(50) NOT NULL DEFAULT '' COMMENT '实名',
  `nickname` varchar(50) NOT NULL DEFAULT '' COMMENT '昵称',
  `password` varchar(50) NOT NULL DEFAULT '' COMMENT '登录密码',
  `phone` varchar(20) NOT NULL DEFAULT '' COMMENT '手机号码',
  `balance_ts` bigint(20) NOT NULL DEFAULT '0' COMMENT '最新交易记录ID',
  `invitecode_self` varchar(50) NOT NULL DEFAULT '' COMMENT '自体邀请码',
  `invitecode_regist` varchar(50) NOT NULL DEFAULT '' COMMENT '邀请我的邀请码',
  `reg_date` datetime NOT NULL COMMENT '注册时间',
  `last_login_time` datetime NOT NULL COMMENT '最终登录日期/时间',
  `app_register` int(11) NOT NULL DEFAULT '1' COMMENT '1 : 在MainApp注册\r\n2 : 在PincheApp注册\r\n3 : 其他',
  `bankcard` varchar(100) NOT NULL DEFAULT '' COMMENT '银行卡信息',
  `bankname` varchar(50) NOT NULL DEFAULT '' COMMENT '银行名称',
  `subbranch` varchar(100) NOT NULL DEFAULT '' COMMENT '开户账户',
  `sex` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 : 男     1 : 女',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `id_card1` varchar(2000) NOT NULL DEFAULT '' COMMENT '身份证正面图地址',
  `driver_license1` varchar(2000) NOT NULL DEFAULT '' COMMENT '驾驶证正面图地址',
  `driving_license1` varchar(2000) NOT NULL DEFAULT '' COMMENT '行驶证正面图地址',
  `id_card2` varchar(2000) NOT NULL DEFAULT '' COMMENT '身份证反面图地址',
  `driver_license2` varchar(2000) NOT NULL DEFAULT '' COMMENT '驾驶证反面图地址',
  `driving_license2` varchar(2000) NOT NULL DEFAULT '' COMMENT '行驶证反面图地址',
  `img` varchar(2000) NOT NULL DEFAULT '' COMMENT '用户头像地址',
  `person_verified` tinyint(4) NOT NULL DEFAULT '0' COMMENT '个人身份认证状况\r\n0 : 未认证\r\n1 : 已认证\r\n2 : 已申请. 待认证',
  `driver_verified` tinyint(4) NOT NULL DEFAULT '0' COMMENT '车主身份认证状况\r\n0 : 未认证\r\n1 : 已认证\r\n2 : 已申请. 待认证',
  `provide_profitsharing_way` tinyint(4) NOT NULL DEFAULT '3' COMMENT '用户参与拼车的时候给邀请方分成的限制方式\r\n\r\n1：按时间\r\n2：按次数\r\n3：都考虑',
  `provide_profitsharing_time_as_passenger` int(11) NOT NULL DEFAULT '0' COMMENT '贡献分成方式按时间的话，是多长时间，此记录一个时间段。此变量在用户注册的时候从全局配置表里读取',
  `provide_profitsharing_count_as_passenger` int(11) NOT NULL DEFAULT '0' COMMENT '用户作为乘客的时候分成给的有效次数',
  `provide_profitsharing_time_as_driver` int(11) NOT NULL DEFAULT '0' COMMENT '用户作为车主的时候分成给的有效期间(单位：月)',
  `provide_profitsharing_count_as_driver` int(11) NOT NULL DEFAULT '0' COMMENT '用户作为车主的时候分成给的有效次数',
  `city_register` varchar(50) NOT NULL DEFAULT '' COMMENT '注册城市',
  `city_cur` varchar(50) NOT NULL DEFAULT '' COMMENT '最后登录城市',
  `ratio_as_passenger` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '乘客方返利(百分比)',
  `integer_as_passenger` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '乘客方返利(固定值)',
  `activeway_as_passenger` int(11) NOT NULL DEFAULT '0' COMMENT '乘客方返利方式\r\n0：百分比      1：固定值',
  `ratio_as_driver` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '车主方返利(百分比)',
  `integer_as_driver` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '车主方返利(固定值)',
  `activeway_as_driver` int(11) NOT NULL DEFAULT '0' COMMENT '车主方返利方式\r\n0：百分比      1：固定值',
  `inviter_type` int(11) NOT NULL DEFAULT '0' COMMENT '邀请方类型\r\n1 : 个人用户\r\n2 : 集团客户\r\n3 : 合作单位\r\n0 : 没有邀请人',
  `is_platform` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 : 不是平台用户\r\n，1 :是平台用户',
  `ratio_as_passenger_self` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '邀请乘客的时候给他设置的乘客方返利百分值',
  `integer_as_passenger_self` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '邀请乘客的时候给他设置的乘客方返利固定值',
  `active_as_passenger_self` int(11) NOT NULL DEFAULT '0' COMMENT '0 : 用百分比方式\r\n1 : 用固定值方式',
  `ratio_as_driver_self` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '邀请乘客的时候给他设置的车主方返利百分值',
  `integer_as_driver_self` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '邀请乘客的时候给他设置的车主方返利固定值',
  `active_as_driver_self` int(11) NOT NULL DEFAULT '0' COMMENT '0 : 用百分比方式\r\n1 : 用固定值方式',
  `limit_way` tinyint(4) NOT NULL DEFAULT '3' COMMENT '用户邀请别的用户的时候设置给他provide_profitsharing_way的字段\r\n\r\n1：按时间\r\n2：按次数\r\n3：都考虑\r\n',
  `limit_month_as_passenger_self` int(11) NOT NULL DEFAULT '0' COMMENT '邀请乘客的时候给他设置的乘客方返利有效期间(单位：月)',
  `limit_month_as_driver_self` int(11) NOT NULL DEFAULT '0' COMMENT '邀请乘客的时候给他设置的车主方返利有效期间(单位：月)',
  `limit_count_as_passenger_self` int(11) NOT NULL DEFAULT '0' COMMENT '邀请乘客的时候给他设置的乘客方返利有效次数',
  `limit_count_as_driver_self` int(11) NOT NULL DEFAULT '0' COMMENT '邀请乘客的时候给他设置的车主方返利有效次数',
  `phone_system` tinyint(4) NOT NULL DEFAULT '0' COMMENT '最后登录的手机系统\r\n\r\n1 : iOS\r\n2 : Android',
  `device_token` varchar(200) NOT NULL DEFAULT '' COMMENT '设备识别代码.',
  `nation` varchar(20) NOT NULL DEFAULT '' COMMENT '民族',
  `id_card_num` varchar(50) NOT NULL DEFAULT '' COMMENT '身份证号',
  `address` varchar(100) NOT NULL DEFAULT '' COMMENT '住址',
  `drivinglicence_num` varchar(50) NOT NULL DEFAULT '' COMMENT '驾照号码',
  `drlicence_ti` varchar(50) NOT NULL DEFAULT '' COMMENT '驾照获取时间',
  `remark` text COMMENT '备注',
  `got_reg_coupon` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否注册的时候收到点券\r\n0：未收到\r\n1：已收到',
  `got_verify_coupon` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否在个人身份认证的时候收到点券\r\n0：未收到\r\n1：已收到',
  `got_order_coupon` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否在完成X个订单的时候收到点券\r\n0：未收到\r\n1：已收到',
  `got_afterreg_coupon` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否在注册后X个月过的时候收到点券\r\n0：未收到\r\n1：已收到',
  `loggedout` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否退出登录\r\n0:未退出\r\n1:已退出',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 : 未删除\r\n1 : 已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8 COMMENT='手机端用户表格';

-- ----------------------------
-- Table structure for `user_copy1`
-- ----------------------------
DROP TABLE IF EXISTS `user_copy1`;
CREATE TABLE `user_copy1` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `usercode` varchar(50) NOT NULL DEFAULT '' COMMENT '登录名',
  `username` varchar(50) NOT NULL DEFAULT '' COMMENT '实名',
  `nickname` varchar(50) NOT NULL DEFAULT '' COMMENT '昵称',
  `password` varchar(50) NOT NULL DEFAULT '' COMMENT '登录密码',
  `phone` varchar(20) NOT NULL DEFAULT '' COMMENT '手机号码',
  `balance_ts` bigint(20) NOT NULL DEFAULT '0' COMMENT '最新交易记录ID',
  `invitecode_self` varchar(50) NOT NULL DEFAULT '' COMMENT '自体邀请码',
  `invitecode_regist` varchar(50) NOT NULL DEFAULT '' COMMENT '邀请我的邀请码',
  `reg_date` datetime NOT NULL COMMENT '注册时间',
  `last_login_time` datetime NOT NULL COMMENT '最终登录日期/时间',
  `app_register` int(11) NOT NULL DEFAULT '1' COMMENT '1 : 在MainApp注册\r\n2 : 在PincheApp注册\r\n3 : 其他',
  `bankcard` varchar(100) NOT NULL DEFAULT '' COMMENT '银行卡信息',
  `bankname` varchar(50) NOT NULL DEFAULT '' COMMENT '银行名称',
  `subbranch` varchar(100) NOT NULL DEFAULT '' COMMENT '开户账户',
  `sex` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 : 男     1 : 女',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `id_card1` varchar(2000) NOT NULL DEFAULT '' COMMENT '身份证正面图地址',
  `driver_license1` varchar(2000) NOT NULL DEFAULT '' COMMENT '驾驶证正面图地址',
  `driving_license1` varchar(2000) NOT NULL DEFAULT '' COMMENT '行驶证正面图地址',
  `id_card2` varchar(2000) NOT NULL DEFAULT '' COMMENT '身份证反面图地址',
  `driver_license2` varchar(2000) NOT NULL DEFAULT '' COMMENT '驾驶证反面图地址',
  `driving_license2` varchar(2000) NOT NULL DEFAULT '' COMMENT '行驶证反面图地址',
  `img` varchar(2000) NOT NULL DEFAULT '' COMMENT '用户头像地址',
  `person_verified` tinyint(4) NOT NULL DEFAULT '0' COMMENT '个人身份认证状况\r\n0 : 未认证\r\n1 : 已认证\r\n2 : 已申请. 待认证',
  `driver_verified` tinyint(4) NOT NULL DEFAULT '0' COMMENT '车主身份认证状况\r\n0 : 未认证\r\n1 : 已认证\r\n2 : 已申请. 待认证',
  `provide_profitsharing_way` tinyint(4) NOT NULL DEFAULT '3' COMMENT '用户参与拼车的时候给邀请方分成的限制方式\r\n\r\n1：按时间\r\n2：按次数\r\n3：都考虑',
  `provide_profitsharing_time_as_passenger` int(11) NOT NULL DEFAULT '0' COMMENT '贡献分成方式按时间的话，是多长时间，此记录一个时间段。此变量在用户注册的时候从全局配置表里读取',
  `provide_profitsharing_count_as_passenger` int(11) NOT NULL DEFAULT '0' COMMENT '用户作为乘客的时候分成给的有效次数',
  `provide_profitsharing_time_as_driver` int(11) NOT NULL DEFAULT '0' COMMENT '用户作为车主的时候分成给的有效期间(单位：月)',
  `provide_profitsharing_count_as_driver` int(11) NOT NULL DEFAULT '0' COMMENT '用户作为车主的时候分成给的有效次数',
  `city_register` varchar(50) NOT NULL DEFAULT '' COMMENT '注册城市',
  `city_cur` varchar(50) NOT NULL DEFAULT '' COMMENT '最后登录城市',
  `ratio_as_passenger` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '乘客方返利(百分比)',
  `integer_as_passenger` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '乘客方返利(固定值)',
  `activeway_as_passenger` int(11) NOT NULL DEFAULT '0' COMMENT '乘客方返利方式\r\n0：百分比      1：固定值',
  `ratio_as_driver` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '车主方返利(百分比)',
  `integer_as_driver` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '车主方返利(固定值)',
  `activeway_as_driver` int(11) NOT NULL DEFAULT '0' COMMENT '车主方返利方式\r\n0：百分比      1：固定值',
  `inviter_type` int(11) NOT NULL DEFAULT '0' COMMENT '邀请方类型\r\n1 : 个人用户\r\n2 : 集团客户\r\n3 : 合作单位\r\n0 : 没有邀请人',
  `is_platform` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 : 不是平台用户\r\n，1 :是平台用户',
  `ratio_as_passenger_self` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '邀请乘客的时候给他设置的乘客方返利百分值',
  `integer_as_passenger_self` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '邀请乘客的时候给他设置的乘客方返利固定值',
  `active_as_passenger_self` int(11) NOT NULL DEFAULT '0' COMMENT '0 : 用百分比方式\r\n1 : 用固定值方式',
  `ratio_as_driver_self` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '邀请乘客的时候给他设置的车主方返利百分值',
  `integer_as_driver_self` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '邀请乘客的时候给他设置的车主方返利固定值',
  `active_as_driver_self` int(11) NOT NULL DEFAULT '0' COMMENT '0 : 用百分比方式\r\n1 : 用固定值方式',
  `limit_way` tinyint(4) NOT NULL DEFAULT '3' COMMENT '用户邀请别的用户的时候设置给他provide_profitsharing_way的字段\r\n\r\n1：按时间\r\n2：按次数\r\n3：都考虑\r\n',
  `limit_month_as_passenger_self` int(11) NOT NULL DEFAULT '0' COMMENT '邀请乘客的时候给他设置的乘客方返利有效期间(单位：月)',
  `limit_month_as_driver_self` int(11) NOT NULL DEFAULT '0' COMMENT '邀请乘客的时候给他设置的车主方返利有效期间(单位：月)',
  `limit_count_as_passenger_self` int(11) NOT NULL DEFAULT '0' COMMENT '邀请乘客的时候给他设置的乘客方返利有效次数',
  `limit_count_as_driver_self` int(11) NOT NULL DEFAULT '0' COMMENT '邀请乘客的时候给他设置的车主方返利有效次数',
  `phone_system` tinyint(4) NOT NULL DEFAULT '0' COMMENT '最后登录的手机系统\r\n\r\n1 : iOS\r\n2 : Android',
  `device_token` varchar(200) NOT NULL DEFAULT '' COMMENT '设备识别代码.',
  `nation` varchar(20) NOT NULL DEFAULT '' COMMENT '民族',
  `id_card_num` varchar(50) NOT NULL DEFAULT '' COMMENT '身份证号',
  `address` varchar(100) NOT NULL DEFAULT '' COMMENT '住址',
  `drivinglicence_num` varchar(50) NOT NULL DEFAULT '' COMMENT '驾照号码',
  `drlicence_ti` varchar(50) NOT NULL DEFAULT '' COMMENT '驾照获取时间',
  `remark` text COMMENT '备注',
  `got_reg_coupon` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否注册的时候收到点券\r\n0：未收到\r\n1：已收到',
  `got_verify_coupon` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否在个人身份认证的时候收到点券\r\n0：未收到\r\n1：已收到',
  `got_order_coupon` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否在完成X个订单的时候收到点券\r\n0：未收到\r\n1：已收到',
  `got_afterreg_coupon` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否在注册后X个月过的时候收到点券\r\n0：未收到\r\n1：已收到',
  `loggedout` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否退出登录\r\n0:未退出\r\n1:已退出',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 : 未删除\r\n1 : 已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8 COMMENT='手机端用户表格';


-- ----------------------------
-- Table structure for `user_login`
-- ----------------------------
DROP TABLE IF EXISTS `user_login`;
CREATE TABLE `user_login` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `source` int(11) NOT NULL DEFAULT '1' COMMENT '来源\r\n1：Main软件\r\n2：拼车软件\r\n3：其他',
  `devtoken` varchar(100) NOT NULL DEFAULT '' COMMENT '设备识别代码',
  `platform` tinyint(4) NOT NULL DEFAULT '1' COMMENT '手机系统\r\n1：苹果(iOS)\r\n2：安卓(Android)',
  `login_time` datetime NOT NULL COMMENT '最后登录时间',
  `pushnotif_token` varchar(200) NOT NULL DEFAULT '' COMMENT '推送识别代码',
  `loggedout` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否退出   0:未推出   1:已退出',
  `auto_login` tinyint(4) NOT NULL DEFAULT '1' COMMENT '暂时不用',
  `remem_pwd` tinyint(4) NOT NULL DEFAULT '1' COMMENT '暂时不用',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除\r\n0：未删除   1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COMMENT='按照设备统计的用户登录记录表格，这个表是推送消息用。此表和user_online表的区别是，user_online表只是';

-- ----------------------------
-- Table structure for `user_online`
-- ----------------------------
DROP TABLE IF EXISTS `user_online`;
CREATE TABLE `user_online` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `lat` decimal(10,6) NOT NULL DEFAULT '0.000000' COMMENT '纬度',
  `lng` decimal(10,6) NOT NULL DEFAULT '0.000000' COMMENT '经度',
  `login_time` datetime NOT NULL COMMENT '登录时间',
  `last_heartbeat_time` datetime NOT NULL COMMENT '心跳时间',
  `userid` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除   0：未删除  1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8 COMMENT='车主上线统计表格';


-- ----------------------------
-- Table structure for `user_pwd`
-- ----------------------------
DROP TABLE IF EXISTS `user_pwd`;
CREATE TABLE `user_pwd` (
  `name` char(30) CHARACTER SET latin1 COLLATE latin1_general_ci NOT NULL DEFAULT '',
  `pass` char(32) CHARACTER SET latin1 COLLATE latin1_general_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_pwd
-- ----------------------------
INSERT INTO `user_pwd` VALUES ('xampp', 'wampp');

-- ----------------------------
-- Table structure for `user_role`
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0',
  `role_id` int(11) NOT NULL DEFAULT '0',
  `deleted` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `work_form`
-- ----------------------------
DROP TABLE IF EXISTS `work_form`;
CREATE TABLE `work_form` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `workform_num` varchar(50) NOT NULL DEFAULT '0' COMMENT '工单编号',
  `adm_id` int(10) NOT NULL DEFAULT '0' COMMENT '管理员编号，坐席编号',
  `form_type` int(11) NOT NULL DEFAULT '1' COMMENT '工单类型\r\n1：订单情况咨询\r\n2：费用咨询\r\n3：App使用咨询\r\n4：综合咨询\r\n5：投诉',
  `bussi_type` int(11) NOT NULL DEFAULT '1' COMMENT '业务类型\r\n1：拼车\r\n2：代驾',
  `phone_incoming` varchar(20) NOT NULL DEFAULT '' COMMENT '来电号码',
  `order_cs_id` bigint(11) NOT NULL DEFAULT '0' COMMENT '拼车订单主键',
  `order_cs_no` int(11) NOT NULL DEFAULT '0' COMMENT '拼车订单编号',
  `order_dj_id` int(11) NOT NULL DEFAULT '0' COMMENT '代驾订单主键',
  `order_dj_no` int(11) NOT NULL DEFAULT '0' COMMENT '代驾订单编号',
  `customer_name` varchar(50) DEFAULT NULL COMMENT '来电用户姓名',
  `sex` int(11) NOT NULL DEFAULT '1' COMMENT '1先生；2女士',
  `city` varchar(50) NOT NULL DEFAULT '' COMMENT '城市code',
  `reason` varchar(500) NOT NULL DEFAULT '' COMMENT '问题原因',
  `form_agree` int(11) NOT NULL DEFAULT '0' COMMENT '是否认定对方违约',
  `process_result` varchar(500) NOT NULL DEFAULT '' COMMENT '问题处理情况',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '问题处理状态：1未处理;2已处理;3处理中',
  `wf_date` datetime NOT NULL COMMENT '添加时间',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未删除     1：已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='工单表';


-- ----------------------------
-- Function structure for `calc_distance`
-- ----------------------------
DROP FUNCTION IF EXISTS `calc_distance`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `calc_distance`(`lat1` DOUBLE, `lng1` DOUBLE, `lat2` DOUBLE, `lng2` DOUBLE) RETURNS double
    COMMENT '计算两个经纬度之间的距离(单位:km)'
BEGIN
	DECLARE theta double;
	DECLARE dist double;
	SET theta = lng1 - lng2;
	SET dist = SIN(lat1 * PI() / 180.0) * SIN(lat2 * PI() / 180.0) + COS(lat1 * PI() / 180.0) * COS(lat2 * PI() / 180.0) * COS(theta * PI() / 180.0);
	SET dist = ACOS(dist);
	SET dist = dist / PI() * 180;
	SET dist = dist * 60 * 1.1515;
	SET dist = dist * 1.609344;
	return dist;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `calc_distance1`
-- ----------------------------
DROP FUNCTION IF EXISTS `calc_distance1`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `calc_distance1`(`lat1` DOUBLE, `lng1` DOUBLE, `lat2` DOUBLE, `lng2` DOUBLE) RETURNS double
    COMMENT '计算两个经纬度之间的距离(单位:km)'
BEGIN
	DECLARE `theta` double;
	DECLARE `dist` double;
	SET theta = lng1 - lng2;
	SET dist = SIN(lat1 * PI() / 180.0) * SIN(lat2 * PI() / 180.0) + COS(lat1 * PI() / 180.0) * COS(lat2 * PI() / 180.0) * COS(theta * PI() / 180.0);
	SET dist = ACOS(dist);
	SET dist = dist / PI() * 180;
	SET dist = dist * 60 * 1.1515;
	SET dist = dist * 1.609344;
	return dist;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `ignoreStrNull`
-- ----------------------------
DROP FUNCTION IF EXISTS `ignoreStrNull`;
DELIMITER ;;
CREATE DEFINER=`bjpc_admin`@`%` FUNCTION `ignoreStrNull`(`inVal` text,`defVal` text) RETURNS text CHARSET utf8
BEGIN
	IF (inVal IS NULL) THEN
		RETURN defVal;
	END IF;

	RETURN inVal;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `timediff_min`
-- ----------------------------
DROP FUNCTION IF EXISTS `timediff_min`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `timediff_min`(`time1` DATETIME, `time2` DATETIME) RETURNS double
    COMMENT '计算两个日期时间变量之间的时间差(单位:分)'
BEGIN
	DECLARE result DOUBLE;

	SET result = TIMESTAMPDIFF(MINUTE, time1, time2);

	RETURN result;
END
;;
DELIMITER ;
