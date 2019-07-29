package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 秒杀商品实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class SeckillGoods {
    private String sgId;            //秒杀商品id

    private String gId;             //商品id

    private BigDecimal sgPrice;     //秒杀商品价格

    private Integer sgStock;        //秒杀商品库存

    private Date sgStartTime;       //秒杀开始时间

    private Date sgEndTime;         //秒杀结束时间
}