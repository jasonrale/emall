package com.emall.service;

import com.emall.dao.ShippingMapper;
import com.emall.entity.Shipping;
import com.emall.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ShippingService {
    @Resource
    ShippingMapper shippingMapper;

    /**
     * 根据用户id查询所有收货地址
     *
     * @param userId
     * @return
     */
    public List<Shipping> queryAll(String userId) {
        return shippingMapper.queryAll(userId);
    }

    /**
     * 新建收货地址
     * @param shipping
     * @return
     */
    public boolean insert(Shipping shipping) {
        return shippingMapper.insert(shipping) != 0;
    }

    /**
     * 修改收货地址信息
     * @param shipping
     * @return
     */
    public boolean update(Shipping shipping) {
        return shippingMapper.update(shipping) != 0;
    }

    /**
     * 根据收货地址id查询收货地址信息
     * @param shippingId
     * @return
     */
    public Shipping selectByShippingId(String shippingId) {
        return shippingMapper.selectByShippingId(shippingId);
    }

    /**
     * 根据用户id查询收货地址数量
     *
     * @param userId
     * @return
     */
    public int count(String userId) {
        return shippingMapper.count(userId);
    }

    /**
     * 删除收货地址信息
     *
     * @param shippingId
     * @return
     */
    public boolean delete(String shippingId) {
        return shippingMapper.deleteByShippingId(shippingId) != 0;
    }

    /**
     * 收货地址id验证
     *
     * @param userId
     * @param shippingId
     */
    public boolean shippingIdValid(String userId, String shippingId) {
        Shipping shipping;
        return !StringUtils.isEmpty(shippingId) && (shipping = selectByShippingId(shippingId)) != null && userId.equals(shipping.getUserId());
    }
}
