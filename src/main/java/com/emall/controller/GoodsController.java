package com.emall.controller;

import com.alibaba.fastjson.JSONObject;
import com.emall.entity.Goods;
import com.emall.result.Result;
import com.emall.service.GoodsService;
import com.emall.service.SeckillGoodsService;
import com.emall.utils.PageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Resource
    private GoodsService goodsService;

    @Resource
    private SeckillGoodsService seckillGoodsService;

    /**
     * 添加商品
     *
     * @return
     */
    @PutMapping(value = "")
    @ResponseBody
    public Result<Goods> insert(@RequestParam("goods") String goodsJson, @RequestParam("imageFile") MultipartFile imageFile, @RequestParam("detailFile") MultipartFile detailFile) {
        logger.info("添加商品");

        Result<Goods> result = goodsValid(goodsJson);
        if (!result.isStatus()) {
            return result;
        }

        String path = "/tmp/";
        return goodsService.insert(result.getObj(), imageFile, detailFile, path);
    }

    /**
     * 修改商品
     *
     * @return
     */
    @PostMapping(value = "")
    @ResponseBody
    public Result<Goods> update(@RequestParam("goods") String goodsJson, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile, @RequestParam(value = "detailFile", required = false) MultipartFile detailFile) {
        logger.info("修改商品");

        Result<Goods> result = goodsValid(goodsJson);
        if (!result.isStatus()) {
            return result;
        }

        String path = "/tmp/";
        return goodsService.update(result.getObj(), imageFile, detailFile, path);
    }

    private Result<Goods> goodsValid(String goodsJson) {
        logger.info("商品参数验证中...");

        Goods goods = JSONObject.parseObject(goodsJson, Goods.class);

        if (goods.getCategoryId().equals("none")) {
            return Result.error("商品类别不能为空");
        } else if (StringUtils.isEmpty(goods.getGoodsName().trim())) {
            return Result.error("商品名称不能为空");
        } else if (StringUtils.isEmpty(goods.getGoodsDescribe().trim())) {
            return Result.error("商品描述不能为空");
        } else if (goods.getGoodsPrice() == null) {
            return Result.error("商品价格不能为空");
        } else if (goods.getGoodsStock() == null) {
            return Result.error("商品库存不能为空");
        } else {
            logger.info("商品参数验证通过");
            return Result.success("商品参数验证通过", goods);
        }
    }

    /**
     * 根据商品id查询商品
     *
     * @return
     */
    @GetMapping(value = "{goodsId}/goodsId")
    @ResponseBody
    public Result<Goods> selectByGoodsId(@PathVariable("goodsId") String goodsId) {
        logger.info("根据商品id=" + goodsId + "查询商品");
        Goods goods = goodsService.selectByGoodsId(goodsId);
        return goods != null ? Result.success("查询商品成功", goods) : Result.error("查询商品失败");
    }

    /**
     * 后台管理--分页查询商品
     *
     * @return
     */
    @GetMapping(value = "/admin/{listType}/{param}")
    @ResponseBody
    public Result queryByType(@Valid PageModel<Goods> pageModel, @PathVariable("listType") String listType, @PathVariable("param") String param) {
        logger.info("查询商品--By " + listType);
        switch (listType) {
            case "all":
                return Result.success("分页查询所有商品", goodsService.queryAll(pageModel));
            case "goodsName":
                return Result.success("根据关键字分页查询商品", goodsService.selectByKeyWordPaged(param, pageModel));
            case "goodsId":
                return Result.success("根据商品id查询商品", goodsService.selectByGoodsId(param));
            case "seckill":
                return Result.success("秒杀商品分页查询成功", seckillGoodsService.queryAllPaged(pageModel));
            default:
                return Result.error("查询失败");
        }
    }

    /**
     * 用户端根据关键字分页查询商品
     *
     * @return
     */
    @GetMapping(value = "/{keyWord}/keyWord/{sort}/sort")
    @ResponseBody
    public Result<PageModel> selectByKeyWordPaged(@PathVariable("keyWord") String keyWord, @PathVariable("sort") String sort, @Valid PageModel<Goods> pageModel) {
        logger.info("根据关键字'" + keyWord + "'查询商品--第" + pageModel.getCurrentNo() + "页，每页" + pageModel.getPageSize() + "条数据");
        return Result.success("查询商品成功", goodsService.selectByKeyWord(keyWord, sort, pageModel));
    }

    /**
     * 用户端根据商品类别分页查询商品
     * @return
     */
    @GetMapping(value = "/{categoryId}/categoryId/{sort}/sort")
    @ResponseBody
    public Result<PageModel> selectByCategoryId(@PathVariable("categoryId") String categoryId, @PathVariable("sort") String sort, @Valid PageModel<Goods> pageModel) {
        logger.info("根据商品类别'" + categoryId + "'查询商品--第" + pageModel.getCurrentNo() + "页，每页" + pageModel.getPageSize() + "条数据");
        return Result.success("查询商品成功", goodsService.selectByCategoryId(categoryId, sort, pageModel));
    }

    /**
     * 下架商品
     *
     * @param goodsId
     * @return
     */
    @PostMapping(value = "/pull")
    @ResponseBody
    public Result pull(@RequestBody String goodsId) {
        logger.info("下架商品,id=" + goodsId);
        return goodsService.pull(goodsId) ? Result.success("下架商品成功", null) : Result.error("下架商品失败");
    }

    /**
     * 上架商品
     * @param goodsId
     * @return
     */
    @PostMapping(value = "/put")
    @ResponseBody
    public Result put(@RequestBody String goodsId) {
        logger.info("上架商品,id=" + goodsId);
        return goodsService.put(goodsId) ? Result.success("上架商品成功", null) : Result.error("上架商品失败");
    }
}