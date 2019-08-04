package com.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 *   收货地址实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class Shipping {
    private String shippingId;              //收货信息id

    private String userId;                  //收货地址用户id

    private String shippingName;            //收货人名称

    private Integer shippingMobileNumber;   //收货人手机号码

    private String shippingAddress;         //收货详细地址
}