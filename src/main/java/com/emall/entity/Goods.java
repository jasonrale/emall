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
    private Long gId;           //商品id

    private String gName;      //商品名称

    private String gDescribe;    //商品描述

    private Long cId;           //商品类别id

    private Integer gStock;     //商品库存

    private BigDecimal gPrice;   //商品价格

    private String gImage;       //商品图片地址

    private String gDetails;      //商品详情

    private Integer gStatus;     //商品状态--已上架 ：1，已下架 ：0
}