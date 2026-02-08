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

INSERT INTO `insu_error_msg` VALUES ('49752', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49753', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49754', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49755', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49756', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49757', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49758', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49759', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49760', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49761', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49762', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49763', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49764', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49765', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49766', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49767', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49768', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49769', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49770', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49771', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49772', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49773', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49774', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49775', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49776', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49777', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49778', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49779', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49780', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49781', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49782', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49783', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49784', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49785', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49786', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49787', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49788', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49789', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49790', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49791', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49792', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49793', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49794', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49795', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49796', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49797', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49798', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49799', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49800', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49801', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49802', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49803', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49804', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49805', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49806', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49807', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49808', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49809', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49810', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49811', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49812', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49813', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49814', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49815', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49816', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49817', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49818', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49819', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49820', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49821', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49822', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49823', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49824', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49825', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49826', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49827', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49828', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49829', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49830', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49831', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49832', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49833', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49834', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49835', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49836', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49837', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49838', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49839', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49840', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49841', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49842', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49843', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49844', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49845', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49846', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49847', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49848', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49849', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49850', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49851', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49852', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49853', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49854', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49855', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49856', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49857', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49858', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49859', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49860', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49861', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49862', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49863', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49864', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49865', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49866', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49867', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49868', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49869', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49870', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49871', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49872', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49873', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49874', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49875', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49876', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49877', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49878', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49879', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49880', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49881', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49882', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49883', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49884', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49885', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49886', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49887', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49888', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49889', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49890', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49891', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49892', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49893', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49894', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49895', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49896', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49897', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49898', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49899', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49900', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49901', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49902', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49903', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49904', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49905', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49906', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49907', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49908', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49909', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49910', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49911', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49912', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49913', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49914', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49915', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49916', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49917', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49918', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49919', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49920', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49921', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49922', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49923', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49924', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49925', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49926', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49927', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49928', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49929', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49930', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49931', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49932', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49933', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49934', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49935', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49936', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49937', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49938', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49939', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49940', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49941', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49942', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49943', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49944', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49945', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49946', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49947', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49948', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49949', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49950', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49951', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49952', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49953', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49954', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49955', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49956', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49957', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49958', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49959', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49960', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49961', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49962', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49963', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49964', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49965', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49966', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49967', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49968', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49969', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49970', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49971', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49972', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49973', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49974', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49975', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49976', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49977', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49978', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49979', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49980', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49981', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49982', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49983', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49984', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49985', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49986', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49987', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49988', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49989', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49990', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49991', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49992', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49993', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49994', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49995', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49996', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49997', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49998', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('49999', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50000', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50001', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50002', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50003', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50004', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50005', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50006', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50007', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50008', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50009', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50010', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50011', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50012', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50013', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50014', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50015', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50016', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50017', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50018', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50019', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50020', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50021', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50022', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50023', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50024', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50025', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50026', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50027', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50028', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50029', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50030', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50031', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50032', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50033', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50034', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50035', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50036', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50037', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50038', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50039', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50040', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50041', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50042', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50043', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50044', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50045', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50046', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50047', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50048', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50049', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50050', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50051', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50052', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50053', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50054', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50055', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50056', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50057', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50058', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50059', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50060', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50061', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50062', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50063', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50064', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50065', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50066', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50067', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50068', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50069', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50070', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50071', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50072', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50073', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50074', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50075', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50076', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50077', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50078', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50079', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50080', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50081', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50082', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50083', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50084', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50085', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50086', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50087', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50088', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50089', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50090', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50091', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50092', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50093', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50094', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50095', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50096', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50097', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50098', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50099', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50100', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50101', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50102', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50103', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50104', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50105', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50106', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50107', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50108', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50109', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50110', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50111', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50112', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50113', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50114', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50115', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50116', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50117', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50118', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50119', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50120', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50121', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50122', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50123', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50124', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50125', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50126', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50127', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50128', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50129', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50130', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50131', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50132', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50133', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50134', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50135', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50136', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50137', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50138', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50139', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50140', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50141', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50142', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50143', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50144', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50145', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50146', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50147', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50148', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50149', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50150', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50151', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50152', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50153', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50154', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50155', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50156', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50157', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50158', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50159', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50160', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50161', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50162', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50163', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50164', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50165', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50166', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50167', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50168', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50169', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50170', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50171', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50172', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50173', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50174', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50175', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50176', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50177', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50178', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50179', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50180', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50181', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50182', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50183', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50184', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50185', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50186', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50187', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50188', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50189', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50190', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50191', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50192', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50193', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50194', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50195', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50196', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50197', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50198', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50199', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50200', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50201', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50202', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50203', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50204', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50205', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50206', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50207', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50208', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50209', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50210', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50211', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50212', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50213', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50214', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50215', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50216', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50217', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50218', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50219', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50220', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50221', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50222', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50223', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50224', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50225', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50226', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50227', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50228', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50229', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50230', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50231', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50232', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50233', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50234', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50235', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50236', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50237', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50238', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50239', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50240', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50241', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50242', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50243', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50244', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50245', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50246', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50247', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50248', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50249', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50250', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50251', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50252', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50253', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50254', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50255', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50256', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50257', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50258', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50259', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50260', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50261', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50262', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50263', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50264', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50265', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50266', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50267', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50268', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50269', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50270', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50271', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50272', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50273', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50274', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50275', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50276', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50277', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50278', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50279', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50280', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50281', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50282', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50283', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50284', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50285', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50286', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50287', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50288', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50289', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50290', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50291', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50292', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50293', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50294', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50295', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50296', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50297', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50298', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50299', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50300', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50301', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50302', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50303', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50304', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50305', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50306', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50307', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50308', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50309', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50310', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50311', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50312', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50313', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50314', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50315', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50316', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50317', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50318', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50319', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50320', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50321', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50322', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50323', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50324', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50325', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50326', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50327', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50328', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50329', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50330', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50331', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50332', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50333', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50334', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50335', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50336', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50337', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50338', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50339', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50340', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50341', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50342', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50343', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50344', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50345', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50346', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50347', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50348', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50349', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50350', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50351', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50352', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50353', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50354', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50355', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50356', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50357', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50358', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50359', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50360', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50361', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50362', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50363', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50364', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50365', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50366', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50367', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50368', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50369', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50370', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50371', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50372', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50373', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50374', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50375', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50376', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50377', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50378', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50379', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50380', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50381', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50382', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50383', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50384', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50385', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50386', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50387', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50388', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50389', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50390', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50391', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50392', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50393', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50394', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50395', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50396', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50397', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50398', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50399', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50400', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50401', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50402', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50403', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50404', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50405', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50406', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50407', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50408', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50409', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50410', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50411', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50412', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50413', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50414', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50415', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50416', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50417', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50418', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50419', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50420', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50421', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50422', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50423', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50424', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50425', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50426', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50427', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50428', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50429', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50430', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50431', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50432', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50433', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50434', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50435', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50436', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50437', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50438', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50439', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50440', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50441', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50442', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50443', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50444', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50445', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50446', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50447', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50448', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50449', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50450', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50451', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50452', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50453', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50454', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50455', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50456', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50457', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50458', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50459', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50460', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50461', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50462', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50463', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50464', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50465', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50466', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50467', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50468', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50469', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50470', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50471', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50472', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50473', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50474', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50475', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50476', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50477', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50478', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50479', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50480', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50481', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50482', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50483', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50484', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50485', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50486', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50487', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50488', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50489', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50490', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50491', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50492', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50493', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50494', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50495', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50496', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50497', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50498', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50499', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50500', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50501', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50502', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50503', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50504', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50505', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50506', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50507', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50508', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50509', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50510', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50511', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50512', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50513', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50514', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50515', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50516', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50517', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50518', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50519', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50520', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50521', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50522', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50523', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50524', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50525', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50526', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50527', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50528', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50529', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50530', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50531', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50532', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50533', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50534', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50535', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50536', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50537', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50538', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50539', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50540', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50541', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50542', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50543', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50544', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50545', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50546', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50547', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50548', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50549', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50550', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50551', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50552', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50553', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50554', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50555', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50556', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50557', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50558', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50559', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50560', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50561', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50562', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50563', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50564', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50565', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50566', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50567', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50568', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50569', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50570', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50571', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50572', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50573', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50574', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50575', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50576', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50577', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50578', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50579', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50580', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50581', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50582', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50583', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50584', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50585', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50586', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50587', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50588', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50589', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50590', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50591', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50592', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50593', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50594', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50595', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50596', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50597', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50598', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50599', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50600', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50601', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50602', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50603', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50604', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50605', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50606', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50607', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50608', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50609', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50610', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50611', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50612', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50613', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50614', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50615', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50616', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50617', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50618', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50619', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50620', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50621', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50622', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50623', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50624', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50625', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50626', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50627', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50628', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50629', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50630', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50631', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50632', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50633', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50634', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50635', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50636', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50637', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50638', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50639', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50640', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50641', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50642', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50643', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50644', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50645', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50646', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50647', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50648', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50649', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50650', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50651', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50652', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50653', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50654', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50655', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50656', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50657', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50658', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50659', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50660', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50661', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50662', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50663', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50664', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50665', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50666', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50667', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50668', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50669', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50670', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50671', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50672', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50673', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50674', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50675', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50676', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50677', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50678', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50679', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50680', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50681', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50682', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50683', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50684', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50685', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50686', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50687', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50688', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50689', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50690', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50691', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50692', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50693', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50694', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50695', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50696', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50697', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50698', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50699', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50700', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50701', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50702', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50703', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50704', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50705', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50706', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50707', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50708', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50709', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50710', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50711', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50712', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50713', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50714', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50715', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50716', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50717', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50718', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50719', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50720', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50721', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50722', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50723', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50724', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50725', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50726', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50727', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50728', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50729', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50730', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50731', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50732', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50733', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50734', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50735', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50736', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50737', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50738', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50739', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50740', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50741', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50742', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50743', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50744', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50745', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50746', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50747', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50748', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50749', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50750', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50751', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50752', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50753', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50754', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50755', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50756', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50757', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50758', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50759', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50760', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50761', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50762', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50763', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50764', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50765', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50766', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50767', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50768', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50769', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50770', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50771', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50772', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50773', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50774', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50775', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50776', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50777', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50778', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50779', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50780', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50781', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50782', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50783', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50784', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50785', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50786', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50787', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50788', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50789', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50790', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50791', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50792', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50793', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50794', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50795', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50796', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50797', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50798', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50799', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50800', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50801', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50802', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50803', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50804', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50805', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50806', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50807', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50808', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50809', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50810', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50811', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50812', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50813', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50814', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50815', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50816', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50817', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50818', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50819', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50820', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50821', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50822', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50823', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50824', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50825', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50826', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50827', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50828', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50829', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50830', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50831', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50832', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50833', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50834', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50835', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50836', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50837', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50838', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50839', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50840', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50841', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50842', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50843', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50844', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50845', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50846', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50847', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50848', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50849', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50850', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50851', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50852', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50853', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50854', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50855', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50856', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50857', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50858', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50859', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50860', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50861', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50862', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50863', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50864', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50865', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50866', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50867', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50868', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50869', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50870', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50871', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50872', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50873', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50874', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50875', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50876', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50877', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50878', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50879', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50880', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50881', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50882', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50883', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50884', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50885', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50886', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50887', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50888', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50889', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50890', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50891', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50892', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50893', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50894', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50895', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50896', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50897', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50898', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50899', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50900', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50901', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50902', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50903', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50904', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50905', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50906', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50907', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50908', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50909', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50910', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50911', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50912', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50913', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50914', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50915', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50916', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50917', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50918', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50919', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50920', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50921', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50922', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50923', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50924', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50925', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50926', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50927', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50928', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50929', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50930', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50931', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50932', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50933', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50934', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50935', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50936', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50937', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50938', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50939', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50940', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50941', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50942', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50943', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50944', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50945', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50946', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50947', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50948', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50949', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50950', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50951', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50952', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50953', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50954', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50955', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50956', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50957', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50958', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50959', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50960', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50961', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50962', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50963', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50964', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50965', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50966', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50967', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50968', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50969', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50970', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50971', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50972', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50973', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50974', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50975', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50976', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50977', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50978', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50979', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50980', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50981', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50982', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50983', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50984', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50985', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50986', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50987', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50988', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50989', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50990', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50991', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50992', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50993', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50994', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50995', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50996', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50997', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50998', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('50999', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51000', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51001', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51002', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51003', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51004', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51005', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51006', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51007', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51008', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51009', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51010', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51011', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51012', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51013', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51014', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51015', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51016', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51017', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51018', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51019', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51020', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51021', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51022', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51023', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51024', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51025', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51026', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51027', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51028', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51029', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51030', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51031', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51032', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51033', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51034', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51035', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51036', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51037', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51038', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51039', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51040', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51041', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51042', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51043', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51044', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51045', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51046', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51047', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51048', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51049', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51050', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51051', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51052', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51053', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51054', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51055', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51056', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51057', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51058', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51059', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51060', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51061', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51062', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51063', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51064', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51065', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51066', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51067', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51068', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51069', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51070', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51071', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51072', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51073', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51074', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51075', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51076', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51077', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51078', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51079', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51080', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51081', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51082', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51083', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51084', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51085', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51086', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51087', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51088', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51089', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51090', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51091', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51092', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51093', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51094', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51095', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51096', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51097', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51098', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51099', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51100', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51101', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51102', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51103', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51104', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51105', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51106', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51107', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51108', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51109', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51110', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51111', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51112', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51113', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51114', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51115', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51116', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51117', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51118', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51119', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51120', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51121', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51122', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51123', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51124', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51125', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51126', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51127', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51128', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51129', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51130', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51131', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51132', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51133', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51134', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51135', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51136', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51137', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51138', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51139', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51140', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51141', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51142', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51143', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51144', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51145', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51146', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51147', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51148', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51149', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51150', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51151', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51152', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51153', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51154', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51155', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51156', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51157', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51158', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51159', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51160', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51161', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51162', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51163', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51164', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51165', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51166', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51167', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51168', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51169', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51170', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51171', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51172', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51173', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51174', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51175', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51176', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51177', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51178', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51179', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51180', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51181', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51182', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51183', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51184', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51185', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51186', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51187', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51188', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51189', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51190', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51191', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51192', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51193', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51194', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51195', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51196', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51197', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51198', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51199', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51200', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51201', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51202', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51203', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51204', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51205', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51206', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51207', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51208', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51209', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51210', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51211', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51212', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51213', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51214', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51215', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51216', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51217', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51218', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51219', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51220', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51221', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51222', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51223', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51224', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51225', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51226', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51227', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51228', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51229', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51230', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51231', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51232', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51233', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51234', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51235', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51236', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51237', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51238', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51239', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51240', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51241', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51242', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51243', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51244', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51245', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51246', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51247', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51248', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51249', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51250', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51251', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51252', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51253', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51254', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51255', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51256', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51257', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51258', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51259', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51260', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51261', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51262', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51263', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51264', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51265', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51266', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51267', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51268', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51269', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51270', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51271', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51272', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51273', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51274', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51275', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51276', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51277', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51278', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51279', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51280', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51281', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51282', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51283', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51284', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51285', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51286', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51287', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51288', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51289', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51290', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51291', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51292', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51293', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51294', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51295', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51296', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51297', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51298', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51299', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51300', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51301', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51302', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51303', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51304', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51305', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51306', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51307', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51308', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51309', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51310', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51311', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51312', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51313', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51314', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51315', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51316', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51317', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51318', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51319', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51320', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51321', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51322', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51323', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51324', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51325', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51326', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51327', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51328', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51329', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51330', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51331', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51332', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51333', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51334', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51335', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51336', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51337', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51338', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51339', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51340', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51341', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51342', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51343', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51344', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51345', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51346', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51347', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51348', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51349', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51350', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51351', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51352', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51353', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51354', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51355', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51356', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51357', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51358', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51359', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51360', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51361', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51362', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51363', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51364', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51365', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51366', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51367', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51368', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51369', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51370', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51371', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51372', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51373', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51374', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51375', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51376', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51377', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51378', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51379', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51380', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51381', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51382', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51383', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51384', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51385', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51386', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51387', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51388', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51389', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51390', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51391', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51392', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51393', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51394', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51395', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51396', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51397', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51398', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51399', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51400', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51401', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51402', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51403', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51404', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51405', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51406', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51407', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51408', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51409', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51410', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51411', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51412', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51413', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51414', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51415', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51416', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51417', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51418', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51419', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51420', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51421', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51422', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51423', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51424', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51425', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51426', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51427', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51428', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51429', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51430', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51431', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51432', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51433', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51434', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51435', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51436', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51437', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51438', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51439', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51440', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51441', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51442', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51443', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51444', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51445', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51446', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51447', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51448', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51449', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51450', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51451', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51452', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51453', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51454', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51455', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51456', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51457', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51458', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51459', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51460', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51461', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51462', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51463', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51464', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51465', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51466', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51467', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51468', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51469', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51470', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51471', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51472', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51473', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51474', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51475', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51476', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51477', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51478', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51479', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51480', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51481', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51482', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51483', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51484', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51485', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51486', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51487', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51488', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51489', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51490', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51491', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51492', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51493', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51494', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51495', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51496', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51497', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51498', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51499', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51500', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51501', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51502', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51503', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51504', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51505', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51506', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51507', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51508', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51509', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51510', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51511', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51512', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51513', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51514', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51515', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51516', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51517', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51518', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51519', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51520', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51521', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51522', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51523', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51524', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51525', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51526', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51527', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51528', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51529', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51530', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51531', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51532', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51533', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51534', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51535', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51536', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51537', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51538', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51539', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51540', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51541', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51542', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51543', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51544', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51545', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51546', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51547', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51548', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51549', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51550', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51551', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51552', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51553', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51554', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51555', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51556', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51557', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51558', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51559', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51560', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51561', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51562', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51563', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51564', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51565', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51566', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51567', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51568', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51569', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51570', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51571', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51572', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51573', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51574', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51575', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51576', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51577', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51578', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51579', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51580', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51581', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51582', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51583', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51584', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51585', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51586', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51587', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51588', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51589', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51590', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51591', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51592', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51593', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51594', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51595', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51596', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51597', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51598', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51599', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51600', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51601', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51602', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51603', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51604', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51605', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51606', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51607', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51608', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51609', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51610', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51611', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51612', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51613', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51614', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51615', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51616', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51617', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51618', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51619', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51620', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51621', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51622', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51623', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51624', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51625', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51626', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51627', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51628', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51629', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51630', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51631', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51632', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51633', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51634', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51635', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51636', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51637', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51638', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51639', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51640', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51641', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51642', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51643', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51644', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51645', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51646', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51647', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51648', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51649', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51650', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51651', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51652', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51653', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51654', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51655', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51656', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51657', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51658', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51659', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51660', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51661', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51662', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51663', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51664', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51665', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51666', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51667', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51668', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51669', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51670', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51671', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51672', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51673', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51674', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51675', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51676', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51677', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51678', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51679', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51680', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51681', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51682', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51683', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51684', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51685', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51686', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51687', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51688', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51689', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51690', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51691', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51692', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51693', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51694', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51695', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51696', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51697', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51698', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51699', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51700', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51701', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51702', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51703', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51704', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51705', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51706', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51707', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51708', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51709', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51710', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51711', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51712', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51713', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51714', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51715', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51716', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51717', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51718', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51719', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51720', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51721', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51722', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51723', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51724', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51725', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51726', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51727', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51728', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51729', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51730', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51731', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51732', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51733', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51734', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51735', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51736', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51737', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51738', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51739', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51740', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51741', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51742', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51743', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51744', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51745', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51746', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51747', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51748', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51749', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51750', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51751', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51752', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51753', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51754', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51755', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51756', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51757', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51758', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51759', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51760', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51761', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51762', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51763', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51764', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51765', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51766', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51767', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51768', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51769', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51770', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51771', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51772', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51773', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51774', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51775', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51776', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51777', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51778', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51779', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51780', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51781', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51782', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51783', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51784', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51785', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51786', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51787', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51788', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51789', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51790', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51791', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51792', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51793', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51794', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51795', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51796', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51797', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51798', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51799', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51800', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51801', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51802', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51803', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51804', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51805', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51806', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51807', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51808', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51809', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51810', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51811', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51812', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51813', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51814', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51815', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51816', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51817', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51818', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51819', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51820', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51821', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51822', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51823', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51824', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51825', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51826', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51827', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51828', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51829', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51830', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51831', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51832', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51833', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51834', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51835', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51836', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51837', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51838', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51839', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51840', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51841', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51842', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51843', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51844', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51845', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51846', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51847', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51848', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51849', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51850', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51851', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51852', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51853', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51854', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51855', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51856', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51857', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51858', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51859', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51860', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51861', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51862', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51863', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51864', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51865', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51866', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51867', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51868', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51869', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51870', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51871', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51872', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51873', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51874', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51875', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51876', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51877', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51878', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51879', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51880', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51881', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51882', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51883', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51884', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51885', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51886', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51887', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51888', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51889', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51890', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51891', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51892', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51893', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51894', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51895', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51896', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51897', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51898', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51899', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51900', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51901', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51902', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51903', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51904', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51905', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51906', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51907', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51908', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51909', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51910', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51911', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51912', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51913', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51914', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51915', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51916', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51917', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51918', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51919', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51920', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51921', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51922', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51923', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51924', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51925', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51926', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51927', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51928', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51929', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51930', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51931', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51932', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51933', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51934', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51935', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51936', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51937', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51938', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51939', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51940', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51941', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51942', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51943', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51944', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51945', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51946', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51947', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51948', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51949', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51950', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51951', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51952', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51953', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51954', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51955', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51956', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51957', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51958', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51959', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51960', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51961', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51962', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51963', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51964', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51965', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51966', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51967', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51968', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51969', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51970', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51971', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51972', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51973', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51974', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51975', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51976', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51977', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51978', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51979', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51980', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51981', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51982', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51983', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51984', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51985', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51986', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51987', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51988', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51989', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51990', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51991', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51992', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51993', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51994', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51995', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51996', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51997', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51998', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('51999', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52000', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52001', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52002', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52003', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52004', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52005', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52006', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52007', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52008', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52009', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52010', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52011', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52012', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52013', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52014', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52015', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52016', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52017', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52018', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52019', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52020', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52021', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52022', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52023', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52024', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52025', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52026', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52027', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52028', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52029', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52030', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52031', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52032', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52033', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52034', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52035', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52036', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52037', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52038', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52039', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52040', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52041', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52042', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52043', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52044', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52045', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52046', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52047', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52048', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52049', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52050', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52051', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52052', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52053', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52054', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52055', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52056', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52057', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52058', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52059', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52060', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52061', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52062', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52063', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52064', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52065', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52066', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52067', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52068', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52069', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52070', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52071', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52072', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52073', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52074', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52075', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52076', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52077', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52078', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52079', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52080', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52081', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52082', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52083', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52084', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52085', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52086', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52087', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52088', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52089', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52090', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52091', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52092', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52093', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52094', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52095', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52096', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52097', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52098', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52099', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52100', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52101', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52102', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52103', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52104', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52105', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52106', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52107', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52108', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52109', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52110', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52111', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52112', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52113', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52114', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52115', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52116', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52117', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52118', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52119', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52120', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52121', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52122', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52123', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52124', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52125', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52126', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52127', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52128', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52129', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52130', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52131', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52132', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52133', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52134', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52135', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52136', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52137', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52138', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52139', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52140', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52141', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52142', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52143', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52144', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52145', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52146', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52147', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52148', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52149', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52150', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52151', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52152', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52153', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52154', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52155', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52156', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52157', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52158', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52159', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52160', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52161', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52162', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52163', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52164', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52165', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52166', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52167', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52168', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52169', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52170', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52171', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52172', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52173', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52174', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52175', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52176', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52177', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52178', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52179', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52180', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52181', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52182', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52183', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52184', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52185', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52186', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52187', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52188', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52189', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52190', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52191', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52192', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52193', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52194', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52195', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52196', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52197', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52198', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52199', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52200', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52201', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52202', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52203', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52204', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52205', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52206', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52207', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52208', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52209', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52210', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52211', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52212', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52213', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52214', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52215', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52216', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52217', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52218', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52219', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52220', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52221', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52222', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52223', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52224', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52225', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52226', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52227', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52228', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52229', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52230', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52231', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52232', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52233', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52234', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52235', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52236', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52237', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52238', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52239', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52240', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52241', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52242', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52243', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52244', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52245', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52246', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52247', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52248', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52249', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52250', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52251', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52252', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52253', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52254', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52255', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52256', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52257', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52258', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52259', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52260', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52261', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52262', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52263', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52264', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52265', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52266', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52267', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52268', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52269', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52270', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52271', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52272', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52273', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52274', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52275', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52276', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52277', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52278', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52279', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52280', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52281', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52282', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52283', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52284', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52285', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52286', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52287', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52288', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52289', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52290', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52291', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52292', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52293', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52294', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52295', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52296', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52297', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52298', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52299', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52300', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52301', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52302', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52303', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52304', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52305', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52306', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52307', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52308', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52309', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52310', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52311', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52312', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52313', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52314', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52315', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52316', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52317', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52318', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52319', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52320', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52321', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52322', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52323', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52324', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52325', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52326', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52327', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52328', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52329', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52330', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52331', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52332', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52333', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52334', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52335', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52336', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52337', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52338', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52339', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52340', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52341', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52342', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52343', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52344', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52345', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52346', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52347', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52348', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52349', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52350', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52351', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52352', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52353', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52354', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52355', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52356', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52357', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52358', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52359', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52360', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52361', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52362', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52363', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52364', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52365', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52366', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52367', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52368', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52369', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52370', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52371', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52372', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52373', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52374', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52375', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52376', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52377', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52378', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52379', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52380', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52381', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52382', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52383', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52384', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52385', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52386', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52387', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52388', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52389', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52390', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52391', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52392', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52393', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52394', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52395', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52396', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52397', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52398', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52399', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52400', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52401', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52402', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52403', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52404', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52405', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52406', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52407', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52408', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52409', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52410', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52411', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52412', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52413', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52414', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52415', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52416', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52417', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52418', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52419', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52420', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52421', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52422', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52423', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52424', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52425', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52426', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52427', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52428', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52429', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52430', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52431', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52432', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52433', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52434', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52435', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52436', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52437', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52438', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52439', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52440', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52441', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52442', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52443', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52444', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52445', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52446', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52447', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52448', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52449', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52450', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52451', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52452', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52453', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52454', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52455', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52456', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52457', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52458', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52459', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52460', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52461', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52462', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52463', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52464', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52465', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52466', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52467', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52468', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52469', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52470', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52471', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52472', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52473', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52474', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52475', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52476', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52477', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52478', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52479', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52480', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52481', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52482', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52483', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52484', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52485', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52486', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52487', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52488', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52489', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52490', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52491', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52492', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52493', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52494', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52495', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52496', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52497', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52498', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52499', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52500', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52501', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52502', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52503', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52504', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52505', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52506', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52507', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52508', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52509', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52510', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52511', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52512', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52513', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52514', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52515', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52516', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52517', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52518', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52519', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52520', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52521', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52522', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52523', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52524', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52525', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52526', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52527', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52528', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52529', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52530', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52531', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52532', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52533', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52534', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52535', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52536', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52537', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52538', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52539', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52540', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52541', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52542', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52543', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52544', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52545', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52546', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52547', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52548', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52549', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52550', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52551', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52552', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52553', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52554', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52555', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52556', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52557', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52558', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52559', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52560', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52561', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52562', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52563', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52564', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52565', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52566', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52567', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52568', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52569', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52570', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52571', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52572', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52573', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52574', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52575', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52576', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52577', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52578', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52579', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52580', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52581', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52582', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52583', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52584', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52585', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52586', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52587', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52588', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52589', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52590', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52591', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52592', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52593', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52594', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52595', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52596', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52597', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52598', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52599', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52600', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52601', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52602', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52603', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52604', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52605', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52606', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52607', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52608', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52609', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52610', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52611', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52612', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52613', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52614', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52615', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52616', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52617', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52618', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52619', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52620', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52621', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52622', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52623', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52624', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52625', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52626', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52627', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52628', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52629', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52630', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52631', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52632', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52633', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52634', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52635', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52636', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52637', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52638', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52639', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52640', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52641', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52642', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52643', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52644', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52645', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52646', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52647', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52648', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52649', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52650', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52651', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52652', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52653', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52654', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52655', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52656', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52657', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52658', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52659', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52660', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52661', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52662', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52663', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52664', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52665', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52666', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52667', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52668', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52669', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52670', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52671', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52672', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52673', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52674', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52675', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52676', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52677', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52678', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52679', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52680', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52681', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52682', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52683', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52684', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52685', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52686', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52687', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52688', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52689', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52690', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52691', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52692', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52693', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52694', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52695', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52696', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52697', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52698', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52699', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52700', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52701', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52702', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52703', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52704', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52705', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52706', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52707', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52708', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52709', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52710', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52711', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52712', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52713', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52714', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52715', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52716', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52717', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52718', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52719', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52720', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52721', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52722', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52723', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52724', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52725', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52726', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52727', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52728', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52729', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52730', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52731', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52732', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52733', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52734', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52735', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52736', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52737', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52738', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52739', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52740', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52741', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52742', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52743', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52744', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52745', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52746', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52747', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52748', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52749', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52750', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52751', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52752', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52753', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52754', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52755', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52756', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52757', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52758', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52759', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52760', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52761', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52762', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52763', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52764', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52765', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52766', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52767', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52768', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52769', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52770', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52771', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52772', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52773', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52774', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52775', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52776', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52777', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52778', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52779', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52780', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52781', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52782', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52783', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52784', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52785', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52786', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52787', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52788', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52789', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52790', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52791', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52792', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52793', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52794', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52795', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52796', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52797', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52798', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52799', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52800', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52801', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52802', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52803', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52804', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52805', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52806', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52807', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52808', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52809', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52810', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52811', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52812', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52813', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52814', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52815', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52816', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52817', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52818', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52819', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52820', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52821', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52822', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52823', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52824', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52825', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52826', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52827', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52828', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52829', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52830', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52831', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52832', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52833', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52834', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52835', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52836', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52837', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52838', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52839', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52840', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52841', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52842', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52843', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52844', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52845', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52846', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52847', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52848', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52849', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52850', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52851', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52852', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52853', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52854', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52855', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52856', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52857', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52858', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52859', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52860', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52861', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52862', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52863', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52864', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52865', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52866', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52867', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52868', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52869', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52870', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52871', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52872', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52873', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52874', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52875', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52876', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52877', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52878', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52879', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52880', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52881', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52882', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52883', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52884', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52885', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52886', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52887', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52888', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52889', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52890', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52891', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52892', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52893', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52894', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52895', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52896', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52897', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52898', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52899', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52900', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52901', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52902', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52903', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52904', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52905', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52906', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52907', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52908', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52909', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52910', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52911', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52912', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52913', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52914', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52915', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52916', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52917', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52918', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52919', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52920', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52921', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52922', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52923', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52924', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52925', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52926', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52927', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52928', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52929', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52930', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52931', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52932', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52933', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52934', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52935', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52936', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52937', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52938', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52939', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52940', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52941', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52942', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52943', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52944', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52945', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52946', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52947', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52948', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52949', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52950', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52951', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52952', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52953', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52954', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52955', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52956', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52957', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52958', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52959', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52960', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52961', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52962', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52963', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52964', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52965', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52966', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52967', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52968', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52969', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52970', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52971', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52972', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52973', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52974', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52975', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52976', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52977', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52978', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52979', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52980', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52981', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52982', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52983', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52984', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52985', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52986', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52987', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52988', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52989', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52990', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52991', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52992', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52993', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52994', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52995', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52996', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52997', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52998', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('52999', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53000', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53001', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53002', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53003', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53004', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53005', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53006', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53007', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53008', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53009', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53010', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53011', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53012', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53013', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53014', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53015', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53016', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53017', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53018', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53019', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53020', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53021', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53022', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53023', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53024', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53025', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53026', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53027', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53028', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53029', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53030', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53031', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53032', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53033', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53034', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53035', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53036', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53037', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53038', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53039', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53040', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53041', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53042', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53043', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53044', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53045', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53046', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53047', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53048', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53049', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53050', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53051', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53052', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53053', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53054', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53055', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53056', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53057', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53058', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53059', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53060', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53061', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53062', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53063', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53064', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53065', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53066', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53067', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53068', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53069', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53070', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53071', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53072', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53073', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53074', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53075', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53076', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53077', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53078', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53079', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53080', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53081', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53082', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53083', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53084', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53085', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53086', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53087', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53088', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53089', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53090', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53091', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53092', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53093', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53094', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53095', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53096', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53097', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53098', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53099', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53100', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53101', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53102', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53103', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53104', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53105', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53106', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53107', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53108', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53109', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53110', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53111', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53112', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53113', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53114', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53115', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53116', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53117', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53118', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53119', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53120', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53121', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53122', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53123', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53124', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53125', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53126', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53127', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53128', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53129', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53130', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53131', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53132', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53133', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53134', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53135', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53136', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53137', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53138', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53139', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53140', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53141', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53142', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53143', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53144', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53145', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53146', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53147', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53148', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53149', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53150', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53151', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53152', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53153', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53154', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53155', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53156', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53157', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53158', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53159', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53160', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53161', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53162', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53163', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53164', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53165', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53166', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53167', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53168', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53169', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53170', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53171', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53172', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53173', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53174', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53175', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53176', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53177', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53178', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53179', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53180', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53181', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53182', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53183', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53184', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53185', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53186', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53187', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53188', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53189', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53190', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53191', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53192', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53193', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53194', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53195', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53196', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53197', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53198', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53199', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53200', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53201', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53202', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53203', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53204', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53205', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53206', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53207', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53208', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53209', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53210', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53211', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53212', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53213', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53214', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53215', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53216', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53217', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53218', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53219', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53220', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53221', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53222', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53223', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53224', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53225', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53226', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53227', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53228', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53229', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53230', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53231', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53232', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53233', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53234', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53235', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53236', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53237', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53238', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53239', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53240', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53241', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53242', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53243', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53244', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53245', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53246', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53247', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53248', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53249', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53250', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53251', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53252', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53253', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53254', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53255', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53256', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53257', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53258', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53259', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53260', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53261', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53262', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53263', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53264', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53265', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53266', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53267', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53268', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53269', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53270', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53271', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53272', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53273', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53274', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53275', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53276', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53277', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53278', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53279', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53280', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53281', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53282', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53283', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53284', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53285', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53286', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53287', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53288', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53289', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53290', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53291', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53292', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53293', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53294', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53295', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53296', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53297', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53298', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53299', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53300', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53301', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53302', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53303', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53304', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53305', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53306', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53307', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53308', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53309', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53310', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53311', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53312', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53313', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53314', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53315', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53316', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53317', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53318', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53319', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53320', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53321', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53322', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53323', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53324', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53325', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53326', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53327', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53328', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53329', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53330', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53331', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53332', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53333', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53334', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53335', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53336', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53337', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53338', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53339', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53340', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53341', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53342', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53343', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53344', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53345', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53346', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53347', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53348', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53349', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53350', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53351', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53352', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53353', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53354', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53355', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53356', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53357', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53358', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53359', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53360', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53361', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53362', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53363', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53364', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53365', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53366', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53367', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53368', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53369', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53370', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53371', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53372', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53373', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53374', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53375', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53376', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53377', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53378', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53379', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53380', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53381', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53382', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53383', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53384', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53385', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53386', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53387', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53388', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53389', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53390', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53391', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53392', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53393', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53394', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53395', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53396', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53397', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53398', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53399', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53400', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53401', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53402', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53403', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53404', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53405', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53406', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53407', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53408', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53409', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53410', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53411', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53412', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53413', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53414', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53415', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53416', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53417', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53418', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53419', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53420', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53421', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53422', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53423', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53424', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53425', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53426', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53427', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53428', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53429', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53430', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53431', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53432', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53433', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53434', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53435', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53436', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53437', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53438', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53439', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53440', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53441', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53442', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53443', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53444', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53445', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53446', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53447', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53448', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53449', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53450', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53451', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53452', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53453', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53454', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53455', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53456', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53457', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53458', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53459', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53460', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53461', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53462', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53463', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53464', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53465', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53466', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53467', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53468', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53469', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53470', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53471', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53472', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53473', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53474', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53475', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53476', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53477', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53478', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53479', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53480', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53481', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53482', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53483', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53484', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53485', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53486', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53487', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53488', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53489', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53490', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53491', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53492', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53493', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53494', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53495', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53496', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53497', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53498', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53499', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53500', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53501', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53502', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53503', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53504', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53505', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53506', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53507', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53508', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53509', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53510', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53511', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53512', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53513', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53514', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53515', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53516', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53517', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53518', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53519', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53520', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53521', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53522', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53523', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53524', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53525', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53526', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53527', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53528', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53529', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53530', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53531', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53532', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53533', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53534', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53535', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53536', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53537', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53538', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53539', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53540', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53541', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53542', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53543', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53544', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53545', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53546', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53547', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53548', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53549', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53550', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53551', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53552', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53553', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53554', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53555', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53556', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53557', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53558', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53559', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53560', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53561', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53562', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53563', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53564', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53565', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53566', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53567', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53568', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53569', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53570', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53571', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53572', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53573', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53574', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53575', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53576', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53577', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53578', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53579', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53580', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53581', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53582', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53583', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53584', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53585', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53586', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53587', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53588', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53589', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53590', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53591', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53592', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53593', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53594', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53595', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53596', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53597', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53598', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53599', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53600', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53601', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53602', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53603', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53604', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53605', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53606', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53607', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53608', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53609', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53610', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53611', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53612', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53613', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53614', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53615', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53616', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53617', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53618', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53619', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53620', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53621', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53622', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53623', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53624', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53625', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53626', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53627', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53628', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53629', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53630', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53631', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53632', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53633', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53634', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53635', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53636', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53637', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53638', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53639', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53640', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53641', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53642', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53643', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53644', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53645', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53646', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53647', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53648', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53649', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53650', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53651', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53652', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53653', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53654', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53655', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53656', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53657', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53658', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53659', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53660', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53661', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53662', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53663', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53664', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53665', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53666', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53667', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53668', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53669', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53670', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53671', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53672', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53673', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53674', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53675', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53676', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53677', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53678', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53679', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53680', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53681', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53682', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53683', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53684', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53685', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53686', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53687', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53688', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53689', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53690', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53691', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53692', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53693', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53694', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53695', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53696', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53697', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53698', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53699', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53700', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53701', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53702', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53703', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53704', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53705', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53706', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53707', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53708', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53709', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53710', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53711', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53712', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53713', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53714', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53715', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53716', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53717', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53718', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53719', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53720', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53721', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53722', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53723', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53724', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53725', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53726', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53727', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53728', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53729', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53730', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53731', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53732', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53733', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53734', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53735', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53736', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53737', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53738', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53739', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53740', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53741', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53742', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53743', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53744', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53745', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53746', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53747', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53748', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53749', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53750', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53751', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53752', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53753', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53754', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53755', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53756', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53757', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53758', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53759', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53760', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53761', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53762', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53763', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53764', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53765', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53766', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53767', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53768', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53769', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53770', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53771', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53772', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53773', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53774', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53775', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53776', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53777', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53778', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53779', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53780', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53781', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53782', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53783', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53784', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53785', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53786', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53787', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53788', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53789', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53790', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53791', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53792', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53793', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53794', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53795', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53796', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53797', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53798', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53799', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53800', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53801', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53802', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53803', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53804', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53805', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53806', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53807', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53808', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53809', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53810', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53811', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53812', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53813', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53814', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53815', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53816', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53817', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53818', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53819', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53820', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53821', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53822', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53823', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53824', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53825', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53826', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53827', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53828', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53829', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53830', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53831', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53832', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53833', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53834', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53835', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53836', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53837', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53838', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53839', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53840', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53841', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53842', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53843', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53844', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53845', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53846', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53847', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53848', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53849', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53850', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53851', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53852', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53853', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53854', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53855', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53856', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53857', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53858', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53859', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53860', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53861', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53862', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53863', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53864', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53865', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53866', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53867', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53868', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53869', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53870', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53871', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53872', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53873', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53874', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53875', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53876', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53877', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53878', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53879', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53880', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53881', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53882', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53883', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53884', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53885', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53886', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53887', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53888', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53889', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53890', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53891', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53892', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53893', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53894', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53895', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53896', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53897', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53898', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53899', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53900', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53901', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53902', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53903', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53904', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53905', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53906', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53907', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53908', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53909', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53910', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53911', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53912', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53913', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53914', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53915', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53916', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53917', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53918', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53919', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53920', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53921', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53922', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53923', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53924', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53925', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53926', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53927', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53928', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53929', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53930', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53931', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53932', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53933', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53934', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53935', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53936', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53937', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53938', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53939', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53940', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53941', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53942', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53943', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53944', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53945', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53946', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53947', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53948', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53949', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53950', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53951', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53952', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53953', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53954', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53955', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53956', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53957', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53958', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53959', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53960', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53961', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53962', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53963', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53964', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53965', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53966', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53967', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53968', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53969', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53970', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53971', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53972', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53973', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53974', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53975', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53976', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53977', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53978', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53979', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53980', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53981', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53982', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53983', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53984', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53985', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53986', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53987', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53988', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53989', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53990', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53991', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53992', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53993', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53994', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53995', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53996', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53997', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53998', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('53999', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54000', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54001', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54002', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54003', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54004', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54005', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54006', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54007', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54008', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54009', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54010', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54011', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54012', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54013', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54014', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54015', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54016', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54017', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54018', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54019', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54020', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54021', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54022', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54023', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54024', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54025', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54026', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54027', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54028', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54029', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54030', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54031', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54032', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54033', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54034', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54035', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54036', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54037', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54038', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54039', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54040', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54041', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54042', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54043', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54044', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54045', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54046', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54047', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54048', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54049', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54050', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54051', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54052', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54053', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54054', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54055', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54056', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54057', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54058', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54059', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54060', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54061', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54062', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54063', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54064', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54065', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54066', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54067', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54068', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54069', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54070', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54071', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54072', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54073', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54074', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54075', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54076', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54077', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54078', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54079', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54080', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54081', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54082', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54083', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54084', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54085', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54086', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54087', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54088', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54089', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54090', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54091', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54092', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54093', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54094', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54095', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54096', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54097', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54098', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54099', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54100', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54101', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54102', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54103', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54104', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54105', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54106', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54107', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54108', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54109', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54110', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54111', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54112', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54113', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54114', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54115', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54116', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54117', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54118', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54119', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54120', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54121', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54122', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54123', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54124', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54125', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54126', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54127', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54128', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54129', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54130', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54131', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54132', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54133', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54134', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54135', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54136', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54137', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54138', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54139', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54140', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54141', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54142', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54143', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54144', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54145', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54146', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54147', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54148', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54149', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54150', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54151', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54152', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54153', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54154', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54155', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54156', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54157', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54158', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54159', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54160', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54161', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54162', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54163', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54164', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54165', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54166', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54167', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54168', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54169', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54170', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54171', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54172', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54173', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54174', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54175', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54176', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54177', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54178', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54179', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54180', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54181', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54182', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54183', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54184', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54185', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54186', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54187', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54188', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54189', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54190', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54191', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54192', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54193', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54194', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54195', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54196', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54197', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54198', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54199', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54200', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54201', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54202', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54203', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54204', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54205', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54206', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54207', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54208', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54209', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54210', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54211', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54212', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54213', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54214', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54215', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54216', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54217', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54218', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54219', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54220', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54221', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54222', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54223', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54224', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54225', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54226', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54227', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54228', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54229', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54230', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54231', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54232', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54233', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54234', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54235', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54236', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54237', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54238', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54239', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54240', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54241', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54242', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54243', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54244', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54245', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54246', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54247', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54248', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54249', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54250', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54251', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54252', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54253', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54254', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54255', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54256', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54257', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54258', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54259', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54260', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54261', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54262', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54263', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54264', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54265', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54266', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54267', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54268', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54269', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54270', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54271', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54272', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54273', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54274', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54275', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54276', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54277', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54278', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54279', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54280', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54281', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54282', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54283', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54284', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54285', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54286', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54287', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54288', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54289', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54290', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54291', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54292', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54293', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54294', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54295', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54296', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54297', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54298', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54299', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54300', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54301', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54302', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54303', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54304', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54305', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54306', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54307', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54308', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54309', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54310', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54311', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54312', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54313', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54314', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54315', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54316', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54317', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54318', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54319', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54320', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54321', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54322', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54323', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54324', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54325', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54326', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54327', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54328', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54329', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54330', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54331', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54332', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54333', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54334', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54335', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54336', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54337', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54338', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54339', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54340', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54341', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54342', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54343', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54344', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54345', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54346', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54347', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54348', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54349', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54350', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54351', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54352', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54353', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54354', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54355', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54356', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54357', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54358', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54359', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54360', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54361', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54362', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54363', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54364', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54365', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54366', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54367', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54368', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54369', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54370', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54371', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54372', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54373', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54374', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54375', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54376', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54377', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54378', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54379', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54380', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54381', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54382', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54383', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54384', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54385', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54386', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54387', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54388', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54389', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54390', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54391', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54392', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54393', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54394', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54395', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54396', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54397', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54398', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54399', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54400', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54401', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54402', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54403', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54404', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54405', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54406', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54407', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54408', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54409', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54410', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54411', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54412', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54413', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54414', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54415', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54416', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54417', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54418', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54419', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54420', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54421', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54422', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54423', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54424', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54425', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54426', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54427', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54428', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54429', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54430', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54431', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54432', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54433', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54434', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54435', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54436', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54437', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54438', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54439', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54440', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54441', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54442', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54443', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54444', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54445', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54446', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54447', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54448', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54449', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54450', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54451', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54452', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54453', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54454', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54455', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54456', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54457', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54458', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54459', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54460', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54461', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54462', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54463', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54464', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54465', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54466', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54467', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54468', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54469', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54470', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54471', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54472', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54473', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54474', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54475', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54476', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54477', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54478', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54479', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54480', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54481', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54482', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54483', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54484', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54485', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54486', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54487', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54488', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54489', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54490', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54491', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54492', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54493', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54494', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54495', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54496', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54497', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54498', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54499', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54500', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54501', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54502', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54503', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54504', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54505', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54506', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54507', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54508', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54509', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54510', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54511', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54512', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54513', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54514', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54515', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54516', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54517', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54518', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54519', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54520', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54521', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54522', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54523', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54524', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54525', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54526', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54527', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54528', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54529', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54530', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54531', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54532', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54533', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54534', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54535', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54536', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54537', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54538', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54539', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54540', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54541', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54542', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54543', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54544', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54545', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54546', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54547', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54548', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54549', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54550', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54551', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54552', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54553', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54554', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54555', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54556', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54557', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54558', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54559', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54560', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54561', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54562', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54563', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54564', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54565', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54566', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54567', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54568', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54569', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54570', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54571', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54572', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54573', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54574', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54575', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54576', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54577', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54578', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54579', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54580', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54581', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54582', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54583', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54584', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54585', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54586', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54587', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54588', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54589', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54590', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54591', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54592', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54593', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54594', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54595', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54596', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54597', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54598', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54599', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54600', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54601', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54602', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54603', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54604', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54605', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54606', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54607', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54608', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54609', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54610', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54611', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54612', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54613', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54614', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54615', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54616', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54617', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54618', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54619', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54620', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54621', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54622', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54623', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54624', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54625', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54626', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54627', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54628', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54629', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54630', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54631', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54632', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54633', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54634', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54635', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54636', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54637', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54638', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54639', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54640', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54641', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54642', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54643', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54644', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54645', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54646', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54647', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54648', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54649', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54650', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54651', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54652', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54653', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54654', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54655', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54656', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54657', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54658', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54659', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54660', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54661', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54662', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54663', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54664', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54665', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54666', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54667', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54668', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54669', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54670', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54671', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54672', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54673', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54674', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54675', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54676', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54677', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54678', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54679', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54680', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54681', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54682', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54683', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54684', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54685', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54686', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54687', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54688', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54689', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54690', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54691', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54692', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54693', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54694', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54695', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54696', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54697', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54698', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54699', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54700', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54701', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54702', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54703', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54704', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54705', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54706', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54707', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54708', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54709', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54710', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54711', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54712', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54713', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54714', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54715', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54716', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54717', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54718', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54719', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54720', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54721', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54722', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54723', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54724', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54725', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54726', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54727', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54728', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54729', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54730', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54731', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54732', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54733', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54734', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54735', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54736', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54737', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54738', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54739', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54740', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54741', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54742', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54743', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54744', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54745', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54746', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54747', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54748', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54749', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54750', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54751', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54752', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54753', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54754', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54755', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54756', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54757', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54758', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54759', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54760', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54761', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54762', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54763', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54764', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54765', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54766', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54767', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54768', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54769', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54770', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54771', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54772', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54773', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54774', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54775', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54776', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54777', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54778', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54779', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54780', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54781', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54782', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54783', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54784', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54785', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54786', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54787', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54788', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54789', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54790', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54791', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54792', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54793', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54794', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54795', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54796', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54797', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54798', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54799', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54800', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54801', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54802', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54803', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54804', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54805', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54806', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54807', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54808', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54809', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54810', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54811', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54812', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54813', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54814', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54815', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54816', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54817', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54818', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54819', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54820', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54821', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54822', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54823', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54824', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54825', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54826', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54827', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54828', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54829', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54830', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54831', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54832', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54833', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54834', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54835', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54836', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54837', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54838', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54839', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54840', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54841', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54842', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54843', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54844', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54845', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54846', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54847', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54848', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54849', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54850', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54851', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54852', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54853', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54854', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54855', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54856', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54857', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54858', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54859', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54860', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54861', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54862', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54863', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54864', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54865', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54866', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54867', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54868', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54869', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54870', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54871', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54872', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54873', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54874', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54875', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54876', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54877', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54878', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54879', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54880', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54881', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54882', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54883', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54884', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54885', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54886', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54887', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54888', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54889', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54890', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54891', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54892', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54893', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54894', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54895', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54896', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54897', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54898', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54899', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54900', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54901', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54902', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54903', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54904', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54905', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54906', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54907', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54908', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54909', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54910', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54911', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54912', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54913', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54914', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54915', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54916', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54917', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54918', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54919', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54920', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54921', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54922', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54923', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54924', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54925', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54926', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54927', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54928', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54929', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54930', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54931', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54932', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54933', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54934', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54935', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54936', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54937', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54938', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54939', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54940', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54941', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54942', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54943', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54944', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54945', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54946', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54947', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54948', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54949', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54950', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54951', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54952', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54953', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54954', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54955', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54956', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54957', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54958', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54959', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54960', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54961', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54962', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54963', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54964', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54965', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54966', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54967', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54968', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54969', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54970', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54971', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54972', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54973', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54974', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54975', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54976', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54977', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54978', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54979', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54980', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54981', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54982', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54983', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54984', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54985', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54986', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54987', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54988', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54989', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54990', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54991', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54992', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54993', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54994', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54995', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54996', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54997', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54998', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('54999', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55000', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55001', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55002', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55003', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55004', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55005', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55006', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55007', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55008', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55009', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55010', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55011', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55012', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55013', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55014', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55015', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55016', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55017', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55018', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55019', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55020', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55021', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55022', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55023', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55024', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55025', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55026', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55027', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55028', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55029', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55030', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55031', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55032', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55033', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55034', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55035', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55036', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55037', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55038', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55039', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55040', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55041', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55042', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55043', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55044', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55045', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55046', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55047', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55048', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55049', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55050', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55051', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55052', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55053', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55054', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55055', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55056', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55057', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55058', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55059', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55060', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55061', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55062', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55063', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55064', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55065', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55066', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55067', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55068', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55069', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55070', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55071', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55072', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55073', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55074', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55075', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55076', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55077', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55078', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55079', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55080', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55081', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55082', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55083', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55084', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55085', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55086', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55087', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55088', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55089', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55090', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55091', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55092', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55093', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55094', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55095', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55096', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55097', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55098', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55099', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55100', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55101', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55102', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55103', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55104', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55105', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55106', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55107', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55108', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55109', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55110', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55111', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55112', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55113', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55114', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55115', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55116', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55117', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55118', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55119', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55120', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55121', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55122', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55123', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55124', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55125', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55126', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55127', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55128', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55129', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55130', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55131', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55132', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55133', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55134', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55135', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55136', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55137', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55138', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55139', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55140', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55141', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55142', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55143', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55144', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55145', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55146', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55147', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55148', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55149', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55150', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55151', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55152', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55153', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55154', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55155', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55156', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55157', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55158', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55159', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55160', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55161', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55162', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55163', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55164', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55165', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55166', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55167', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55168', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55169', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55170', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55171', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55172', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55173', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55174', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55175', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55176', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55177', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55178', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55179', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55180', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55181', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55182', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55183', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55184', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55185', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55186', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55187', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55188', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55189', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55190', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55191', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55192', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55193', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55194', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55195', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55196', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55197', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55198', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55199', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55200', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55201', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55202', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55203', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55204', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55205', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55206', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55207', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55208', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55209', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55210', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55211', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55212', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55213', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55214', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55215', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55216', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55217', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55218', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55219', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55220', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55221', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55222', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55223', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55224', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55225', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55226', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55227', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55228', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55229', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55230', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55231', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55232', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55233', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55234', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55235', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55236', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55237', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55238', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55239', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55240', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55241', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55242', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55243', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55244', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55245', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55246', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55247', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55248', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55249', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55250', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55251', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55252', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55253', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55254', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55255', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55256', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55257', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55258', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55259', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55260', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55261', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55262', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55263', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55264', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55265', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55266', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55267', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55268', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55269', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55270', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55271', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55272', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55273', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55274', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55275', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55276', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55277', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55278', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55279', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55280', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55281', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55282', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55283', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55284', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55285', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55286', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55287', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55288', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55289', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55290', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55291', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55292', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55293', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55294', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55295', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55296', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55297', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55298', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55299', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55300', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55301', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55302', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55303', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55304', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55305', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55306', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55307', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55308', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55309', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55310', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55311', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55312', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55313', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55314', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55315', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55316', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55317', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55318', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55319', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55320', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55321', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55322', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55323', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55324', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55325', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55326', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55327', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55328', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55329', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55330', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55331', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55332', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55333', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55334', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55335', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55336', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55337', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55338', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55339', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55340', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55341', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55342', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55343', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55344', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55345', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55346', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55347', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55348', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55349', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55350', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55351', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55352', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55353', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55354', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55355', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55356', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55357', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55358', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55359', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55360', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55361', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55362', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55363', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55364', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55365', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55366', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55367', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55368', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55369', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55370', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55371', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55372', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55373', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55374', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55375', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55376', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55377', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55378', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55379', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55380', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55381', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55382', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55383', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55384', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55385', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55386', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55387', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55388', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55389', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55390', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55391', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55392', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55393', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55394', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55395', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55396', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55397', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55398', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55399', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55400', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55401', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55402', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55403', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55404', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55405', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55406', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55407', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55408', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55409', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55410', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55411', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55412', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55413', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55414', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55415', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55416', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55417', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55418', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55419', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55420', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55421', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55422', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55423', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55424', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55425', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55426', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55427', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55428', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55429', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55430', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55431', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55432', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55433', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55434', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55435', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55436', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55437', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55438', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55439', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55440', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55441', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55442', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55443', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55444', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55445', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55446', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55447', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55448', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55449', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55450', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55451', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55452', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55453', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55454', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55455', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55456', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55457', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55458', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55459', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55460', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55461', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55462', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55463', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55464', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55465', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55466', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55467', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55468', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55469', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55470', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55471', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55472', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55473', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55474', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55475', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55476', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55477', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55478', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55479', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55480', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55481', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55482', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55483', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55484', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55485', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55486', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55487', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55488', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55489', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55490', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55491', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55492', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55493', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55494', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55495', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55496', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55497', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55498', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55499', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55500', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55501', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55502', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55503', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55504', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55505', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55506', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55507', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55508', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55509', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55510', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55511', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55512', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55513', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55514', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55515', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55516', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55517', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55518', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55519', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55520', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55521', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55522', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55523', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55524', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55525', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55526', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55527', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55528', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55529', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55530', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55531', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55532', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55533', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55534', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55535', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55536', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55537', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55538', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55539', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55540', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55541', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55542', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55543', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55544', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55545', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55546', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55547', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55548', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55549', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55550', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55551', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55552', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55553', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55554', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55555', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55556', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55557', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55558', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55559', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55560', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55561', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55562', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55563', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55564', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55565', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55566', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55567', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55568', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55569', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55570', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55571', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55572', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55573', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55574', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55575', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55576', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55577', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55578', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55579', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55580', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55581', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55582', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55583', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55584', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55585', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55586', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55587', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55588', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55589', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55590', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55591', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55592', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55593', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55594', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55595', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55596', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55597', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55598', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55599', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55600', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55601', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55602', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55603', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55604', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55605', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55606', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55607', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55608', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55609', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55610', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55611', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55612', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55613', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55614', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55615', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55616', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55617', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55618', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55619', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55620', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55621', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55622', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55623', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55624', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55625', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55626', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55627', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55628', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55629', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55630', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55631', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55632', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55633', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55634', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55635', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55636', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55637', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55638', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55639', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55640', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55641', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55642', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55643', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55644', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55645', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55646', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55647', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55648', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55649', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55650', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55651', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55652', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55653', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55654', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55655', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55656', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55657', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55658', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55659', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55660', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55661', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55662', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55663', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55664', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55665', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55666', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55667', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55668', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55669', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55670', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55671', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55672', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55673', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55674', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55675', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55676', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55677', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55678', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55679', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55680', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55681', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55682', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55683', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55684', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55685', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55686', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55687', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55688', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55689', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55690', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55691', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55692', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55693', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55694', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55695', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55696', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55697', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55698', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55699', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55700', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55701', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55702', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55703', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55704', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55705', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55706', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55707', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55708', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55709', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55710', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55711', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55712', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55713', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55714', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55715', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55716', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55717', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55718', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55719', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55720', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55721', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55722', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55723', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55724', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55725', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55726', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55727', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55728', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55729', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55730', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55731', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55732', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55733', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55734', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55735', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55736', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55737', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55738', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55739', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55740', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55741', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55742', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55743', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55744', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55745', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55746', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55747', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55748', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55749', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55750', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55751', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55752', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55753', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55754', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55755', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55756', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55757', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55758', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55759', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55760', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55761', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55762', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55763', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55764', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55765', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55766', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55767', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55768', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55769', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55770', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55771', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55772', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55773', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55774', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55775', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55776', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55777', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55778', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55779', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55780', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55781', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55782', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55783', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55784', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55785', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55786', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55787', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55788', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55789', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55790', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55791', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55792', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55793', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55794', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55795', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55796', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55797', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55798', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55799', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55800', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55801', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55802', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55803', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55804', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55805', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55806', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55807', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55808', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55809', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55810', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55811', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55812', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55813', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55814', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55815', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55816', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55817', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55818', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55819', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55820', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55821', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55822', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55823', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55824', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55825', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55826', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55827', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55828', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55829', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55830', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55831', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55832', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55833', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55834', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55835', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55836', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55837', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55838', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55839', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55840', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55841', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55842', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55843', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55844', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55845', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55846', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55847', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55848', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55849', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55850', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55851', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55852', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55853', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55854', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55855', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55856', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55857', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55858', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55859', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55860', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55861', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55862', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55863', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55864', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55865', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55866', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55867', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55868', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55869', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55870', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55871', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55872', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55873', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55874', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55875', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55876', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55877', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55878', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55879', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55880', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55881', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55882', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55883', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55884', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55885', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55886', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55887', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55888', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55889', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55890', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55891', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55892', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55893', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55894', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55895', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55896', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55897', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55898', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55899', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55900', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55901', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55902', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55903', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55904', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55905', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55906', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55907', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55908', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55909', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55910', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55911', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55912', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55913', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55914', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55915', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55916', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55917', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55918', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55919', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55920', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55921', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55922', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55923', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55924', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55925', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55926', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55927', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55928', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55929', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55930', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55931', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55932', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55933', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55934', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55935', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55936', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55937', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55938', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55939', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55940', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55941', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55942', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55943', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55944', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55945', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55946', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55947', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55948', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55949', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55950', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55951', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55952', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55953', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55954', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55955', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55956', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55957', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55958', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55959', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55960', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55961', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55962', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55963', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55964', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55965', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55966', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55967', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55968', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55969', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55970', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55971', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55972', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55973', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55974', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55975', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55976', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55977', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55978', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55979', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55980', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55981', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55982', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55983', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55984', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55985', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55986', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55987', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55988', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55989', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55990', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55991', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55992', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55993', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55994', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55995', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55996', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55997', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55998', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('55999', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56000', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56001', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56002', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56003', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56004', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56005', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56006', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56007', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56008', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56009', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56010', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56011', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56012', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56013', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56014', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56015', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56016', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56017', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56018', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56019', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56020', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56021', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56022', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56023', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56024', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56025', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56026', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56027', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56028', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56029', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56030', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56031', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56032', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56033', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56034', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56035', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56036', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56037', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56038', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56039', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56040', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56041', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56042', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56043', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56044', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56045', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56046', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56047', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56048', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56049', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56050', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56051', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56052', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56053', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56054', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56055', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56056', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56057', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56058', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56059', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56060', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56061', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56062', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56063', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56064', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56065', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56066', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56067', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56068', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56069', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56070', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56071', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56072', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56073', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56074', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56075', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56076', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56077', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56078', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56079', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56080', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56081', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56082', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56083', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56084', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56085', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56086', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56087', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56088', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56089', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56090', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56091', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56092', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56093', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56094', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56095', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56096', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56097', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56098', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56099', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56100', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56101', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56102', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56103', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56104', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56105', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56106', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56107', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56108', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56109', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56110', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56111', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56112', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56113', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56114', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56115', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56116', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56117', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56118', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56119', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56120', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56121', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56122', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56123', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56124', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56125', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56126', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56127', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56128', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56129', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56130', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56131', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56132', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56133', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56134', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56135', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56136', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56137', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56138', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56139', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56140', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56141', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56142', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56143', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56144', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56145', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56146', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56147', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56148', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56149', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56150', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56151', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56152', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56153', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56154', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56155', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56156', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56157', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56158', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56159', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56160', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56161', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56162', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56163', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56164', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56165', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56166', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56167', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56168', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56169', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56170', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56171', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56172', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56173', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56174', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56175', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56176', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56177', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56178', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56179', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56180', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56181', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56182', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56183', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56184', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56185', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56186', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56187', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56188', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56189', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56190', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56191', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56192', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56193', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56194', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56195', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56196', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56197', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56198', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56199', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56200', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56201', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56202', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56203', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56204', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56205', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56206', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56207', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56208', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56209', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56210', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56211', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56212', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56213', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56214', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56215', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56216', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56217', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56218', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56219', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56220', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56221', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56222', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56223', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56224', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56225', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56226', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56227', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56228', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56229', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56230', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56231', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56232', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56233', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56234', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56235', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56236', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56237', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56238', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56239', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56240', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56241', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56242', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56243', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56244', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56245', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56246', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56247', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56248', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56249', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56250', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56251', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56252', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56253', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56254', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56255', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56256', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56257', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56258', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56259', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56260', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56261', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56262', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56263', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56264', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56265', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56266', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56267', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56268', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56269', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56270', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56271', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56272', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56273', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56274', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56275', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56276', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56277', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56278', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56279', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56280', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56281', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56282', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56283', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56284', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56285', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56286', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56287', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56288', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56289', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56290', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56291', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56292', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56293', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56294', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56295', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56296', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56297', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56298', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56299', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56300', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56301', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56302', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56303', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56304', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56305', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56306', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56307', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56308', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56309', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56310', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56311', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56312', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56313', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56314', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56315', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56316', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56317', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56318', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56319', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56320', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56321', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56322', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56323', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56324', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56325', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56326', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56327', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56328', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56329', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56330', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56331', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56332', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56333', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56334', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56335', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56336', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56337', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56338', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56339', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56340', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56341', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56342', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56343', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56344', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56345', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56346', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56347', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56348', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56349', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56350', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56351', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56352', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56353', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56354', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56355', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56356', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56357', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56358', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56359', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56360', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56361', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56362', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56363', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56364', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56365', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56366', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56367', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56368', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56369', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56370', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56371', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56372', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56373', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56374', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56375', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56376', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56377', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56378', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56379', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56380', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56381', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56382', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56383', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56384', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56385', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56386', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56387', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56388', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56389', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56390', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56391', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56392', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56393', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56394', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56395', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56396', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56397', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56398', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56399', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56400', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56401', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56402', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56403', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56404', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56405', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56406', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56407', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56408', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56409', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56410', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56411', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56412', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56413', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56414', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56415', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56416', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56417', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56418', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56419', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56420', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56421', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56422', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56423', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56424', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56425', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56426', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56427', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56428', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56429', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56430', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56431', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56432', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56433', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56434', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56435', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56436', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56437', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56438', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56439', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56440', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56441', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56442', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56443', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56444', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56445', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56446', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56447', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56448', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56449', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56450', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56451', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56452', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56453', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56454', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56455', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56456', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56457', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56458', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56459', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56460', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56461', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56462', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56463', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56464', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56465', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56466', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56467', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56468', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56469', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56470', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56471', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56472', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56473', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56474', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56475', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56476', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56477', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56478', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56479', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56480', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56481', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56482', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56483', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56484', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56485', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56486', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56487', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56488', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56489', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56490', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56491', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56492', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56493', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56494', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56495', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56496', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56497', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56498', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56499', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56500', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56501', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56502', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56503', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56504', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56505', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56506', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56507', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56508', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56509', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56510', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56511', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56512', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56513', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56514', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56515', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56516', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56517', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56518', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56519', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56520', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56521', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56522', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56523', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56524', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56525', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56526', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56527', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56528', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56529', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56530', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56531', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56532', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56533', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56534', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56535', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56536', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56537', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56538', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56539', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56540', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56541', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56542', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56543', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56544', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56545', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56546', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56547', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56548', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56549', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56550', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56551', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56552', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56553', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56554', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56555', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56556', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56557', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56558', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56559', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56560', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56561', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56562', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56563', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56564', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56565', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56566', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56567', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56568', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56569', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56570', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56571', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56572', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56573', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56574', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56575', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56576', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56577', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56578', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56579', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56580', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56581', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56582', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56583', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56584', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56585', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56586', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56587', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56588', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56589', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56590', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56591', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56592', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56593', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56594', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56595', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56596', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56597', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56598', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56599', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56600', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56601', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56602', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56603', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56604', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56605', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56606', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56607', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56608', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56609', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56610', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56611', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56612', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56613', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56614', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56615', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56616', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56617', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56618', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56619', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56620', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56621', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56622', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56623', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56624', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56625', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56626', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56627', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56628', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56629', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56630', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56631', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56632', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56633', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56634', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56635', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56636', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56637', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56638', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56639', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56640', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56641', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56642', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56643', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56644', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56645', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56646', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56647', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56648', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56649', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56650', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56651', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56652', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56653', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56654', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56655', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56656', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56657', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56658', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56659', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56660', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56661', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56662', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56663', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56664', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56665', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56666', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56667', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56668', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56669', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56670', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56671', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56672', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56673', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56674', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56675', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56676', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56677', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56678', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56679', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56680', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56681', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56682', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56683', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56684', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56685', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56686', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56687', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56688', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56689', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56690', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56691', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56692', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56693', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56694', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56695', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56696', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56697', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56698', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56699', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56700', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56701', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56702', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56703', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56704', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56705', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56706', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56707', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56708', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56709', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56710', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56711', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56712', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56713', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56714', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56715', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56716', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56717', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56718', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56719', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56720', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56721', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56722', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56723', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56724', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56725', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56726', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56727', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56728', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56729', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56730', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56731', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56732', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56733', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56734', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56735', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56736', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56737', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56738', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56739', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56740', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56741', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56742', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56743', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56744', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56745', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56746', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56747', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56748', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56749', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56750', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56751', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56752', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56753', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56754', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56755', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56756', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56757', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56758', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56759', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56760', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56761', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56762', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56763', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56764', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56765', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56766', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56767', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56768', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56769', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56770', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56771', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56772', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56773', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56774', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56775', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56776', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56777', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56778', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56779', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56780', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56781', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56782', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56783', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56784', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56785', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56786', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56787', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56788', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56789', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56790', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56791', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56792', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56793', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56794', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56795', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56796', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56797', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56798', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56799', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56800', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56801', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56802', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56803', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56804', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56805', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56806', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56807', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56808', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56809', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56810', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56811', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56812', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56813', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56814', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56815', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56816', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56817', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56818', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56819', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56820', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56821', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56822', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56823', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56824', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56825', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56826', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56827', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56828', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56829', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56830', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56831', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56832', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56833', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56834', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56835', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56836', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56837', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56838', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56839', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56840', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56841', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56842', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56843', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56844', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56845', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56846', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56847', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56848', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56849', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56850', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56851', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56852', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56853', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56854', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56855', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56856', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56857', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56858', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56859', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56860', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56861', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56862', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56863', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56864', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56865', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56866', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56867', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56868', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56869', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56870', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56871', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56872', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56873', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56874', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56875', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56876', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56877', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56878', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56879', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56880', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56881', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56882', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56883', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56884', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56885', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56886', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56887', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56888', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56889', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56890', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56891', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56892', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56893', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56894', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56895', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56896', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56897', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56898', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56899', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56900', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56901', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56902', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56903', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56904', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56905', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56906', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56907', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56908', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56909', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56910', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56911', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56912', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56913', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56914', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56915', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56916', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56917', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56918', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56919', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56920', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56921', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56922', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56923', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56924', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56925', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56926', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56927', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56928', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56929', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56930', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56931', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56932', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56933', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56934', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56935', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56936', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56937', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56938', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56939', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56940', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56941', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56942', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56943', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56944', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56945', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56946', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56947', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56948', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56949', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56950', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56951', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56952', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56953', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56954', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56955', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56956', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56957', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56958', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56959', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56960', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56961', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56962', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56963', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56964', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56965', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56966', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56967', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56968', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56969', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56970', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56971', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56972', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56973', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56974', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56975', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56976', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56977', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56978', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56979', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56980', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56981', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56982', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56983', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56984', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56985', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56986', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56987', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56988', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56989', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56990', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56991', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56992', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56993', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56994', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56995', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56996', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56997', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56998', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('56999', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57000', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57001', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57002', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57003', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57004', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57005', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57006', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57007', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57008', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57009', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57010', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57011', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57012', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57013', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57014', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57015', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57016', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57017', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57018', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57019', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57020', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57021', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57022', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57023', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57024', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57025', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57026', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57027', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57028', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57029', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57030', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57031', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57032', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57033', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57034', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57035', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57036', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57037', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57038', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57039', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57040', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57041', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57042', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57043', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57044', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57045', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57046', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57047', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57048', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57049', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57050', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57051', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57052', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57053', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57054', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57055', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57056', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57057', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57058', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57059', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57060', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57061', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57062', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57063', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57064', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57065', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57066', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57067', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57068', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57069', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57070', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57071', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57072', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57073', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57074', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57075', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57076', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57077', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57078', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57079', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57080', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57081', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57082', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57083', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57084', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57085', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57086', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57087', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57088', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57089', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57090', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57091', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57092', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57093', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57094', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57095', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57096', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57097', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57098', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57099', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57100', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57101', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57102', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57103', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57104', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57105', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57106', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57107', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57108', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57109', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57110', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57111', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57112', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57113', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57114', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57115', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57116', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57117', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57118', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57119', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57120', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57121', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57122', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57123', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57124', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57125', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57126', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57127', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57128', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57129', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57130', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57131', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57132', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57133', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57134', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57135', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57136', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57137', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57138', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57139', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57140', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57141', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57142', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57143', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57144', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57145', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57146', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57147', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57148', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57149', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57150', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57151', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57152', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57153', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57154', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57155', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57156', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57157', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57158', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57159', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57160', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57161', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57162', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57163', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57164', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57165', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57166', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57167', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57168', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57169', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57170', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57171', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57172', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57173', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57174', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57175', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57176', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57177', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57178', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57179', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57180', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57181', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57182', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57183', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57184', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57185', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57186', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57187', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57188', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57189', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57190', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57191', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57192', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57193', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57194', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57195', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57196', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57197', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57198', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57199', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57200', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57201', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57202', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57203', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57204', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57205', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57206', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57207', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57208', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57209', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57210', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57211', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57212', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57213', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57214', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57215', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57216', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57217', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57218', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57219', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57220', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57221', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57222', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57223', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57224', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57225', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57226', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57227', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57228', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57229', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57230', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57231', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57232', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57233', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57234', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57235', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57236', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57237', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57238', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57239', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57240', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57241', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57242', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57243', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57244', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57245', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57246', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57247', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57248', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57249', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57250', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57251', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57252', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57253', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57254', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57255', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57256', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57257', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57258', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57259', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57260', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57261', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57262', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57263', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57264', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57265', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57266', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57267', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57268', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57269', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57270', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57271', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57272', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57273', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57274', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57275', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57276', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57277', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57278', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57279', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57280', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57281', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57282', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57283', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57284', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57285', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57286', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57287', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57288', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57289', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57290', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57291', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57292', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57293', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57294', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57295', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57296', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57297', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57298', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57299', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57300', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57301', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57302', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57303', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57304', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57305', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57306', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57307', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57308', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57309', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57310', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57311', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57312', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57313', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57314', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57315', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57316', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57317', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57318', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57319', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57320', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57321', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57322', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57323', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57324', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57325', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57326', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57327', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57328', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57329', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57330', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57331', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57332', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57333', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57334', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57335', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57336', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57337', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57338', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57339', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57340', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57341', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57342', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57343', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57344', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57345', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57346', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57347', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57348', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57349', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57350', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57351', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57352', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57353', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57354', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57355', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57356', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57357', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57358', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57359', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57360', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57361', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57362', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57363', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57364', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57365', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57366', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57367', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57368', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57369', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57370', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57371', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57372', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57373', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57374', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57375', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57376', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57377', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57378', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57379', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57380', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57381', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57382', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57383', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57384', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57385', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57386', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57387', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57388', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57389', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57390', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57391', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57392', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57393', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57394', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57395', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57396', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57397', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57398', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57399', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57400', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57401', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57402', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57403', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57404', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57405', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57406', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57407', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57408', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57409', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57410', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57411', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57412', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57413', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57414', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57415', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57416', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57417', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57418', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57419', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57420', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57421', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57422', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57423', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57424', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57425', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57426', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57427', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57428', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57429', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57430', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57431', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57432', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57433', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57434', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57435', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57436', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57437', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57438', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57439', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57440', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57441', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57442', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57443', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57444', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57445', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57446', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57447', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57448', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57449', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57450', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57451', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57452', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57453', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57454', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57455', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57456', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57457', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57458', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57459', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57460', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57461', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57462', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57463', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57464', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57465', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57466', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57467', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57468', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57469', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57470', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57471', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57472', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57473', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57474', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57475', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57476', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57477', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57478', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57479', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57480', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57481', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57482', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57483', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57484', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57485', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57486', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57487', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57488', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57489', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57490', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57491', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57492', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57493', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57494', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57495', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57496', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57497', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57498', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57499', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57500', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57501', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57502', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57503', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57504', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57505', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57506', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57507', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57508', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57509', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57510', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57511', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57512', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57513', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57514', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57515', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57516', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57517', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57518', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57519', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57520', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57521', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57522', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57523', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57524', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57525', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57526', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57527', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57528', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57529', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57530', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57531', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57532', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57533', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57534', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57535', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57536', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57537', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57538', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57539', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57540', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57541', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57542', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57543', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57544', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57545', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57546', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57547', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57548', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57549', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57550', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57551', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57552', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57553', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57554', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57555', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57556', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57557', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57558', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57559', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57560', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57561', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57562', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57563', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57564', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57565', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57566', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57567', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57568', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57569', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57570', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57571', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57572', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57573', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57574', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57575', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57576', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57577', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57578', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57579', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57580', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57581', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57582', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57583', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57584', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57585', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57586', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57587', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57588', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57589', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57590', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57591', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57592', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57593', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57594', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57595', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57596', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57597', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57598', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57599', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57600', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57601', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57602', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57603', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57604', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57605', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57606', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57607', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57608', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57609', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57610', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57611', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57612', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57613', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57614', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57615', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57616', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57617', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57618', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57619', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57620', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57621', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57622', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57623', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57624', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57625', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57626', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57627', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57628', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57629', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57630', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57631', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57632', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57633', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57634', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57635', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57636', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57637', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57638', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57639', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57640', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57641', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57642', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57643', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57644', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57645', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57646', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57647', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57648', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57649', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57650', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57651', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57652', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57653', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57654', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57655', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57656', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57657', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57658', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57659', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57660', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57661', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57662', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57663', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57664', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57665', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57666', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57667', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57668', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57669', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57670', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57671', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57672', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57673', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57674', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57675', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57676', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57677', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57678', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57679', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57680', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57681', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57682', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57683', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57684', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57685', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57686', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57687', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57688', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57689', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57690', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57691', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57692', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57693', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57694', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57695', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57696', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57697', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57698', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57699', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57700', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57701', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57702', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57703', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57704', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57705', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57706', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57707', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57708', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57709', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57710', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57711', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57712', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57713', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57714', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57715', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57716', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57717', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57718', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57719', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57720', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57721', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57722', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57723', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57724', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57725', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57726', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57727', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57728', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57729', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57730', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57731', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57732', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57733', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57734', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57735', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57736', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57737', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57738', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57739', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57740', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57741', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57742', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57743', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57744', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57745', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57746', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57747', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57748', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57749', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57750', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57751', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57752', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57753', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57754', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57755', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57756', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57757', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57758', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57759', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57760', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57761', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57762', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57763', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57764', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57765', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57766', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57767', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57768', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57769', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57770', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57771', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57772', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57773', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57774', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57775', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57776', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57777', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57778', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57779', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57780', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57781', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57782', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57783', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57784', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57785', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57786', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57787', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57788', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57789', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57790', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57791', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57792', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57793', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57794', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57795', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57796', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57797', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57798', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57799', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57800', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57801', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57802', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57803', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57804', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57805', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57806', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57807', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57808', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57809', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57810', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57811', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57812', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57813', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57814', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57815', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57816', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57817', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57818', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57819', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57820', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57821', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57822', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57823', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57824', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57825', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57826', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57827', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57828', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57829', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57830', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57831', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57832', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57833', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57834', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57835', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57836', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57837', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57838', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57839', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57840', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57841', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57842', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57843', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57844', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57845', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57846', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57847', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57848', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57849', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57850', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57851', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57852', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57853', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57854', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57855', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57856', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57857', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57858', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57859', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57860', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57861', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57862', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57863', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57864', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57865', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57866', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57867', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57868', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57869', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57870', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57871', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57872', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57873', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57874', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57875', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57876', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57877', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57878', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57879', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57880', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57881', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57882', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57883', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57884', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57885', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57886', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57887', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57888', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57889', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57890', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57891', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57892', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57893', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57894', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57895', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57896', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57897', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57898', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57899', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57900', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57901', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57902', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57903', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57904', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57905', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57906', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57907', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57908', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57909', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57910', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57911', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57912', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57913', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57914', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57915', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57916', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57917', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57918', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57919', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57920', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57921', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57922', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57923', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57924', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57925', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57926', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57927', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57928', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57929', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57930', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57931', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57932', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57933', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57934', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57935', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57936', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57937', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57938', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57939', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57940', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57941', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57942', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57943', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57944', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57945', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57946', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57947', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57948', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57949', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57950', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57951', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57952', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57953', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57954', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57955', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57956', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57957', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57958', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57959', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57960', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57961', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57962', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57963', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57964', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57965', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57966', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57967', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57968', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57969', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57970', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57971', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57972', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57973', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57974', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57975', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57976', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57977', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57978', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57979', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57980', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57981', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57982', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57983', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57984', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57985', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57986', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57987', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57988', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57989', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57990', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57991', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57992', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57993', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57994', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57995', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57996', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57997', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57998', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('57999', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58000', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58001', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58002', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58003', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58004', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58005', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58006', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58007', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58008', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58009', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58010', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58011', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58012', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58013', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58014', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58015', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58016', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58017', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58018', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58019', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58020', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58021', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58022', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58023', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58024', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58025', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58026', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58027', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58028', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58029', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58030', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58031', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58032', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58033', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58034', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58035', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58036', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58037', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58038', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58039', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58040', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58041', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58042', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58043', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58044', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58045', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58046', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58047', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58048', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58049', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58050', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58051', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58052', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58053', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58054', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58055', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58056', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58057', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58058', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58059', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58060', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58061', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58062', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58063', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58064', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58065', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58066', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58067', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58068', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58069', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58070', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58071', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58072', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58073', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58074', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58075', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58076', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58077', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58078', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58079', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58080', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58081', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58082', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58083', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58084', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58085', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58086', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58087', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58088', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58089', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58090', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58091', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58092', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58093', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58094', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58095', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58096', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58097', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58098', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58099', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58100', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58101', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58102', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58103', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58104', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58105', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58106', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58107', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58108', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58109', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58110', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58111', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58112', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58113', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58114', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58115', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58116', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58117', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58118', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58119', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58120', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58121', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58122', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58123', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58124', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58125', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58126', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58127', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58128', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58129', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58130', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58131', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58132', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58133', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58134', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58135', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58136', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58137', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58138', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58139', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58140', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58141', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58142', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58143', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58144', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58145', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58146', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58147', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58148', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58149', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58150', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58151', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58152', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58153', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58154', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58155', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58156', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58157', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58158', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58159', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58160', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58161', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58162', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58163', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58164', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58165', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58166', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58167', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58168', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58169', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58170', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58171', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58172', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58173', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58174', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58175', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58176', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58177', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58178', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58179', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58180', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58181', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58182', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58183', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58184', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58185', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58186', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58187', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58188', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58189', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58190', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58191', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58192', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58193', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58194', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58195', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58196', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58197', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58198', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58199', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58200', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58201', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58202', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58203', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58204', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58205', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58206', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58207', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58208', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58209', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58210', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58211', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58212', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58213', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58214', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58215', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58216', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58217', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58218', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58219', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58220', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58221', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58222', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58223', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58224', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58225', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58226', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58227', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58228', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58229', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58230', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58231', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58232', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58233', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58234', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58235', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58236', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58237', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58238', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58239', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58240', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58241', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58242', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58243', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58244', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58245', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58246', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58247', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58248', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58249', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58250', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58251', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58252', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58253', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58254', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58255', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58256', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58257', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58258', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58259', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58260', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58261', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58262', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58263', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58264', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58265', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58266', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58267', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58268', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58269', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58270', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58271', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58272', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58273', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58274', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58275', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58276', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58277', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58278', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58279', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58280', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58281', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58282', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58283', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58284', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58285', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58286', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58287', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58288', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58289', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58290', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58291', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58292', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58293', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58294', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58295', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58296', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58297', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58298', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58299', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58300', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58301', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58302', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58303', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58304', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58305', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58306', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58307', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58308', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58309', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58310', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58311', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58312', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58313', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58314', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58315', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58316', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58317', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58318', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58319', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58320', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58321', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58322', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58323', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58324', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58325', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58326', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58327', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58328', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58329', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58330', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58331', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58332', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58333', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58334', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58335', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58336', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58337', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58338', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58339', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58340', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58341', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58342', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58343', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58344', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58345', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58346', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58347', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58348', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58349', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58350', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58351', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58352', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58353', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58354', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58355', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58356', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58357', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58358', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58359', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58360', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58361', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58362', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58363', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58364', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58365', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58366', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58367', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58368', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58369', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58370', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58371', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58372', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58373', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58374', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58375', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58376', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58377', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58378', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58379', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58380', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58381', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58382', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58383', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58384', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58385', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58386', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58387', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58388', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58389', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58390', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58391', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58392', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58393', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58394', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58395', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58396', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58397', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58398', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58399', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58400', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58401', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58402', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58403', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58404', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58405', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58406', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58407', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58408', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58409', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58410', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58411', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58412', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58413', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58414', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58415', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58416', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58417', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58418', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58419', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58420', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58421', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58422', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58423', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58424', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58425', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58426', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58427', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58428', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58429', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58430', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58431', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58432', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58433', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58434', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58435', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58436', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58437', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58438', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58439', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58440', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58441', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58442', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58443', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58444', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58445', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58446', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58447', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58448', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58449', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58450', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58451', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58452', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58453', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58454', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58455', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58456', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58457', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58458', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58459', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58460', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58461', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58462', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58463', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58464', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58465', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58466', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58467', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58468', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58469', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58470', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58471', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58472', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58473', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58474', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58475', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58476', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58477', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58478', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58479', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58480', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58481', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58482', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58483', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58484', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58485', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58486', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58487', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58488', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58489', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58490', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58491', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58492', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58493', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58494', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58495', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58496', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58497', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58498', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58499', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58500', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58501', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58502', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58503', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58504', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58505', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58506', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58507', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58508', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58509', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58510', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58511', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58512', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58513', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58514', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58515', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58516', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58517', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58518', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58519', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58520', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58521', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58522', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58523', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58524', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58525', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58526', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58527', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58528', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58529', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58530', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58531', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58532', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58533', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58534', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58535', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58536', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58537', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58538', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58539', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58540', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58541', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58542', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58543', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58544', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58545', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58546', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58547', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58548', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58549', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58550', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58551', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58552', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58553', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58554', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58555', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58556', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58557', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58558', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58559', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58560', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58561', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58562', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58563', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58564', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58565', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58566', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58567', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58568', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58569', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58570', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58571', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58572', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58573', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58574', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58575', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58576', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58577', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58578', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58579', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58580', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58581', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58582', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58583', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58584', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58585', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58586', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58587', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58588', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58589', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58590', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58591', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58592', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58593', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58594', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58595', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58596', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58597', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58598', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58599', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58600', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58601', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58602', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58603', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58604', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58605', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58606', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58607', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58608', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58609', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58610', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58611', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58612', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58613', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58614', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58615', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58616', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58617', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58618', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58619', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58620', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58621', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58622', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58623', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58624', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58625', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58626', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58627', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58628', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58629', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58630', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58631', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58632', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58633', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58634', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58635', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58636', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58637', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58638', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58639', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58640', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58641', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58642', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58643', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58644', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58645', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58646', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58647', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58648', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58649', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58650', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58651', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58652', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58653', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58654', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58655', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58656', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58657', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58658', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58659', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58660', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58661', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58662', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58663', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58664', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58665', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58666', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58667', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58668', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58669', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58670', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58671', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58672', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58673', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58674', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58675', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58676', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58677', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58678', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58679', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58680', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58681', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58682', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58683', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58684', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58685', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58686', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58687', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58688', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58689', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58690', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58691', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58692', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58693', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58694', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58695', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58696', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58697', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58698', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58699', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58700', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58701', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58702', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58703', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58704', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58705', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58706', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58707', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58708', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58709', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58710', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58711', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58712', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58713', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58714', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58715', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58716', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58717', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58718', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58719', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58720', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58721', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58722', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58723', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58724', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58725', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58726', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58727', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58728', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58729', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58730', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58731', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58732', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58733', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58734', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58735', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58736', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58737', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58738', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58739', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58740', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58741', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58742', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58743', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58744', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58745', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58746', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58747', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58748', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58749', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58750', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58751', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58752', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58753', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58754', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58755', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58756', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58757', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58758', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58759', '15', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58760', '16', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58761', '17', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58762', '18', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58763', '19', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58764', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58765', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58766', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58767', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58768', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58769', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58770', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58771', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58772', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58773', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58774', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58775', '15', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58776', '16', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58777', '17', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58778', '18', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58779', '19', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58780', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58781', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58782', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58783', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58784', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58785', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58786', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58787', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58788', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58789', '303', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58790', '304', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58791', '305', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58792', '306', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58793', '307', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58794', '303', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58795', '304', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58796', '305', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58797', '306', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58798', '307', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58799', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58800', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58801', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58802', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58803', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58804', '15', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58805', '16', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58806', '17', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58807', '18', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58808', '302', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58809', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58810', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58811', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58812', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58813', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58814', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58815', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58816', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58817', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58818', '21', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58819', '22', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58820', '23', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58821', '24', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58822', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58823', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58824', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58825', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58826', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58827', '15', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58828', '16', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58829', '17', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58830', '18', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58831', '19', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58832', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58833', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58834', '21', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58835', '22', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58836', '23', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58837', '24', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58838', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58839', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58840', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58841', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58842', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58843', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58844', '21', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58845', '22', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58846', '23', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58847', '24', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58848', '21', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58849', '22', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58850', '23', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58851', '24', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58852', '21', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58853', '22', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58854', '23', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58855', '24', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58856', '26', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58857', '27', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58858', '28', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58859', '29', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58860', '21', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58861', '22', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58862', '23', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58863', '24', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58864', '26', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58865', '27', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58866', '28', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58867', '29', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58868', '26', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58869', '27', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58870', '28', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58871', '29', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58872', '30', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58873', '31', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58874', '32', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58875', '33', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58876', '34', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58877', '21', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58878', '22', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58879', '23', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58880', '24', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58881', '26', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58882', '27', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58883', '28', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58884', '29', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58885', '30', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58886', '31', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58887', '32', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58888', '33', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58889', '34', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58890', '35', '1', 'null');
INSERT INTO `insu_error_msg` VALUES ('58891', '36', '1', 'null');
INSERT INTO `insu_error_msg` VALUES ('58892', '37', '1', 'null');
INSERT INTO `insu_error_msg` VALUES ('58893', '38', '1', 'null');
INSERT INTO `insu_error_msg` VALUES ('58894', '39', '1', 'null');
INSERT INTO `insu_error_msg` VALUES ('58895', '35', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58896', '36', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58897', '37', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58898', '38', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58899', '39', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58900', '21', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58901', '22', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58902', '23', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58903', '24', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58904', '30', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58905', '31', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58906', '32', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58907', '33', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58908', '34', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58909', '35', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58910', '36', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58911', '37', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58912', '38', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58913', '39', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58914', '40', '1', 'id[40业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('58915', '41', '1', 'id[41业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('58916', '42', '1', 'id[42业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('58917', '43', '1', 'id[43业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('58918', '44', '1', 'id[44业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('58919', '40', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58920', '41', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58921', '42', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58922', '43', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58923', '44', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58924', '21', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58925', '22', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58926', '23', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58927', '24', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58928', '30', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58929', '31', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58930', '32', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58931', '33', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58932', '34', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58933', '35', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58934', '36', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58935', '37', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58936', '38', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58937', '39', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58938', '40', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58939', '41', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58940', '42', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58941', '43', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58942', '44', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58943', '45', '1', 'id[45业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('58944', '46', '1', 'id[46业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('58945', '47', '1', 'id[47业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('58946', '48', '1', 'id[48业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('58947', '49', '1', 'id[49业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('58948', '40', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58949', '41', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58950', '42', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58951', '43', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58952', '45', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58953', '46', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58954', '47', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58955', '48', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58956', '49', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58957', '21', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58958', '22', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58959', '23', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58960', '24', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58961', '30', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58962', '31', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58963', '32', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58964', '33', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58965', '34', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58966', '35', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58967', '36', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58968', '37', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58969', '38', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58970', '39', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58971', '40', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58972', '41', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58973', '42', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58974', '43', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58975', '44', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58976', '45', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58977', '46', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58978', '47', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58979', '48', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58980', '49', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58981', '50', '1', 'id[50业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('58982', '51', '1', 'id[51业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('58983', '52', '1', 'id[52业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('58984', '53', '1', 'id[53业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('58985', '54', '1', 'id[54业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('58986', '49', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58987', '50', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58988', '51', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58989', '52', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58990', '53', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58991', '54', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58992', '21', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58993', '22', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58994', '23', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58995', '24', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58996', '30', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58997', '31', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58998', '32', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('58999', '33', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59000', '34', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59001', '35', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59002', '36', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59003', '37', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59004', '38', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59005', '39', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59006', '44', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59007', '45', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59008', '46', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59009', '47', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59010', '48', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59011', '49', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59012', '50', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59013', '51', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59014', '52', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59015', '53', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59016', '54', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59017', '1', '1', 'id[1业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59018', '2', '1', 'id[2业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59019', '3', '1', 'id[3业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59020', '4', '1', 'id[4业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59021', '5', '1', 'id[5业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59022', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59023', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59024', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59025', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59026', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59027', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59028', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59029', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59030', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59031', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59032', '6', '1', 'id[6业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59033', '7', '1', 'id[7业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59034', '8', '1', 'id[8业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59035', '9', '1', 'id[9业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59036', '10', '1', 'id[10业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59037', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59038', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59039', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59040', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59041', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59042', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59043', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59044', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59045', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59046', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59047', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59048', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59049', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59050', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59051', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59052', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59053', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59054', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59055', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59056', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59057', '11', '1', 'id[11业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59058', '12', '1', 'id[12业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59059', '13', '1', 'id[13业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59060', '14', '1', 'id[14业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59061', '15', '1', 'id[15业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59062', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59063', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59064', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59065', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59066', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59067', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59068', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59069', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59070', '15', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59071', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59072', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59073', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59074', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59075', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59076', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59077', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59078', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59079', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59080', '15', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59081', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59082', '16', '1', 'id[16业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59083', '17', '1', 'id[17业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59084', '18', '1', 'id[18业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59085', '19', '1', 'id[19业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59086', '20', '1', 'id[20业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59087', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59088', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59089', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59090', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59091', '15', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59092', '16', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59093', '17', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59094', '18', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59095', '19', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59096', '20', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59097', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59098', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59099', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59100', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59101', '15', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59102', '16', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59103', '17', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59104', '18', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59105', '19', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59106', '20', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59107', '21', '1', 'id[21业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59108', '22', '1', 'id[22业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59109', '23', '1', 'id[23业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59110', '24', '1', 'id[24业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59111', '25', '1', 'id[25业务类型错误！');
INSERT INTO `insu_error_msg` VALUES ('59112', '16', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59113', '17', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59114', '18', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59115', '19', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59116', '21', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59117', '22', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59118', '23', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59119', '24', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59120', '25', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59121', '16', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59122', '17', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59123', '18', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59124', '19', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59125', '20', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59126', '21', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59127', '22', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59128', '23', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59129', '24', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59130', '25', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59131', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59132', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59133', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59134', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59135', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59136', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59137', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59138', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59139', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59140', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59141', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59142', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59143', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59144', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59145', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59146', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59147', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59148', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59149', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59150', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59151', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59152', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59153', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59154', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59155', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59156', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59157', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59158', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59159', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59160', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59161', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59162', '10', '1', 'id为[10]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59163', '10', '1', 'id为[10]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59164', '1', '1', 'id为[1]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59165', '2', '1', 'id为[2]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59166', '5', '1', 'id为[5]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59167', '1', '1', 'id为[1]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59168', '2', '1', 'id为[2]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59169', '5', '1', 'id为[5]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59170', '5', '1', 'id为[5]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59171', '5', '1', 'id为[5]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59172', '5', '1', 'id为[5]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59173', '1', '1', 'id为[1]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59174', '2', '1', 'id为[2]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59175', '5', '1', 'id为[5]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59176', '1', '1', 'id为[1]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59177', '2', '1', 'id为[2]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59178', '1', '1', 'id为[1]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59179', '2', '1', 'id为[2]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59180', '1', '1', 'id为[1]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59181', '2', '1', 'id为[2]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59182', '3', '1', 'id为[3]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59183', '4', '1', 'id为[4]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59184', '5', '1', 'id为[5]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59185', '5', '1', 'id为[5]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59186', '5', '1', 'id为[5]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59187', '5', '1', 'id为[5]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59188', '5', '1', 'id为[5]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59189', '5', '1', 'id为[5]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59190', '1', '1', 'id为[1]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59191', '2', '1', 'id为[2]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59192', '3', '1', 'id为[3]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59193', '4', '1', 'id为[4]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59194', '5', '1', 'id为[5]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59195', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59196', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59197', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59198', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59199', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59200', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59201', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59202', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59203', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59204', '15', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59205', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59206', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59207', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59208', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59209', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59210', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59211', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59212', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59213', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59214', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59215', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59216', '15', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59217', '1', '1', 'id为[1]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59218', '6', '1', 'id为[6]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59219', '7', '1', 'id为[7]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59220', '8', '1', 'id为[8]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59221', '9', '1', 'id为[9]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59222', '10', '1', 'id为[10]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59223', '10', '1', 'id为[10]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59224', '10', '1', 'id为[10]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59225', '10', '1', 'id为[10]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59226', '10', '1', 'id为[10]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59227', '10', '1', 'id为[10]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59228', '10', '1', 'id为[10]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59229', '10', '1', 'id为[10]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59230', '11', '1', 'id为[11]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59231', '12', '1', 'id为[12]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59232', '13', '1', 'id为[13]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59233', '14', '1', 'id为[14]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59234', '14', '1', 'id为[14]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59235', '14', '1', 'id为[14]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59236', '14', '1', 'id为[14]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59237', '14', '1', 'id为[14]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59238', '14', '1', 'id为[14]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59239', '14', '1', 'id为[14]的数据:保单生效时间不能小于当前日期!');
INSERT INTO `insu_error_msg` VALUES ('59240', '303', 'OOKP1417096842622', '未获取到id为[303]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59241', '304', 'OOKP1417096842797', '未获取到id为[304]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59242', '305', 'OOKP1417244884844', '未获取到id为[305]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59243', '306', 'OOKP1417358215714', '未获取到id为[306]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59244', '307', 'OOKP1417504906840', '未获取到id为[307]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59245', '307', 'OOKP1417504906840', '未获取到id为[307]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59246', '307', 'OOKP1417504906840', '未获取到id为[307]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59247', '307', 'OOKP1417504906840', '未获取到id为[307]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59248', '307', 'OOKP1417504906840', '未获取到id为[307]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59249', '307', 'OOKP1417504906840', '未获取到id为[307]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59250', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59251', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59252', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59253', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59254', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59255', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59256', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59257', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59258', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59259', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59260', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59261', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59262', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59263', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59264', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59265', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59266', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59267', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59268', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59269', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59270', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59271', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59272', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59273', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59274', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59275', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59276', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59277', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59278', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59279', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59280', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59281', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59282', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59283', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59284', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59285', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59286', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59287', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59288', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59289', '15', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59290', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59291', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59292', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59293', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59294', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59295', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59296', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59297', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59298', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59299', '15', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59300', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59301', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59302', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59303', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59304', '15', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59305', '16', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59306', '17', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59307', '18', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59308', '19', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59309', '20', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59310', '11', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59311', '12', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59312', '13', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59313', '14', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59314', '15', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59315', '16', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59316', '17', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59317', '18', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59318', '19', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59319', '20', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59320', '21', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59321', '22', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59322', '23', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59323', '24', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59324', '25', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59325', '16', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59326', '17', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59327', '18', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59328', '19', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59329', '21', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59330', '22', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59331', '23', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59332', '24', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59333', '25', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59334', '16', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59335', '17', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59336', '18', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59337', '19', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59338', '20', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59339', '21', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59340', '22', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59341', '23', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59342', '24', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59343', '25', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59344', '26', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59345', '27', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59346', '28', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59347', '29', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59348', '30', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59349', '21', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59350', '22', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59351', '23', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59352', '24', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59353', '25', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59354', '20', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59355', '26', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59356', '27', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59357', '28', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59358', '29', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59359', '30', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59360', '26', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59361', '27', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59362', '28', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59363', '29', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59364', '30', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59365', '31', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59366', '32', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59367', '33', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59368', '34', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59369', '35', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59370', '20', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59371', '26', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59372', '27', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59373', '28', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59374', '29', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59375', '30', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59376', '31', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59377', '32', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59378', '33', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59379', '34', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59380', '35', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59381', '36', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59382', '37', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59383', '38', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59384', '39', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59385', '40', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59386', '33', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59387', '34', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59388', '20', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59389', '36', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59390', '37', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59391', '38', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59392', '36', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59393', '39', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59394', '37', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59395', '40', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59396', '38', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59397', '31', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59398', '39', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59399', '32', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59400', '40', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59401', '33', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59402', '34', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59403', '35', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59404', '33', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59405', '34', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59406', '36', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59407', '37', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59408', '38', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59409', '39', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59410', '41', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59411', '40', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59412', '42', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59413', '20', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59414', '43', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59415', '36', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59416', '44', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59417', '37', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59418', '45', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59419', '38', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59420', '39', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59421', '40', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59422', '31', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59423', '32', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59424', '33', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59425', '34', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59426', '41', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59427', '42', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59428', '35', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59429', '40', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59430', '43', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59431', '36', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59432', '44', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59433', '37', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59434', '45', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59435', '38', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59436', '39', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59437', '40', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59438', '40', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59439', '20', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59440', '41', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59441', '42', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59442', '43', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59443', '44', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59444', '41', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59445', '45', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59446', '42', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59447', '43', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59448', '36', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59449', '44', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59450', '37', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59451', '45', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59452', '38', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59453', '39', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59454', '40', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59455', '31', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59456', '32', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59457', '35', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59458', '36', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59459', '37', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59460', '38', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59461', '39', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59462', '40', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59463', '20', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59464', '41', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59465', '42', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59466', '43', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59467', '44', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59468', '45', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59469', '36', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59470', '37', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59471', '38', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59472', '39', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59473', '40', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59474', '31', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59475', '32', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59476', '35', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59477', '36', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59478', '37', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59479', '38', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59480', '39', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59481', '41', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59482', '42', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59483', '43', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59484', '44', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59485', '45', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59486', '20', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59487', '41', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59488', '42', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59489', '43', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59490', '44', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59491', '45', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59492', '36', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59493', '37', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59494', '38', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59495', '39', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59496', '31', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59497', '32', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59498', '35', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59499', '41', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59500', '42', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59501', '41', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59502', '42', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59503', '46', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59504', '47', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59505', '48', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59506', '49', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59507', '50', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59508', '36', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59509', '37', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59510', '38', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59511', '39', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59512', '41', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59513', '42', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59514', '43', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59515', '44', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59516', '45', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59517', '20', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59518', '41', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59519', '42', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59520', '43', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59521', '44', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59522', '45', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59523', '41', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59524', '42', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59525', '43', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59526', '44', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59527', '45', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59528', '36', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59529', '37', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59530', '38', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59531', '39', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59532', '31', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59533', '32', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59534', '35', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59535', '46', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59536', '47', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59537', '48', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59538', '49', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59539', '50', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59540', '49', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59541', '50', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59542', '51', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59543', '52', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59544', '53', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59545', '54', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59546', '55', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59547', '36', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59548', '37', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59549', '38', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59550', '39', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59551', '43', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59552', '44', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59553', '45', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59554', '20', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59555', '43', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59556', '44', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59557', '45', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59558', '43', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59559', '44', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59560', '45', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59561', '36', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59562', '37', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59563', '38', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59564', '39', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59565', '31', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59566', '32', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59567', '35', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59568', '46', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59569', '47', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59570', '48', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59571', '49', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59572', '50', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59573', '51', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59574', '52', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59575', '53', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59576', '54', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59577', '55', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59578', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59579', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59580', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59581', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59582', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59583', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59584', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59585', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59586', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59587', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59588', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59589', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59590', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59591', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59592', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59593', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59594', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59595', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59596', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59597', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59598', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59599', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59600', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59601', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59602', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59603', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59604', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59605', '6', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59606', '7', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59607', '8', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59608', '9', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59609', '10', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59610', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59611', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59612', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59613', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59614', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59615', '1', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59616', '2', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59617', '3', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59618', '4', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59619', '5', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('59620', '322', 'OOKP1417601719941', '未获取到id为[322]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59621', '323', 'OOKP1417601720083', '未获取到id为[323]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59622', '324', 'OOKP1417601978797', '未获取到id为[324]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59623', '325', 'OOKP1417601978800', '未获取到id为[325]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59624', '326', 'OOKP1417605740387', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59625', '326', 'OOKP1417605740387', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59626', '326', 'OOKP1417605740387', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59627', '326', 'OOKP1417605740387', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59628', '326', 'OOKP1417605740387', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59629', '326', 'OOKP1417605740387', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59630', '322', 'OOKP1417601719941', '未获取到id为[322]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59631', '323', 'OOKP1417601720083', '未获取到id为[323]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59632', '324', 'OOKP1417601978797', '未获取到id为[324]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59633', '325', 'OOKP1417601978800', '未获取到id为[325]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59634', '2', 'ookp001', '系统超时');
INSERT INTO `insu_error_msg` VALUES ('59635', '2', 'ookp001', '系统超时');
INSERT INTO `insu_error_msg` VALUES ('59636', '322', 'OOKP1417601719941', 'id为[322]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59637', '323', 'OOKP1417601720083', 'id为[323]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59638', '324', 'OOKP1417601978797', 'id为[324]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59639', '325', 'OOKP1417601978800', 'id为[325]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59640', '326', 'OOKP1417605740387', 'id为[326]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59641', '322', 'OOKP1417601719941', 'id为[322]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59642', '323', 'OOKP1417601720083', 'id为[323]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59643', '324', 'OOKP1417601978797', 'id为[324]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59644', '325', 'OOKP1417601978800', 'id为[325]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59645', '326', 'OOKP1417605740387', 'id为[326]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59646', '322', 'OOKP1417601719941', 'id为[322]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59647', '323', 'OOKP1417601720083', 'id为[323]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59648', '324', 'OOKP1417601978797', 'id为[324]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59649', '325', 'OOKP1417601978800', 'id为[325]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59650', '326', 'OOKP1417605740387', 'id为[326]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59651', '322', 'null', '未获取到id为[322]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59652', '323', 'null', '未获取到id为[323]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59653', '324', 'null', '未获取到id为[324]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59654', '322', 'null', '未获取到id为[322]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59655', '325', 'null', '未获取到id为[325]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59656', '323', 'null', '未获取到id为[323]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59657', '326', 'null', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59658', '324', 'null', '未获取到id为[324]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59659', '322', 'null', '未获取到id为[322]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59660', '322', 'null', '未获取到id为[322]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59661', '325', 'null', '未获取到id为[325]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59662', '323', 'null', '未获取到id为[323]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59663', '323', 'null', '未获取到id为[323]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59664', '326', 'null', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59665', '324', 'null', '未获取到id为[324]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59666', '322', 'null', '未获取到id为[322]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59667', '325', 'null', '未获取到id为[325]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59668', '324', 'null', '未获取到id为[324]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59669', '326', 'null', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59670', '323', 'null', '未获取到id为[323]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59671', '325', 'null', '未获取到id为[325]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59672', '322', 'null', '未获取到id为[322]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59673', '324', 'null', '未获取到id为[324]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59674', '326', 'null', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59675', '323', 'null', '未获取到id为[323]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59676', '325', 'null', '未获取到id为[325]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59677', '326', 'null', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59678', '324', 'null', '未获取到id为[324]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59679', '322', 'null', '未获取到id为[322]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59680', '322', 'null', '未获取到id为[322]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59681', '323', 'null', '未获取到id为[323]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59682', '325', 'null', '未获取到id为[325]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59683', '323', 'null', '未获取到id为[323]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59684', '324', 'null', '未获取到id为[324]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59685', '326', 'null', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59686', '324', 'null', '未获取到id为[324]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59687', '325', 'null', '未获取到id为[325]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59688', '322', 'null', '未获取到id为[322]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59689', '325', 'null', '未获取到id为[325]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59690', '323', 'null', '未获取到id为[323]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59691', '326', 'null', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59692', '326', 'null', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59693', '324', 'null', '未获取到id为[324]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59694', '325', 'null', '未获取到id为[325]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59695', '326', 'null', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59696', '322', 'OOKP1417601719941', 'id为[322]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59697', '323', 'OOKP1417601720083', 'id为[323]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59698', '324', 'OOKP1417601978797', 'id为[324]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59699', '325', 'OOKP1417601978800', 'id为[325]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59700', '326', 'OOKP1417605740387', 'id为[326]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59701', '327', 'OOKP1417605740438', 'id为[327]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59702', '328', 'OOKP1417607057299', 'id为[328]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59703', '329', 'OOKP1417607057583', 'id为[329]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59704', '330', 'OOKP1417607184805', 'id为[330]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59705', '331', 'OOKP1417607184930', 'id为[331]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59706', '327', 'null', '未获取到id为[327]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59707', '328', 'null', '未获取到id为[328]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59708', '327', 'null', '未获取到id为[327]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59709', '329', 'null', '未获取到id为[329]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59710', '328', 'null', '未获取到id为[328]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59711', '330', 'null', '未获取到id为[330]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59712', '329', 'null', '未获取到id为[329]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59713', '331', 'null', '未获取到id为[331]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59714', '330', 'null', '未获取到id为[330]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59715', '322', 'null', '未获取到id为[322]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59716', '331', 'null', '未获取到id为[331]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59717', '323', 'null', '未获取到id为[323]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59718', '322', 'null', '未获取到id为[322]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59719', '324', 'null', '未获取到id为[324]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59720', '323', 'null', '未获取到id为[323]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59721', '325', 'null', '未获取到id为[325]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59722', '324', 'null', '未获取到id为[324]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59723', '326', 'null', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59724', '325', 'null', '未获取到id为[325]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59725', '326', 'null', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59726', '332', 'OOKP1417607184930', 'id为[332]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59727', '333', 'OOKP1417607184805', 'id为[333]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59728', '334', 'OOKP1417607270079', 'id为[334]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59729', '335', 'OOKP1417607270081', 'id为[335]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59730', '336', 'OOKP1417607352812', 'id为[336]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59731', '327', 'null', '未获取到id为[327]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59732', '328', 'null', '未获取到id为[328]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59733', '329', 'null', '未获取到id为[329]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59734', '330', 'null', '未获取到id为[330]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59735', '331', 'null', '未获取到id为[331]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59736', '322', 'null', '未获取到id为[322]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59737', '323', 'null', '未获取到id为[323]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59738', '324', 'null', '未获取到id为[324]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59739', '325', 'null', '未获取到id为[325]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59740', '326', 'null', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59741', '332', 'null', '未获取到id为[332]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59742', '333', 'null', '未获取到id为[333]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59743', '334', 'null', '未获取到id为[334]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59744', '335', 'null', '未获取到id为[335]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59745', '336', 'null', '未获取到id为[336]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59746', '322', 'OOKP1417601719941', 'id为[322]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59747', '323', 'OOKP1417601720083', 'id为[323]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59748', '324', 'OOKP1417601978797', 'id为[324]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59749', '325', 'OOKP1417601978800', 'id为[325]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59750', '326', 'OOKP1417605740387', 'id为[326]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59751', '326', 'OOKP1417605740387', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59752', '326', 'OOKP1417605740387', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59753', '326', 'OOKP1417605740387', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59754', '326', 'OOKP1417605740387', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59755', '326', 'OOKP1417605740387', '未获取到id为[326]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59756', '337', 'OOKP1417607352814', 'id为[337]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59757', '338', 'OOKP1417607352814', 'id为[338]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59758', '339', 'OOKP1417607352812', 'id为[339]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59759', '340', 'OOKP1417607451482', 'id为[340]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59760', '341', 'OOKP1417607451485', 'id为[341]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59761', '322', 'OOKP1417601719941', 'id为[322]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59762', '327', 'null', '未获取到id为[327]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59763', '328', 'null', '未获取到id为[328]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59764', '322', 'OOKP1417601719941', 'id为[322]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59765', '327', 'OOKP1417605740438', 'id为[327]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59766', '322', 'OOKP1417601719941', 'id为[322]的数据:保单生效时间不能小于当前日期!合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59767', '322', 'OOKP1417601719941', 'id为[246]的数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59768', '332', 'OOKP1417607184930', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59769', '333', 'OOKP1417607184805', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59770', '332', 'OOKP1417607184930', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59771', '333', 'OOKP1417607184805', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59772', '327', 'OOKP1417605740438', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59773', '323', 'OOKP1417601720083', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59774', '332', 'OOKP1417607184930', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59775', '333', 'OOKP1417607184805', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59776', '337', 'OOKP1417607352814', 'id为[259]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59777', '338', 'OOKP1417607352814', 'id为[259]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59778', '339', 'OOKP1417607352812', 'id为[258]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59779', '323', 'OOKP1417601720083', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59780', '325', 'OOKP1417601978800', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59781', '322', 'OOKP1417601719941', 'id为[246]的投保单数据:被保人序号值非法！被保人类型不能为空！');
INSERT INTO `insu_error_msg` VALUES ('59782', '323', 'OOKP1417601720083', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59783', '324', 'OOKP1417601978797', 'id为[248]的投保单数据:被保人序号值非法！被保人类型不能为空！');
INSERT INTO `insu_error_msg` VALUES ('59784', '325', 'OOKP1417601978800', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59785', '326', 'OOKP1417605740387', 'id为[250]的投保单数据:被保人序号值非法！被保人类型不能为空！');
INSERT INTO `insu_error_msg` VALUES ('59786', '322', 'OOKP1417601719941', 'id为[246]的投保单数据:被保人类型不能为空！');
INSERT INTO `insu_error_msg` VALUES ('59787', '323', 'OOKP1417601720083', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59788', '324', 'OOKP1417601978797', 'id为[248]的投保单数据:被保人类型不能为空！');
INSERT INTO `insu_error_msg` VALUES ('59789', '325', 'OOKP1417601978800', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59790', '326', 'OOKP1417605740387', 'id为[250]的投保单数据:被保人类型不能为空！');
INSERT INTO `insu_error_msg` VALUES ('59791', '327', 'OOKP1417605740438', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59792', '328', 'OOKP1417607057299', 'id为[252]的投保单数据:被保人类型不能为空！');
INSERT INTO `insu_error_msg` VALUES ('59793', '329', 'OOKP1417607057583', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59794', '330', 'OOKP1417607184805', 'id为[254]的投保单数据:被保人类型不能为空！');
INSERT INTO `insu_error_msg` VALUES ('59795', '331', 'OOKP1417607184930', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59796', '323', 'OOKP1417601720083', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59797', '325', 'OOKP1417601978800', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59798', '327', 'OOKP1417605740438', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59799', '329', 'OOKP1417607057583', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59800', '331', 'OOKP1417607184930', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59801', '323', 'OOKP1417601720083', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59802', '325', 'OOKP1417601978800', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59803', '327', 'OOKP1417605740438', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59804', '329', 'OOKP1417607057583', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59805', '331', 'OOKP1417607184930', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59806', '332', 'OOKP1417607184930', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59807', '333', 'OOKP1417607184805', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59808', '335', 'OOKP1417607270081', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59809', '323', 'OOKP1417601720083', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59810', '325', 'OOKP1417601978800', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59811', '326', 'OOKP1417605740387', 'id为[250]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59812', '327', 'OOKP1417605740438', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59813', '329', 'OOKP1417607057583', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59814', '331', 'OOKP1417607184930', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59815', '326', 'OOKP1417605740387', 'id为[250]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59816', '323', 'OOKP1417601720083', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59817', '325', 'OOKP1417601978800', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59818', '326', 'OOKP1417605740387', 'id为[250]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59819', '326', 'OOKP1417605740387', 'id为[250]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59820', '323', 'OOKP1417601720083', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59821', '325', 'OOKP1417601978800', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59822', '326', 'OOKP1417605740387', 'id为[250]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59823', '326', 'OOKP1417605740387', 'id为[250]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59824', '323', 'OOKP1417601720083', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59825', '325', 'OOKP1417601978800', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59826', '326', 'OOKP1417605740387', 'id为[250]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59827', '326', 'OOKP1417605740387', 'id为[250]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59828', '323', 'OOKP1417601720083', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59829', '325', 'OOKP1417601978800', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59830', '326', 'OOKP1417605740387', 'id为[250]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59831', '323', 'OOKP1417601720083', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59832', '325', 'OOKP1417601978800', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59833', '323', 'null', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59834', '325', 'null', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59835', '323', 'null', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59836', '325', 'null', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59837', '323', 'null', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59838', '325', 'null', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59839', '327', 'OOKP1417605740438', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59840', '329', 'OOKP1417607057583', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59841', '331', 'OOKP1417607184930', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59842', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59843', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59844', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59845', '332', 'OOKP1417607184930', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59846', '333', 'OOKP1417607184805', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59847', '335', 'OOKP1417607270081', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59848', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59849', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59850', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59851', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59852', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59853', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59854', '337', 'OOKP1417607352814', 'id为[259]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59855', '338', 'OOKP1417607352814', 'id为[259]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59856', '339', 'OOKP1417607352812', 'id为[258]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59857', '341', 'OOKP1417607451485', 'id为[261]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59858', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59859', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59860', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59861', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59862', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59863', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59864', '337', 'null', 'id为[259]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59865', '338', 'null', 'id为[259]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59866', '339', 'null', 'id为[258]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59867', '341', 'null', 'id为[261]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59868', '346', 'OOKP1417675755925', 'id为[266]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59869', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59870', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59871', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59872', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59873', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59874', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59875', '346', 'null', 'id为[266]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59876', '337', 'null', 'id为[259]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59877', '338', 'null', 'id为[259]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59878', '339', 'null', 'id为[258]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59879', '341', 'null', 'id为[261]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59880', '347', 'OOKP1417680592672', 'id为[267]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59881', '348', 'OOKP1417680592822', 'id为[268]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59882', '349', 'OOKP1417680592822', 'id为[268]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59883', '350', 'OOKP1417680592672', 'id为[267]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59884', '351', 'OOKP1417681664024', 'id为[269]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59885', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59886', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59887', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59888', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59889', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59890', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59891', '347', 'null', 'id为[267]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59892', '348', 'null', 'id为[268]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59893', '349', 'null', 'id为[268]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59894', '350', 'null', 'id为[267]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59895', '351', 'null', 'id为[269]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59896', '346', 'null', 'id为[266]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59897', '337', 'null', 'id为[259]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59898', '338', 'null', 'id为[259]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59899', '339', 'null', 'id为[258]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59900', '341', 'null', 'id为[261]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59901', '352', 'OOKP1417681902133', 'id为[270]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59902', '353', 'OOKP1417681902300', 'id为[271]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59903', '354', 'OOKP1417682411148', 'id为[272]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59904', '355', 'OOKP1417682411151', 'id为[273]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59905', '356', 'OOKP1417682442453', 'id为[274]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59906', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59907', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59908', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59909', '352', 'null', 'id为[270]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59910', '353', 'null', 'id为[271]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59911', '354', 'null', 'id为[272]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59912', '355', 'null', 'id为[273]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59913', '356', 'null', 'id为[274]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59914', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59915', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59916', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59917', '347', 'null', 'id为[267]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59918', '348', 'null', 'id为[268]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59919', '349', 'null', 'id为[268]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59920', '350', 'null', 'id为[267]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59921', '351', 'null', 'id为[269]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59922', '346', 'null', 'id为[266]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59923', '337', 'null', 'id为[259]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59924', '338', 'null', 'id为[259]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59925', '339', 'null', 'id为[258]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59926', '341', 'null', 'id为[261]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59927', '357', 'OOKP1417682605927', 'id为[275]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59928', '358', 'OOKP1417683326030', 'id为[276]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59929', '359', 'OOKP1417683326180', 'id为[277]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59930', '360', 'OOKP1417683326180', 'id为[277]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59931', '361', 'OOKP1417683326030', 'id为[276]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59932', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59933', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59934', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59935', '352', 'null', 'id为[270]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59936', '353', 'null', 'id为[271]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59937', '354', 'null', 'id为[272]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59938', '355', 'null', 'id为[273]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59939', '356', 'null', 'id为[274]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59940', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59941', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59942', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59943', '347', 'null', 'id为[267]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59944', '348', 'null', 'id为[268]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59945', '349', 'null', 'id为[268]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59946', '350', 'null', 'id为[267]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59947', '351', 'null', 'id为[269]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59948', '346', 'null', 'id为[266]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59949', '337', 'null', 'id为[259]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59950', '338', 'null', 'id为[259]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59951', '339', 'null', 'id为[258]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59952', '341', 'null', 'id为[261]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59953', '357', 'null', 'id为[275]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59954', '358', 'null', 'id为[276]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59955', '359', 'null', 'id为[277]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59956', '360', 'null', 'id为[277]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59957', '361', 'null', 'id为[276]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59958', '362', 'OOKP1417685838176', 'id为[278]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59959', '363', 'OOKP1417685838327', 'id为[279]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59960', '364', 'OOKP1417691328138', 'id为[280]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59961', '365', 'OOKP1417691328288', 'id为[281]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59962', '366', 'OOKP1417691894632', 'id为[282]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59963', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59964', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59965', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59966', '352', 'null', 'id为[270]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59967', '353', 'null', 'id为[271]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59968', '354', 'null', 'id为[272]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59969', '355', 'null', 'id为[273]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59970', '356', 'null', 'id为[274]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59971', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59972', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59973', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59974', '347', 'null', 'id为[267]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59975', '348', 'null', 'id为[268]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59976', '349', 'null', 'id为[268]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59977', '350', 'null', 'id为[267]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59978', '351', 'null', 'id为[269]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59979', '362', 'null', 'id为[278]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59980', '363', 'null', 'id为[279]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59981', '364', 'null', 'id为[280]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59982', '365', 'null', 'id为[281]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59983', '366', 'null', 'id为[282]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59984', '346', 'null', 'id为[266]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59985', '337', 'null', 'id为[259]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59986', '338', 'null', 'id为[259]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59987', '339', 'null', 'id为[258]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59988', '341', 'null', 'id为[261]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('59989', '357', 'null', 'id为[275]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59990', '358', 'null', 'id为[276]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59991', '359', 'null', 'id为[277]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59992', '360', 'null', 'id为[277]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59993', '361', 'null', 'id为[276]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('59994', '367', 'OOKP1417691894824', 'id为[283]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59995', '368', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59996', '369', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('59997', '370', 'OOKP1417758167014', 'id为[284]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59998', '371', 'OOKP1417758417492', 'id为[285]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('59999', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60000', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60001', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60002', '352', 'null', 'id为[270]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60003', '353', 'null', 'id为[271]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60004', '354', 'null', 'id为[272]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60005', '355', 'null', 'id为[273]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60006', '356', 'null', 'id为[274]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60007', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60008', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60009', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60010', '347', 'null', 'id为[267]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60011', '348', 'null', 'id为[268]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60012', '349', 'null', 'id为[268]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60013', '350', 'null', 'id为[267]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60014', '351', 'null', 'id为[269]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60015', '362', 'null', 'id为[278]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60016', '363', 'null', 'id为[279]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60017', '364', 'null', 'id为[280]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60018', '365', 'null', 'id为[281]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60019', '366', 'null', 'id为[282]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60020', '367', 'null', 'id为[283]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60021', '368', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('60022', '369', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('60023', '370', 'null', 'id为[284]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60024', '371', 'null', 'id为[285]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60025', '346', 'null', 'id为[266]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60026', '337', 'null', 'id为[259]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60027', '338', 'null', 'id为[259]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60028', '339', 'null', 'id为[258]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60029', '341', 'null', 'id为[261]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60030', '357', 'null', 'id为[275]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60031', '358', 'null', 'id为[276]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60032', '359', 'null', 'id为[277]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60033', '360', 'null', 'id为[277]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60034', '361', 'null', 'id为[276]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60035', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60036', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60037', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60038', '352', 'null', 'id为[270]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60039', '353', 'null', 'id为[271]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60040', '354', 'null', 'id为[272]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60041', '355', 'null', 'id为[273]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60042', '356', 'null', 'id为[274]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60043', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60044', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60045', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60046', '347', 'null', 'id为[267]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60047', '348', 'null', 'id为[268]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60048', '349', 'null', 'id为[268]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60049', '350', 'null', 'id为[267]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60050', '351', 'null', 'id为[269]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60051', '362', 'null', 'id为[278]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60052', '363', 'null', 'id为[279]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60053', '364', 'null', 'id为[280]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60054', '365', 'null', 'id为[281]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60055', '366', 'null', 'id为[282]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60056', '367', 'null', 'id为[283]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60057', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60058', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60059', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60060', '352', 'null', 'id为[270]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60061', '353', 'null', 'id为[271]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60062', '354', 'null', 'id为[272]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60063', '355', 'null', 'id为[273]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60064', '356', 'null', 'id为[274]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60065', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60066', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60067', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60068', '347', 'null', 'id为[267]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60069', '348', 'null', 'id为[268]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60070', '349', 'null', 'id为[268]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60071', '350', 'null', 'id为[267]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60072', '351', 'null', 'id为[269]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60073', '362', 'null', 'id为[278]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60074', '363', 'null', 'id为[279]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60075', '364', 'null', 'id为[280]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60076', '365', 'null', 'id为[281]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60077', '366', 'null', 'id为[282]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60078', '367', 'null', 'id为[283]的投保单数据:该机构下没有该产品的销售权限合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60079', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60080', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60081', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60082', '352', 'null', 'id为[270]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60083', '353', 'null', 'id为[271]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60084', '354', 'null', 'id为[272]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60085', '355', 'null', 'id为[273]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60086', '356', 'null', 'id为[274]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60087', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60088', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60089', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60090', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60091', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60092', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60093', '347', 'null', 'id为[267]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60094', '373', 'OOKP1417959720519', 'id为[287]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60095', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60096', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60097', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60098', '352', 'null', 'id为[270]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60099', '353', 'null', 'id为[271]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60100', '354', 'null', 'id为[272]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60101', '355', 'null', 'id为[273]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60102', '356', 'null', 'id为[274]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60103', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60104', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60105', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60106', '347', 'null', 'id为[267]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60107', '348', 'null', 'id为[268]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60108', '349', 'null', 'id为[268]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60109', '350', 'null', 'id为[267]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60110', '351', 'null', 'id为[269]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60111', '362', 'null', 'id为[278]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60112', '363', 'null', 'id为[279]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60113', '364', 'null', 'id为[280]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60114', '365', 'null', 'id为[281]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60115', '366', 'null', 'id为[282]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60116', '367', 'null', 'id为[283]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60117', '368', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('60118', '369', 'null', 'null');
INSERT INTO `insu_error_msg` VALUES ('60119', '370', 'null', 'id为[284]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60120', '371', 'null', 'id为[285]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60121', '346', 'null', 'id为[266]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60122', '373', 'null', 'id为[287]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60123', '337', 'null', 'id为[259]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60124', '338', 'null', 'id为[259]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60125', '339', 'null', 'id为[258]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60126', '341', 'null', 'id为[261]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60127', '357', 'null', 'id为[275]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60128', '358', 'null', 'id为[276]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60129', '359', 'null', 'id为[277]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60130', '360', 'null', 'id为[277]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60131', '361', 'null', 'id为[276]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60132', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60133', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60134', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60135', '352', 'null', 'id为[270]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60136', '353', 'null', 'id为[271]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60137', '354', 'null', 'id为[272]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60138', '355', 'null', 'id为[273]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60139', '356', 'null', 'id为[274]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60140', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60141', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60142', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60143', '347', 'null', 'id为[267]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60144', '348', 'null', 'id为[268]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60145', '349', 'null', 'id为[268]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60146', '350', 'null', 'id为[267]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60147', '351', 'null', 'id为[269]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60148', '362', 'null', 'id为[278]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60149', '363', 'null', 'id为[279]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60150', '364', 'null', 'id为[280]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60151', '365', 'null', 'id为[281]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60152', '366', 'null', 'id为[282]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60153', '367', 'null', 'id为[283]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60154', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60155', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60156', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60157', '352', 'null', 'id为[270]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60158', '353', 'null', 'id为[271]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60159', '354', 'null', 'id为[272]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60160', '355', 'null', 'id为[273]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60161', '356', 'null', 'id为[274]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60162', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60163', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60164', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60165', '347', 'null', 'id为[267]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60166', '348', 'null', 'id为[268]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60167', '349', 'null', 'id为[268]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60168', '350', 'null', 'id为[267]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60169', '351', 'null', 'id为[269]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60170', '362', 'null', 'id为[278]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60171', '363', 'null', 'id为[279]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60172', '364', 'null', 'id为[280]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60173', '365', 'null', 'id为[281]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60174', '366', 'null', 'id为[282]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60175', '367', 'null', 'id为[283]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60176', '368', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('60177', '369', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('60178', '370', 'null', 'id为[284]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60179', '371', 'null', 'id为[285]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60180', '346', 'null', 'id为[266]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60181', '373', 'null', 'id为[287]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60182', '337', 'null', 'id为[259]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60183', '338', 'null', 'id为[259]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60184', '339', 'null', 'id为[258]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60185', '341', 'null', 'id为[261]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60186', '357', 'null', 'id为[275]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60187', '358', 'null', 'id为[276]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60188', '359', 'null', 'id为[277]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60189', '360', 'null', 'id为[277]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60190', '361', 'null', 'id为[276]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60191', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60192', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60193', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60194', '352', 'null', 'id为[270]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60195', '353', 'null', 'id为[271]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60196', '354', 'null', 'id为[272]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60197', '355', 'null', 'id为[273]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60198', '356', 'null', 'id为[274]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60199', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60200', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60201', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60202', '347', 'null', 'id为[267]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60203', '348', 'null', 'id为[268]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60204', '349', 'null', 'id为[268]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60205', '350', 'null', 'id为[267]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60206', '351', 'null', 'id为[269]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60207', '362', 'null', 'id为[278]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60208', '363', 'null', 'id为[279]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60209', '364', 'null', 'id为[280]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60210', '365', 'null', 'id为[281]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60211', '366', 'null', 'id为[282]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60212', '367', 'null', 'id为[283]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60213', '368', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('60214', '369', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('60215', '370', 'null', 'id为[284]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60216', '371', 'null', 'id为[285]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60217', '346', 'null', 'id为[266]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60218', '373', 'null', 'id为[287]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60219', '337', 'null', 'id为[259]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60220', '338', 'null', 'id为[259]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60221', '339', 'null', 'id为[258]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60222', '341', 'null', 'id为[261]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60223', '357', 'null', 'id为[275]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60224', '358', 'null', 'id为[276]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60225', '359', 'null', 'id为[277]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60226', '360', 'null', 'id为[277]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60227', '361', 'null', 'id为[276]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60228', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60229', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60230', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60231', '352', 'null', 'id为[270]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60232', '353', 'null', 'id为[271]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60233', '354', 'null', 'id为[272]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60234', '355', 'null', 'id为[273]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60235', '356', 'null', 'id为[274]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60236', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60237', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60238', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60239', '347', 'null', 'id为[267]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60240', '348', 'null', 'id为[268]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60241', '349', 'null', 'id为[268]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60242', '350', 'null', 'id为[267]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60243', '351', 'null', 'id为[269]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60244', '362', 'null', 'id为[278]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60245', '363', 'null', 'id为[279]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60246', '364', 'null', 'id为[280]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60247', '365', 'null', 'id为[281]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60248', '366', 'null', 'id为[282]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60249', '367', 'null', 'id为[283]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60250', '368', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('60251', '369', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('60252', '370', 'null', 'id为[284]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60253', '371', 'null', 'id为[285]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60254', '346', 'null', 'id为[266]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60255', '373', 'null', 'id为[287]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60256', '337', 'null', 'id为[259]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60257', '338', 'null', 'id为[259]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60258', '339', 'null', 'id为[258]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60259', '341', 'null', 'id为[261]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60260', '357', 'null', 'id为[275]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60261', '358', 'null', 'id为[276]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60262', '359', 'null', 'id为[277]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60263', '360', 'null', 'id为[277]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60264', '361', 'null', 'id为[276]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60265', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60266', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60267', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60268', '352', 'null', 'id为[270]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60269', '353', 'null', 'id为[271]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60270', '354', 'null', 'id为[272]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60271', '355', 'null', 'id为[273]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60272', '356', 'null', 'id为[274]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60273', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60274', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60275', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60276', '347', 'null', 'id为[267]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60277', '348', 'null', 'id为[268]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60278', '349', 'null', 'id为[268]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60279', '350', 'null', 'id为[267]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60280', '351', 'null', 'id为[269]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60281', '362', 'null', 'id为[278]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60282', '363', 'null', 'id为[279]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60283', '364', 'null', 'id为[280]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60284', '365', 'null', 'id为[281]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60285', '366', 'null', 'id为[282]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60286', '367', 'null', 'id为[283]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60287', '368', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('60288', '369', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('60289', '370', 'null', 'id为[284]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60290', '371', 'null', 'id为[285]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60291', '346', 'null', 'id为[266]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60292', '373', 'null', 'id为[287]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60293', '337', 'null', 'id为[259]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60294', '338', 'null', 'id为[259]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60295', '339', 'null', 'id为[258]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60296', '341', 'null', 'id为[261]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60297', '357', 'null', 'id为[275]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60298', '358', 'null', 'id为[276]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60299', '359', 'null', 'id为[277]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60300', '360', 'null', 'id为[277]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60301', '361', 'null', 'id为[276]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60302', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60303', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60304', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60305', '352', 'null', 'id为[270]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60306', '353', 'null', 'id为[271]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60307', '354', 'null', 'id为[272]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60308', '355', 'null', 'id为[273]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60309', '356', 'null', 'id为[274]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60310', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60311', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60312', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60313', '347', 'null', 'id为[267]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60314', '348', 'null', 'id为[268]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60315', '349', 'null', 'id为[268]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60316', '350', 'null', 'id为[267]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60317', '351', 'null', 'id为[269]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60318', '362', 'null', 'id为[278]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60319', '363', 'null', 'id为[279]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60320', '364', 'null', 'id为[280]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60321', '365', 'null', 'id为[281]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60322', '366', 'null', 'id为[282]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60323', '367', 'null', 'id为[283]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60324', '368', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('60325', '369', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('60326', '370', 'null', 'id为[284]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60327', '371', 'null', 'id为[285]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60328', '346', 'null', 'id为[266]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60329', '373', 'null', 'id为[287]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60330', '337', 'null', 'id为[259]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60331', '338', 'null', 'id为[259]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60332', '339', 'null', 'id为[258]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60333', '341', 'null', 'id为[261]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60334', '357', 'null', 'id为[275]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60335', '358', 'null', 'id为[276]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60336', '359', 'null', 'id为[277]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60337', '360', 'null', 'id为[277]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60338', '361', 'null', 'id为[276]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60339', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60340', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60341', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60342', '352', 'null', 'id为[270]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60343', '353', 'null', 'id为[271]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60344', '354', 'null', 'id为[272]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60345', '355', 'null', 'id为[273]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60346', '356', 'null', 'id为[274]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60347', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60348', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60349', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60350', '347', 'null', 'id为[267]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60351', '348', 'null', 'id为[268]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60352', '349', 'null', 'id为[268]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60353', '350', 'null', 'id为[267]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60354', '351', 'null', 'id为[269]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60355', '362', 'null', 'id为[278]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60356', '363', 'null', 'id为[279]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60357', '364', 'null', 'id为[280]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60358', '365', 'null', 'id为[281]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60359', '366', 'null', 'id为[282]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60360', '367', 'null', 'id为[283]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60361', '368', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('60362', '369', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('60363', '370', 'null', 'id为[284]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60364', '371', 'null', 'id为[285]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60365', '346', 'null', 'id为[266]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60366', '373', 'null', 'id为[287]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60367', '337', 'null', 'id为[259]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60368', '338', 'null', 'id为[259]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60369', '339', 'null', 'id为[258]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60370', '341', 'null', 'id为[261]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60371', '357', 'null', 'id为[275]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60372', '358', 'null', 'id为[276]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60373', '359', 'null', 'id为[277]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60374', '360', 'null', 'id为[277]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60375', '361', 'null', 'id为[276]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60376', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60377', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60378', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60379', '352', 'null', 'id为[270]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60380', '353', 'null', 'id为[271]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60381', '354', 'null', 'id为[272]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60382', '355', 'null', 'id为[273]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60383', '356', 'null', 'id为[274]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60384', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60385', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60386', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60387', '347', 'null', 'id为[267]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60388', '348', 'null', 'id为[268]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60389', '349', 'null', 'id为[268]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60390', '350', 'null', 'id为[267]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60391', '351', 'null', 'id为[269]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60392', '362', 'null', 'id为[278]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60393', '363', 'null', 'id为[279]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60394', '364', 'null', 'id为[280]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60395', '365', 'null', 'id为[281]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60396', '366', 'null', 'id为[282]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60397', '367', 'null', 'id为[283]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60398', '368', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('60399', '369', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('60400', '370', 'null', 'id为[284]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60401', '371', 'null', 'id为[285]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60402', '346', 'null', 'id为[266]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60403', '373', 'null', 'id为[287]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60404', '337', 'null', 'id为[259]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60405', '338', 'null', 'id为[259]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60406', '339', 'null', 'id为[258]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60407', '341', 'null', 'id为[261]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60408', '357', 'null', 'id为[275]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60409', '358', 'null', 'id为[276]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60410', '359', 'null', 'id为[277]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60411', '360', 'null', 'id为[277]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60412', '361', 'null', 'id为[276]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60413', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60414', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60415', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60416', '352', 'null', 'id为[270]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60417', '353', 'null', 'id为[271]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60418', '354', 'null', 'id为[272]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60419', '355', 'null', 'id为[273]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60420', '356', 'null', 'id为[274]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60421', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60422', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60423', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60424', '347', 'null', 'id为[267]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60425', '348', 'null', 'id为[268]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60426', '349', 'null', 'id为[268]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60427', '350', 'null', 'id为[267]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60428', '351', 'null', 'id为[269]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60429', '362', 'null', 'id为[278]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60430', '363', 'null', 'id为[279]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60431', '364', 'null', 'id为[280]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60432', '365', 'null', 'id为[281]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60433', '366', 'null', 'id为[282]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60434', '367', 'null', 'id为[283]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60435', '368', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('60436', '369', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('60437', '370', 'null', 'id为[284]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60438', '371', 'null', 'id为[285]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60439', '346', 'null', 'id为[266]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60440', '373', 'null', 'id为[287]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60441', '337', 'null', 'id为[259]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60442', '338', 'null', 'id为[259]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60443', '339', 'null', 'id为[258]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60444', '341', 'null', 'id为[261]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60445', '357', 'null', 'id为[275]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60446', '358', 'null', 'id为[276]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60447', '359', 'null', 'id为[277]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60448', '360', 'null', 'id为[277]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60449', '361', 'null', 'id为[276]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60450', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60451', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60452', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60453', '352', 'null', 'id为[270]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60454', '353', 'null', 'id为[271]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60455', '354', 'null', 'id为[272]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60456', '355', 'null', 'id为[273]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60457', '356', 'null', 'id为[274]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60458', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60459', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60460', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60461', '347', 'null', 'id为[267]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60462', '348', 'null', 'id为[268]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60463', '349', 'null', 'id为[268]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60464', '350', 'null', 'id为[267]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60465', '351', 'null', 'id为[269]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60466', '362', 'null', 'id为[278]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60467', '363', 'null', 'id为[279]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60468', '364', 'null', 'id为[280]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60469', '365', 'null', 'id为[281]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60470', '366', 'null', 'id为[282]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60471', '367', 'null', 'id为[283]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60472', '368', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('60473', '369', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('60474', '370', 'null', 'id为[284]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60475', '371', 'null', 'id为[285]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60476', '346', 'null', 'id为[266]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60477', '373', 'null', 'id为[287]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60478', '337', 'null', 'id为[259]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60479', '338', 'null', 'id为[259]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60480', '339', 'null', 'id为[258]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60481', '341', 'null', 'id为[261]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60482', '357', 'null', 'id为[275]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60483', '358', 'null', 'id为[276]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60484', '359', 'null', 'id为[277]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60485', '360', 'null', 'id为[277]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60486', '361', 'null', 'id为[276]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60487', '332', 'null', 'id为[255]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60488', '333', 'null', 'id为[254]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60489', '335', 'null', 'id为[257]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60490', '352', 'null', 'id为[270]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60491', '353', 'null', 'id为[271]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60492', '354', 'null', 'id为[272]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60493', '355', 'null', 'id为[273]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60494', '356', 'null', 'id为[274]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60495', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60496', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60497', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60498', '347', 'null', 'id为[267]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60499', '348', 'null', 'id为[268]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60500', '349', 'null', 'id为[268]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60501', '350', 'null', 'id为[267]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60502', '351', 'null', 'id为[269]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60503', '362', 'null', 'id为[278]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60504', '363', 'null', 'id为[279]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60505', '364', 'null', 'id为[280]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60506', '365', 'null', 'id为[281]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60507', '366', 'null', 'id为[282]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60508', '367', 'null', 'id为[283]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60509', '368', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('60510', '369', 'null', '未获取到id为[0]的保单信息');
INSERT INTO `insu_error_msg` VALUES ('60511', '370', 'null', 'id为[284]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60512', '371', 'null', 'id为[285]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60513', '346', 'null', 'id为[266]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60514', '373', 'null', 'id为[287]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60515', '337', 'null', 'id为[259]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60516', '338', 'null', 'id为[259]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60517', '339', 'null', 'id为[258]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60518', '341', 'null', 'id为[261]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60519', '357', 'null', 'id为[275]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60520', '358', 'null', 'id为[276]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60521', '359', 'null', 'id为[277]的投保单数据:该机构下没有该产品的销售权限！合计保费与系统定义产品保费不一致!');
INSERT INTO `insu_error_msg` VALUES ('60522', '360', 'null', 'id为[277]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60523', '361', 'null', 'id为[276]的撤单数据业务日期须小于生效日期!');
INSERT INTO `insu_error_msg` VALUES ('60524', '323', 'OOKP1417601720083', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60525', '325', 'OOKP1417601978800', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60526', '323', 'null', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60527', '325', 'null', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60528', '323', 'OOKP1417601720083', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60529', '325', 'OOKP1417601978800', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60530', '323', 'null', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60531', '325', 'null', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60532', '327', 'OOKP1417605740438', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60533', '329', 'OOKP1417607057583', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60534', '331', 'OOKP1417607184930', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60535', '323', 'null', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60536', '325', 'null', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60537', '327', 'null', 'id为[251]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60538', '329', 'null', 'id为[253]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60539', '331', 'null', 'id为[255]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60540', '323', 'OOKP1417601720083', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60541', '325', 'OOKP1417601978800', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60542', '323', 'null', 'id为[247]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60543', '325', 'null', 'id为[249]的投保单数据:投保人身份证号码值非法！');
INSERT INTO `insu_error_msg` VALUES ('60544', '2', 'ookp001', '系统超时');
INSERT INTO `insu_error_msg` VALUES ('60545', '2', 'ookp001', '系统超时');

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
-- Records of order_longdistance_details
-- ----------------------------
INSERT INTO `order_longdistance_details` VALUES ('2', 'Long1414051675983221', '2014-11-23 16:22:00', '1.00', '2301', '2301', '辽宁省沈阳市东陵区长白街172-2号-6门', '217路支线2(沈阳军区现代管理学院-松山路(地铁口))', '41.737804', '123.408845', '41.899633', '123.468291', '1', '29', '2014-10-23 16:07:55', '2014-10-23 16:12:40', '2014-10-25 10:40:40', null, null, null, null, null, null, '1', '', '3', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('3', 'Long1414053090572423', '2015-10-23 16:46:00', '0.00', '2301', '2301', '辽宁省沈阳市东陵区长白街172-2号-6门', '沈阳奥体中心', '41.737636', '123.408797', '41.746795', '123.470082', '1', '29', '2014-10-23 16:31:30', '2014-10-24 10:06:51', '2014-10-25 11:04:36', null, null, null, null, null, null, '1', '', '3', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('4', 'Long1414053200501198', '2014-12-23 16:47:00', '0.00', '2301', '2301', '辽宁省沈阳市东陵区长白街125号', '三洪庄村', '41.738108', '123.408734', '41.517611', '123.393491', '27', '29', '2014-10-23 16:33:20', '2014-10-24 10:29:15', '2014-10-24 14:58:32', null, null, null, null, null, '2014-10-28 10:05:29', '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('5', 'Long1414055462761790', '2014-11-23 17:25:00', '1.00', '2301', '2301', '辽宁省沈阳市东陵区长白街172-2号-6门', '四龙湾村', '41.737758', '123.408795', '42.105826', '123.315287', '100', '26', '2014-10-23 17:11:02', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('6', 'Long1414057430879380', '2014-10-25 14:54:00', '60.00', '2301', '13', '辽宁省沈阳市东陵区长白街172-2号-6门', '西直门', '41.737661', '123.408636', '39.946525', '116.358742', '6', '25', '2014-10-23 17:54:51', '2014-10-23 17:54:51', '2014-10-23 18:32:56', null, null, null, null, null, null, '0', '', '3', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('7', 'Long1414058439100242', '2014-10-24 14:15:00', '100.00', '2301', 'v', '辽宁省沈阳市东陵区长白街172-2号-6门', 'PP俱乐部', '41.737652', '123.408633', '31.228742', '121.402201', '4', '25', '2014-10-23 18:00:39', '2014-10-23 18:02:28', '2014-10-23 18:09:06', '2014-10-23 18:09:22', null, null, null, null, null, '1', '', '4', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('8', 'Long1414065028317542', '2014-10-24 21:04:00', '50.00', '2301', '2303', '辽宁省沈阳市东陵区长白街172-2号-6门', '以勒齿科', '41.737870', '123.408914', '41.155654', '123.035000', '4', '25', '2014-10-23 19:50:28', '2014-10-23 19:50:42', '2014-10-23 19:51:06', '2014-10-23 19:51:13', null, null, null, null, null, '1', '', '4', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('9', 'Long1414066617963740', '2014-10-31 20:31:00', '10.00', '2301', '13', '辽宁省沈阳市东陵区长白街172-2号-6门', '台北纯K', '41.737937', '123.409153', '31.234918', '121.488495', '6', '25', '2014-10-23 20:16:57', '2014-10-24 11:12:01', null, null, null, null, null, null, null, '5', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('10', 'Long1414116337270708', '2014-10-25 10:20:00', '2.00', '2301', '11', '辽宁省沈阳市东陵区长白街172-2号-6门', '来故乡超市', '41.737528', '123.408495', '40.003890', '116.482558', '3', '14', '2014-10-24 10:05:37', '2014-10-24 10:20:14', '2014-10-24 10:21:56', '2014-10-24 10:26:49', '2014-10-24 10:27:03', '2014-10-24 10:27:05', '2014-10-25 09:58:45', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('11', 'Long1414117788402839', '2014-10-25 10:43:00', '1.00', '2301', '11', '辽宁省沈阳市东陵区长白街172-2号-6门', '阿美发屋', '41.737578', '123.408606', '39.922258', '116.192871', '1', '14', '2014-10-24 10:29:48', '2014-10-24 10:30:00', '2014-10-24 10:30:23', '2014-10-24 10:30:25', null, null, null, null, null, '1', '', '4', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('12', 'Long1414120309526655', '2014-10-27 11:26:00', '16.00', '2301', '13', '辽宁省沈阳市东陵区长白街172-2号-6门', 'Nancy k', '41.737706', '123.408757', '31.161155', '121.435879', '5', '26', '2014-10-24 11:11:49', '2014-10-24 11:14:02', '2014-10-24 11:14:06', '2014-10-24 11:14:07', '2014-10-24 11:14:33', '2014-10-24 11:14:35', '2014-10-24 11:14:54', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('13', 'Long1414203791356226', '2014-10-26 10:37:00', '1.00', '2301', '11', '辽宁省沈阳市东陵区长白街172-2号-6门', '安康智能电供暖', '41.737663', '123.408752', '41.813168', '123.420170', '3', '29', '2014-10-25 10:23:11', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('14', 'Long1414375692829157', '2014-10-29 10:22:00', '1.00', '2301', '11', '辽宁省沈阳市东陵区长白街172-2号-6门', '好家居建材总汇', '41.737704', '123.408668', '39.970389', '116.354052', '1', '14', '2014-10-27 10:08:12', '2014-10-27 10:08:59', null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('15', 'Long1414400848188412', '2014-10-28 16:16:00', '4.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '有一家', '41.737662', '123.408780', '38.894417', '121.533785', '5', '42', '2014-10-27 17:07:28', '2014-10-27 17:07:33', '2014-10-27 17:07:43', '2014-10-27 17:07:44', '2014-10-27 17:07:54', '2014-10-27 17:07:58', '2014-10-27 17:08:22', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('16', 'Long1414428568566146', '2014-10-29 23:59:00', '17.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '君丰川菜管', '41.737630', '123.408785', '38.875038', '121.537633', '3', '6', '2014-10-28 00:49:28', '2014-10-28 00:49:37', '2014-10-28 00:49:50', '2014-10-28 00:49:59', '2014-10-28 10:03:09', '2014-10-28 10:03:11', '2014-11-04 15:09:11', null, null, '1', '', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('17', 'Long1414467061022179', '2014-10-29 11:45:00', '50.00', '2301', '11', '辽宁省沈阳市东陵区长白街172-13号-4门', '西直门', '41.737524', '123.408651', '39.946436', '116.361442', '5', '25', '2014-10-28 11:31:01', '2014-10-28 11:31:14', '2014-10-28 11:45:08', '2014-10-28 11:45:11', '2014-10-29 15:42:03', '2014-10-29 15:42:39', '2014-10-29 15:43:20', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('18', 'Long1414482815382765', '2014-10-30 16:07:00', '1.00', '2301', '11', '辽宁省沈阳市东陵区长白街172-2号-6门', '望京壳牌换油中心(望京店)', '41.737846', '123.408928', '39.984661', '116.475514', '1', '44', '2014-10-28 15:53:35', '2014-10-28 15:53:45', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('19', 'Long1414487636973927', '2014-10-29 17:12:00', '1.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连万达广场', '41.737754', '123.408853', '38.916261', '121.614894', '1', '49', '2014-10-28 17:13:56', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('20', 'Long1414487682666518', '2014-10-29 00:29:00', '1.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '阿尔滨游泳健身中心', '41.737754', '123.408853', '39.102818', '121.709850', '1', '49', '2014-10-28 17:14:42', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('21', 'Long1414488520144297', '2014-10-29 17:43:00', '9999.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '阿尔滨游泳健身中心', '41.737718', '123.408785', '39.102818', '121.709850', '1', '49', '2014-10-28 17:28:40', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('22', 'Long1414488709793386', '2014-10-29 17:43:00', '9999.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '阿尔滨游泳健身中心', '41.737651', '123.408771', '39.102818', '121.709850', '1', '49', '2014-10-28 17:31:49', null, null, null, null, null, null, null, null, '0', '记得记得就觉话好的好的好的还记得解答解答都会觉得觉得就到家记得哈回答回答下等哈大家觉得记得记得就觉得解答解答实话实说各睡各的很大很大好回答回答电话好的好的好的还记得解答解答都会觉得觉得就到家记得哈回答回答下等哈大家觉得记得记得就觉得解答解答实话实说各睡各的很大很大好回答回答电话好的好的好的还记得解答解答都会觉得觉得就到家记得哈回答回答下等哈大家觉得记得记得就觉得解答解答', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('23', 'Long1414489004934956', '2014-10-29 17:48:00', '2.00', '2301', '2302', '万象城', '阿尔滨游泳健身中心', '41.780747', '123.442020', '39.102818', '121.709850', '1', '49', '2014-10-28 17:36:44', '2014-10-29 10:37:06', '2014-10-29 10:40:25', null, null, null, null, null, '2014-10-29 10:40:48', '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('24', 'Long1414489210990308', '2014-10-29 17:52:00', '1.00', '2301', '2302', '中共沈阳市委沈阳市人民政府信访局(沈阳市人民建议征集办公室北)', '大连市城乡建设委员会招标投标管理处', '41.809526', '123.437403', '38.915160', '121.644568', '1', '49', '2014-10-28 17:40:10', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('25', 'Long1414489994571730', '2014-10-30 18:07:00', '12.00', '2301', '13', '哈尔滨银行(沈阳分行)', '东方明珠', '41.811240', '123.430875', '31.245372', '121.506260', '6', '56', '2014-10-28 17:53:14', '2014-10-29 14:08:13', '2014-10-30 17:33:18', '2014-10-30 17:33:29', null, null, null, null, null, '6', '', '4', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('26', 'Long1414500325944737', '2014-10-29 21:59:00', '7.00', '2301', '13', '辽宁省沈阳市东陵区长白街172-2号-6门', '东方明珠', '41.737831', '123.408879', '31.245372', '121.506260', '2', '56', '2014-10-28 20:45:25', '2014-10-28 20:51:45', '2014-10-28 20:53:18', '2014-10-28 20:53:19', '2014-10-28 20:54:41', '2014-10-28 20:54:42', '2014-10-28 20:55:15', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('27', 'Long1414547375092144', '2014-10-30 09:58:00', '26.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '洋山深水港', '41.737652', '123.408420', '30.645026', '122.073016', '5', '42', '2014-10-29 09:49:35', '2014-10-29 09:49:58', '2014-10-29 09:51:46', '2014-10-29 09:51:56', '2014-10-29 09:54:26', '2014-10-29 09:54:33', null, null, null, '3', '', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('28', 'Long1414551165523698', '2014-10-31 11:02:00', '10.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连站', '41.737780', '123.408791', '38.928030', '121.639696', '2', '49', '2014-10-29 10:52:45', '2014-10-29 13:46:54', '2014-10-29 15:00:53', '2014-10-29 15:12:30', '2014-10-29 15:13:15', '2014-10-29 15:13:18', '2014-10-29 15:13:33', null, null, '2', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('29', 'Long1414552489385324', '2014-10-31 11:17:00', '1.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '没有名美甲会所', '41.737787', '123.408819', '38.926171', '121.641184', '11', '42', '2014-10-29 11:14:49', '2014-10-29 11:15:18', '2014-11-03 09:16:29', null, null, null, null, null, null, '2', '', '3', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('30', 'Long1414563283477162', '2014-10-31 18:27:00', '35.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-13号-4门', '好乐迪量贩KTV', '41.737502', '123.408686', '38.891104', '121.592081', '5', '49', '2014-10-29 14:14:43', '2014-10-29 14:23:56', null, null, null, null, null, null, null, '1', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('31', 'Long1414563460352844', '2014-11-01 14:30:00', '19.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '阿尔滨游泳健身中心', '41.737672', '123.408793', '39.102818', '121.709850', '6', '49', '2014-10-29 14:17:40', '2014-10-29 14:27:33', '2014-10-29 15:06:59', '2014-10-29 15:12:09', '2014-10-29 15:15:17', '2014-10-29 15:15:18', null, null, null, '2', '快来', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('32', 'Long1414565719475476', '2014-10-30 16:01:00', '28.00', '2301', '13', '辽宁省沈阳市东陵区长白街172-2号-6门', '东方明珠', '41.737953', '123.408868', '31.242905', '121.507150', '6', '25', '2014-10-29 14:55:19', '2014-10-30 16:03:29', null, null, null, null, null, null, null, '2', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('33', 'Long1414566363514192', '2014-11-03 15:18:00', '9999.00', '2301', '13', '辽宁省沈阳市东陵区长白街172-2号-6门', '龙王庙桥', '41.737764', '123.408804', '41.767479', '123.404613', '4', '49', '2014-10-29 15:06:03', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('34', 'Long1414566599257264', '2014-10-30 15:24:00', '10.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '庙岭幼儿园', '41.737952', '123.408823', '38.906779', '121.533082', '3', '49', '2014-10-29 15:09:59', '2014-10-29 15:10:24', null, null, null, null, null, null, null, '1', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('35', 'Long1414567930916900', '2014-10-30 15:45:00', '9.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连万达广场', '41.737694', '123.408824', '38.916261', '121.614903', '3', '49', '2014-10-29 15:32:10', '2014-10-29 15:36:24', '2014-10-29 15:44:18', null, null, null, null, null, null, '1', '', '3', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('36', 'Long1414568340711474', '2014-10-31 16:53:00', '23.00', '2301', '11', '辽宁省沈阳市东陵区长白街172-2号-6门', '阿尤', '41.737778', '123.408831', '39.926816', '116.359581', '1', '49', '2014-10-29 15:39:00', '2014-10-29 15:45:30', '2014-10-29 15:45:41', '2014-10-29 15:45:42', '2014-10-29 15:45:59', '2014-10-29 15:46:01', null, null, null, '1', '', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('37', 'Long1414568848647821', '2014-10-30 16:01:00', '13.00', '2301', '11', '辽宁省沈阳市东陵区长白街172-2号-6门', '阿尤', '41.737776', '123.408827', '39.926816', '116.359581', '1', '49', '2014-10-29 15:47:28', '2014-10-29 15:47:45', '2014-10-29 15:47:56', '2014-10-29 15:47:57', '2014-10-29 15:48:03', '2014-10-29 15:48:05', '2014-10-29 15:48:18', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('38', 'Long1414570415679230', '2014-10-31 00:00:00', '8.00', '2301', '13', '辽宁省沈阳市东陵区长白街172-2号-6门', '龙阳路', '41.737961', '123.408821', '31.209599', '121.563888', '1', '49', '2014-10-29 16:13:35', '2014-10-29 16:15:12', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('39', 'Long1414657424897364', '2014-11-01 00:46:00', '2.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '因特摩尔', '41.737771', '123.408778', '38.865838', '121.531682', '2', '43', '2014-10-30 16:23:44', null, null, null, null, null, null, null, null, '0', '啊啊啊', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('40', 'Long1414657551681861', '2050-12-01 16:21:00', '10.00', '2301', '2307', '奥体中心', '黑山县', '41.749034', '123.460170', '41.799698', '122.260736', '30', '26', '2014-10-30 16:25:51', '2014-10-30 16:26:13', '2014-10-30 16:28:59', '2014-10-31 14:28:49', '2014-10-31 14:29:08', '2014-10-31 14:29:12', null, null, null, '2', '我的家里的人', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('41', 'Long1414657960523231', '2014-11-30 16:45:00', '10.00', '2301', '11', '中街', '朝阳大悦城', '41.805334', '123.460449', '39.929894', '116.524274', '30', '26', '2014-10-30 16:32:40', '2014-10-30 16:32:57', '2014-10-30 16:38:09', '2014-10-30 16:48:26', '2014-10-30 16:54:37', '2014-10-30 16:54:46', null, null, null, '6', '', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('42', 'Long1414658965945536', '2014-11-30 17:03:00', '1.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '因特摩尔', '41.737800', '123.408922', '38.865838', '121.531682', '7', '43', '2014-10-30 16:49:25', '2014-10-30 17:18:26', '2014-11-04 10:17:00', null, null, null, null, null, null, '5', '爸爸', '3', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('43', 'Long1414659571634181', '2014-10-31 17:14:00', '5.00', '2301', '11', '奥体中心', '天安门', '41.749034', '123.460170', '39.915168', '116.403875', '30', '26', '2014-10-30 16:59:31', '2014-10-30 17:01:19', null, null, null, null, null, null, null, '30', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('44', 'Long1414660978320949', '2014-10-31 17:23:00', '16.00', '2301', '13', '辽宁省沈阳市东陵区长白街172-2号-6门', '东方明珠', '41.737813', '123.409047', '31.242905', '121.507150', '6', '56', '2014-10-30 17:22:58', '2014-10-30 17:32:34', '2014-10-30 17:34:32', '2014-10-30 17:34:34', '2014-10-30 17:35:22', '2014-10-30 17:35:29', '2014-10-30 17:36:41', null, null, '3', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('45', 'Long1414661388969662', '2014-12-30 17:20:00', '10.00', '2301', '11', '辽宁省沈阳市东陵区长白街172-2号-6门', '天安门', '41.737654', '123.408794', '39.915168', '116.403875', '6', '26', '2014-10-30 17:29:48', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('46', 'Long1414662996695475', '2014-11-02 18:10:00', '10.00', '2301', '13', '辽宁省沈阳市东陵区长白街172-2号-6门', '东方明珠', '41.737849', '123.409015', '31.242905', '121.507150', '6', '56', '2014-10-30 17:56:36', '2014-10-30 17:57:01', '2014-10-30 18:18:41', '2014-10-30 18:18:43', '2014-10-30 18:19:18', '2014-10-30 18:19:21', '2014-10-30 18:20:22', null, null, '4', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('47', 'Long1414664407729554', '2014-12-30 18:34:00', '1.00', '2301', '11', '奥体中心', '西单大悦城', '41.749034', '123.460170', '39.917527', '116.378948', '6', '26', '2014-10-30 18:20:07', '2014-10-30 18:20:29', '2014-10-30 18:20:40', '2014-10-30 18:21:38', '2014-10-30 18:22:50', '2014-10-30 18:22:53', '2014-10-30 18:23:11', null, null, '6', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('48', 'Long1414742355113263', '2014-11-01 16:40:00', '1.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '老虎滩海洋公园', '41.737634', '123.408635', '38.882397', '121.683951', '18', '26', '2014-10-31 15:59:15', '2014-10-31 15:59:50', '2014-10-31 16:03:14', '2014-10-31 16:18:12', '2014-10-31 16:26:35', '2014-10-31 16:27:13', null, null, null, '1', '\n', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('49', 'Long1414744160762725', '2014-11-30 16:44:00', '1.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-13号-4门', '大连森林动物园', '41.737580', '123.408772', '38.887193', '121.621459', '13', '26', '2014-10-31 16:29:20', '2014-10-31 16:29:51', '2014-10-31 16:36:07', '2014-10-31 16:36:29', '2014-10-31 16:58:05', '2014-10-31 16:58:48', null, null, null, '1', '', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('50', 'Long1414746015274509', '2014-11-01 11:00:00', '1.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-13号-4门', '西郊国家森林公园', '41.737687', '123.409095', '38.938169', '121.516179', '16', '26', '2014-10-31 17:00:15', '2014-10-31 17:00:34', '2014-10-31 17:01:55', '2014-10-31 17:01:59', '2014-11-03 10:17:54', '2014-11-03 10:18:07', null, null, null, '1', '\n\n\n', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('51', 'Long1414811274482186', '2014-11-03 11:22:00', '1.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '劳动公园', '41.737700', '123.408920', '38.916928', '121.640825', '16', '26', '2014-11-01 11:07:54', '2014-11-01 11:08:30', '2014-11-01 11:08:54', '2014-11-01 11:12:32', null, null, null, null, '2014-12-10 14:19:47', '0', '\n', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('52', 'Long1414822765512816', '2014-11-02 14:33:00', '1.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '海之韵公园', '41.737870', '123.408878', '38.906280', '121.711467', '11', '26', '2014-11-01 14:19:25', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('53', 'Long1414979530450786', '2014-11-05 10:05:00', '15.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '欧陆公司', '41.737707', '123.408781', '38.995474', '0.000000', '10', '42', '2014-11-03 09:52:10', '2014-11-03 16:32:58', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('54', 'Long1414980549216143', '2014-11-04 04:44:00', '1.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连北站', '41.737874', '123.408870', '39.021001', '121.613253', '19', '26', '2014-11-03 10:09:09', '2014-11-03 10:15:10', '2014-11-03 14:22:20', '2014-11-03 14:22:22', '2014-11-03 14:42:35', '2014-11-03 14:42:37', null, null, null, '1', '', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('55', 'Long1414980659075468', '2014-11-04 04:44:00', '1.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连银行(星海支行)', '41.737874', '123.408870', '38.864532', '121.535837', '17', '26', '2014-11-03 10:10:59', '2014-11-03 10:14:55', '2014-11-03 14:19:43', null, null, null, null, null, '2014-11-03 14:19:58', '0', '\n', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('56', 'Long1414982585876088', '2014-11-04 04:40:00', '15.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连银行(滨海支行)', '41.737664', '123.408732', '38.884167', '121.691579', '1', '26', '2014-11-03 10:43:05', '2014-11-03 10:43:26', '2014-11-03 10:43:40', '2014-11-03 10:43:53', '2014-11-03 10:43:59', '2014-11-03 10:44:00', null, null, null, '1', '\n', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('57', 'Long1415004115943797', '2014-11-04 11:11:00', '31.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '中国工商银行(新桥支行)', '41.738009', '123.408827', '39.054756', '121.792682', '13', '43', '2014-11-03 16:41:55', '2014-11-03 16:42:12', '2014-11-03 16:42:26', null, null, null, null, null, '2014-11-03 16:44:31', '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('58', 'Long1415064444353878', '2014-11-04 09:43:00', '15.00', '辽宁省沈阳市东陵区长白街172-2号-6门', '用友软件园', '辽宁省沈阳市东陵区长白街172-2号-6门', '用友软件园', '41.737670', '123.408935', '40.072692', '116.240894', '3', '6', '2014-11-06 10:04:41', '2014-11-06 10:04:41', '2014-11-07 15:10:03', '2014-11-07 15:10:22', null, null, null, null, null, '3', '', '4', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('59', 'Long1415070420544311', '2014-11-04 15:21:00', '28.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连市高新区管委会', '41.737727', '123.408686', '38.863050', '121.532417', '7', '6', '2014-11-04 11:07:00', '2014-11-04 12:23:05', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('60', 'Long1415084800536318', '2014-11-06 18:20:00', '42.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '从众居公寓(联合路)', '41.737716', '123.408700', '38.920795', '121.604627', '20', '6', '2014-11-04 15:06:40', '2014-11-04 15:07:05', '2014-11-04 15:07:24', '2014-11-04 15:07:26', '2014-11-04 15:07:43', '2014-11-04 15:07:44', '2014-11-04 15:08:17', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('61', 'Long1415090825533965', '2014-11-05 17:00:00', '1.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '军港公园', '41.737759', '123.408912', '38.809391', '121.264851', '14', '43', '2014-11-04 16:47:05', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('62', 'Long1415156055807591', '2014-11-05 12:04:00', '20.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '西安咸阳国际机场', '41.737611', '123.408653', '34.445703', '108.766653', '6', '6', '2014-11-05 10:54:30', '2014-11-05 10:54:30', '2014-11-05 13:10:10', '2014-11-05 15:19:04', '2014-11-05 15:20:03', '2014-11-05 15:20:04', '2014-11-05 15:20:31', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('63', 'Long1415181434125558', '2014-11-08 17:55:00', '38.00', '', '', 'åå¹³åº', 'ä¸åæ¡¥', '41.786474', '123.414332', '39.966880', '116.463573', '6', '28', '2014-11-05 17:57:14', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('64', 'Long1415189375708853', '2014-11-06 20:08:00', '55.00', '', '', 'ä¸åæ¡¥', 'å¤§è¿åç«(ååºç«å£)', '39.966880', '116.463573', '39.021533', '121.614423', '5', '28', '2014-11-05 20:09:35', null, null, null, null, null, null, null, null, '0', 'ä»åºæ cfv', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('65', 'Long1415189716791689', '2014-11-07 20:14:00', '56.00', '11', '13', '北京火车站', '上海火车站北广场-进站口', '39.909793', '116.433646', '31.257193', '121.462868', '4', '28', '2014-11-05 20:15:16', '2014-11-06 11:11:07', '2014-11-07 11:03:12', '2014-11-07 15:04:05', '2014-11-07 15:13:17', '2014-11-08 17:13:04', '2014-11-10 11:14:16', null, null, '1', '黄金季节jjk', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('66', 'Long1415206627403678', '2014-11-08 01:13:00', '7.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '皮长高速公路', '41.737778', '123.408501', '39.403567', '122.296967', '4', '6', '2014-11-06 00:57:07', '2014-11-06 10:03:19', '2014-11-07 04:40:34', null, null, null, null, null, null, '3', '', '3', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('67', 'Long1415293751276879', '2014-11-08 01:23:00', '103.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '兄弟拉面', '41.737581', '123.408487', '38.907186', '121.580300', '24', '6', '2014-11-07 01:09:11', '2014-11-07 04:41:03', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('68', 'Long1415326269121891', '2014-11-07 10:20:00', '75.00', '2301', '2313', '奥体中心', '朝阳四中', '41.749034', '123.460170', '41.564930', '120.444103', '1', '79', '2014-11-07 10:11:09', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('69', 'Long1415330156084269', '2014-11-07 11:20:00', '36.00', '2301', '11', '奥体中心', '天安门广场', '41.749034', '123.460170', '39.912733', '116.404015', '4', '28', '2014-11-07 11:15:56', '2014-11-07 11:17:47', '2014-11-07 11:21:37', '2014-11-07 11:22:32', '2014-11-07 11:22:51', '2014-11-07 11:23:17', '2014-11-08 10:44:52', null, null, '1', '给红红火火', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('70', 'Long1415341979974717', '2014-12-07 11:51:00', '111.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '甘井子区', '41.738023', '123.408819', '38.955462', '121.528500', '1', '80', '2014-11-07 14:32:59', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('71', 'Long1415342323836742', '2014-11-09 14:52:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连万达广场', '41.737655', '123.408810', '38.916261', '121.614894', '1', '80', '2014-11-07 14:38:43', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('72', 'Long1415584548367801', '2014-12-10 10:09:00', '100.00', '2301', '2302', '长白三街', '大连理工大学', '41.758662', '123.405439', '38.888674', '121.536184', '1', '80', '2014-11-10 09:55:48', '2014-11-10 16:45:31', null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('73', 'Long1415607673707831', '2014-11-10 16:35:00', '50.00', '2301', '11', '辽宁省沈阳市东陵区长白街172-2号-6门', '西直门', '41.737890', '123.408822', '39.946436', '116.361442', '7', '56', '2014-11-10 16:21:13', '2014-11-10 16:21:33', '2014-11-10 16:21:48', '2014-11-10 16:22:09', '2014-11-14 18:09:05', '2014-11-14 18:09:06', null, null, null, '1', '', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('74', 'Long1415609085779091', '2014-12-10 16:56:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连理工大学', '41.737637', '123.408852', '38.888674', '121.536184', '6', '80', '2014-11-10 16:44:45', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('75', 'Long1415610895112113', '2014-11-11 17:29:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连理工大学', '41.737637', '123.408852', '38.888674', '121.536184', '4', '80', '2014-11-10 17:14:55', '2014-11-10 17:15:04', '2014-11-10 17:15:21', '2014-11-10 17:15:23', '2014-11-10 17:15:43', '2014-11-10 17:15:44', '2014-11-10 17:16:03', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('76', 'Long1415611093184838', '2014-11-10 17:32:00', '60.00', '2301', '11', '辽宁省沈阳市东陵区长白街172-2号-6门', '哈哈镜鸭脖子', '41.737713', '123.408974', '39.937231', '116.513764', '4', '56', '2014-11-10 17:18:13', '2014-11-10 17:40:15', '2014-11-19 22:21:51', '2014-11-19 22:21:55', '2014-11-19 22:23:22', '2014-11-19 22:23:35', null, null, null, '3', '', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('77', 'Long1415612278116147', '2014-11-11 17:50:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连理工大学', '41.737637', '123.408852', '38.888674', '121.536184', '1', '80', '2014-11-10 17:37:58', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('78', 'Long1415614035734598', '2014-11-11 18:21:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连海事大学', '41.737904', '123.408999', '38.877552', '121.538757', '1', '79', '2014-11-10 18:07:15', '2014-11-10 18:07:21', '2014-11-10 18:10:51', '2014-11-10 18:10:53', '2014-11-24 15:04:45', '2014-11-24 15:04:54', null, null, null, '1', '', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('79', 'Long1415618675086967', '2014-12-10 19:38:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连理工大学', '41.737751', '123.408652', '38.888681', '121.536184', '1', '79', '2014-11-10 19:24:35', null, null, null, null, null, null, null, null, '0', '', '1', '1', '1');
INSERT INTO `order_longdistance_details` VALUES ('80', 'Long1415619147132301', '2014-11-11 19:46:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连理工大学', '41.737939', '123.408950', '38.888674', '121.536184', '1', '79', '2014-11-10 19:32:27', null, null, null, null, null, null, null, null, '0', '', '8', '1', '1');
INSERT INTO `order_longdistance_details` VALUES ('81', 'Long1415619714308017', '2014-11-12 19:56:00', '102.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-13号-4门', '从众居公寓(联合路)', '41.737472', '123.408524', '38.920795', '121.604627', '8', '79', '2014-11-10 19:41:54', '2014-11-10 19:43:28', '2014-11-10 19:43:46', '2014-11-10 19:48:16', '2014-11-10 19:48:38', '2014-11-10 19:48:53', '2014-11-10 20:04:23', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('82', 'Long1415688039189440', '2014-11-12 14:54:00', '100.00', '2301', '2303', '辽宁省沈阳市东陵区长白街172-2号-6门', '鞍山师范学院', '41.737748', '123.408916', '41.083256', '123.000849', '1', '80', '2014-11-11 14:40:39', '2014-11-11 14:41:25', null, null, null, null, null, null, null, '1', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('83', 'Long1415688826457945', '2014-11-12 15:08:00', '100.00', '2301', '2306', '辽宁省沈阳市东陵区长白街172-2号-6门', '丹东市机电职业技术学校仪表园校区', '41.731954', '123.402057', '40.017975', '124.356922', '1', '26', '2014-11-11 14:53:46', '2014-11-11 14:54:18', '2014-11-11 14:57:58', null, null, null, null, null, '2014-11-22 11:19:24', '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('84', 'Long1415691186970926', '2014-12-11 15:46:00', '100.00', '2301', '2303', '辽宁省沈阳市东陵区长白街172-2号-6门', '鞍山市交通局(解放东路)', '41.737647', '123.408732', '41.095551', '123.001680', '20', '80', '2014-11-11 15:33:06', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('85', 'Long1415691341760905', '2014-12-11 15:49:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连东软信息学院', '41.737647', '123.408732', '38.894066', '121.542163', '17', '80', '2014-11-11 15:35:41', '2014-11-11 16:06:43', '2014-11-11 16:29:36', null, null, null, null, null, null, '10', '', '3', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('86', 'Long1415692301712064', '2014-12-11 16:06:00', '100.00', '2301', '2313', '辽宁省沈阳市东陵区长白街172-2号-6门', '朝阳高等师范专科学校', '41.737636', '123.408736', '41.539145', '120.439220', '16', '80', '2014-11-11 15:51:41', '2014-11-11 15:52:22', '2014-11-11 15:52:51', null, null, null, null, null, null, '5', '', '3', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('87', 'Long1415935530465240', '2014-12-14 11:39:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区胜利南街', '老鳖湾', '41.737145', '123.400973', '38.908099', '121.638632', '1', '79', '2014-11-14 11:25:30', '2014-11-14 11:39:44', null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('88', 'Long1415936627958420', '2014-12-14 11:58:00', '100.00', '2301', '2313', '辽宁省沈阳市东陵区胜利南街', '朝阳高等师范专科学校', '41.737177', '123.400960', '41.539145', '120.439220', '1', '79', '2014-11-14 11:43:47', '2014-11-14 11:43:57', '2014-11-14 11:44:08', '2014-11-14 11:44:13', null, null, null, null, null, '1', '', '4', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('89', 'Long1415959715847423', '2014-11-14 18:20:00', '51.00', '2301', '13', '辽宁省沈阳市东陵区胜利南街', '东方明珠', '41.737032', '123.400904', '31.245369', '121.506260', '6', '56', '2014-11-14 18:08:35', '2014-11-15 17:39:37', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('90', 'Long1416054440045646', '2014-12-15 20:41:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '东直门', '41.737173', '123.400927', '39.947892', '116.441454', '2', '95', '2014-11-15 20:27:20', '2014-11-15 20:27:51', null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('91', 'Long1416056008977286', '2014-12-15 21:05:00', '50.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '三元桥', '41.736941', '123.400979', '39.966880', '116.463573', '8', '95', '2014-11-15 20:53:28', '2014-11-15 20:53:56', '2014-11-15 20:54:40', null, null, null, null, null, null, '2', '', '3', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('92', 'Long1416141458063164', '2014-11-17 20:52:00', '50.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '新闻大厦酒店', '41.737100', '123.400889', '39.913618', '116.428783', '4', '95', '2014-11-16 20:37:38', '2014-11-16 20:37:54', '2014-11-16 20:38:18', null, null, null, null, null, null, '1', '', '3', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('93', 'Long1416211854624965', '2014-11-17 17:24:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区长白街172-2号-6门', '凤凰汇购物中心', '41.738041', '123.409097', '39.968391', '116.464206', '1', '96', '2014-11-17 16:10:54', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('94', 'Long1416303138094937', '2014-11-20 17:30:00', '12.00', '2301', '11', '和平区', '北京火车站', '41.786474', '123.414332', '39.909793', '116.433646', '2', '96', '2014-11-18 17:32:18', null, null, null, null, null, null, null, null, '0', '咯哦咯哦', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('95', 'Long1416332717914172', '2014-11-21 01:59:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区长白街172-2号-6门', '风情发型设计工作室', '41.737613', '123.408547', '39.975400', '116.443858', '1', '14', '2014-11-19 01:45:17', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('96', 'Long1416374684575103', '2014-12-25 06:02:00', '200.00', '11', '4301', '北京市朝阳区西坝河路甲1号', '郑州国际会展中心', '39.977203', '116.458805', '34.777958', '113.737287', '3', '100', '2014-11-19 13:24:44', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('97', 'Long1416375162730310', '2014-12-19 13:44:00', '100.00', '11', '12', '北京市朝阳区东三环北路戊2号', '宝岛眼镜(无锡乐购店)', '39.964102', '116.465243', '39.529261', '117.304801', '3', '106', '2014-11-19 13:32:42', '2014-12-14 16:22:43', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('98', 'Long1416391984361425', '2014-11-20 18:24:00', '1001.00', '11', '6701', '北京市朝阳区太阳宫北街', '北京南路', '39.977226', '116.458839', '43.868720', '87.571512', '3', '100', '2014-11-19 18:13:04', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('99', 'Long1416397844618316', '2014-11-21 20:04:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区长白街172-2号-6门', '共济国际酒店', '41.737772', '123.408768', '39.976616', '116.461741', '1', '6', '2014-11-19 19:50:44', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('100', 'Long1416397856448505', '2014-11-21 20:04:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区长白街172-2号-6门', '共济国际酒店', '41.737772', '123.408768', '39.976616', '116.461741', '1', '6', '2014-11-19 19:50:56', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('101', 'Long1416397868538666', '2014-11-21 20:04:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区长白街172-2号-6门', '共济国际酒店', '41.737772', '123.408768', '39.976616', '116.461741', '1', '6', '2014-11-19 19:51:08', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('102', 'Long1416397889198148', '2014-11-21 20:04:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区长白街172-2号-6门', '共济国际酒店', '41.737772', '123.408768', '39.976616', '116.461741', '1', '6', '2014-11-19 19:51:29', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('103', 'Long1416397906375517', '2014-11-21 20:04:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区长白街172-2号-6门', '共济国际酒店', '41.737772', '123.408768', '39.976616', '116.461741', '1', '6', '2014-11-19 19:51:46', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('104', 'Long1416398765066723', '2014-11-23 20:20:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区长白街172-2号-6门', '九号温泉', '41.737665', '123.408730', '39.911460', '116.508653', '4', '44', '2014-11-19 20:06:05', '2014-11-19 20:08:03', null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('105', 'Long1416399548669875', '2014-11-20 20:33:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连理工大学', '41.737688', '123.408873', '38.888674', '121.536184', '5', '6', '2014-11-19 20:19:08', '2014-11-19 22:30:44', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('106', 'Long1416402878922741', '2014-11-20 21:25:00', '301.00', '11', '6301', '北京市朝阳区太阳宫北街', '太华路', '39.977173', '116.458911', '34.287375', '108.978287', '3', '106', '2014-11-19 21:14:38', '2014-11-19 22:07:16', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('107', 'Long1416406777642113', '2014-11-19 22:21:00', '52.00', '2301', '11', '沈阳火车站东广场-出站口', '天安门广场', '41.800768', '123.402114', '39.912733', '116.404015', '4', '56', '2014-11-19 22:19:37', '2014-12-15 20:18:54', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('108', 'Long1416409959751558', '2014-12-19 23:26:00', '11.00', '11', '12', '北京市朝阳区太阳宫北街', '外环路', '39.975769', '116.464490', '39.081744', '117.158750', '1', '106', '2014-11-19 23:12:39', '2014-11-19 23:13:24', '2014-11-19 23:15:54', '2014-11-19 23:19:13', null, null, null, null, '2014-11-19 23:30:30', '0', '\n', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('109', 'Long1416411474585599', '2014-11-20 23:51:00', '11.00', '11', '12', '北京市朝阳区太阳宫北街', '外环路', '39.975777', '116.464480', '39.081744', '117.158750', '1', '106', '2014-11-19 23:37:54', '2014-11-19 23:38:30', '2014-11-19 23:39:19', '2014-11-19 23:39:25', null, null, null, null, '2014-11-19 23:41:11', '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('110', 'Long1416411823829181', '2014-11-19 23:57:00', '11.00', '11', '12', '北京市朝阳区太阳宫北街', '外环路', '39.975786', '116.464483', '39.081744', '117.158750', '1', '106', '2014-11-19 23:43:43', '2014-11-19 23:43:57', '2014-11-19 23:44:20', '2014-11-19 23:44:25', '2014-11-19 23:44:43', '2014-11-19 23:44:51', '2014-11-19 23:47:08', null, null, '1', '\n\n', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('111', 'Long1416450482808622', '2014-11-22 10:39:00', '1.00', '11', '1510', '北京南站', '龙峰超市', '39.871130', '116.385521', '39.538074', '116.684626', '3', '103', '2014-11-20 10:28:02', '2014-11-20 17:42:19', '2014-12-03 17:10:52', null, null, null, null, null, null, '3', '\n廊坊', '3', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('112', 'Long1416456393766840', '2014-11-21 23:25:00', '1.00', '11', '1510', '北京市朝阳区东三环北路戊2号', '廊坊火车站', '39.964297', '116.464998', '39.514859', '116.714077', '1', '105', '2014-11-20 12:06:33', '2014-11-20 12:08:58', '2014-11-20 12:19:49', '2014-11-20 12:21:35', '2014-11-20 12:29:40', '2014-11-20 12:51:58', null, null, null, '1', '', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('113', 'Long1416483596793845', '2014-11-20 19:48:00', '11.00', '11', '12', '北京市朝阳区霄云路66号', '外环西路', '39.963975', '116.465500', '39.044813', '117.196908', '1', '106', '2014-11-20 19:39:56', '2014-11-20 19:42:00', '2014-11-20 19:47:19', '2014-11-20 19:50:33', '2014-11-20 19:57:05', '2014-11-20 20:07:09', '2014-11-20 20:08:36', null, null, '1', '\n\n', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('114', 'Long1416483809638886', '2014-11-20 19:55:00', '100.00', '11', '13', '北京市昌平区市场中路', '南京东路', '40.105566', '116.290916', '31.243738', '121.490857', '1', '108', '2014-11-20 19:43:29', '2014-12-04 16:08:28', '2014-12-05 16:59:15', '2014-12-08 13:45:10', null, null, null, null, '2014-12-08 13:45:20', '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('115', 'Long1416485789413358', '2014-11-20 20:30:00', '11.00', '11', '5301', '北京市朝阳区霄云路66号', '盐市口广场', '39.963887', '116.465497', '30.661104', '104.076721', '1', '99', '2014-11-20 20:16:29', '2014-11-20 20:21:38', '2014-11-20 20:32:46', '2014-11-20 20:38:13', '2014-11-20 20:38:32', '2014-11-20 20:40:51', '2014-11-20 20:41:11', null, null, '1', '13910221869', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('116', 'Long1416486179633328', '2014-11-21 20:37:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连理工大学', '41.737816', '123.408962', '38.888674', '121.536184', '1', '80', '2014-11-20 20:22:59', '2014-11-20 20:23:24', '2014-11-20 20:24:00', '2014-11-20 20:24:02', null, null, null, null, null, '1', '', '4', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('117', 'Long1416556802067731', '2014-11-24 16:14:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区长白街172-2号-6门', '北京火车站', '41.737746', '123.408862', '39.909793', '116.433646', '1', '44', '2014-11-21 16:00:02', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('118', 'Long1416579505920785', '2014-11-22 23:32:00', '224.00', '11', '6301', '北京市朝阳区太阳宫北街', '太华路-凤城八路口', '39.977240', '116.458901', '34.346266', '108.978190', '1', '100', '2014-11-21 22:18:25', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('119', 'Long1416623105382809', '2014-11-22 11:38:00', '107.00', '2301', '', '辽宁省沈阳市东陵区胜利南街', '大什字街', '41.736971', '123.400864', '41.815640', '123.474936', '20', '6', '2014-11-22 10:25:05', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('120', 'Long1416624086232580', '2014-11-22 10:43:00', '100.00', '2301', '2311', '辽宁省沈阳市东陵区胜利南街', '兴隆台区', '41.737082', '123.400893', '41.155831', '121.969629', '6', '6', '2014-11-22 10:41:26', '2014-11-22 10:41:50', '2014-11-22 10:44:58', '2014-11-22 10:45:01', null, null, null, null, '2014-11-22 10:58:00', '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('121', 'Long1416625998307431', '2014-11-24 11:27:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '蓝色光标广告公司', '41.736973', '123.400914', '39.996912', '116.500975', '1', '44', '2014-11-22 11:13:18', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('122', 'Long1416626145285979', '2014-11-22 13:29:00', '100.00', '2301', '2308', '辽宁省沈阳市东陵区胜利南街', '营口兴隆购物广场', '41.737114', '123.400766', '40.677826', '122.250222', '15', '6', '2014-11-22 11:15:45', '2014-11-22 11:16:04', null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('123', 'Long1416626423557456', '2014-11-22 13:34:00', '100.00', '2301', '2308', '辽宁省沈阳市东陵区胜利南街', '鲅鱼圈区', '41.737099', '123.400767', '40.252584', '122.176897', '9', '80', '2014-11-22 11:20:23', '2014-11-24 09:55:32', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('124', 'Long1416631318096406', '2014-11-22 14:56:00', '100.00', '2301', '2308', '辽宁省沈阳市东陵区胜利南街', '鲅鱼圈区', '41.737074', '123.400836', '40.252584', '122.176897', '7', '80', '2014-11-22 12:41:58', '2014-11-22 12:42:34', '2014-11-22 12:42:49', '2014-11-22 12:43:24', '2014-11-22 12:45:02', '2014-11-22 12:45:30', '2014-11-22 12:46:13', null, null, '3', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('125', 'Long1416793392034880', '2014-11-24 10:57:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连理工大学', '41.737725', '123.408918', '38.888674', '121.536184', '1', '114', '2014-11-24 09:43:12', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('126', 'Long1416793517419607', '2014-11-24 10:59:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连海事大学', '41.737820', '123.408880', '38.877552', '121.538757', '1', '114', '2014-11-24 09:45:17', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('127', 'Long1416798935990001', '2014-11-29 11:30:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '北京银行(经济技术开发区支行)', '41.737285', '123.400951', '39.807098', '116.515190', '1', '44', '2014-11-24 11:15:35', '2014-11-24 11:15:45', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('128', 'Long1416812405855014', '2014-11-25 15:14:00', '138.00', '4603', '2501', '广东省深圳市福田区益田路4068-7', '哈尔滨火车站', '22.539964', '114.062482', '45.767845', '126.639610', '2', '100', '2014-11-24 15:00:05', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('129', 'Long1416812522596323', '2014-11-24 16:14:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连海事大学', '41.737835', '123.408844', '38.877552', '121.538757', '1', '80', '2014-11-24 15:02:02', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('130', 'Long1416812879528403', '2014-11-24 16:22:00', '100.00', '2301', '2303', '辽宁省沈阳市东陵区长白街172-2号-6门', '鞍钢体育馆', '41.737765', '123.408760', '41.135098', '123.016414', '10', '79', '2014-11-24 15:07:59', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('131', 'Long1416886865654864', '2014-11-26 11:54:00', '1.00', '11', '1510', '北京市朝阳区东三环北路戊2号', '廊坊火车站', '39.964228', '116.465239', '39.514859', '116.714077', '1', '105', '2014-11-25 11:41:05', '2014-11-25 11:45:43', '2014-11-25 11:46:41', '2014-11-25 11:47:36', '2014-11-25 11:49:37', '2014-11-25 11:50:09', '2014-11-25 14:20:39', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('132', 'Long1416898584008525', '2014-11-25 15:10:00', '100.00', '2301', '2303', '辽宁省沈阳市东陵区长白街172-2号-6门', '鞍山师范学院', '41.737874', '123.408833', '41.083256', '123.000849', '1', '80', '2014-11-25 14:56:24', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('133', 'Long1416898601751883', '2014-11-25 15:10:00', '100.00', '2301', '2303', '辽宁省沈阳市东陵区长白街172-2号-6门', '鞍山师范学院', '41.737874', '123.408833', '41.083256', '123.000849', '1', '80', '2014-11-25 14:56:41', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('134', 'Long1416898667000731', '2016-11-25 15:00:00', '100.00', '2301', '2303', '辽宁省沈阳市东陵区长白街172-2号-6门', '鞍山师范学院', '41.737730', '123.408888', '41.083256', '123.000849', '4', '115', '2014-11-25 14:57:47', '2014-11-25 15:01:25', '2014-11-25 15:02:10', '2014-11-25 15:02:14', '2014-11-25 15:02:54', '2014-11-25 15:03:04', '2014-11-25 15:03:27', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('135', 'Long1416898675250952', '2016-11-25 15:00:00', '100.00', '2301', '2303', '辽宁省沈阳市东陵区长白街172-2号-6门', '鞍山师范学院', '41.737730', '123.408888', '41.083256', '123.000849', '4', '115', '2014-11-25 14:57:55', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('136', 'Long1416900049925979', '2014-11-25 15:34:00', '100.00', '2301', '2304', '辽宁省沈阳市东陵区长白街172-2号-6门', '抚顺北站', '41.737708', '123.408735', '41.893624', '123.924538', '3', '115', '2014-11-25 15:20:49', '2014-11-25 15:21:17', '2014-11-25 15:22:09', '2014-11-25 15:22:24', '2014-11-25 15:25:49', '2014-11-25 15:26:17', '2014-11-25 15:29:55', null, null, '2', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('137', 'Long1416906947058600', '2014-11-25 18:27:00', '100.00', '2301', '2311', '辽宁省沈阳市东陵区长白街172-2号-6门', '盘锦红海滩风景区', '41.737708', '123.408735', '40.972734', '121.982972', '7', '115', '2014-11-25 17:15:47', '2014-11-25 17:39:52', null, null, null, null, null, null, null, '5', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('138', 'Long1416907049988081', '2014-11-25 18:31:00', '100.00', '2301', '5529', '辽宁省沈阳市东陵区长白街172-2号-6门', '大理古城', '41.737708', '123.408735', '25.693527', '100.170462', '13', '115', '2014-11-25 17:17:29', '2014-11-25 17:35:28', null, null, null, null, null, null, null, '4', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('139', 'Long1416907111860624', '2014-11-25 18:32:00', '100.00', '2301', '5526', '辽宁省沈阳市东陵区长白街172-2号-6门', '文山学院', '41.737708', '123.408735', '23.363607', '104.254940', '10', '115', '2014-11-25 17:18:31', '2014-11-25 17:35:06', '2014-11-25 17:41:38', '2014-11-25 17:41:41', '2014-11-25 17:42:29', '2014-11-25 17:42:41', '2014-11-25 17:42:57', null, null, '5', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('140', 'Long1416907121970788', '2014-11-25 18:32:00', '100.00', '2301', '5526', '辽宁省沈阳市东陵区长白街172-2号-6门', '文山学院', '41.737708', '123.408735', '23.363607', '104.254940', '10', '115', '2014-11-25 17:18:41', '2014-11-25 17:31:52', null, null, null, null, null, null, null, '2', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('141', 'Long1416913716185794', '2014-11-25 21:22:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连理工大学', '41.737765', '123.408760', '38.888674', '121.536184', '5', '115', '2014-11-25 19:08:36', '2014-11-25 19:09:05', '2014-11-25 19:09:36', '2014-11-25 19:10:03', '2014-11-25 19:10:31', '2014-11-25 19:11:25', '2014-11-25 19:11:37', null, null, '3', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('142', 'Long1416970276284226', '2014-11-28 11:05:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '和园景逸大酒店', '41.737823', '123.400835', '40.090923', '116.493506', '1', '44', '2014-11-26 10:51:16', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('143', 'Long1416970730574784', '2014-11-27 11:13:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '对外经济贸易大学就业指导中心', '41.737139', '123.401012', '39.985414', '116.433527', '1', '44', '2014-11-26 10:58:50', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('144', 'Long1416970797124963', '2014-11-30 11:14:00', '110.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '太阳宫', '41.737047', '123.400824', '39.978367', '116.454059', '1', '44', '2014-11-26 10:59:57', '2014-11-26 11:12:33', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('145', 'Long1416970961017607', '2014-11-27 12:16:00', '105.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '来广营乡', '41.737002', '123.400905', '40.038203', '116.440714', '1', '44', '2014-11-26 11:02:41', '2014-11-26 11:12:22', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('146', 'Long1416971324636397', '2014-11-26 11:23:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '来春园', '41.736989', '123.400889', '40.041172', '116.445772', '1', '44', '2014-11-26 11:08:44', '2014-11-26 11:12:04', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('147', 'Long1416971607904314', '2014-11-28 11:27:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '卡欧丝(万柳中路店)', '41.737023', '123.400943', '39.974232', '116.304036', '3', '44', '2014-11-26 11:13:27', '2014-11-26 11:13:39', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('148', 'Long1416972377253899', '2015-02-26 11:40:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '来广营乡政府', '41.737640', '123.400897', '40.038120', '116.440564', '1', '116', '2014-11-26 11:26:17', '2014-11-26 11:26:30', '2014-11-26 11:28:34', '2014-11-26 11:28:37', '2014-11-26 11:28:58', '2014-11-26 11:29:02', '2014-11-26 11:29:25', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('149', 'Long1416982819632799', '2014-11-26 14:34:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '太平洋海底世界博览馆', '41.737028', '123.400898', '39.924769', '116.312665', '1', '44', '2014-11-26 14:20:19', '2014-11-26 14:20:33', '2014-11-26 14:21:03', '2014-11-26 14:21:04', null, null, null, null, '2014-11-26 14:21:33', '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('150', 'Long1416984750607880', '2014-11-27 15:06:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', 'HI辣火锅', '41.736991', '123.400898', '39.945425', '116.417710', '1', '56', '2014-11-26 14:52:30', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('151', 'Long1416985194090581', '2014-11-26 15:09:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '新中关购物中心', '41.737143', '123.400866', '39.984246', '116.321930', '1', '56', '2014-11-26 14:59:54', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('152', 'Long1416986243484338', '2014-11-27 15:30:00', '100.00', '2301', '11', 'FTC沈阳金融中心购物广场', '孤山寨', '41.817147', '123.441865', '39.642624', '115.633868', '1', '44', '2014-11-26 15:17:23', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('153', 'Long1416987227421961', '2014-11-28 15:48:00', '100.00', '2301', '12', '辽宁省沈阳市东陵区胜利南街', '刚泽洗浴', '41.737160', '123.400875', '39.262548', '117.134788', '1', '44', '2014-11-26 15:33:47', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('154', 'Long1416989584554414', '2014-11-26 16:20:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '太阳宫', '41.737065', '123.400942', '39.978367', '116.454059', '1', '44', '2014-11-26 16:13:04', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('155', 'Long1416989658994911', '2014-11-26 16:16:00', '5.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '喔窝鸡', '41.737190', '123.400844', '40.022299', '116.323714', '1', '44', '2014-11-26 16:14:18', '2014-11-26 16:14:25', '2014-11-26 16:14:51', '2014-11-26 16:18:35', null, null, null, null, null, '1', '', '4', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('156', 'Long1416990082349535', '2014-11-26 16:25:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '刚仔食府', '41.737154', '123.400868', '39.820733', '116.447979', '1', '116', '2014-11-26 16:21:22', '2014-11-26 16:21:30', '2014-11-26 16:21:57', '2014-11-26 16:22:00', null, null, null, null, '2014-11-26 16:40:20', '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('157', 'Long1416991338198929', '2014-11-26 16:56:00', '10.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '安立路干洗店', '41.737161', '123.400922', '40.012643', '116.416824', '1', '116', '2014-11-26 16:42:18', '2014-11-26 16:42:31', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('158', 'Long1416991410842484', '2014-11-26 16:58:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '北京给力馆', '41.737142', '123.400899', '39.925294', '116.461240', '1', '116', '2014-11-26 16:43:30', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('159', 'Long1416991421192595', '2014-11-26 16:58:00', '5.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '北京给力馆', '41.737142', '123.400899', '39.925294', '116.461240', '1', '116', '2014-11-26 16:43:41', '2014-11-26 16:44:00', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('160', 'Long1416991572915017', '2014-11-26 17:00:00', '5.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '好苑建国酒店', '41.737048', '123.400945', '39.915770', '116.430095', '1', '116', '2014-11-26 16:46:12', '2014-11-26 16:46:21', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('161', 'Long1416991618086578', '2014-11-27 17:01:00', '1.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '奥体中心', '41.737728', '123.408752', '41.749034', '123.460170', '1', '114', '2014-11-26 16:46:58', '2014-11-26 16:47:30', null, null, null, null, null, null, null, '1', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('162', 'Long1416991707146269', '2014-11-27 17:02:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '奥体中心', '41.737971', '123.408801', '41.749034', '123.460170', '1', '114', '2014-11-26 16:48:27', '2014-11-26 16:48:37', null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('163', 'Long1416992146072635', '2014-11-28 17:10:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '奥体中心', '41.737727', '123.408920', '41.749034', '123.460170', '1', '80', '2014-11-26 16:55:46', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('164', 'Long1416992230153818', '2014-11-27 17:11:00', '1.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '西直门', '41.737922', '123.408787', '39.946436', '116.361442', '1', '80', '2014-11-26 16:57:10', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('165', 'Long1416992334524690', '2014-11-28 17:12:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '奥体中心', '41.737964', '123.408813', '41.749034', '123.460170', '1', '114', '2014-11-26 16:58:54', '2014-11-26 16:59:15', null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('166', 'Long1416994007088553', '2014-11-26 20:41:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连海事学院', '41.737707', '123.408745', '38.877552', '121.538757', '5', '80', '2014-11-26 17:26:47', '2014-11-26 17:27:00', '2014-11-26 17:27:19', '2014-11-26 17:27:27', '2014-11-26 17:27:39', '2014-11-26 17:27:43', '2014-11-26 17:27:53', null, null, '5', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('167', 'Long1417059046355915', '2014-11-28 11:45:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连海洋大学渤海校区', '41.737668', '123.408762', '38.972672', '121.329236', '3', '80', '2014-11-27 11:30:46', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('168', 'Long1417074312978068', '2014-11-27 16:59:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连理工大学', '41.737904', '123.408796', '38.888674', '121.536184', '4', '80', '2014-11-27 15:45:12', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('169', 'Long1417074313011912', '2014-11-27 16:59:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连理工大学', '41.737904', '123.408796', '38.888674', '121.536184', '4', '80', '2014-11-27 15:45:13', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('170', 'Long1417074313065649', '2014-11-27 16:59:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连理工大学', '41.737904', '123.408796', '38.888674', '121.536184', '4', '80', '2014-11-27 15:45:13', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('171', 'Long1417074313112616', '2014-11-27 16:59:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连理工大学', '41.737904', '123.408796', '38.888674', '121.536184', '4', '80', '2014-11-27 15:45:13', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('172', 'Long1417074313122694', '2014-11-27 16:59:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连理工大学', '41.737904', '123.408796', '38.888674', '121.536184', '4', '80', '2014-11-27 15:45:13', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('173', 'Long1417074313159770', '2014-11-27 16:59:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连理工大学', '41.737904', '123.408796', '38.888674', '121.536184', '4', '80', '2014-11-27 15:45:13', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('174', 'Long1417074313208409', '2014-11-27 16:59:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连理工大学', '41.737904', '123.408796', '38.888674', '121.536184', '4', '80', '2014-11-27 15:45:13', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('175', 'Long1417074370095867', '2014-11-27 16:59:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连理工大学', '41.737904', '123.408796', '38.888674', '121.536184', '4', '80', '2014-11-27 15:46:10', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('176', 'Long1417076409387854', '2014-11-27 17:34:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-13号-4门', '辽宁师范大学（黄河路校区）', '41.737637', '123.408962', '38.922564', '121.568817', '1', '80', '2014-11-27 16:20:09', '2014-11-27 16:35:13', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('177', 'Long1417082398423954', '2014-11-27 20:13:00', '100.00', '2301', '', '辽宁省沈阳市东陵区长白街172-2号-6门', '凌源市', '41.737878', '123.408911', '40.981801', '119.271543', '1', '80', '2014-11-27 17:59:58', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('178', 'Long1417083007287332', '2014-11-27 19:24:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '老鳖湾', '41.737750', '123.408867', '38.908099', '121.638632', '3', '80', '2014-11-27 18:10:07', '2014-12-02 15:56:49', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('179', 'Long1417507516346107', '2014-12-03 16:20:00', '10.00', '11', '1510', '北京市朝阳区三环', '廊坊万达广场', '39.962274', '116.465352', '39.527271', '116.713985', '1', '44', '2014-12-02 16:05:16', '2014-12-02 16:05:38', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('180', 'Long1417572756380992', '2014-12-03 11:10:00', '1.00', '2301', '2302', '奥体中心', '大连海事学院', '41.749034', '123.460170', '38.877552', '121.538757', '1', '79', '2014-12-03 10:12:36', '2014-12-03 10:13:11', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('181', 'Long1417597970177126', '2014-12-04 17:12:00', '18.00', '2301', '11', '沈阳火车北站', '北京火车站', '41.823981', '123.443230', '39.909793', '116.433646', '10', '96', '2014-12-03 17:12:50', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('182', 'Long1417598058600716', '2014-12-04 17:28:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连理工大学', '41.737893', '123.408925', '38.888674', '121.536184', '1', '79', '2014-12-03 17:14:18', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('183', 'Long1417599302112561', '2014-12-04 17:34:00', '14.00', '2301', '11', '沈阳火车北站', '北京火车站', '41.823981', '123.443230', '39.909793', '116.433646', '6', '96', '2014-12-03 17:35:02', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('184', 'Long1417599679300884', '2014-12-04 17:40:00', '6.00', '2301', '11', '沈阳火车站东广场-出站口', '北京火车站', '41.800768', '123.402114', '39.909793', '116.433646', '3', '96', '2014-12-03 17:41:19', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('185', 'Long1417601952722624', '2014-12-05 18:33:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连海事学院', '41.737751', '123.408718', '38.877552', '121.538757', '6', '80', '2014-12-03 18:19:12', '2014-12-03 18:19:38', '2014-12-03 18:20:14', '2014-12-03 18:20:18', null, null, null, null, null, '1', '', '4', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('186', 'Long1417605660474861', '2014-12-05 19:35:00', '15.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连外国语大学(旅顺校区)', '41.737961', '123.408888', '38.818528', '121.315428', '1', '80', '2014-12-03 19:21:00', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('187', 'Long1417605670135006', '2014-12-05 19:35:00', '15.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连外国语大学(旅顺校区)', '41.737961', '123.408888', '38.818528', '121.315428', '1', '80', '2014-12-03 19:21:10', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('188', 'Long1417605680155652', '2014-12-05 19:35:00', '15.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连外国语大学(旅顺校区)', '41.737961', '123.408888', '38.818528', '121.315428', '1', '80', '2014-12-03 19:21:20', '2014-12-03 19:22:20', null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('189', 'Long1417607257462413', '2014-12-04 20:02:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '清华大学', '41.737307', '123.400901', '40.002467', '116.328374', '2', '127', '2014-12-03 19:47:37', '2014-12-03 19:47:49', '2014-12-03 19:48:05', '2014-12-03 19:48:07', '2014-12-03 19:48:14', '2014-12-03 19:48:17', '2014-12-03 19:48:27', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('190', 'Long1417607343233710', '2014-12-05 20:03:00', '100.00', '2301', '12', '辽宁省沈阳市东陵区胜利南街', '南开大学', '41.737670', '123.400869', '39.109299', '117.176953', '1', '127', '2014-12-03 19:49:03', '2014-12-03 19:49:12', null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('191', 'Long1417607436963741', '2014-12-06 20:05:00', '100.00', '2301', '4401', '辽宁省沈阳市东陵区胜利南街', '黄鹤楼', '41.737670', '123.400869', '30.550232', '114.309053', '1', '127', '2014-12-03 19:50:36', '2014-12-03 19:50:51', '2014-12-03 19:51:04', '2014-12-03 19:51:05', '2014-12-03 19:51:19', '2014-12-03 19:51:22', '2014-12-03 19:51:31', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('192', 'Long1417662787843003', '2014-12-05 11:27:00', '9991.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连外国语大学(旅顺校区)', '41.737842', '123.408696', '38.818528', '121.315428', '1', '79', '2014-12-04 11:13:07', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('193', 'Long1417670381129638', '2014-12-04 13:33:00', '100.00', '11', '4601', '北京市朝阳区三环', '白云机场铂尔曼大酒店', '39.962252', '116.465347', '23.395153', '113.310199', '1', '94', '2014-12-04 13:19:41', '2014-12-04 13:20:04', '2014-12-04 13:20:45', '2014-12-04 13:20:51', '2014-12-04 13:21:25', '2014-12-04 13:22:43', '2014-12-04 13:29:29', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('194', 'Long1417681433750511', '2014-12-04 16:37:00', '100.00', '11', '1503', '北京市朝阳区三环', '北京奥林匹克公园', '39.962252', '116.465347', '39.994915', '116.396110', '1', '94', '2014-12-04 16:23:53', '2014-12-04 16:27:43', '2014-12-05 13:53:57', '2014-12-12 20:50:14', '2014-12-12 20:50:26', '2014-12-12 20:50:28', null, null, null, '1', '', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('195', 'Long1417682281108216', '2014-12-04 16:52:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区长白街172-2号-6门', '大连理工大学', '41.737890', '123.408987', '38.888674', '121.536184', '4', '79', '2014-12-04 16:38:01', '2014-12-04 16:40:11', '2014-12-10 17:40:57', '2014-12-11 15:25:53', null, null, null, null, '2014-12-11 15:26:11', '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('196', 'Long1417682283218488', '2014-12-04 16:52:00', '95.00', '11', '1503', '北京市朝阳区三环', '北京奥林匹克公园', '39.962252', '116.465347', '39.994915', '116.396110', '1', '94', '2014-12-04 16:38:03', '2014-12-04 16:40:42', '2014-12-05 13:56:56', '2014-12-12 20:49:48', '2014-12-12 20:50:07', '2014-12-12 20:50:10', null, null, null, '1', '', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('197', 'Long1417682598129694', '2014-12-04 16:56:00', '5.00', '11', '1503', '北京市朝阳区三环', '北京奥林匹克公园', '39.962252', '116.465347', '39.994915', '116.396110', '1', '94', '2014-12-04 16:43:18', '2014-12-04 16:43:25', '2014-12-05 13:56:38', '2014-12-09 10:22:22', '2014-12-12 20:54:01', '2014-12-12 20:54:03', null, null, null, '1', '', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('198', 'Long1417758241494494', '2014-12-05 13:58:00', '100.00', '11', '5501', '北京市朝阳区三环', '大理风味小吃', '39.962238', '116.465375', '25.033322', '102.747780', '1', '94', '2014-12-05 13:44:01', '2014-12-13 13:45:02', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('199', 'Long1417758337141130', '2014-12-05 13:59:00', '1.00', '11', '', '北京市朝阳区三环', '洞口县', '39.962238', '116.465375', '27.103196', '110.599739', '1', '94', '2014-12-05 13:45:37', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('200', 'Long1417758406712369', '2014-12-05 14:01:00', '1.00', '11', '1503', '北京市朝阳区三环', '北京奥林匹克公园', '39.962238', '116.465375', '39.994915', '116.396110', '1', '94', '2014-12-05 13:46:46', '2014-12-05 13:46:57', '2014-12-05 13:47:14', '2014-12-05 13:47:26', '2014-12-12 20:49:35', '2014-12-12 20:49:37', null, null, null, '1', '', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('201', 'Long1417758571153866', '2014-12-05 14:04:00', '1.00', '11', '', '北京市朝阳区三环', '昆明机场', '39.962238', '116.465375', '25.107813', '102.943285', '2', '94', '2014-12-05 13:49:31', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('202', 'Long1417758644903153', '2014-12-05 14:05:00', '1.00', '11', '13', '北京市朝阳区三环', '南京路步行街', '39.962238', '116.465375', '31.240645', '121.481669', '1', '94', '2014-12-05 13:50:44', '2014-12-05 13:50:55', '2014-12-05 13:52:45', '2014-12-05 13:52:48', '2014-12-05 13:53:29', '2014-12-05 13:53:33', '2014-12-13 12:23:41', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('203', 'Long1417762530746770', '2015-01-10 15:09:00', '225.00', '11', '1502', '北京市朝阳区g1', '稻地中心医院', '39.876870', '116.575331', '39.543778', '118.201218', '2', '100', '2014-12-05 14:55:30', '2014-12-14 15:45:00', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('204', 'Long1418017475142462', '2014-12-08 13:59:00', '100.00', '11', '13', '北京市朝阳区三环', '香港特别行政区政府驻上海经济贸易办事处', '39.962279', '116.465366', '31.236755', '121.484324', '2', '108', '2014-12-08 13:44:35', '2014-12-14 14:34:23', null, null, null, null, null, null, null, '2', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('205', 'Long1418091335142991', '2014-12-09 11:13:00', '100.00', '2301', '', '沈阳火车北站', '凌源市', '41.823981', '123.443230', '40.981801', '119.271543', '2', '80', '2014-12-09 10:15:35', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('206', 'Long1418181572289065', '2014-12-10 13:34:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区胜利南街', '大连理工大学', '41.736998', '123.400629', '38.888674', '121.536184', '4', '26', '2014-12-10 11:19:32', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('207', 'Long1418192218518309', '2014-12-10 16:31:00', '95.00', '2301', '2302', '辽宁省沈阳市东陵区胜利南街', '大连理工大学', '41.737098', '123.401008', '38.888674', '121.536184', '6', '26', '2014-12-10 14:16:58', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('208', 'Long1418192503746102', '2014-12-10 16:36:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区胜利南街', '大连理工大学', '41.737151', '123.400963', '38.888674', '121.536184', '1', '152', '2014-12-10 14:21:43', '2014-12-10 14:24:51', '2014-12-10 14:25:32', null, null, null, null, null, '2014-12-10 14:31:25', '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('209', 'Long1418200903867134', '2014-12-12 08:40:00', '110.00', '2301', '11', '奥体中心', '东直门', '41.749034', '123.460170', '39.947892', '116.441454', '4', '43', '2014-12-10 16:41:43', '2014-12-13 14:34:26', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('210', 'Long1418204701915915', '2014-12-11 17:40:00', '5.00', '11', '11', '国际港D座', '前门大街', '39.964265', '116.465765', '39.899996', '116.404634', '2', '150', '2014-12-10 17:45:01', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('211', 'Long1418265666054573', '2014-12-11 22:23:00', '1.00', '11', '12', '国际港', '天津南站', '39.964327', '116.464690', '39.062797', '117.067499', '1', '150', '2014-12-11 10:41:06', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('212', 'Long1418268002774178', '2014-12-11 11:33:00', '5.00', '11', '1503', '北京市朝阳区三环', '太阳宫大厦', '39.962262', '116.465428', '39.976796', '116.453664', '1', '94', '2014-12-11 11:20:02', '2014-12-11 11:20:31', '2014-12-11 11:21:12', '2014-12-12 20:48:00', '2014-12-12 20:48:07', '2014-12-12 20:48:09', null, null, null, '1', '', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('213', 'Long1418268348760765', '2014-12-11 11:40:00', '5.00', '11', '2302', '北京市朝阳区三环', '沈阳奥体中心', '39.962196', '116.465473', '41.746795', '123.470082', '1', '94', '2014-12-11 11:25:48', '2014-12-11 11:26:13', '2014-12-12 20:47:40', '2014-12-12 20:47:42', '2014-12-12 20:47:51', '2014-12-12 20:47:54', null, null, null, '1', '', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('214', 'Long1418280135499243', '2014-12-11 16:40:00', '5.00', '11', '12', '三元桥', '天津之眼', '39.966880', '116.463573', '39.160023', '117.193411', '1', '150', '2014-12-11 14:42:15', '2014-12-11 15:01:15', null, null, null, null, null, null, null, '1', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('215', 'Long1418286958515361', '2014-12-11 17:35:00', '4.00', '11', '12', '三元桥', '天津滨海国际机场', '39.966880', '116.463573', '39.135890', '117.365451', '2', '150', '2014-12-11 16:35:58', '2014-12-11 16:37:48', '2014-12-11 16:39:51', '2014-12-11 16:40:02', '2014-12-11 16:41:10', '2014-12-11 16:59:58', '2014-12-11 17:20:35', null, null, '2', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('216', 'Long1418448406080188', '2014-12-13 14:40:00', '100.00', '11', '', '北京市朝阳区霄云里南街', 'YHOTEL-杭州青年会酒店)', '39.964238', '116.465842', '30.254993', '120.174835', '1', '80', '2014-12-13 13:26:46', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('217', 'Long1418448858628200', '2014-12-13 13:45:00', '100.00', '11', '3501', '北京市朝阳区三环', '突击风暴真人CS', '39.962265', '116.465416', '30.281069', '120.095407', '1', '80', '2014-12-13 13:34:18', '2014-12-13 15:36:44', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('218', 'Long1418449401442432', '2014-12-14 00:00:00', '100.00', '11', '3501', '北京市朝阳区三环', 'F2国际赛车馆', '39.962322', '116.465450', '30.330730', '120.123242', '1', '153', '2014-12-13 13:43:21', '2014-12-13 14:14:45', '2014-12-13 14:17:15', '2014-12-13 14:17:34', '2014-12-13 14:18:22', '2014-12-13 14:18:27', '2014-12-13 14:19:32', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('219', 'Long1418451993692518', '2014-12-15 08:08:00', '50.00', '11', '3501', '北京市朝阳区三环', 'Two子气球', '39.962220', '116.465463', '30.302142', '120.147762', '5', '153', '2014-12-13 14:26:33', '2014-12-13 15:44:24', null, null, null, null, null, null, null, '2', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('220', 'Long1418452412587853', '2014-12-13 15:30:00', '9.00', '2301', '2302', '奥体中心', '大连理工大学', '41.749034', '123.460170', '38.888674', '121.536184', '4', '43', '2014-12-13 14:33:32', '2014-12-13 14:34:05', null, null, null, null, null, null, null, '3', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('221', 'Long1418452550113436', '2014-12-13 16:50:00', '25.00', '2301', '2302', '辽宁省沈阳市东陵区胜利南街', '老鳖湾', '41.737038', '123.401026', '38.908099', '121.638632', '1', '26', '2014-12-13 14:35:50', '2014-12-13 14:43:50', '2014-12-13 14:44:36', '2014-12-13 14:44:38', '2014-12-13 15:19:40', '2014-12-13 15:20:01', '2014-12-13 15:20:18', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('222', 'Long1418453315785155', '2014-12-13 18:03:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区胜利南街', '大连银行(星海支行)', '41.737024', '123.401054', '38.864532', '121.535837', '1', '26', '2014-12-13 14:48:35', '2014-12-13 14:48:52', '2014-12-13 14:49:07', '2014-12-13 14:49:11', '2014-12-13 14:51:04', '2014-12-13 14:51:07', '2014-12-13 15:01:40', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('223', 'Long1418455257187417', '2014-12-14 15:35:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区胜利南街', '大连海事学院', '41.736973', '123.400857', '38.877552', '121.538757', '2', '26', '2014-12-13 15:20:57', '2014-12-13 15:27:49', null, null, null, null, null, null, null, '1', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('224', 'Long1418455747911209', '2014-12-14 14:14:00', '100.00', '2301', '2303', '辽宁省沈阳市东陵区胜利南街', '鞍山师范学院', '41.737073', '123.401021', '41.083256', '123.000849', '1', '26', '2014-12-13 15:29:07', '2014-12-13 15:29:26', null, null, null, null, null, null, null, '1', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('225', 'Long1418456033432303', '2014-12-14 15:48:00', '100.00', '2301', '2313', '辽宁省沈阳市东陵区胜利南街', '凌源市', '41.737069', '123.400967', '40.981801', '119.271543', '1', '26', '2014-12-13 15:33:53', '2014-12-13 15:34:06', null, null, null, null, null, null, null, '1', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('226', 'Long1418456526685020', '2014-12-15 15:54:00', '100.00', '2301', '2313', '辽宁省沈阳市东陵区胜利南街', '鄂尔多斯市交警支队车管所', '41.737035', '123.400928', '39.705762', '109.870458', '1', '26', '2014-12-13 15:42:06', '2014-12-13 15:42:26', null, null, null, null, null, null, null, '1', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('227', 'Long1418457291119275', '2014-12-13 15:54:00', '18.00', '2301', '2302', '车友奥迪汽车维修服务有限公司', '大连理工大学', '41.813982', '123.452030', '38.888674', '121.536184', '1', '43', '2014-12-13 15:54:51', '2014-12-13 15:55:08', '2014-12-13 15:55:42', '2014-12-13 15:55:46', null, null, null, null, null, '1', '', '4', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('228', 'Long1418457429211393', '2014-12-13 16:12:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区胜利南街', '大连理工大学', '41.737096', '123.400987', '38.888674', '121.536184', '1', '26', '2014-12-13 15:57:09', '2014-12-13 15:57:18', '2014-12-13 16:00:01', '2014-12-13 16:00:03', '2014-12-13 16:00:18', '2014-12-13 16:01:32', '2014-12-13 16:09:18', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('229', 'Long1418458184845960', '2014-12-13 16:24:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '西直门', '41.737012', '123.400983', '39.946436', '116.361442', '1', '26', '2014-12-13 16:09:44', '2014-12-13 16:10:05', '2014-12-13 16:10:17', null, null, null, null, null, '2014-12-13 16:13:00', '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('230', 'Long1418458536238911', '2014-12-13 16:28:00', '100.00', '2301', '2313', '辽宁省沈阳市东陵区胜利南街', '从众旅社', '41.737000', '123.400924', '41.590967', '120.452243', '1', '26', '2014-12-13 16:15:36', '2014-12-13 16:15:44', '2014-12-13 16:16:04', '2014-12-13 16:16:09', '2014-12-13 16:16:26', '2014-12-13 16:16:40', '2014-12-13 16:17:01', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('231', 'Long1418458688851536', '2014-12-13 16:32:00', '100.00', '2301', '2312', '辽宁省沈阳市东陵区胜利南街', '武侯直销鞋城', '41.737098', '123.400983', '30.604689', '103.989739', '1', '26', '2014-12-13 16:18:08', '2014-12-13 16:18:25', '2014-12-13 16:18:44', '2014-12-13 16:19:04', '2014-12-13 16:20:57', '2014-12-13 16:21:00', '2014-12-13 16:21:04', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('232', 'Long1418458884220820', '2014-12-13 16:36:00', '100.00', '2301', '11', '辽宁省沈阳市东陵区胜利南街', '东直门', '41.737072', '123.400977', '39.947892', '116.441454', '1', '26', '2014-12-13 16:21:24', '2014-12-13 16:21:34', '2014-12-13 16:21:44', '2014-12-13 16:21:45', '2014-12-13 16:22:43', '2014-12-13 16:22:46', '2014-12-13 16:23:23', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('233', 'Long1418460064607847', '2014-12-13 16:28:00', '1.00', '2301', '2313', '奥体中心', '凌源市', '41.749034', '123.460170', '40.981801', '119.271543', '1', '43', '2014-12-13 16:41:04', '2014-12-13 16:41:29', '2014-12-13 17:05:57', '2014-12-13 17:05:58', '2014-12-14 12:34:27', '2014-12-14 12:34:33', null, null, null, '1', '', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('234', 'Long1418536198292906', '2014-12-14 15:04:00', '100.00', '11', '1501', '北京市朝阳区霄云路66号', '北方大厦', '39.963981', '116.465689', '38.095708', '114.527723', '1', '137', '2014-12-14 13:49:58', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('235', 'Long1418537054290982', '2014-12-14 14:30:00', '100.00', '11', '1501', '北京市朝阳区霄云路66号', '安侨商务', '39.962978', '116.465633', '38.057568', '114.529362', '1', '137', '2014-12-14 14:04:14', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('236', 'Long1418537188996920', '2014-12-14 16:05:00', '1.00', '11', '12', '首都医科大学附属北京安贞医院', '天津之眼摩天轮-红桥入口', '39.979346', '116.408707', '39.159813', '117.192941', '3', '150', '2014-12-14 14:06:28', '2014-12-14 14:27:26', '2014-12-14 14:28:03', null, null, null, null, null, '2014-12-14 14:36:39', '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('237', 'Long1418538420682033', '2014-12-15 14:39:00', '100.00', '11', '1501', '北京市朝阳区三环', '安联青年城', '39.962277', '116.465509', '37.993072', '114.457020', '1', '137', '2014-12-14 14:27:00', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('238', 'Long1418538664557984', '2014-12-14 14:45:00', '100.00', '2301', '2302', '辽宁省沈阳市东陵区胜利南街', '大连理工大学', '41.737009', '123.400907', '38.888674', '121.536184', '1', '26', '2014-12-14 14:31:04', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('239', 'Long1418538726456271', '2014-12-14 14:46:00', '5.00', '2301', '2302', '辽宁省沈阳市东陵区胜利南街', '她他乐重庆火锅城', '41.737026', '123.400941', '38.898110', '121.656491', '1', '26', '2014-12-14 14:32:06', '2014-12-14 14:33:28', null, null, null, null, null, null, null, '1', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('240', 'Long1418539096740275', '2014-12-14 19:37:00', '1.00', '11', '12', '首都医科大学附属北京安贞医院', '天津之眼摩天轮-红桥入口', '39.979346', '116.408707', '39.159813', '117.192941', '3', '150', '2014-12-14 14:38:16', '2014-12-14 14:42:23', '2014-12-14 15:31:07', '2014-12-14 15:36:37', '2014-12-14 15:36:52', '2014-12-14 15:36:54', '2014-12-14 15:48:31', null, null, '2', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('241', 'Long1418539524457314', '2014-12-14 14:59:00', '100.00', '11', '1504', '北京市朝阳区霄云路66号', '了能私房菜素姐食店', '39.963086', '116.465698', '22.829608', '113.267541', '1', '161', '2014-12-14 14:45:24', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('242', 'Long1418542002710514', '2014-12-14 15:41:00', '100.00', '11', '1504', '北京市朝阳区三环', '李宁(振兴路店)', '39.962123', '116.465508', '36.817662', '115.182080', '1', '161', '2014-12-14 15:26:42', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('243', 'Long1418542516167070', '2014-12-14 15:50:00', '100.00', '11', '1504', '北京市朝阳区三环', '把荷乡', '39.962199', '116.465490', '23.120663', '106.860392', '1', '124', '2014-12-14 15:35:16', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('244', 'Long1418542594383542', '2014-12-14 15:51:00', '100.00', '11', '1504', '北京市朝阳区三环', '峰峰集团万年矿', '39.962130', '116.465501', '36.594723', '114.135020', '1', '153', '2014-12-14 15:36:34', '2014-12-14 15:36:52', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('245', 'Long1418543382491187', '2014-12-14 16:03:00', '1.00', '11', '12', '北京市朝阳区三环', '滨海新区政府', '39.962155', '116.465483', '39.009822', '117.717130', '1', '151', '2014-12-14 15:49:42', '2014-12-14 15:50:04', '2014-12-14 15:50:52', '2014-12-14 15:50:58', '2014-12-14 15:51:10', '2014-12-14 15:53:32', '2014-12-14 15:55:52', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('246', 'Long1418544420623070', '2014-12-14 19:06:00', '1.00', '11', '1510', '法国大使馆', '香河县钱旺乡大河各庄村民委员会', '39.957946', '116.475152', '39.788416', '117.095027', '3', '150', '2014-12-14 16:07:00', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('247', 'Long1418544481352854', '2014-12-14 17:07:00', '1.00', '11', '1510', '大河庄苑', '健康大药房(永华道)', '39.988372', '116.310376', '39.510252', '116.698779', '2', '150', '2014-12-14 16:08:01', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('248', 'Long1418545058833762', '2014-12-14 16:09:00', '20.00', '2301', '11', '太原街', '西直门', '41.795427', '123.413131', '39.946436', '116.361442', '2', '162', '2014-12-14 16:17:38', '2014-12-15 17:41:30', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('249', 'Long1418545061783450', '2014-12-14 16:09:00', '20.00', '2301', '11', '太原街', '西直门', '41.795427', '123.413131', '39.946436', '116.361442', '2', '162', '2014-12-14 16:17:41', '2014-12-14 16:18:23', '2014-12-14 16:18:36', '2014-12-14 16:18:39', null, null, null, null, null, '1', '', '4', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('250', 'Long1418545278150997', '2014-12-14 18:20:00', '1.00', '11', '12', '度小月 三里屯店', '凤凰城', '39.940391', '116.461567', '39.099661', '117.193032', '3', '150', '2014-12-14 16:21:18', '2014-12-14 16:21:37', null, null, null, null, null, null, null, '1', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('251', 'Long1418545689929739', '2014-12-14 17:27:00', '1.00', '11', '12', '大河庄苑4号楼', '天津海河剧院', '39.987280', '116.310927', '39.104253', '117.185346', '3', '150', '2014-12-14 16:28:09', '2014-12-14 16:28:15', '2014-12-14 16:29:10', null, null, null, null, null, null, '1', '', '3', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('252', 'Long1418616102518769', '2014-12-15 12:00:00', '10.00', '2301', '2302', '奥体中心', '大连理工大学MEM沈阳校区', '41.749034', '123.460170', '41.812180', '123.440589', '1', '164', '2014-12-15 12:01:42', '2014-12-15 12:01:50', null, null, null, null, null, null, null, '1', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('253', 'Long1418623370416759', '2014-12-15 14:02:00', '23.00', '2301', '11', '奥体中心', '西直门', '41.749034', '123.460170', '39.946436', '116.361442', '1', '164', '2014-12-15 14:02:50', '2014-12-15 14:03:06', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('254', 'Long1418627188006483', '2014-12-15 17:00:00', '1.00', '11', '1508', '南银大厦', '你形我塑', '39.962903', '116.463656', '40.982286', '117.947963', '2', '80', '2014-12-15 15:06:28', '2014-12-15 15:07:00', null, null, null, null, null, null, null, '2', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('255', 'Long1418627362596332', '2014-12-15 21:08:00', '30.00', '11', '1508', '元辰鑫大厦', '港湾花园', '39.981295', '116.388154', '40.935609', '117.972071', '2', '80', '2014-12-15 15:09:22', '2014-12-15 15:09:31', '2014-12-15 15:11:03', '2014-12-15 15:11:11', '2014-12-15 15:11:48', '2014-12-15 15:11:49', null, null, null, '1', '', '6', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('256', 'Long1418633750437914', '2014-12-15 16:55:00', '1.00', '11', '1504', '东四条', '南京条形码艺术酒店（新街口侠客店）', '39.886973', '116.388667', '32.045206', '118.796206', '1', '80', '2014-12-15 16:55:50', '2014-12-15 16:55:58', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('257', 'Long1418634263850766', '2014-12-15 17:03:00', '16.00', '11', '1504', '他她DIY甜品小屋', '邯郸东站', '39.905524', '116.498358', '36.626973', '114.564601', '1', '79', '2014-12-15 17:04:23', '2014-12-15 17:04:30', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('258', 'Long1418634649587371', '2014-12-15 17:09:00', '1.00', '11', '3401', '天天家园', '南京条形码艺术酒店（新街口侠客店）', '39.871275', '116.412545', '32.045206', '118.796206', '1', '80', '2014-12-15 17:10:49', '2014-12-15 17:38:27', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('259', 'Long1418644085719348', '2014-12-15 19:47:00', '21.00', '2301', '11', '和平区', '天安门', '41.786474', '123.414332', '39.915168', '116.403875', '2', '125', '2014-12-15 19:48:05', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('260', 'Long1418646265690556', '2014-12-15 20:23:00', '30.00', '2301', '12', '和平区长白街道办事处沙岗子村民委员会', '塘沽外滩公园', '41.735693', '123.411971', '39.021394', '117.671982', '3', '125', '2014-12-15 20:24:25', '2014-12-16 11:55:54', '2014-12-16 15:36:56', '2014-12-16 15:37:03', '2014-12-16 15:37:37', '2014-12-16 15:37:40', '2014-12-16 15:38:12', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('261', 'Long1418648488085057', '2014-12-15 20:56:00', '16.00', '2301', '11', '中街', '东直门', '41.807626', '123.468114', '39.947892', '116.441454', '1', '83', '2014-12-15 21:01:28', '2014-12-15 21:09:53', null, null, null, null, null, null, null, '1', '', '2', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('262', 'Long1418697083365591', '2014-12-16 10:30:00', '65.00', '2301', '2302', '奥体中心', '大连海事学院', '41.749034', '123.460170', '38.877552', '121.538757', '2', '81', '2014-12-16 10:31:23', '2014-12-16 10:31:33', '2014-12-16 10:46:40', '2014-12-16 10:58:53', '2014-12-16 10:59:00', '2014-12-16 11:05:24', '2014-12-16 11:38:58', null, null, '1', '', '7', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('263', 'Long1418702112114523', '2014-12-16 11:54:00', '71.00', '2301', '2302', '太原街', '大连理工大学', '41.795427', '123.413131', '38.888674', '121.536184', '1', '81', '2014-12-16 11:55:12', '2014-12-16 11:55:45', '2014-12-16 11:56:41', null, null, null, null, null, null, '1', '', '3', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('264', 'Long1418712469855453', '2014-12-16 14:47:00', '11.00', '2301', '11', '和平区长白街道办事处沙岗子村民委员会', '大兴区人民医院', '41.735693', '123.411971', '39.737237', '116.340269', '4', '125', '2014-12-16 14:47:49', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('265', 'Long1418714450590931', '2014-12-16 15:13:00', '16.00', '12', '11', '海河外滩公园', '天安门', '39.021379', '117.670957', '39.915168', '116.403875', '3', '125', '2014-12-16 15:20:50', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('266', 'Long1418716121801665', '2014-12-16 16:48:00', '21.00', '12', '2301', '塘沽', '和平区', '39.035228', '117.667631', '41.786474', '123.414332', '3', '125', '2014-12-16 15:48:41', null, null, null, null, null, null, null, null, '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('267', 'Long1418716263362498', '2014-12-16 16:50:00', '16.00', '2301', '12', '和平区', '塘沽外滩公园', '41.786474', '123.414332', '39.021394', '117.671982', '3', '125', '2014-12-16 15:51:03', '2014-12-16 15:53:02', '2014-12-16 15:53:46', '2014-12-16 15:54:21', null, null, null, null, '2014-12-16 15:56:32', '0', '', '8', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('268', 'Long1418716774815664', '2014-12-16 15:58:00', '21.00', '2301', '', '和平区长白街道办事处沙岗子村民委员会', '廊坊市公安局交警支队车管所', '41.735693', '123.411971', '39.579525', '116.757455', '2', '125', '2014-12-16 15:59:34', null, null, null, null, null, null, null, null, '0', '', '1', '1', '0');
INSERT INTO `order_longdistance_details` VALUES ('269', 'Long1418719599334577', '2014-12-16 16:46:00', '16.00', '2301', '1510', '和平区', '永清县', '41.786474', '123.414332', '39.302836', '116.560557', '2', '125', '2014-12-16 16:46:39', '2014-12-16 16:47:11', '2014-12-16 16:58:30', null, null, null, null, null, null, '1', '', '3', '1', '0');

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
