package com.emall.controller;

import com.emall.entity.Category;
import com.emall.result.Result;
import com.emall.service.CategoryService;
import com.emall.utils.PageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Resource
    CategoryService categoryService;

    /**
     * 查询所有商品类别
     * @return
     */
    @GetMapping("")
    @ResponseBody
    public Result<List<Category>> adminQueryAll() {
        logger.info("查询所有商品类别");
        return Result.success("查询所有品类成功", categoryService.QueryAll());
    }

    /**
     * 分页查询所有商品类别
     *
     * @return
     */
    @GetMapping("/admin")
    @ResponseBody
    public Result<PageModel> adminQueryAll(@Valid PageModel<Category> pageModel) {
        logger.info("查询所有商品类别--第" + pageModel.getCurrentNo() + "页，每页" + pageModel.getPageSize() + "条数据");
        return Result.success("查询所有品类成功", categoryService.adminQueryAll(pageModel));
    }

    /**
     * 添加商品类别
     * @return
     */
    @PutMapping("/admin")
    @ResponseBody
    public Result<String> insert(@RequestParam("categoryName") @RequestBody String categoryName) {
        logger.info("添加商品类别--" + categoryName);
        if (StringUtils.isEmpty(categoryName)) {
            return Result.error("品类名称不能为空");
        }
        return categoryService.insert(categoryName) ? Result.success("添加品类成功", categoryName) : Result.error("添加品类失败");
    }

    /**
     * 修改商品类别
     * @return
     */
    @PostMapping("/admin")
    @ResponseBody
    public Result<Category> update(@Valid @RequestBody Category category) {
        logger.info("修改商品类别--" + category);
        return categoryService.update(category) ? Result.success("修改品类名称成功", category) : Result.error("修改品类名称失败");
    }

    /**
     * 删除商品类别
     * @return
     */
    @DeleteMapping("/admin")
    @ResponseBody
    public Result<Category> delete(@RequestBody String categoryId) {
        logger.info("删除商品类别");
        return categoryService.delete(categoryId) ? Result.success("删除商品类别成功", null) : Result.error("删除商品类别失败");
    }
}
