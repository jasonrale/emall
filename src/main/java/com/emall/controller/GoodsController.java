package com.emall.controller;

import com.alibaba.fastjson.JSONObject;
import com.emall.entity.Goods;
import com.emall.result.Result;
import com.emall.service.GoodsService;
import com.emall.utils.PageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
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
    @PostMapping(value = "")
    @ResponseBody
    public Result<Goods> insert(@RequestParam("goods") String goodsJson, @RequestParam("imageFile") MultipartFile imageFile, @RequestParam("detailFile") MultipartFile detailFile) {
        logger.info("添加商品");
        Goods goods = JSONObject.parseObject(goodsJson, Goods.class);
        String path = "/tmp/";

        return goodsService.insert(goods, imageFile, detailFile, path);
    }

    /**
     * 后台管理--分页查询商品
     *
     * @return
     */
    @GetMapping(value = "/admin/queryByType")
    @ResponseBody
    public Result queryByType(@Valid PageModel<Goods> pageModel, String listType, String param) {
        logger.info("查询商品--By " + listType);
        if (listType.equals("all")) {
            return Result.success("分页查询所有商品", goodsService.queryAll(pageModel));
        } else if (listType.equals("goodsName")) {
            return Result.success("根据关键字分页查询商品", goodsService.selectByKeyWord(param, pageModel));
        } else {
            return Result.success("根据商品id查询商品", goodsService.selectByGoodsId(param));
        }
    }

    /**
     * 根据关键字分页查询商品
     * @return
     */
    @GetMapping(value = "/selectByKeyWord")
    @ResponseBody
    public Result<PageModel> selectByKeyWord(String keyWord, @Valid PageModel<Goods> pageModel) {
        logger.info("根据关键字'" + keyWord + "'查询商品--第" + pageModel.getCurrentNo() + "页，每页" + pageModel.getPageSize() + "条数据");
        keyWord = "%" + keyWord + "%";
        return Result.success("查询商品成功", goodsService.selectByKeyWord(keyWord, pageModel));
    }



    /**
     * 根据商品id查询商品
     * @return
     */
    @GetMapping(value = "/selectByGoodsId")
    @ResponseBody
    public Result<PageModel> selectByGoodsId(String categoryId, @Valid PageModel<Goods> pageModel) {
        logger.info("根据商品类别'" + categoryId + "'查询商品--第" + pageModel.getCurrentNo() + "页，每页" + pageModel.getPageSize() + "条数据");
        return Result.success("查询商品成功", goodsService.selectByCategoryId(categoryId, pageModel));
    }

    /**
     * 根据商品类别分页查询商品
     * @return
     */
    @GetMapping(value = "/selectByCategoryId")
    @ResponseBody
    public Result<PageModel> selectByCategoryId(String categoryId, @Valid PageModel<Goods> pageModel) {
        logger.info("根据商品类别'" + categoryId + "'查询商品--第" + pageModel.getCurrentNo() + "页，每页" + pageModel.getPageSize() + "条数据");
        return Result.success("查询商品成功", goodsService.selectByCategoryId(categoryId, pageModel));
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