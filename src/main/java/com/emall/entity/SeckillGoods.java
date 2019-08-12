package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

/**
 *   秒杀商品实体类。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class SeckillGoods {
    public static final String OFFSHELF = "已下架";
    public static final String PREPARING = "准备中";
    public static final String ONGOING = "进行中";
    public static final String COMPLETE = "已结束";

    private String seckillGoodsId;          //秒杀商品id

    private String goodsId;                 //商品id

    private String seckillGoodsName;        //秒杀商品名称

    private String seckillGoodsDescribe;    //秒杀商品描述

    private Integer seckillGoodsStock;      //秒杀商品库存

    private BigDecimal seckillGoodsPrice;   //秒杀商品价格

    private String seckillGoodsImage;       //秒杀商品图片

    private String seckillGoodsDetails;     //秒杀商品详情

    private Date seckillGoodsStartTime;     //秒杀开始时间

    private Date seckillGoodsEndTime;       //秒杀结束时间

    private String seckillGoodsStatus;      //秒杀商品状态
}