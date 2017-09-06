package com.lefore.ssm.web.user.controller;

import com.lefore.ssm.api.user.entity.User;
import com.lefore.ssm.api.user.service.UserService;
import com.lefore.ssm.comment.dto.BootStrapTableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * author: lefore
 * date: 2017/8/29
 * email: 862080515@qq.com
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

/*    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, Integer offset, Integer limit) {
        LOG.info("invoke----------/user/list");
        offset = offset == null ? 0 : offset;   //默认偏移0
        limit = limit == null ? 50 : limit;   //默认展示50条
        List<User> userList = userService.getUserList(offset, limit);
        model.addAttribute("userList", userList);
        return "userlist";
    }*/

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public BootStrapTableResult<User> list(Integer offset, Integer limit) {
        LOG.info("invoke----------/user/list");
        offset = offset == null ? 0 : offset;//默认便宜0
        limit = limit == null ? 50 : limit;//默认展示50条
        List<User> list = userService.getUserList(offset, limit);
        return new BootStrapTableResult<>(list);
    }
}
