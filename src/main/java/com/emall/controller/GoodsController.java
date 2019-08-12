package com.emall.controller;

import com.alibaba.fastjson.JSONObject;
import com.emall.entity.Goods;
import com.emall.result.Result;
import com.emall.service.GoodsService;
import com.emall.utils.PageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Resource
    private GoodsService goodsService;

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

    public Result<Goods> goodsValid(String goodsJson) {
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
            return Result.success("商品参数验证通过", goods);
        }
    }

    /**
     * 根据关键字分页查询商品
     *
     * @return
     */
    @GetMapping(value = "{goodsId}/goodsId")
    @ResponseBody
    public Result<Goods> selectByGoodsId(@PathVariable("goodsId") String goodsId) {
        logger.info("根据商品id=" + goodsId + "查询商品");
        return Result.success("查询商品成功", goodsService.selectByGoodsId(goodsId));
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
                return Result.success("根据关键字分页查询商品", goodsService.selectByKeyWord(param, pageModel));
            case "goodsId":
                return Result.success("根据商品id查询商品", goodsService.selectByGoodsId(param));
            default:
                return Result.error("查询失败");
        }
    }

    /**
     * 根据关键字分页查询商品
     * @return
     */
    @GetMapping(value = "/{keyWord}/keyWord")
    @ResponseBody
    public Result<PageModel> selectByKeyWord(@PathVariable("keyWord") String keyWord, @Valid PageModel<Goods> pageModel) {
        logger.info("根据关键字'" + keyWord + "'查询商品--第" + pageModel.getCurrentNo() + "页，每页" + pageModel.getPageSize() + "条数据");
        keyWord = "%" + keyWord + "%";
        return Result.success("查询商品成功", goodsService.selectByKeyWord(keyWord, pageModel));
    }

    /**
     * 根据商品类别分页查询商品
     * @return
     */
    @GetMapping(value = "/{categoryId}/categoryId")
    @ResponseBody
    public Result<PageModel> selectByCategoryId(@PathVariable("categoryId") String categoryId, @Valid PageModel<Goods> pageModel) {
        logger.info("根据商品类别'" + categoryId + "'查询商品--第" + pageModel.getCurrentNo() + "页，每页" + pageModel.getPageSize() + "条数据");
        return Result.success("查询商品成功", goodsService.selectByCategoryId(categoryId, pageModel));
    }

    /**
     * 下架商品
     *
     * @param goodsId
     * @return
     */
    @PostMapping(value = "/admin/pull")
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
    @PostMapping(value = "/admin/put")
    @ResponseBody
    public Result put(@RequestBody String goodsId) {
        logger.info("上架商品,id=" + goodsId);
        return goodsService.put(goodsId) ? Result.success("上架商品成功", null) : Result.error("上架商品失败");
    }



//    //根据商品关键字查询到前台
//    @GetMapping("/keyWord")
//    public Result<Goods> selectByKeyWordToTheStage(String keyWord, HttpSession session) {
//        ModelAndView mv = new ModelAndView("goods/goodslist");
//        glist = goodsService.selectByKeyWord(keyWord);
//        session.setAttribute("glist", glist);
//        return mv;
//    }
//
//    //主页关键字查询到前台
//    @RequestMapping("/goods/key")
//    public ModelAndView selectByKeyToTheStage(@RequestParam("key") String key, HttpSession session) {
//        ModelAndView mv = new ModelAndView("goods/goodslist");
//        glist = goodsService.selectByKeyWord(key);
//        session.setAttribute("glist", glist);
//        return mv;
//    }
//
//    //根据商品类别查询到前台
//    @RequestMapping("/goods/cId")
//    public ModelAndView selectByCid(@RequestParam("cId") Integer cId, HttpSession session) {
//        ModelAndView mv = new ModelAndView("goods/goodslist");
//
//        glist = goodsService.selectByCid(cId);
//        session.setAttribute("glist", glist);
//
//        return mv;
//    }
//
//   //根据商品id查询详情
//    @RequestMapping("/goods/detail")
//    public ModelAndView selectByGid(@RequestParam("gId") Integer gId) {
//        ModelAndView mv = new ModelAndView("goods/detail");
//
//        Goods goods = goodsService.selectByGid(gId);
//        mv.addObject("goods", goods);
//
//        return mv;
//    }
//
//    //商品列表排序
//    @RequestMapping("/goods/sort")
//    public ModelAndView goodsSort(@RequestParam("sort") String sort, HttpServletRequest request) {
//        HttpSession session = request.getSession();
//
//        if (sort.equals("price")) {
//            count++;
//            List sortList = (List) session.getAttribute("glist");
//
//            if (count % 2 == 1) {
//                Collections.sort(sortList);
//            } else {
//                Collections.reverse(sortList);
//            }
//            session.setAttribute("glist", sortList);
//
//        } else if (sort.equals("default")) {
//            session.setAttribute("glist", glist);
//        }
//
//        ModelAndView mv = new ModelAndView("goods/goodslist");
//
//        return mv;
//    }
}