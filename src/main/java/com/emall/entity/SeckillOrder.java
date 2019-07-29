package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 秒杀订单实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class SeckillOrder {
    private String soId;            //秒杀订单id

    private Integer uId;            //秒杀用户id

    private Integer gId;            //秒杀商品id

    private Integer oId;            //订单id
}