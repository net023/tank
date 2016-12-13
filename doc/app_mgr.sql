/*
Navicat MySQL Data Transfer

Source Server         : loc_mysql
Source Server Version : 50614
Source Host           : localhost:3306
Source Database       : common_db

Target Server Type    : MYSQL
Target Server Version : 50614
File Encoding         : 65001

Date: 2016-12-13 08:23:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for app_mgr
-- ----------------------------
DROP TABLE IF EXISTS `app_mgr`;
CREATE TABLE `app_mgr` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version` varchar(20) DEFAULT NULL,
  `update_info` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1',
  `apk_url` varchar(150) DEFAULT NULL,
  `ipa_url` varchar(150) DEFAULT NULL,
  `order` smallint(3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for company_profile
-- ----------------------------
DROP TABLE IF EXISTS `company_profile`;
CREATE TABLE `company_profile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `info` varchar(2000) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `telephone` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for custom_service
-- ----------------------------
DROP TABLE IF EXISTS `custom_service`;
CREATE TABLE `custom_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `telephone` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for irradiate
-- ----------------------------
DROP TABLE IF EXISTS `irradiate`;
CREATE TABLE `irradiate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `province` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `latitude` double(5,2) DEFAULT NULL,
  `mj` double(5,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pdf
-- ----------------------------
DROP TABLE IF EXISTS `pdf`;
CREATE TABLE `pdf` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pdf_name` varchar(50) DEFAULT NULL,
  `pdf_res` longblob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(100) DEFAULT NULL COMMENT 'key',
  `field` varchar(100) DEFAULT NULL COMMENT '字段',
  `val` varchar(100) DEFAULT NULL COMMENT '值',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `sort` int(11) DEFAULT NULL COMMENT '排序号',
  `state` int(11) DEFAULT NULL COMMENT '状态（0，停用，1，正常）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据字典表';

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userID` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `loginIP` varchar(50) NOT NULL,
  `loginDate` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1160 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for sys_menus
-- ----------------------------
DROP TABLE IF EXISTS `sys_menus`;
CREATE TABLE `sys_menus` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单编号',
  `menuName` varchar(30) NOT NULL COMMENT '菜单名称',
  `remark` varchar(30) DEFAULT NULL COMMENT '备注',
  `isParent` tinyint(1) NOT NULL COMMENT '是否为父节点',
  `parentID` int(11) NOT NULL COMMENT '父节点编号',
  `url` varchar(200) DEFAULT NULL COMMENT 'URL',
  `status` int(11) NOT NULL COMMENT '状态',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_posts
-- ----------------------------
DROP TABLE IF EXISTS `sys_posts`;
CREATE TABLE `sys_posts` (
  `id` int(11) NOT NULL COMMENT '权限编号',
  `postName` varchar(30) NOT NULL COMMENT '权限名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleID` int(11) NOT NULL,
  `menuID` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sys_role_menu_ibfk_2` (`menuID`)
) ENGINE=InnoDB AUTO_INCREMENT=190 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_roles
-- ----------------------------
DROP TABLE IF EXISTS `sys_roles`;
CREATE TABLE `sys_roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色编号',
  `roleName` varchar(50) NOT NULL COMMENT '角色名称',
  `parentRole` int(11) NOT NULL COMMENT '上级角色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `nick` varchar(30) DEFAULT NULL COMMENT '昵称',
  `username` varchar(30) NOT NULL COMMENT '用户名',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT 'Email',
  `qq` varchar(20) DEFAULT NULL COMMENT 'QQ',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态 0-停用 1-正常',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `createDate` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userID` int(11) NOT NULL,
  `postID` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sys_user_post_ibfk_2` (`postID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userID` int(11) NOT NULL,
  `roleID` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sys_user_role_ibfk_2` (`roleID`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for wxuser
-- ----------------------------
DROP TABLE IF EXISTS `wxuser`;
CREATE TABLE `wxuser` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `openid` varchar(50) DEFAULT NULL COMMENT '微信openid',
  `reg_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` smallint(1) DEFAULT '0' COMMENT '状态 0已启用 1已禁用',
  `name` varchar(50) DEFAULT NULL COMMENT '微信昵称',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `login_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
