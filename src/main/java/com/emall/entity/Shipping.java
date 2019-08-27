package com.emall.entity;

import com.emall.annotation.IsMobile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotEmpty;

/**
 *   收货地址实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class Shipping {
    public static final Integer MAXCOUNT = 4;    //最大收获地址数量

    private String shippingId;              //收货信息id

    private String userId;                  //收货地址用户id

    @NotEmpty(message = "收货人名称不能为空")
    private String shippingName;            //收货人名称

    @IsMobile
    private String shippingMobileNumber;   //收货人手机号码

    @NotEmpty(message = "收货地址不能为空")
    private String shippingAddress;         //收货详细地址
}