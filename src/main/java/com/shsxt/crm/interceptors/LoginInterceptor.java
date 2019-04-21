package com.shsxt.crm.interceptors;

import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xlf on 2018/11/13.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        /***
         * 登陆状态判断
         * 1. 通过Request获取cookie中的id
         * 2. 通过id查询用户是否存在
         * */
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        AssertUtil.isNotLogin(null==userId || null==userService.queryById(userId),"用户未登陆或不存在");
        return true;
    }
}
