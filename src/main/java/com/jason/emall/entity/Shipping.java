package com.jason.emall.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Repository;

/**
 *   收货信息实体类
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Repository
public class Shipping {
    private Integer sId;            //收货信息id

    private String sName;           //收货人名称

    private Integer sMobileNumber;  //收货人手机号码

    private String sAddress;        //收货地址
}