package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 秒杀商品实体类。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class SeckillGoods {
    public static final Integer OFFSHELF = 0;       //未上架
    public static final Integer PREPARING = 1;      //准备中
    public static final Integer ONGOING = 2;        //进行中
    public static final Integer COMPLETE = 3;       //已结束

    private String seckillGoodsId;          //秒杀商品id

    private String seckillGoodsName;        //秒杀商品名称

    private String categoryId;              //秒杀商品类别

    private String seckillGoodsDescribe;    //秒杀商品描述

    private Integer seckillGoodsStock;      //秒杀商品库存

    private BigDecimal seckillGoodsPrice;   //秒杀商品价格

    private String seckillGoodsImage;       //秒杀商品图片

    private String seckillGoodsDetails;     //秒杀商品详情

    private Date seckillGoodsStartTime;     //秒杀开始时间

    private Date seckillGoodsEndTime;       //秒杀结束时间

    private Integer seckillGoodsStatus;      //秒杀商品状态
}