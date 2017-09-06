package com.lefore.ssm.web;

import com.lefore.ssm.dto.BaseResult;
import com.lefore.ssm.dto.BootStrapTableResult;
import com.lefore.ssm.entity.Goods;
import com.lefore.ssm.enums.ResultEnum;
import com.lefore.ssm.exception.BizException;
import com.lefore.ssm.service.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * author: lefore
 * date: 2017/8/23
 * email: 862080515@qq.com
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GoodsService goodsService;

/*    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, Integer offset, Integer limit) {
        LOG.info("invoke----------/goods/list");
        offset = offset == null ? 0 : offset;   //默认偏移 0
        limit = limit == null ? 50 : limit; //默认展示 50 条
        LOG.info("offset:" + offset);
        List<Goods> list = goodsService.getGoodsList(offset, limit);
        model.addAttribute("goodslist", list);
        return "goodslist";
    }*/

    /**
     * 摒弃jsp页面通过ajax接口做到真正意义上的前后分离
     *
     * @param model
     * @param offset
     * @param limit
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public BootStrapTableResult<Goods> list(Model model, Integer offset, Integer limit) {
        LOG.info("invoke----------/goods/list");
        offset = offset == null ? 0 : offset;   //默认偏移 0
        limit = limit == null ? 50 : limit; //默认展示 50 条
        List<Goods> list = goodsService.getGoodsList(offset, limit);
        return new BootStrapTableResult<Goods>(list);
    }

    /**
     * @param userPhone
     * @param goods
     * @param result
     * @return
     * @Resposnes 标签（配合 jackson 库）会自动根据 http request accept 内容，返回相应的 json 或 xml 内容
     * @RequestMapping 标签中 produces 指明返回的内容类型
     * @CookieValue 会自动获取 http 中 cookie 参数
     * @Valid 会对参数进行校验
     */
    @RequestMapping(value = "/{goodsId}/buy", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public BaseResult<Object> buy(@CookieValue(value = "userPhone", required = false) Long userPhone,
                                  /*@PathVariable("goodsId") Long goodsId*/@Valid Goods goods, BindingResult result, HttpSession httpSession) {
        LOG.info("invoke----------/" + goods.getGoodsId() + "/buy userPhone:" + userPhone);
        if (userPhone == null) {
            return new BaseResult<Object>(false, ResultEnum.INVALID_USER.getMsg());
        }

        //Valid 参数验证(这里注释掉，采用AOP的方式验证,见BindingResultAop.java)
        //if (result.hasErrors()) {
        //    String errorInfo = "[" + result.getFieldError().getField() + "]" + result.getFieldError().getDefaultMessage();
        //    return new BaseResult<Object>(false, errorInfo);
        //}

        //这里纯粹是为了验证集群模式下的session共享功能上
        LOG.info("lastSessionTime:" + httpSession.getAttribute("sessionTime"));
        httpSession.setAttribute("sessionTime", System.currentTimeMillis());
        try {
            goodsService.buyGoods(userPhone, goods.getGoodsId(), false);
        } catch (BizException e) {
            return new BaseResult<Object>(false, e.getMessage());
        } catch (Exception e) {
            return new BaseResult<Object>(false, ResultEnum.INNER_ERROR.getMsg());
        }
        return new BaseResult<Object>(true, null);
    }

}
