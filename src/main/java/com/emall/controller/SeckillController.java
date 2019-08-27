package com.emall.controller;

import com.emall.entity.SeckillOrder;
import com.emall.entity.User;
import com.emall.rabbitmq.MessageProducer;
import com.emall.rabbitmq.SeckillMessage;
import com.emall.result.Result;
import com.emall.service.SeckillOrderService;
import com.emall.service.SeckillService;
import com.emall.utils.LoginSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;

@Controller
@RequestMapping("/seckill")
public class SeckillController {
    private static final Logger logger = LoggerFactory.getLogger(SeckillController.class);

    @Resource
    SeckillService seckillService;

    @Resource
    SeckillOrderService seckillOrderService;

    @Resource
    LoginSession loginSession;

    @Resource
    MessageProducer messageProducer;

    @Resource
    private HashMap<String, Boolean> stockFlagMap;

    /**
     * 生成秒杀验证码
     *
     * @param response
     * @param seckillGoodsId
     * @return
     */
    @GetMapping("/{seckillGoodsId}/captcha")
    @ResponseBody
    public Result getSeckillCaptcha(HttpServletResponse response, @PathVariable("seckillGoodsId") String seckillGoodsId) {
        logger.info("生成秒杀验证码");

        try {
            BufferedImage image = seckillService.createCaptcha(loginSession.getUserSession(), seckillGoodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return Result.success("生成验证码", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("就差一点点哦，秒杀失败");
        }
    }

    /**
     * 生成秒杀路径参数
     *
     * @param seckillGoodsId
     * @param captchaResult
     * @return
     */
    @GetMapping("/{seckillGoodsId}/{captchaResult}/captcha/path")
    @ResponseBody
    public Result<String> captchaPath(@PathVariable("seckillGoodsId") String seckillGoodsId, @PathVariable(value = "captchaResult") int captchaResult) {
        logger.info("验证计算结果并生成秒杀路径参数");

        User user = loginSession.getUserSession();
        Result<String> result = seckillService.checkCaptchaResult(loginSession.getUserSession(), seckillGoodsId, captchaResult);
        if (!result.isStatus()) {
            return result;
        }

        String path = seckillService.createSeckillPath(user, seckillGoodsId);
        return Result.success("获取秒杀路径参数成功", path);
    }

    /**
     * 验证path
     *
     * @param seckillGoodsId
     * @param path
     * @return
     */
    @GetMapping("/{seckillGoodsId}/{path}/checkPath")
    @ResponseBody
    public Result<String> checkPath(@PathVariable("seckillGoodsId") String seckillGoodsId, @PathVariable(value = "path") String path) {
        logger.info("验证秒杀路径参数：" + path);

        boolean valid = seckillService.pathValid(loginSession.getUserSession(), seckillGoodsId, path);
        if (!valid) {
            return Result.error("请求非法");
        }
        return Result.success("验证秒杀路径成功", path);
    }


    /**
     * 尝试秒杀
     *
     * @param seckillGoodsId
     * @param path
     * @return
     */
    @PostMapping("/{path}/trySeckill")
    @ResponseBody
    public Result<Integer> trySeckill(@RequestParam("seckillGoodsId") String seckillGoodsId, @PathVariable("path") String path) {
        logger.info("尝试秒杀，秒杀商品id：" + seckillGoodsId);

        User user = loginSession.getUserSession();

        //验证path
        boolean valid = seckillService.pathValid(user, seckillGoodsId, path);
        if (!valid) {
            return Result.error("请求非法");
        }

        //库存标记，减少redis访问
        if (stockFlagMap.containsKey(seckillGoodsId)) {
            boolean over = stockFlagMap.get(seckillGoodsId);
            if (over) {
                return Result.error("就差一点点哦，秒杀失败");
            }
        }

        //预减库存
        int stock = seckillService.reduceCacheStock(seckillGoodsId);
        if (stock < 0) {
            stockFlagMap.put(seckillGoodsId, true);
            return Result.error("就差一点点哦，秒杀失败");
        }

        //判断是否已经秒杀到了
        SeckillOrder seckillOrder = seckillOrderService.selectByUserIdGoodsId(user.getUserId(), seckillGoodsId);
        if (seckillOrder != null) {
            return Result.error("不能重复秒杀");
        }

        //入队
        SeckillMessage seckillMessage = new SeckillMessage(user, seckillGoodsId);
        messageProducer.sendSeckillMessage(seckillMessage);
        return Result.success("排队中", null);
    }

    /**
     * 获取秒杀结果
     *
     * @param seckillGoodsId
     * @return
     */
    @GetMapping("/{seckillGoodsId}/result")
    @ResponseBody
    public Result<String> seckillResult(@PathVariable("seckillGoodsId") String seckillGoodsId) {
        logger.info("获取秒杀结果");

        User user = loginSession.getUserSession();
        String result = seckillService.getSeckillResult(user.getUserId(), seckillGoodsId);
        return Result.success("正在秒杀", result);
    }
}
