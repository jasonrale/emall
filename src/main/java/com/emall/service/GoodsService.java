package com.emall.service;

import com.emall.dao.CategoryMapper;
import com.emall.dao.GoodsMapper;
import com.emall.dao.SeckillGoodsMapper;
import com.emall.entity.Category;
import com.emall.entity.Goods;
import com.emall.redis.RedisKeyUtil;
import com.emall.result.Result;
import com.emall.utils.PageModel;
import com.emall.utils.SnowflakeIdWorker;
import com.emall.utils.UploadUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 商品业务层
 */
@Service
public class GoodsService {
    @Resource
    GoodsMapper goodsMapper;

    @Resource
    CategoryMapper categoryMapper;

    @Resource
    SeckillGoodsMapper seckillGoodsMapper;

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    UploadUtil uploadUtil;

    @Resource
    SnowflakeIdWorker snowflakeIdWorker;

    /**
     * 查询所有商品类别(添加或编辑商品时下拉栏)
     *
     * @return
     */
    public List<Category> queryCategoryList() {
        return categoryMapper.queryAll();
    }

    /**
     * 分页查询全部商品的完整逻辑
     * @param pageModel
     * @return
     */
    public PageModel<Goods> queryAll(PageModel<Goods> pageModel) {
        String listKey = RedisKeyUtil.goodsAll(pageModel.getCurrentNo(), pageModel.getPageSize());

        PageModel<Goods> searchResult = accessCache(listKey, pageModel);

        return searchResult == null ? queryAllFromDB(pageModel) : pageModel;
    }

    /**
     * 从数据库查询所有商品
     *
     * @param pageModel
     * @return
     */
    private PageModel<Goods> queryAllFromDB(PageModel<Goods> pageModel) {
        String listKey = RedisKeyUtil.goodsAll(pageModel.getCurrentNo(), pageModel.getPageSize());

        long limit = pageModel.getPageSize();
        long offset = (pageModel.getCurrentNo() - 1) * limit;

        List<Goods> goodsList = goodsMapper.queryAll(limit, offset);
        int count = goodsMapper.count();

        return pageToRedis(listKey, goodsList, count, pageModel);
    }

    /**
     * 根据关键字分页查询商品列表
     * @param keyWord
     * @param pageModel
     * @return
     */
    public PageModel<Goods> selectByKeyWordPaged(String keyWord, PageModel<Goods> pageModel) {
        String listKey = RedisKeyUtil.goodsByKeyWord(keyWord, pageModel.getCurrentNo(), pageModel.getPageSize());

        PageModel<Goods> searchResult = accessCache(listKey, pageModel);

        return searchResult == null ? selectByKeyWordFromDB(keyWord, pageModel) : pageModel;
    }

    /**
     * 根据关键字在数据库中查询商品列表并缓存到Redis
     * @param keyWord
     * @param pageModel
     * @return
     */
    public PageModel<Goods> selectByKeyWordFromDB(String keyWord, PageModel<Goods> pageModel) {
        String listKey = RedisKeyUtil.goodsByKeyWord(keyWord, pageModel.getCurrentNo(), pageModel.getPageSize());

        long limit = pageModel.getPageSize();
        long offset = (pageModel.getCurrentNo() - 1) * limit;

        List<Goods> goodsList = goodsMapper.selectByKeyWordPaged(keyWord, limit, offset);
        int count = goodsMapper.countByKeyWord(keyWord);

        return pageToRedis(listKey, goodsList, count, pageModel);
    }

    /**
     * 用户端根据关键字查询商品列表（已上架）
     * @param keyWord
     * @return
     */
    public PageModel<Goods> selectByKeyWord(String keyWord, String sort, PageModel<Goods> pageModel) {
        String listKey = RedisKeyUtil.keyWordOfSort(keyWord, sort, pageModel.getCurrentNo(), pageModel.getPageSize());

        PageModel<Goods> searchResult = accessCache(listKey, pageModel);

        return searchResult == null ? selectByKeyWordForUser(keyWord, sort, pageModel) : pageModel;
    }


    /**
     * 用户端根据关键字在数据库中查询商品列表并缓存到Redis
     * @param keyWord
     * @param pageModel
     * @return
     */
    public PageModel<Goods> selectByKeyWordForUser(String keyWord, String sort, PageModel<Goods> pageModel) {
        long limit = pageModel.getPageSize();
        long offset = (pageModel.getCurrentNo() - 1) * limit;
        String listKey = RedisKeyUtil.keyWordOfSort(keyWord, sort, pageModel.getCurrentNo(), pageModel.getPageSize());

        int count = goodsMapper.countByKeyWordForUser(keyWord);
        List<Goods> goodsList;

        if (sort.equals("none")) {
            goodsList = goodsMapper.selectByKeyWord(keyWord, limit, offset);
        } else if (sort.equals("asc")) {
            goodsList = goodsMapper.selectByKeyWordAsc(keyWord, limit, offset);
        } else {
            goodsList = goodsMapper.selectByKeyWordDesc(keyWord, limit, offset);
        }

        return pageToRedis(listKey, goodsList, count, pageModel);
    }

    /**
     * 用户端根据商品类别id分页查询商品列表
     * @param categoryId
     * @param pageModel
     * @return
     */
    public PageModel<Goods> selectByCategoryId(String categoryId, String sort, PageModel<Goods> pageModel) {
        String listKey = RedisKeyUtil.categoryOfSort(categoryId, sort, pageModel.getCurrentNo(), pageModel.getPageSize());

        PageModel<Goods> searchResult = accessCache(listKey, pageModel);

        return searchResult == null ? selectByCategoryIdForUser(categoryId, sort, pageModel) : pageModel;
    }

    /**
     * 访问缓存
     *
     * @param listKey
     * @param pageModel
     * @return
     */
    public PageModel<Goods> accessCache(String listKey, PageModel<Goods> pageModel) {
        if (redisTemplate.hasKey(listKey)) {
            int newVersion = (int) redisTemplate.opsForValue().get(RedisKeyUtil.GOODS_VERSION);
            int oldVersion = (int) redisTemplate.opsForList().index(listKey, -1);
            //判断缓存版本是否改变
            if (newVersion == oldVersion) {
                List<String> goodsKeyList = redisTemplate.opsForList().range(listKey, 0, -3);
                if (goodsKeyList != null || goodsKeyList.size() != 0) {
                    int count = (int) redisTemplate.opsForList().index(listKey, -2);
                    return getFromRedis(count, goodsKeyList, pageModel);
                }
            }
        }

        return null;
    }

    /**
     * 用户端根据商品类别从数据库中查询并缓存到redis
     * @param categoryId
     * @param sort
     * @param pageModel
     * @return
     */
    public PageModel<Goods> selectByCategoryIdForUser(String categoryId, String sort, PageModel<Goods> pageModel) {
        long limit = pageModel.getPageSize();
        long offset = (pageModel.getCurrentNo() - 1) * limit;
        String listKey = RedisKeyUtil.categoryOfSort(categoryId, sort, pageModel.getCurrentNo(), pageModel.getPageSize());
        int count = goodsMapper.countByCategoryIdForUser(categoryId);
        List<Goods> goodsList;

        if (sort.equals("none")) {
            goodsList = goodsMapper.selectByCategoryId(categoryId, limit, offset);
        } else if (sort.equals("asc")) {
            goodsList = goodsMapper.selectByCategoryIdAsc(categoryId, limit, offset);
        } else {
            goodsList = goodsMapper.selectByCategoryIdDesc(categoryId, limit, offset);
        }

        return pageToRedis(listKey, goodsList, count, pageModel);
    }

    /**
     * 分页数据存入缓存
     *
     * @param listKey
     * @param goodsList
     * @param count
     * @param pageModel
     * @return
     */
    @Transactional
    public PageModel<Goods> pageToRedis(String listKey, List<Goods> goodsList, int count, PageModel<Goods> pageModel) {
        //如果存在该商品列表缓存，则清空，重新缓存
        if (redisTemplate.hasKey(listKey)) {
            redisTemplate.delete(listKey);
        }

        for (Goods goods : goodsList) {
            String key = RedisKeyUtil.goodsByGoodsId(goods);
            //将查询到的每个商品缓存到Redis
            redisTemplate.opsForValue().set(key, goods, 1800, TimeUnit.SECONDS);
            //将Redis缓存中每个商品的键存入list
            redisTemplate.opsForList().rightPush(listKey, key);
        }
        //将商品总数量追加到list末尾
        redisTemplate.opsForList().rightPush(listKey, count);
        //如果不存在缓存版本号则设置缓存版本
        if (!redisTemplate.hasKey(RedisKeyUtil.GOODS_VERSION)) {
            redisTemplate.opsForValue().set(RedisKeyUtil.GOODS_VERSION, 1);
        }
        //将缓存版本追加到list末尾
        redisTemplate.opsForList().rightPush(listKey, redisTemplate.opsForValue().get(RedisKeyUtil.GOODS_VERSION));
        redisTemplate.expire(listKey, 1800, TimeUnit.SECONDS);

        pageModel.setCount(count);
        pageModel.setList(goodsList);
        pageModel.setTotalPages();

        return pageModel;
    }

    /**
     * 从缓存获取分页数据
     *
     * @param count
     * @param goodsKeyList
     * @param pageModel
     * @return
     */
    public PageModel<Goods> getFromRedis(int count, List<String> goodsKeyList, PageModel pageModel) {
        List<Goods> goodsList = new ArrayList<>();

        for (String goodsKey : goodsKeyList) {
            if (redisTemplate.hasKey(goodsKey)) {
                //通过键获取商品对象填充到页面list
                goodsList.add((Goods) redisTemplate.opsForValue().get(goodsKey));
            } else {
                String[] part = goodsKey.split(":");
                goodsList.add(selectByGoodsId(part[1]));
            }

        }

        pageModel.setCount(count);
        pageModel.setList(goodsList);
        pageModel.setTotalPages();

        return pageModel;
    }

    /**
     * 根据商品id查询商品并缓存
     * @param goodsId
     * @return
     */
    public Goods selectByGoodsId(String goodsId) {
        String goodsKey = RedisKeyUtil.GOODS_PREFIX + goodsId;
        if (redisTemplate.hasKey(goodsKey)) {
            return (Goods) redisTemplate.opsForValue().get(goodsKey);
        }

        Goods goods = goodsMapper.selectByGoodsId(goodsId);
        redisTemplate.opsForValue().set(goodsKey, goods, 1800, TimeUnit.SECONDS);
        return goods;
    }

    /**
     * 商品添加
     *
     * @param goods
     * @param imageFile
     * @param detailFile
     * @param path
     * @return
     */
    @Transactional
    public Result<Goods> insert(Goods goods, MultipartFile imageFile, MultipartFile detailFile, String path) {
        List<String> urlList = uploadUtil.uploadToServer(imageFile, detailFile, path);

        goods.setGoodsId(String.valueOf(snowflakeIdWorker.nextId()));
        goods.setGoodsImage(urlList.get(0));
        goods.setGoodsDetails(urlList.get(1));

        //如果不存在缓存版本号则设置缓存版本
        if (!redisTemplate.hasKey(RedisKeyUtil.GOODS_VERSION)) {
            redisTemplate.opsForValue().set(RedisKeyUtil.GOODS_VERSION, 1);
        }
        redisTemplate.opsForValue().increment(RedisKeyUtil.GOODS_VERSION);

        return goodsMapper.insert(goods) != 0 ? Result.success("商品添加成功", goods) : Result.error("商品添加失败");
    }

    /**
     * 商品修改
     *
     * @param goods
     * @param imageFile
     * @param detailFile
     * @param path
     * @return
     */
    @Transactional
    public Result<Goods> update(Goods goods, MultipartFile imageFile, MultipartFile detailFile, String path) {
        if (imageFile == null && detailFile == null) {
            if (goodsMapper.updateByGoodsId(goods) != 0) {
                deleteGoodsCache(goods.getGoodsId());
                return Result.success("商品修改成功", goods);
            }
        } else if (imageFile != null && detailFile != null) {
            List<String> urlList = uploadUtil.uploadToServer(imageFile, detailFile, path);
            goods.setGoodsImage(urlList.get(0));
            goods.setGoodsDetails(urlList.get(1));
            if (goodsMapper.updateByGoodsId(goods) != 0) {
                deleteGoodsCache(goods.getGoodsId());
                return Result.success("商品修改成功", goods);
            }
        } else if (imageFile != null) {
            List<String> urlList = uploadUtil.uploadToServer(imageFile, null, path);
            goods.setGoodsImage(urlList.get(0));
            if (goodsMapper.updateByGoodsId(goods) != 0) {
                deleteGoodsCache(goods.getGoodsId());
                return Result.success("商品修改成功", goods);
            }
        } else {
            List<String> urlList = uploadUtil.uploadToServer(null, detailFile, path);
            goods.setGoodsDetails(urlList.get(0));
            if (goodsMapper.updateByGoodsId(goods) != 0) {
                deleteGoodsCache(goods.getGoodsId());
                return Result.success("商品修改成功", goods);
            }
        }

        return Result.error("商品修改失败");
    }



    /**
     * 商品删除
     *
     * @param goodsId
     * @return
     */
    @Transactional
    public boolean deleteByGoodsId(String goodsId) {
        boolean success = goodsMapper.deleteByGoodsId(goodsId) != 0;

        //缓存失效
        deleteGoodsCache(goodsId);

        //如果不存在缓存版本号则设置缓存版本
        if (!redisTemplate.hasKey(RedisKeyUtil.GOODS_VERSION)) {
            redisTemplate.opsForValue().set(RedisKeyUtil.GOODS_VERSION, 1);
        }
        redisTemplate.opsForValue().increment(RedisKeyUtil.GOODS_VERSION);

        return success;
    }

    /**
     * 下架商品
     * @param goodsId
     * @return
     */
    @Transactional
    public boolean pull(String goodsId) {
        boolean success = goodsMapper.pull(goodsId) != 0;

        //缓存失效
        deleteGoodsCache(goodsId);

        //如果不存在缓存版本号则设置缓存版本
        if (!redisTemplate.hasKey(RedisKeyUtil.GOODS_VERSION)) {
            redisTemplate.opsForValue().set(RedisKeyUtil.GOODS_VERSION, 1);
        }
        redisTemplate.opsForValue().increment(RedisKeyUtil.GOODS_VERSION);

        return success;
    }

    /**
     * 上架商品
     * @param goodsId
     * @return
     */
    @Transactional
    public boolean put(String goodsId) {
        boolean success = goodsMapper.put(goodsId) != 0;

        //缓存失效
        deleteGoodsCache(goodsId);

        //如果不存在缓存版本号则设置缓存版本
        if (!redisTemplate.hasKey(RedisKeyUtil.GOODS_VERSION)) {
            redisTemplate.opsForValue().set(RedisKeyUtil.GOODS_VERSION, 1);
        }
        redisTemplate.opsForValue().increment(RedisKeyUtil.GOODS_VERSION);

        return success;
    }

    /**
     * 减库存
     *
     * @param goodsId
     * @param count
     * @return
     */
    @Transactional
    public boolean reduceStock(String goodsId, Integer count) {
        //先减库存
        boolean success = goodsMapper.reduceStock(goodsId, count) != 0;

        //缓存失效
        deleteGoodsCache(goodsId);

        return success;
    }

    /**
     * 根据商品id删除商品缓存
     *
     * @param goodsId
     */
    public void deleteGoodsCache(String goodsId) {
        String goodsKey = RedisKeyUtil.GOODS_PREFIX + goodsId;

        //缓存失效
        if (redisTemplate.hasKey(goodsKey)) {
            redisTemplate.delete(goodsKey);
        }
    }
}
