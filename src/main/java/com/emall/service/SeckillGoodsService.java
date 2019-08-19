package com.emall.service;

import com.emall.dao.SeckillGoodsMapper;
import com.emall.entity.SeckillGoods;
import com.emall.entity.User;
import com.emall.redis.RedisKeyUtil;
import com.emall.utils.FutureRunnable;
import com.emall.utils.PageModel;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillGoodsService {
    @Resource
    SeckillGoodsMapper seckillGoodsMapper;

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    ScheduledExecutorService scheduleThreadPool;

    //验证码数学运算符
    private static char[] ops = new char[]{'+', '-', '*'};

    /**
     * 管理员分页查询全部秒杀商品
     *
     * @param pageModel
     * @return
     */
    public PageModel queryAllPaged(PageModel pageModel) {
        long limit = pageModel.getPageSize();
        long offset = (pageModel.getCurrentNo() - 1) * limit;

        List seckillGoodsList = seckillGoodsMapper.queryAll(limit, offset);
        int count = seckillGoodsMapper.count();

        pageModel.setCount(count);
        pageModel.setList(seckillGoodsList);
        pageModel.setTotalPages();

        return pageModel;
    }

    /**
     * 根据秒杀商品id从缓存查询商品
     *
     * @param seckillGoodsId
     * @return
     */
    public SeckillGoods selectBySeckillGoodsIdFromCache(String seckillGoodsId) {
        String seckillGoodsKey = RedisKeyUtil.SECKILL_GOODS_PREFIX + seckillGoodsId;
        if (redisTemplate.hasKey(seckillGoodsKey)) {
            return (SeckillGoods) redisTemplate.opsForValue().get(seckillGoodsKey);
        }

        return null;
    }

    /**
     * 根据秒杀商品id从数据库查询商品
     * @param seckillGoodsId
     * @return
     */
    public SeckillGoods selectBySeckillGoodsId(String seckillGoodsId) {
        return seckillGoodsMapper.selectBySeckillGoodsId(seckillGoodsId);
    }

    /**
     * 根据秒杀商品id删除缓存与数据库记录
     * @param seckillGoodsId
     * @return
     */
    public boolean deleteBySeckillGoodsId(String seckillGoodsId) {
        int status = selectBySeckillGoodsId(seckillGoodsId).getSeckillGoodsStatus();
        if (status == 1 || status == 2) {
            return false;
        }

        return seckillGoodsMapper.deleteBySeckillGoodsId(seckillGoodsId) != 0;
    }

    /**
     * 上架秒杀商品
     * @param seckillGoodsId
     * @return
     */
    @Transactional
    public boolean put(String seckillGoodsId, Date startTime, Date endTime) {
        String seckillGoodsKey = RedisKeyUtil.SECKILL_GOODS_PREFIX + seckillGoodsId;
        String seckillStockKey = RedisKeyUtil.seckillStockById(seckillGoodsId);

        SeckillGoods seckillGoods = selectBySeckillGoodsId(seckillGoodsId);
        seckillGoods.setSeckillGoodsStartTime(startTime);
        seckillGoods.setSeckillGoodsEndTime(endTime);
        seckillGoods.setSeckillGoodsStatus(1);

        boolean flag = seckillGoodsMapper.update(seckillGoods) != 0;
        long end = endTime.getTime() / 1000;
        long now = System.currentTimeMillis() / 1000;
        //秒杀商品缓存 结束10分钟后时失效
        redisTemplate.opsForValue().set(seckillGoodsKey, seckillGoods, end - now + 600, TimeUnit.SECONDS);
        //秒杀库存缓存
        redisTemplate.opsForValue().set(seckillStockKey, seckillGoods.getSeckillGoodsStock(), end - now + 600, TimeUnit.SECONDS);
        redisTemplate.opsForList().rightPush(RedisKeyUtil.seckillGoodsAll(), seckillGoodsKey);

        //定时任务
        FutureRunnable task = new FutureRunnable() {
            @Override
            public void run() {
                long start = startTime.getTime() / 1000;
                long end = endTime.getTime() / 1000;
                long now = System.currentTimeMillis() / 1000;
                if (now < start) {
                    System.out.println("秒杀商品" + seckillGoodsId + "准备中");
                } else if (now == start) {
                    seckillGoodsMapper.changeStatus(seckillGoodsId, SeckillGoods.ONGOING);
                    System.out.println("秒杀商品" + seckillGoodsId + "开始");
                } else if (now < end) {
                    System.out.println("秒杀商品" + seckillGoodsId + "进行中");
                } else if (now > end) {
                    seckillGoodsMapper.changeStatus(seckillGoodsId, SeckillGoods.COMPLETE);
                    System.out.println("秒杀商品" + seckillGoodsId + "已结束");
                    getFuture().cancel(false);
                }
            }
        };

        Future future = scheduleThreadPool.scheduleAtFixedRate(task, 1, 1, TimeUnit.SECONDS);
        task.setFuture(future);
        return flag;
    }

    /**
     * 查看已上架的秒杀商品数
     * @return
     */
    public int countOnShelf() {
        return seckillGoodsMapper.countOnShelf();
    }

    /**
     * 用户端查询所有秒杀商品
     *
     * @return
     */
    public List<SeckillGoods> queryAll() {
        String listKey = RedisKeyUtil.seckillGoodsAll();

        if (redisTemplate.hasKey(listKey)) {
            List<String> seckillGoodsKeyList = redisTemplate.opsForList().range(listKey, 0, -1);
            if (seckillGoodsKeyList == null || seckillGoodsKeyList.size() == 0) {
                return null;
            }
            return getFromRedis(seckillGoodsKeyList);
        }

        return null;
    }

    /**
     * 用户端从数据库查询所有秒杀商品
     *
     * @return
     */
    public List<SeckillGoods> queryAllForUser() {
        String redisKey = RedisKeyUtil.seckillGoodsAll();

        List<SeckillGoods> list = seckillGoodsMapper.queryAllOnShelf();
        for (SeckillGoods seckillGoods : list) {
            String seckillGoodsKey = RedisKeyUtil.seckillGoodsById(seckillGoods);
            redisTemplate.opsForValue().set(seckillGoodsKey, seckillGoods, 1800, TimeUnit.SECONDS);
            redisTemplate.opsForList().rightPush(redisKey, seckillGoodsKey);
        }

        redisTemplate.expire(redisKey, 1800, TimeUnit.SECONDS);

        return list;
    }

    /**
     * 从缓存查询所有秒杀商品
     *
     * @return
     */
    public List<SeckillGoods> getFromRedis(List<String> seckillGoodsKeyList) {
        List<SeckillGoods> seckillGoodsList = new ArrayList<>();

        for (String seckillGoodsKey : seckillGoodsKeyList) {
            if (redisTemplate.hasKey(seckillGoodsKey)) {
                //通过键获取商品对象填充到页面list
                seckillGoodsList.add((SeckillGoods) redisTemplate.opsForValue().get(seckillGoodsKey));
            } else {
                //若没有缓存说明秒杀已结束
                redisTemplate.opsForList().remove(RedisKeyUtil.seckillGoodsAll(), 0, seckillGoodsKey);
            }
        }

        return seckillGoodsList;
    }

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
        int calcResult = calc(captcha);
        redisTemplate.opsForValue().set(RedisKeyUtil.captcha(user.getUserId(), seckillGoodsId), calcResult);

        return image;
    }

//    public boolean checkCaptcha(MiaoshaUser user, long goodsId, int verifyCode) {
//        if(user == null || goodsId <=0) {
//            return false;
//        }
//        Integer codeOld = redisService.get(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, Integer.class);
//        if(codeOld == null || codeOld - verifyCode != 0 ) {
//            return false;
//        }
//        redisService.delete(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId);
//        return true;
//    }

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
        return "" + num1 + op1 + num2 + op2 + num3;
    }
}
