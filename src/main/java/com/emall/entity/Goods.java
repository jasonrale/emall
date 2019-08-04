package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 *   商品实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class Goods {
    private String goodsId;         //商品id

    private String goodsName;       //商品名称

    private String goodsDescribe;   //商品描述

    private String categoryId;       //商品类别id

    private Integer goodsStock;     //商品库存

    private BigDecimal goodsPrice;  //商品价格

    private String goodsImage;      //商品图片地址

    private String goodsDetails;    //商品详情

    private Integer goodsStatus;    //商品状态--已上架 ：1，已下架 ：0
}