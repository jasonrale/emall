package com.emall.service;

import com.emall.entity.SeckillGoods;
import com.emall.entity.SeckillOrder;
import com.emall.entity.User;
import com.emall.redis.RedisKeyUtil;
import com.emall.result.Result;
import com.emall.shiro.ShiroEncrypt;
import com.emall.utils.SnowflakeIdWorker;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

@Service
public class SeckillService {
    @Resource
    RedisTemplate redisTemplate;

    @Resource
    SnowflakeIdWorker snowflakeIdWorker;

    @Resource
    SeckillGoodsService seckillGoodsService;

    @Resource
    SeckillOrderService seckillOrderService;

    //验证码数学运算符
    private static char[] ops = new char[]{'+', '-', '*'};

    /**
     * 创建验证码
     *
     * @param user
     * @param seckillGoodsId
     * @return
     */
    public BufferedImage createCaptcha(User user, String seckillGoodsId) {
        int width = 80;
        int height = 32;

        //创建图像
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        //设置背景颜色
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);

        //设置画笔颜色
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);

        //生成随机干扰点
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        //生成随机码
        String captcha = generateCaptcha(random);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(captcha, 8, 24);
        g.dispose();

        //把验证码存到redis中
        int calcResult = calc(captcha.substring(0, 5));
        redisTemplate.opsForValue().set(RedisKeyUtil.captcha(user.getUserId(), seckillGoodsId), calcResult);

        return image;
    }

    /**
     * 验证码计算结果验证
     *
     * @param user
     * @param seckillGoodsId
     * @param captchaResult
     * @return
     */
    public Result<String> checkCaptchaResult(User user, String seckillGoodsId, int captchaResult) {
        String captchaKey = RedisKeyUtil.captcha(user.getUserId(), seckillGoodsId);

        Integer calcResult = null;

        if (redisTemplate.hasKey(captchaKey)) {
            calcResult = (Integer) redisTemplate.opsForValue().get(captchaKey);
            redisTemplate.delete(captchaKey);
        }

        if (calcResult == null) {
            return Result.error("验证码失效");
        } else if (calcResult != captchaResult) {
            return Result.error("验证结果错误");
        }

        return Result.success("验证通过", null);
    }

    /**
     * 计算数学表达式结果
     *
     * @param exp
     * @return
     */
    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer) engine.eval(exp);
        } catch (ScriptException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 生成加减乘验证码
     *
     * @param random
     * @return
     */
    private String generateCaptcha(Random random) {
        int num1 = random.nextInt(10);
        int num2 = random.nextInt(10);
        int num3 = random.nextInt(10);
        char op1 = ops[random.nextInt(3)];
        char op2 = ops[random.nextInt(3)];
        return "" + num1 + op1 + num2 + op2 + num3 + "=";
    }

    /**
     * 创建秒杀路径
     *
     * @param user
     * @param seckillGoodsId
     * @return
     */
    public String createSeckillPath(User user, String seckillGoodsId) {
        if (StringUtils.isEmpty(seckillGoodsId)) {
            return null;
        }

        String path = ShiroEncrypt.shiroEncrypt(String.valueOf(snowflakeIdWorker.nextId()), "creatPath");
        redisTemplate.opsForValue().set(RedisKeyUtil.seckillPath(user.getUserId(), seckillGoodsId), path);

        return path;
    }

    /**
     * 秒杀接口验证
     *
     * @param user
     * @param seckillGoodsId
     * @param path
     * @return
     */
    public boolean pathValid(User user, String seckillGoodsId, String path) {
        if (user == null || path == null) {
            return false;
        }
        String pathOld = (String) redisTemplate.opsForValue().get(RedisKeyUtil.seckillPath(user.getUserId(), seckillGoodsId));
        return path.equals(pathOld);
    }

    /**
     * 秒杀
     *
     * @param user
     * @param seckillGoods
     * @return
     */
    @Transactional
    public SeckillOrder seckill(User user, SeckillGoods seckillGoods) {
        //减库存 写入秒杀订单
        boolean success = seckillGoodsService.reduceStock(seckillGoods);
        if (success) {
            return seckillOrderService.insertCache(user, seckillGoods);
        } else {
            //库存不足
            setStockOver(seckillGoods.getSeckillGoodsId());
            return null;
        }
    }

    /**
     * 设置库存不足标记
     *
     * @param seckillGoodsId
     */
    private void setStockOver(String seckillGoodsId) {
        redisTemplate.opsForValue().set(RedisKeyUtil.seckillStockOver(seckillGoodsId), true);
    }

    /**
     * 获取秒杀结果
     *
     * @param userId
     * @param seckillGoodsId
     * @return
     */
    public String getSeckillResult(String userId, String seckillGoodsId) {
        SeckillOrder seckillOrder = seckillOrderService.selectByUserIdGoodsId(userId, seckillGoodsId);
        if (seckillOrder != null) {//秒杀成功
            return seckillGoodsId;
        } else {
            boolean isOver = getStockOver(seckillGoodsId);
            if (isOver) {
                return "fail";
            } else {
                return "queuing";
            }
        }
    }

    /**
     * 获取库存是否不足标记
     *
     * @param seckillGoodsId
     * @return
     */
    private boolean getStockOver(String seckillGoodsId) {
        return redisTemplate.hasKey(RedisKeyUtil.seckillStockOver(seckillGoodsId));
    }

    /**
     * 预减库存
     *
     * @param seckillGoodsId
     * @return
     */
    public int reduceCacheStock(String seckillGoodsId) {
        String seckillStockKey = RedisKeyUtil.seckillStockById(seckillGoodsId);

        if ((int) redisTemplate.opsForValue().get(seckillStockKey) > 0) {
            return Math.toIntExact(redisTemplate.opsForValue().decrement(seckillStockKey));
        }

        return -1;
    }
}
