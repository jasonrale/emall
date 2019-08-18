package com.emall.vo;

import com.emall.entity.SeckillGoods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 秒杀商品业务对象类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeckillGoodsVo {
    private int remainSeconds = 0;          //倒计时
    private int goingSeconds = 0;           //持续时间
    private SeckillGoods seckillGoods;
}
