/*
 Navicat Premium Data Transfer

 Source Server         : Link
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : emall

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 04/07/2019 23:33:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`
(
    `ct_id`       int(255)                                                NOT NULL AUTO_INCREMENT COMMENT '购物车id',
    `u_id`        int(255)                                                NOT NULL COMMENT '用户id',
    `g_id`        int(255)                                                NOT NULL COMMENT '商品id',
    `g_count`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品数量',
    `g_price`     decimal(65, 2)                                          NOT NULL COMMENT '商品价格',
    `g_image`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品图片地址',
    `ct_subtotal` decimal(65, 2)                                          NOT NULL COMMENT '购物车小计',
    PRIMARY KEY (`ct_id`) USING BTREE,
    INDEX `u_id` (`u_id`) USING BTREE COMMENT '购物车用户id索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '此表为购物车明细表。'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`
(
    `c_id`   int(255)                                                NOT NULL AUTO_INCREMENT COMMENT '商品类别id',
    `c_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品类别名称',
    PRIMARY KEY (`c_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '此表为商品类别表。'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
                          `g_id`       int(255)                                                NOT NULL COMMENT '商品id',
                          `g_name`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称',
                          `g_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品描述',
                          `c_id`       int(255)                                                NOT NULL COMMENT '商品类别id',
                          `g_stock`    int(255)                                                NOT NULL COMMENT '商品库存',
                          `g_price`    decimal(65, 2)                                          NOT NULL COMMENT '商品价格',
                          `g_image`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品图片地址',
                          `g_details`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品详情',
                          `g_status`   int(255)                                                NOT NULL COMMENT '商品状态',
                          PRIMARY KEY (`g_id`) USING BTREE,
                          INDEX `g_name` (`g_name`) USING BTREE COMMENT '商品名称索引',
                          INDEX `c_id` (`c_id`) USING BTREE COMMENT '商品类别id索引',
                          INDEX `g_status` (`g_status`) USING BTREE COMMENT '商品状态索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '此表为商品表。'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
                          `o_id`           int(255)                                                NOT NULL AUTO_INCREMENT COMMENT '订单id',
                          `u_id`           int(255)                                                NOT NULL COMMENT '订单用户id',
                          `o_payment`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单总付款',
                          `o_status`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单状态',
                          `o_create_time`  timestamp(0)                                            NOT NULL COMMENT '订单创建时间',
                          `o_payment_time` timestamp(0)                                            NOT NULL COMMENT '订单支付时间',
                          `o_send_time`    timestamp(0)                                            NOT NULL COMMENT '订单商品发货时间',
                          `o_end_time`     timestamp(0)                                            NOT NULL COMMENT '订单完成时间',
                          PRIMARY KEY (`o_id`) USING BTREE,
                          INDEX `u_id` (`u_id`) USING BTREE COMMENT '订单用户id索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '此表为订单表。'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for orderitem
-- ----------------------------
DROP TABLE IF EXISTS `orderitem`;
CREATE TABLE `orderitem`
(
    `ot_id`       int(255)                                                NOT NULL AUTO_INCREMENT COMMENT '订单明细id',
    `o_id`        int(255)                                                NOT NULL COMMENT '订单id',
    `u_id`        int(255)                                                NOT NULL COMMENT '订单用户id',
    `g_id`        int(255)                                                NOT NULL COMMENT '订单商品id',
    `g_name`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单商品名称',
    `g_image`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单商品图片地址',
    `g_price`     decimal(65, 2)                                          NOT NULL COMMENT '订单商品价格',
    `g_count`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单商品数量',
    `ot_subtotal` decimal(65, 2)                                          NOT NULL COMMENT '订单明细小计',
    PRIMARY KEY (`ot_id`) USING BTREE,
    UNIQUE INDEX `o_id` (`o_id`) USING BTREE COMMENT '订单id索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '此表为订单明细表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
                         `r_id`   int(255)                                                NOT NULL AUTO_INCREMENT COMMENT '角色id',
                         `r_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
                         `r_perm` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色权限',
                         PRIMARY KEY (`r_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '此表为角色权限表。' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for shipping
-- ----------------------------
DROP TABLE IF EXISTS `shipping`;
CREATE TABLE `shipping`
(
    `s_id`            int(255)                                                NOT NULL AUTO_INCREMENT COMMENT '收货信息id',
    `s_name`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人名称',
    `s_mobile_number` int(255)                                                NULL DEFAULT NULL COMMENT '收货人手机号码',
    `s_address`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收获地址',
    PRIMARY KEY (`s_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '此表为收货信息表。'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
                         `u_id`            int(255)                                                NOT NULL AUTO_INCREMENT COMMENT '用户id',
                         `u_name`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名称',
                         `u_password`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码',
                         `u_sex`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户性别',
                         `u_mobile_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户手机号码',
                         `u_role`          int(255)                                                NOT NULL COMMENT '用户角色',
                         `u_salt`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码盐值',
                         `r_id`            int(255)                                                NOT NULL COMMENT '用户角色id',
                         PRIMARY KEY (`u_id`) USING BTREE,
                         UNIQUE INDEX `u_name` (`u_name`) USING BTREE COMMENT '用户名称索引',
                         INDEX `r_id` (`r_id`) USING BTREE COMMENT '用户角色id索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '此表为用户表。'
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
