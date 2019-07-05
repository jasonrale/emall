package com.emall.dao;

import com.emall.entity.Shipping;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingMapper {
    int deleteByPrimaryKey(Integer sId);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer sId);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);
}