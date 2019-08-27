package com.emall.service;

import com.emall.dao.ShippingMapper;
import com.emall.entity.Shipping;
import com.emall.result.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ShippingService {
    @Resource
    ShippingMapper shippingMapper;


    public List<Shipping> queryAll(String userId) {
        return shippingMapper.queryAll(userId);
    }

    public boolean insert(Shipping shipping) {
        return shippingMapper.insert(shipping) != 0;
    }

    public boolean update(Shipping shipping) {
        return shippingMapper.update(shipping) != 0;
    }

    public Shipping selectByShippingId(String shippingId) {
        return shippingMapper.selectByShippingId(shippingId);
    }
}
