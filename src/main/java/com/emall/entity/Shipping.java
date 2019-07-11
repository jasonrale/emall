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
    private Long sId;               //收货地址id

    private String sName;          //收货人名称

    private Integer sMobileNumber; //收货人手机号码

    private String sAddress;        //收获详细地址
}