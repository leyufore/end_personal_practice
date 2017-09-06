package com.lefore.ssm.web;

import com.lefore.ssm.entity.User;
import com.lefore.ssm.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * author: lefore
 * date: 2017/8/12
 * email: 862080515@qq.com
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, Integer offset, Integer limit) {
        LOG.info("invoke----------/user/list");
        offset = offset == null ? 0 : offset;   //默认偏移0
        limit = limit == null ? 50 : limit;   //默认展示50条
        List<User> userList = userService.getUserList(offset, limit);
        model.addAttribute("userList", userList);
        return "userlist";
    }

}
