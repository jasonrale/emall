package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 秒杀订单实体类。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class SeckillOrder {
    private String seckillOrderId;          //秒杀订单id

    private String orderId;                 //订单id

    private String userId;                  //秒杀订单用户id

    private String seckillGoodsId;          //秒杀商品id
}