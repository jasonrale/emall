package com.emall.controller;

import com.emall.entity.Goods;
import com.emall.entity.SeckillGoods;
import com.emall.result.Result;
import com.emall.service.GoodsService;
import com.emall.service.SeckillGoodsService;
import com.emall.utils.SnowFlakeConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;

@Controller
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {
    @Resource
    GoodsService goodsService;

    @Resource
    SeckillGoodsService seckillGoodsService;

    @Resource
    SnowFlakeConfig.SnowflakeIdWorker snowflakeIdWorker;

    /**
     * 秒杀商品上架
     * @param goodsId
     * @param startTime
     * @param endTime
     * @return
     */
    @PutMapping("")
    @ResponseBody
    public Result put(@RequestParam("goodsId") String goodsId, @RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime) {
        Goods goods = goodsService.selectByGoodsId(goodsId);

        SeckillGoods seckillGoods = new SeckillGoods(
                String.valueOf(snowflakeIdWorker.nextId()),
                goodsId,
                goods.getGoodsName(),
                goods.getGoodsDescribe(),
                goods.getGoodsStock(),
                goods.getGoodsPrice(),
                goods.getGoodsImage(),
                goods.getGoodsDetails(),
                startTime,
                endTime,
                SeckillGoods.PREPARING
        );

        return seckillGoodsService.insert(seckillGoods) ?
                Result.success("秒杀商品上架成功", seckillGoods) :
                Result.error("秒杀商品上架失败");
    }
}
