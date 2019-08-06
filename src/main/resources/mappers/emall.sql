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

 Date: 06/08/2019 23:05:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart_item
-- ----------------------------
DROP TABLE IF EXISTS `cart_item`;
CREATE TABLE `cart_item`
(
    `cart_item_id`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '购物车明细id',
    `user_id`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '购物车用户id',
    `goods_id`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '购物车商品id',
    `goods_count`        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '购物车商品数量',
    `goods_price`        decimal(65, 2)                                          NOT NULL COMMENT '购物车商品价格',
    `goods_image`        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '购物车商品图片地址',
    `cart_item_subtotal` decimal(65, 2)                                          NOT NULL COMMENT '购物车明细小计',
    PRIMARY KEY (`cart_item_id`) USING BTREE,
    INDEX `user_id` (`user_id`) USING BTREE COMMENT '购物车用户id索引'
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
    `category_id`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品类别id',
    `category_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品类别名称',
    PRIMARY KEY (`category_id`) USING BTREE,
    UNIQUE INDEX `category_name` (`category_name`) USING BTREE COMMENT '商品类别名称唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '此表为商品类别表。'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category`
VALUES ('607702836326694912', 'e');
INSERT INTO `category`
VALUES ('607702920330215424', 'i');
INSERT INTO `category`
VALUES ('607702802730319872', 'q');
INSERT INTO `category`
VALUES ('607702849698136064', 'r');
INSERT INTO `category`
VALUES ('607702862025195520', 't');
INSERT INTO `category`
VALUES ('607702899274809344', 'u');
INSERT INTO `category`
VALUES ('607702817339080704', 'w');
INSERT INTO `category`
VALUES ('607294872130420736', '家电');
INSERT INTO `category`
VALUES ('607610800135208960', '电脑');
INSERT INTO `category`
VALUES ('607610833572200448', '衣服');
INSERT INTO `category`
VALUES ('607670215462879232', '裤子');
INSERT INTO `category`
VALUES ('607622331283537920', '鞋子');

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`
(
    `goods_id`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品id',
    `goods_name`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称',
    `goods_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品描述',
    `category_id`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品类别id',
    `goods_price`    decimal(65, 2)                                          NOT NULL COMMENT '商品价格',
    `goods_stock`    int(255)                                                NOT NULL COMMENT '商品库存',
    `goods_image`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品图片地址',
    `goods_details`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品详情',
    `goods_status`   int(255)                                                NOT NULL COMMENT '商品状态',
    PRIMARY KEY (`goods_id`) USING BTREE,
    INDEX `goods_name` (`goods_name`) USING BTREE COMMENT '商品名称索引',
    INDEX `goods_status` (`goods_status`) USING BTREE COMMENT '商品状态索引',
    INDEX `category_id` (`category_id`) USING BTREE COMMENT '商品类别id索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '此表为商品表。'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`
(
    `order_id`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单id',
    `user_id`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单用户id',
    `order_payment`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单总付款',
    `order_status`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单状态',
    `order_create_time`  timestamp(0)                                            NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '订单创建时间',
    `order_payment_time` timestamp(0)                                            NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '订单支付时间',
    `order_send_time`    timestamp(0)                                            NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '订单商品发货时间',
    `order_end_time`     timestamp(0)                                            NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '订单完成时间',
    PRIMARY KEY (`order_id`) USING BTREE,
    INDEX `user_id` (`user_id`) USING BTREE COMMENT '订单用户id索引',
    INDEX `order_status` (`order_status`) USING BTREE COMMENT '订单状态索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '此表为订单表。'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`
(
    `order_item_id`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单明细id',
    `order_id`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单id',
    `goods_id`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单商品id',
    `goods_name`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单商品名称',
    `goods_image`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单商品图片地址',
    `goods_price`         decimal(65, 2)                                          NOT NULL COMMENT '订单商品价格',
    `goods_count`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单商品数量',
    `order_item_subtotal` decimal(65, 2)                                          NOT NULL COMMENT '订单明细小计',
    PRIMARY KEY (`order_item_id`) USING BTREE,
    UNIQUE INDEX `order_id` (`order_id`) USING BTREE COMMENT '订单id唯一索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '此表为订单明细表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for seckill_goods
-- ----------------------------
DROP TABLE IF EXISTS `seckill_goods`;
CREATE TABLE `seckill_goods`
(
    `seckill_goods_id`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '秒杀商品id',
    `seckill_goods_name`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '秒杀商品名称',
    `seckill_goods_describe`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '秒杀商品描述',
    `seckill_goods_stock`      int(255)                                                NOT NULL COMMENT '秒杀商品库存',
    `seckill_goods_price`      decimal(65, 2)                                          NOT NULL COMMENT '秒杀商品价格',
    `seckill_goods_image`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '秒杀商品图片',
    `seckill_goods_details`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '秒杀商品详情',
    `seckill_goods_start_time` timestamp(0)                                            NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '秒杀开始时间',
    `seckill_goods_end_time`   timestamp(0)                                            NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '秒杀结束时间',
    `seckill_goods_status`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '秒杀商品状态',
    PRIMARY KEY (`seckill_goods_id`) USING BTREE,
    INDEX `seckill_goods_status` (`seckill_goods_status`) USING BTREE COMMENT '秒杀商品状态索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '此表为秒杀商品表。'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for seckill_order
-- ----------------------------
DROP TABLE IF EXISTS `seckill_order`;
CREATE TABLE `seckill_order`
(
    `seckill_order_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '秒杀订单id',
    `order_id`         varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '订单id',
    `user_id`          varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '秒杀订单用户id',
    `seckill_goods_id` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '秒杀商品id',
    PRIMARY KEY (`seckill_order_id`) USING BTREE,
    UNIQUE INDEX `order_id` (`order_id`) USING BTREE COMMENT '订单id索引',
    INDEX `user_id` (`user_id`) USING BTREE COMMENT '秒杀用户id索引',
    INDEX `seckill_goods_id` (`seckill_goods_id`) USING BTREE COMMENT '秒杀商品id索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '此表为秒杀订单表。'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for shipping
-- ----------------------------
DROP TABLE IF EXISTS `shipping`;
CREATE TABLE `shipping`
(
    `shipping_id`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货信息id',
    `user_id`                varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货地址用户id',
    `shipping_name`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人名称',
    `shipping_mobile_number` int(255)                                                NOT NULL COMMENT '收货人手机号码',
    `shipping_address`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货详细地址',
    PRIMARY KEY (`shipping_id`) USING BTREE,
    INDEX `user_id` (`user_id`) USING BTREE COMMENT '收货地址用户id索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '此表为收货地址表。'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `user_id`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
    `user_name`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名称',
    `user_password`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码',
    `user_sex`           int(255)                                                NOT NULL COMMENT '用户性别',
    `user_mobile_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户手机号码',
    `user_role`          int(255)                                                NOT NULL COMMENT '用户角色',
    `user_salt`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码盐值',
    PRIMARY KEY (`user_id`) USING BTREE,
    UNIQUE INDEX `user_name` (`user_name`) USING BTREE COMMENT '用户名称唯一索引',
    INDEX `user_role` (`user_role`) USING BTREE COMMENT '用户角色索引'
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '此表为用户表。'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user`
VALUES ('605196406440853504', 'jason', 'f2d048a64a44c49324ee88c7df1d85ea687b5d711981aab8866918af68950c23', 1,
        '13096383242', 0, '91c4f002482e96ce7b178ea6703a04e0');
INSERT INTO `user`
VALUES ('605210786658254848', 'martin', '1e62c6ac6d44bf9ac0d4c236253d0ace274d0219a90d4594552e557938e85e91', 1,
        '13096383242', 0, 'aea6ecc9c5f7f88e92175cfb18e49379');
INSERT INTO `user`
VALUES ('605211133124542464', 'avicii', '6ea7067d466ee427dce601b7c2ec9b7339fca633dd693e62966bcf33b57fb14d', 1,
        '18584839287', 0, '532a33db8091d760436163ba208d7c45');
INSERT INTO `user`
VALUES ('605211886136328192', 'lily', '53fb5de5de68a8641099ca76ea107d0d5ad54c43cb1a5db6bdb3ec9207aaabd0', 1,
        '13096383242', 0, '40030bb1c809b9667bad0fb5eb73c43e');
INSERT INTO `user`
VALUES ('605212038012076032', 'akali', 'ef648b0e933ba6fca806e0f5423580a732690ac6cde7c4d69866fef3926f1b44', 1,
        '13096383242', 0, 'e92d5251962ee1d14b588468f67c6146');
INSERT INTO `user`
VALUES ('605212753275125760', 'bibi', '28e45ab3c1aad6450b201c6902ddef92ddad23766c8ede0dc11db7e1fb4ca16b', 1,
        '13096383242', 0, '0b9270e3a900f1cd305ea218765fe204');
INSERT INTO `user`
VALUES ('605214000254287872', 'kiki', 'a0638b63484ee975c336ac9004d2f3ad0ddb16162acbcc162e4292975fc2c242', 1,
        '13096383242', 0, 'fae93d1cb1fcc8f22ac2300a5b5fe138');
INSERT INTO `user`
VALUES ('605215057864818688', 'miko', 'ef708a3e7461f4ad9388e0fae4dc7c077adc1108d3d9fc70534bfcceee9c6b0a', 1,
        '18584839287', 0, 'ae85f3330a9eebd2bd16be46f2dac7ba');
INSERT INTO `user`
VALUES ('605902437106057216', 'admin', '38cdaa5ea08fe82297175cef94a82765c05cdc9cd3f8508c74a255641eadab6b', 1,
        '18584839287', 1, 'fa9526430cfb1ed2f2b7822c99658150');
INSERT INTO `user`
VALUES ('607288353687076864', 'siliy', '8343053de3a74b80ab648ca35b3b9f680d5d393700cdb0badfac93998e498876', 1,
        '13096383242', 0, 'b9015affe33e98b48e7cc2f0a9783ca2');
INSERT INTO `user`
VALUES ('607288471131783168', 'marry', 'bdfd935ccbce53e73d04507634b1002f2162aa9294ea5e817abbc16fb6480a9e', 1,
        '18584839287', 0, '1fdbca7f9396cb8882ecd187ddd6f376');
INSERT INTO `user`
VALUES ('607289081470124032', 'easy', '4aa074e53a0ade599cee79e08adf0be226af443d129ca75322824c8410adf7e5', 1,
        '13096383242', 0, 'b4013aa9a32927c9962c21cf74d27ebc');
INSERT INTO `user`
VALUES ('607290526634344448', 'open', 'a0e622bc1383b70a673a3f0c21e7928fce47d651d124b64730a39d85e5388367', 1,
        '13096383242', 0, '273857ac9c1fdcc9494ac1f51a0dab1b');

SET FOREIGN_KEY_CHECKS = 1;
