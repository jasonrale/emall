package com.emall.controller;

import com.emall.entity.SeckillGoods;
import com.emall.result.Result;
import com.emall.service.SeckillGoodsService;
import com.emall.service.SeckillService;
import com.emall.utils.LoginSession;
import com.emall.vo.SeckillGoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Date;

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
        } else if (now > endTime) {//秒杀已经结束
            seckillGoods.setSeckillGoodsStatus(SeckillGoods.COMPLETE);
            remainSeconds = (int) (startTime - endTime);
        } else {//秒杀进行中
            seckillGoods.setSeckillGoodsStatus(SeckillGoods.ONGOING);
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
        if (startTime == null) {
            return Result.error("开始时间不能为空");
        } else if (endTime == null) {
            return Result.error("结束时间不能为空");
        } else if (startTime.after(endTime)) {
            return Result.error("开始时间不能大于结束时间");
        }
//        else if (startTime.before(new Date(System.currentTimeMillis() + 900 * 1000))) {
//            return Result.error("开始时间至少在当前时间半小时以后");
//        }

        if (seckillGoodsService.countOnShelf() >= 12) {
            return Result.error("秒杀商品同时上架不可超过十二种");
        }

        return seckillGoodsService.put(seckillGoodsId, startTime, endTime) ? Result.success("商品上架成功", null) : Result.error("商品上架失败");
    }
}
