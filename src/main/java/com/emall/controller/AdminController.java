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
     * 后台管理-分页查询所有商品
     * @return
     */
    @GetMapping(value = "/goods")
    @ResponseBody
    public Result<PageModel> allGoods(PageModel<Goods> pageModel) {
        logger.info("查询所有商品--第" + pageModel.getCurrentNo() + "页，每页" + pageModel.getPageSize() + "条数据");
        return Result.success("查询所有商品成功", goodsService.queryAll(pageModel));
    }


}
