package com.emall.service;

import com.emall.dao.CategoryMapper;
import com.emall.entity.Category;
import com.emall.exception.GeneralException;
import com.emall.redis.RedisKeyUtil;
import com.emall.utils.PageModel;
import com.emall.utils.SnowflakeIdWorker;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 商品类别业务层
 */
@Service
public class CategoryService {
    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Resource
    CategoryMapper categoryMapper;

    @Resource
    RedisTemplate redisTemplate;

    /**
     * 分页查询所有商品类别
     * @param pageModel
     * @return
     */
    public PageModel<Category> adminQueryAll(PageModel<Category> pageModel) {
        long limit = pageModel.getPageSize();
        long offset = (pageModel.getCurrentNo() - 1) * limit;

        List<Category> categoryList;
        categoryList = categoryMapper.adminQueryAll(limit, offset);
        int count = categoryMapper.count();

        pageModel.setList(categoryList);
        pageModel.setCount(count);
        pageModel.setTotalPages();

        return pageModel;
    }

    /**
     * 根据商品类别id查询
     * @param categoryId
     * @return
     */
    public Category selectByCategoryId(String categoryId) {
        String categoryKey = RedisKeyUtil.CATEGORY_PREFIX + categoryId;
        if (redisTemplate.hasKey(categoryKey)) {
            return (Category) redisTemplate.opsForValue().get(categoryKey);
        }

        Category category = categoryMapper.selectByCategoryId(categoryId);
        redisTemplate.opsForValue().set(categoryKey, category, 1800, TimeUnit.SECONDS);
        return category;
    }

    /**
     * 新建商品类别
     * @param categoryName
     * @return
     * @throws GeneralException
     */
    public boolean insert(String categoryName) throws GeneralException {
        Assert.isTrue(!categoryMapper.isExistByName(categoryName), "商品类别已存在");

        String categoryId = String.valueOf(snowflakeIdWorker.nextId());
        Category category = new Category(categoryId, categoryName);
        return categoryMapper.insert(category) != 0;
    }

    /**
     * 修改商品类别
     * @param category
     * @return
     */
    public boolean update(Category category) {
        Assert.isTrue(!categoryMapper.isExistByName(category.getCategoryName()), "商品类别已存在");

        return categoryMapper.update(category) != 0;
    }

    /**
     * 删除商品类别
     * @param categoryId
     * @return
     */
    public boolean delete(String categoryId) {
        return categoryMapper.deleteByCategoryId(categoryId) != 0;
    }
}
