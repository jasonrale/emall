package com.emall.controller;

import com.alibaba.fastjson.JSONObject;
import com.emall.entity.SeckillGoods;
import com.emall.result.Result;
import com.emall.service.SeckillGoodsService;
import com.emall.vo.SeckillGoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 秒杀商品控制层
 */
@Controller
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {
    private static final Logger logger = LoggerFactory.getLogger(SeckillGoodsController.class);

    @Resource
    SeckillGoodsService seckillGoodsService;

    /**
     * 用户端查询所有秒杀商品
     *
     * @return
     */
    @GetMapping("")
    @ResponseBody
    public Result queryAllForUser() {
        logger.info("查询上架秒杀商品");

        return Result.success("秒杀商品分页查询成功", seckillGoodsService.queryAll());
    }

    /**
     * 用户端根据秒杀商品id查询
     *
     * @param seckillGoodsId
     * @return
     */
    @GetMapping("/{seckillGoodsId}/seckillGoodsId")
    @ResponseBody
    public Result<SeckillGoodsVo> selectBySeckillGoodsIdForUser(@PathVariable("seckillGoodsId") String seckillGoodsId) {
        logger.info("根据秒杀商品id=" + seckillGoodsId + "查询商品信息");
        SeckillGoods seckillGoods = seckillGoodsService.selectBySeckillGoodsIdFromCache(seckillGoodsId);
        int stock = seckillGoodsService.stockBySeckillGoodsId(seckillGoodsId);
        seckillGoods.setSeckillGoodsStock(stock);

        if (seckillGoodsId == null) {
            return Result.error("查询商品信息失败");
        }

        long startTime = seckillGoods.getSeckillGoodsStartTime().getTime() / 1000;
        long endTime = seckillGoods.getSeckillGoodsEndTime().getTime() / 1000;
        long now = System.currentTimeMillis() / 1000;
        int remainSeconds = 0;
        if (now < startTime) {//秒杀准备中，倒计时
            seckillGoods.setSeckillGoodsStatus(SeckillGoods.PREPARING);
            remainSeconds = (int) (startTime - now);
        } else if (now == startTime) {//秒杀进行中
            seckillGoods.setSeckillGoodsStatus(SeckillGoods.ONGOING);
        } else if (now < endTime) {//秒杀进行中
            remainSeconds = (int) (startTime - now);
            seckillGoods.setSeckillGoodsStatus(SeckillGoods.ONGOING);
        } else {//秒杀已经结束
            seckillGoods.setSeckillGoodsStatus(SeckillGoods.COMPLETE);
            remainSeconds = (int) (startTime - endTime);
        }
        SeckillGoodsVo vo = new SeckillGoodsVo();
        vo.setSeckillGoods(seckillGoods);
        vo.setGoingSeconds((int) (startTime - endTime));
        vo.setRemainSeconds(remainSeconds);

        return Result.success("秒杀商品查询成功", vo);
    }

    /**
     * 根据秒杀商品id从数据库查询
     *
     * @param seckillGoodsId
     * @return
     */
    @GetMapping("/fromDB/{seckillGoodsId}/seckillGoodsId")
    @ResponseBody
    public Result<SeckillGoods> selectBySeckillGoodsIdForAdmin(@PathVariable("seckillGoodsId") String seckillGoodsId) {
        logger.info("根据秒杀商品id=" + seckillGoodsId + "查询商品信息");

        return seckillGoodsId != null ? Result.success("秒杀商品查询成功", seckillGoodsService.selectBySeckillGoodsId(seckillGoodsId)) : Result.error("秒杀商品查询失败");
    }

    /**
     * 根据秒杀商品id删除
     *
     * @param seckillGoodsId
     * @return
     */
    @DeleteMapping("")
    @ResponseBody
    public Result delete(@RequestBody String seckillGoodsId) {
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
    @PostMapping("/put")
    @ResponseBody
    public Result put(@RequestParam("seckillGoodsId") String seckillGoodsId, @RequestParam("startTime") Date startTime, @RequestParam("endTime") Date endTime) {
        if (startTime == null) {
            return Result.error("开始时间不能为空");
        } else if (endTime == null) {
            return Result.error("结束时间不能为空");
        } else if (startTime.after(endTime)) {
            return Result.error("开始时间不能大于结束时间");
        } else if (startTime.before(new Date(System.currentTimeMillis()))) {
            return Result.error("开始时间不能小于当前时间");
        }

        if (seckillGoodsService.countOnShelf() >= 12) {
            return Result.error("秒杀商品同时上架不可超过十二种");
        }

        return seckillGoodsService.put(seckillGoodsId, startTime, endTime) ? Result.success("商品上架成功", null) : Result.error("商品上架失败");
    }

    /**
     * 秒杀商品下架
     *
     * @param seckillGoodsId
     * @return
     */
    @PostMapping("/pull")
    @ResponseBody
    public Result pull(@RequestBody String seckillGoodsId) {
        logger.info("根据秒杀商品id=" + seckillGoodsId + "下架");

        return seckillGoodsService.pull(seckillGoodsId) ? Result.success("商品下架成功", null) : Result.error("商品下架失败");
    }

    /**
     * 修改秒杀商品信息
     *
     * @return
     */
    @PostMapping(value = "")
    @ResponseBody
    public Result<SeckillGoods> update(@RequestParam("seckillGoods") String goodsJson, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile, @RequestParam(value = "detailFile", required = false) MultipartFile detailFile) {
        logger.info("修改秒杀商品信息");

        Result<SeckillGoods> result = seckillGoodsValid(goodsJson);
        if (!result.isStatus()) {
            return result;
        }

        String path = "/tmp/";
        return seckillGoodsService.update(result.getObj(), imageFile, detailFile, path);
    }

    /**
     * 秒杀商品参数验证
     *
     * @param goodsJson
     * @return
     */
    private Result<SeckillGoods> seckillGoodsValid(String goodsJson) {
        logger.info("商品参数验证中...");

        SeckillGoods seckillGoods = JSONObject.parseObject(goodsJson, SeckillGoods.class);

        if (seckillGoods.getCategoryId().equals("none")) {
            return Result.error("商品类别不能为空");
        } else if (StringUtils.isEmpty(seckillGoods.getSeckillGoodsName().trim())) {
            return Result.error("商品名称不能为空");
        } else if (StringUtils.isEmpty(seckillGoods.getSeckillGoodsDescribe().trim())) {
            return Result.error("商品描述不能为空");
        } else if (seckillGoods.getSeckillGoodsPrice() == null) {
            return Result.error("商品价格不能为空");
        } else if (seckillGoods.getSeckillGoodsStock() == null) {
            return Result.error("商品库存不能为空");
        } else {
            logger.info("商品参数验证通过");
            return Result.success("商品参数验证通过", seckillGoods);
        }
    }
}
