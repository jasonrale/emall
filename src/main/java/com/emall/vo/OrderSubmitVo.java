package com.emall.vo;

import com.emall.entity.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单提交业务对象类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSubmitVo {
    private Goods goods;            //商品

    private String shippingId;      //收货地址id

    private Integer count;          //商品数量
}
