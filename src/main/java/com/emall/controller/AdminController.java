package com.emall.controller;

import com.emall.entity.Category;
import com.emall.entity.Goods;
import com.emall.entity.User;
import com.emall.exception.GeneralException;
import com.emall.result.Result;
import com.emall.service.CategoryService;
import com.emall.service.GoodsService;
import com.emall.utils.ClassCastUtil;
import com.emall.utils.PageModel;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.UnknownSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Resource
    private ClassCastUtil castUtil;

    @Resource
    private GoodsService goodsService;

    @Resource
    CategoryService categoryService;

    /**
     * 获取管理员登录信息
     * @param
     * @return Result
     */
    @GetMapping("")
    @ResponseBody
    public Result<Object> adminInfo() {
        logger.info("获取管理员登录信息中......");

        User adminInfo = null;
        try {
            Object object = SecurityUtils.getSubject().getSession().getAttribute("SysAdmin");
            if (object != null) {
                adminInfo = castUtil.classCast(object, User.class);
            }

        } catch (IllegalAccessException | InstantiationException | UnknownSessionException e) {
            throw new GeneralException("登录已过期");
        }

        return adminInfo != null ? Result.success("管理员" + adminInfo.getUserName() + "已登录", adminInfo) : null;
    }

    /**
     * 后台管理-分页查询所有商品
     * @return
     */
    @GetMapping(value = "/goods")
    @ResponseBody
    public Result<PageModel> allGoods(PageModel<Goods> pageModel) {
        logger.info("查询所有商品--第" + pageModel.getCurrentNo() + "页，每页" + pageModel.getPageSize() + "条数据");
        return Result.success("查询所有商品成功", goodsService.queryAll(pageModel));
    }

    /**
     * 后台管理-分页查询所有商品类别
     * @return
     */
    @GetMapping("/category")
    @ResponseBody
    public Result<PageModel> queryAll(@Valid PageModel<Category> pageModel) {
        logger.info("查询所有商品类别--第" + pageModel.getCurrentNo() + "页，每页" + pageModel.getPageSize() + "条数据");
        return Result.success("查询所有品类成功", categoryService.queryAll(pageModel));
    }

    /**
     * 后台管理-添加商品类别
     * @return
     */
    @PutMapping("/category")
    @ResponseBody
    public Result<String> insert(@RequestParam("categoryName") @RequestBody String categoryName) {
        logger.info("添加商品类别--" + categoryName);
        if (StringUtils.isEmpty(categoryName)) {
            return Result.error("品类名称不能为空");
        }
        return categoryService.insert(categoryName) ? Result.success("添加品类成功", categoryName) : Result.error("添加品类失败");
    }

    /**
     * 后台管理-修改商品类别
     * @return
     */
    @PostMapping("/category")
    @ResponseBody
    public Result<Category> update(@Valid @RequestBody Category category) {
        logger.info("修改商品类别--" + category);
        return categoryService.update(category) ? Result.success("修改品类名称成功", category) : Result.error("修改品类名称失败");
    }

    /**
     * 后台管理-删除商品类别
     * @return
     */
    @DeleteMapping("/category")
    @ResponseBody
    public Result<Category> delete(@RequestBody String categoryId) {
        logger.info("删除商品类别");
        return categoryService.delete(categoryId) ? Result.success("删除商品类别成功", null) : Result.error("删除商品类别失败");
    }
}
