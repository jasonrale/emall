package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 商品实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class Goods {
    public static final String PRICE_ASC = "PriceAsc";
    public static final String PRICE_DESC = "PriceDesc";

    private String goodsId;         //商品id

    @NotEmpty(message = "商品名称不能为空")
    private String goodsName;       //商品名称

    @NotEmpty(message = "商品描述不能为空")
    private String goodsDescribe;   //商品描述

    @NotEmpty(message = "商品类别不能为空")
    private String categoryId;      //商品类别id

    @NotEmpty(message = "商品库存不能为空")
    private Integer goodsStock;     //商品库存

    @NotEmpty(message = "商品价格不能为空")
    private BigDecimal goodsPrice;  //商品价格

    @NotEmpty(message = "商品图片不能为空")
    private String goodsImage;      //商品图片地址

    @NotEmpty(message = "商品详情图片不能为空")
    private String goodsDetails;    //商品详情

    @NotEmpty(message = "商品状态不能为空")
    private Integer goodsStatus;    //商品状态--已上架 ：1，已下架 ：0

    //非数据库字段，标识是否为普通商品
    @NotEmpty(message = "商品活动不能为空")
    private Integer goodsActivity;  //商品活动--无 ：0，秒杀 ：1
}