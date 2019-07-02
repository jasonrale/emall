/*
 Navicat Premium Data Transfer

 Source Server         : Jason
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : emall

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 02/07/2019 18:15:10
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart_item
-- ----------------------------
DROP TABLE IF EXISTS `cart_item`;
CREATE TABLE `cart_item`  (
  `ci_id` int(255) NOT NULL AUTO_INCREMENT COMMENT '???id',
  `u_id` int(255) NOT NULL COMMENT '??id',
  `g_id` int(255) NOT NULL COMMENT '??id',
  `g_count` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '????',
  `g_price` decimal(65, 2) NOT NULL COMMENT '????',
  `g_image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '??????',
  `ci_subtotal` decimal(65, 2) NOT NULL COMMENT '?????',
  PRIMARY KEY (`ci_id`) USING BTREE,
  INDEX `u_id`(`u_id`) USING BTREE COMMENT '?????id??'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '此表为购物车明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `c_id` int(255) NOT NULL AUTO_INCREMENT COMMENT '????id',
  `c_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '??????',
  PRIMARY KEY (`c_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '?????????' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `g_id` int(255) NOT NULL COMMENT '??id',
  `g_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '????',
  `g_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '????',
  `c_id` int(255) NOT NULL COMMENT '????id',
  `g_stock` int(255) NOT NULL COMMENT '????',
  `g_price` decimal(65, 2) NOT NULL COMMENT '????',
  `g_image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '??????',
  `g_details` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '????',
  `g_status` int(255) NOT NULL COMMENT '????',
  PRIMARY KEY (`g_id`) USING BTREE,
  INDEX `g_name`(`g_name`) USING BTREE COMMENT '??????',
  INDEX `c_id`(`c_id`) USING BTREE COMMENT '????id??',
  INDEX `g_status`(`g_status`) USING BTREE COMMENT '??????'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '???????' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `o_id` int(255) NOT NULL AUTO_INCREMENT COMMENT '??id',
  `u_id` int(255) NOT NULL COMMENT '????id',
  `o_payment` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '?????',
  `o_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '????',
  `o_create_time` timestamp(0) NOT NULL COMMENT '??????',
  `o_payment_time` timestamp(0) NOT NULL COMMENT '??????',
  `o_send_time` timestamp(0) NOT NULL COMMENT '????????',
  `o_end_time` timestamp(0) NOT NULL COMMENT '??????',
  PRIMARY KEY (`o_id`) USING BTREE,
  INDEX `u_id`(`u_id`) USING BTREE COMMENT '????id??'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '???????' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `oi_id` int(255) NOT NULL AUTO_INCREMENT COMMENT '????id',
  `o_id` int(255) NOT NULL COMMENT '??id',
  `u_id` int(255) NOT NULL COMMENT '????id',
  `g_id` int(255) NOT NULL COMMENT '????id',
  `g_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '??????',
  `g_image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '????????',
  `g_price` decimal(65, 2) NOT NULL COMMENT '??????',
  `g_count` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '??????',
  `oi_subtotal` decimal(65, 2) NOT NULL COMMENT '??????',
  PRIMARY KEY (`oi_id`) USING BTREE,
  UNIQUE INDEX `o_id`(`o_id`) USING BTREE COMMENT '??id??'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '????????' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `r_id` int(255) NOT NULL COMMENT '角色id',
  `r_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `r_perm` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色权限',
  PRIMARY KEY (`r_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '此表为角色权限表。' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for shipping
-- ----------------------------
DROP TABLE IF EXISTS `shipping`;
CREATE TABLE `shipping`  (
  `s_id` int(255) NOT NULL AUTO_INCREMENT COMMENT '????id',
  `s_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '?????',
  `s_mobile_number` int(255) NULL DEFAULT NULL COMMENT '???????',
  `s_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '????',
  PRIMARY KEY (`s_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '?????????' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `u_id` int(255) NOT NULL AUTO_INCREMENT COMMENT '??id',
  `u_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '????',
  `u_password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '????',
  `u_sex` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '????',
  `u_mobile_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '??????',
  `u_salt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码盐值',
  `r_id` int(255) NOT NULL COMMENT '用户角色id',
  PRIMARY KEY (`u_id`) USING BTREE,
  UNIQUE INDEX `u_name`(`u_name`) USING BTREE COMMENT '??????'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '???????' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
