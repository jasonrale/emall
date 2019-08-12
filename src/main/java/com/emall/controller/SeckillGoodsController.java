package com.emall.controller;

import com.emall.entity.SeckillGoods;
import com.emall.result.Result;
import com.emall.service.SeckillGoodsService;
import com.emall.utils.PageModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {
    @Resource
    SeckillGoodsService seckillGoodsService;

    /**
     * 查询所有秒杀商品
     *
     * @param pageModel
     * @return
     */
    @GetMapping("")
    @ResponseBody
    public Result queryAll(@Valid PageModel<SeckillGoods> pageModel) {
        return Result.success("秒杀商品分页查询成功", seckillGoodsService.queryAll(pageModel));
    }

    /**
     * 秒杀商品上架
     * @param seckillGoodsId
     * @param startTime
     * @param endTime
     * @return
     */
    @PutMapping("")
    @ResponseBody
    public Result put(@RequestParam("seckillGoodsId") String seckillGoodsId, @RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime) {
        return null;
    }
}
