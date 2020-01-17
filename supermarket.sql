/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : supermarket

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 17/01/2020 16:08:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_commodity
-- ----------------------------
DROP TABLE IF EXISTS `tb_commodity`;
CREATE TABLE `tb_commodity`  (
  `id` int(11) NOT NULL COMMENT '商品编码',
  `name` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名',
  `specification` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规格',
  `units` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位',
  `price` double(10, 2) NULL DEFAULT NULL COMMENT '单价',
  `stock` int(5) NULL DEFAULT NULL COMMENT '库存',
  `is_delete` bigint(1) NULL DEFAULT NULL COMMENT '是否删除，0在售，1下架',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_member
-- ----------------------------
DROP TABLE IF EXISTS `tb_member`;
CREATE TABLE `tb_member`  (
  `id` int(11) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '会员名',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `points` int(11) NULL DEFAULT NULL COMMENT '返点',
  `total` double(10, 2) NULL DEFAULT NULL COMMENT '总价',
  `register_time` datetime(3) NULL DEFAULT NULL COMMENT '注册时间',
  `update_time` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '会员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_member_record
-- ----------------------------
DROP TABLE IF EXISTS `tb_member_record`;
CREATE TABLE `tb_member_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `member_id` int(11) NULL DEFAULT NULL COMMENT '会员Id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '管理员Id',
  `order_number` int(11) NULL DEFAULT NULL COMMENT '订单号',
  `sum` double(10, 2) NULL DEFAULT NULL COMMENT '消费总金额',
  `balance` double(10, 2) NULL DEFAULT NULL COMMENT '余额',
  `received_points` int(11) NULL DEFAULT NULL COMMENT '返积分数',
  `checkout_time` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '结账时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '会员记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_order
-- ----------------------------
DROP TABLE IF EXISTS `tb_order`;
CREATE TABLE `tb_order`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `order_number` int(11) NULL DEFAULT NULL COMMENT '订单号',
  `sum` double(10, 2) NULL DEFAULT NULL COMMENT '总价',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '管理员Id',
  `member_id` int(11) NULL DEFAULT NULL COMMENT '会员Id',
  `checkout_type` int(2) NULL DEFAULT NULL COMMENT '0：未结算；1;已结算；2取消订单',
  `checkout_time` datetime(3) NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '状态变化时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_order_item
-- ----------------------------
DROP TABLE IF EXISTS `tb_order_item`;
CREATE TABLE `tb_order_item`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_number` int(11) NULL DEFAULT NULL COMMENT '订单号',
  `commodity_id` int(11) NULL DEFAULT NULL COMMENT '商品编码',
  `commodity_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `price` double(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `count` int(11) NULL DEFAULT NULL COMMENT '数量',
  `total` double(10, 2) NULL DEFAULT NULL COMMENT '总价',
  `is_checked` int(2) NULL DEFAULT NULL COMMENT '结账状态，0未结账；1已结账',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `commodityID`(`commodity_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 85 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `role` int(2) NOT NULL COMMENT '角色1：管理员；2收银员，3库管',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
