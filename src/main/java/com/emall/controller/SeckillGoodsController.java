package com.emall.controller;

import com.emall.entity.SeckillGoods;
import com.emall.result.Result;
import com.emall.service.SeckillGoodsService;
import com.emall.utils.PageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {
    private static final Logger logger = LoggerFactory.getLogger(SeckillGoodsController.class);

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
        logger.info("分页查询所有秒杀商品，第" + pageModel.getCurrentNo() + "页，每页" + pageModel.getPageSize() + "条数据");

        return Result.success("秒杀商品分页查询成功", seckillGoodsService.queryAll(pageModel));
    }

    /**
     * 根据秒杀商品id查询
     *
     * @param seckillGoodsId
     * @return
     */
    @GetMapping("/{seckillGoodsId}/seckillGoodsId")
    @ResponseBody
    public Result queryAll(@PathVariable("seckillGoodsId") String seckillGoodsId) {
        logger.info("根据秒杀商品id=" + seckillGoodsId + "查询商品信息");

        return seckillGoodsId != null ? Result.success("秒杀商品查询成功", seckillGoodsService.selectBySeckillGoodsId(seckillGoodsId)) :
                Result.error("秒杀商品查询失败");
    }

    /**
     * 根据秒杀商品id删除
     *
     * @param seckillGoodsId
     * @return
     */
    @DeleteMapping("/{seckillGoodsId}/seckillGoodsId")
    @ResponseBody
    public Result delete(@PathVariable("seckillGoodsId") String seckillGoodsId) {
        logger.info("根据秒杀商品id=" + seckillGoodsId + "删除");

        return seckillGoodsService.deleteBySeckillGoodsId(seckillGoodsId) ? Result.success("秒杀商品删除成功", null) : Result.error("秒杀商品已上架，无法删除");
    }

    /**
     * 秒杀商品上架
     * @param seckillGoodsId
     * @param startTime
     * @param endTime
     * @return
     */
    @PostMapping("")
    @ResponseBody
    public Result put(@RequestParam("seckillGoodsId") String seckillGoodsId, @RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime) {
        Date date = new Date(System.currentTimeMillis() + 1800 * 1000);
        if (startTime == null) {
            return Result.error("开始时间不能为空");
        } else if (endTime == null) {
            return Result.error("结束时间不能为空");
        } else if (startTime.after(endTime)) {
            return Result.error("开始时间不能大于结束时间");
        } else if (startTime.before(new Date(System.currentTimeMillis() + 1800 * 1000))) {
            return Result.error("开始时间至少在当前时间一小时以后");
        }

        if (seckillGoodsService.countOnShelf() >= 3) {
            return Result.error("秒杀商品同时上架不可超过三种");
        }

        return seckillGoodsService.put(seckillGoodsId, startTime, endTime) ? Result.success("商品上架成功", null) : Result.error("商品上架失败");
    }

    /**
     * 下架商品
     *
     * @param seckillGoodsId
     * @return
     */
    @PostMapping(value = "/pull")
    @ResponseBody
    public Result pull(@RequestBody String seckillGoodsId) {
        logger.info("下架商品,id=" + seckillGoodsId);
        return seckillGoodsService.pull(seckillGoodsId) ? Result.success("商品下架成功", null) : Result.error("秒杀进行中，无法下架");
    }
}
