package com.shsxt.crm.controller;

import com.shsxt.crm.base.BaseController;
import com.shsxt.crm.constant.CrmConstant;
import com.shsxt.crm.dto.UserDto;
import com.shsxt.crm.po.User;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MainController extends BaseController {
    @Autowired
    private UserService userService;
    @RequestMapping("main")
    public String index(HttpServletRequest request){
        //存放用户信息
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        UserDto user = userService.queryById(userId);
        request.setAttribute("user",user);
        //存放权限列表
        List<String> permissions = userService.queryAllOptValueByUserId(userId);
        request.getSession().setAttribute(CrmConstant.USER_PERMISSIONS,permissions);
        System.out.println("=============================");
        System.out.println(userId);
        System.out.println(permissions.toString());
        System.out.println("=============================");
        return "main";
    }
}
