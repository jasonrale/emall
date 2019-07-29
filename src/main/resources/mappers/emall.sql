/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.153.130
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : 192.168.153.130:3306
 Source Schema         : emall

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 30/07/2019 02:41:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart_item
-- ----------------------------
DROP TABLE IF EXISTS `cart_item`;
CREATE TABLE `cart_item`  (
  `ci_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '购物车明细id',
  `u_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '购物车用户id',
  `g_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '购物车商品id',
  `g_count` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '购物车商品数量',
  `g_price` decimal(65, 2) NOT NULL COMMENT '购物车商品价格',
  `g_image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '购物车商品图片地址',
  `ci_subtotal` decimal(65, 2) NOT NULL COMMENT '购物车明细小计',
  PRIMARY KEY (`ci_id`) USING BTREE,
  INDEX `u_id`(`u_id`) USING BTREE COMMENT '购物车用户id索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '此表为购物车明细表。' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `c_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品类别id',
  `c_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品类别名称',
  PRIMARY KEY (`c_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '此表为商品类别表。' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `g_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品id',
  `g_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称',
  `g_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品描述',
  `c_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品类别id',
  `g_stock` int(255) NOT NULL COMMENT '商品库存',
  `g_price` decimal(65, 2) NOT NULL COMMENT '商品价格',
  `g_image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品图片地址',
  `g_details` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品详情',
  `g_status` int(255) NOT NULL COMMENT '商品状态',
  PRIMARY KEY (`g_id`) USING BTREE,
  INDEX `g_name`(`g_name`) USING BTREE COMMENT '商品名称索引',
  INDEX `c_id`(`c_id`) USING BTREE COMMENT '商品类别id索引',
  INDEX `g_status`(`g_status`) USING BTREE COMMENT '商品状态索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '此表为商品表。' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `o_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单id',
  `u_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单用户id',
  `o_payment` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单总付款',
  `o_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单状态',
  `o_create_time` timestamp(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '订单创建时间',
  `o_payment_time` timestamp(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '订单支付时间',
  `o_send_time` timestamp(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '订单商品发货时间',
  `o_end_time` timestamp(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '订单完成时间',
  PRIMARY KEY (`o_id`) USING BTREE,
  INDEX `u_id`(`u_id`) USING BTREE COMMENT '订单用户id索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '此表为订单表。' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `oi_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单明细id',
  `o_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单id',
  `u_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单用户id',
  `g_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单商品id',
  `g_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单商品名称',
  `g_image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单商品图片地址',
  `g_price` decimal(65, 2) NOT NULL COMMENT '订单商品价格',
  `g_count` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单商品数量',
  `oi_subtotal` decimal(65, 2) NOT NULL COMMENT '订单明细小计',
  PRIMARY KEY (`oi_id`) USING BTREE,
  UNIQUE INDEX `o_id`(`o_id`) USING BTREE COMMENT '订单id索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '此表为订单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for seckill_goods
-- ----------------------------
DROP TABLE IF EXISTS `seckill_goods`;
CREATE TABLE `seckill_goods`  (
  `sg_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '秒杀商品id',
  `g_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品id',
  `sg_price` decimal(65, 2) NULL DEFAULT NULL COMMENT '秒杀商品价格',
  `sg_stock` int(255) NULL DEFAULT NULL COMMENT '秒杀商品库存',
  `sg_start_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '秒杀开始时间',
  `sg_end_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '秒杀结束时间',
  PRIMARY KEY (`sg_id`) USING BTREE,
  UNIQUE INDEX `g_id`(`g_id`) USING BTREE COMMENT '商品id索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '此表为秒杀商品表。' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for seckill_order
-- ----------------------------
DROP TABLE IF EXISTS `seckill_order`;
CREATE TABLE `seckill_order`  (
  `so_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '秒杀订单id',
  `u_id` int(11) NULL DEFAULT NULL COMMENT '秒杀用户id',
  `g_id` int(11) NULL DEFAULT NULL COMMENT '秒杀商品id',
  `o_id` int(11) NULL DEFAULT NULL COMMENT '订单id',
  PRIMARY KEY (`so_id`) USING BTREE,
  UNIQUE INDEX `o_id`(`o_id`) USING BTREE COMMENT '订单id索引',
  INDEX `u_id`(`u_id`) USING BTREE COMMENT '用户id索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '此表为秒杀订单表。' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for shipping
-- ----------------------------
DROP TABLE IF EXISTS `shipping`;
CREATE TABLE `shipping`  (
  `s_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货信息id',
  `s_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人名称',
  `s_mobile_number` int(255) NOT NULL COMMENT '收货人手机号码',
  `s_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收获详细地址',
  PRIMARY KEY (`s_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '此表为收货地址表。' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `u_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `u_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名称',
  `u_password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码',
  `u_sex` int(255) NOT NULL COMMENT '用户性别',
  `u_mobile_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户手机号码',
  `u_role` int(255) NOT NULL COMMENT '用户角色',
  `u_salt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码盐值',
  PRIMARY KEY (`u_id`) USING BTREE,
  UNIQUE INDEX `u_name`(`u_name`) USING BTREE COMMENT '用户名称索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '此表为用户表。' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('605196406440853504', 'jason', 'f2d048a64a44c49324ee88c7df1d85ea687b5d711981aab8866918af68950c23', 1, '13096383242', 0, '91c4f002482e96ce7b178ea6703a04e0');
INSERT INTO `user` VALUES ('605210786658254848', 'martin', '1e62c6ac6d44bf9ac0d4c236253d0ace274d0219a90d4594552e557938e85e91', 1, '13096383242', 0, 'aea6ecc9c5f7f88e92175cfb18e49379');
INSERT INTO `user` VALUES ('605211133124542464', 'avicii', '6ea7067d466ee427dce601b7c2ec9b7339fca633dd693e62966bcf33b57fb14d', 1, '18584839287', 0, '532a33db8091d760436163ba208d7c45');
INSERT INTO `user` VALUES ('605211886136328192', 'lily', '53fb5de5de68a8641099ca76ea107d0d5ad54c43cb1a5db6bdb3ec9207aaabd0', 1, '13096383242', 0, '40030bb1c809b9667bad0fb5eb73c43e');
INSERT INTO `user` VALUES ('605212038012076032', 'akali', 'ef648b0e933ba6fca806e0f5423580a732690ac6cde7c4d69866fef3926f1b44', 1, '13096383242', 0, 'e92d5251962ee1d14b588468f67c6146');
INSERT INTO `user` VALUES ('605212753275125760', 'bibi', '28e45ab3c1aad6450b201c6902ddef92ddad23766c8ede0dc11db7e1fb4ca16b', 1, '13096383242', 0, '0b9270e3a900f1cd305ea218765fe204');
INSERT INTO `user` VALUES ('605214000254287872', 'kiki', 'a0638b63484ee975c336ac9004d2f3ad0ddb16162acbcc162e4292975fc2c242', 1, '13096383242', 0, 'fae93d1cb1fcc8f22ac2300a5b5fe138');
INSERT INTO `user` VALUES ('605215057864818688', 'miko', 'ef708a3e7461f4ad9388e0fae4dc7c077adc1108d3d9fc70534bfcceee9c6b0a', 1, '18584839287', 0, 'ae85f3330a9eebd2bd16be46f2dac7ba');

SET FOREIGN_KEY_CHECKS = 1;
