package com.shsxt.crm.exceptions;

import com.alibaba.fastjson.JSON;
import com.shsxt.crm.model.ResultInfo;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

public class GlobalExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //创建默认返回
        ModelAndView mv = createDefaultModelAndView(request,ex);
        /**
         * 区分:json请求和页面请求
         */
        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            ResponseBody responseBody = method.getAnnotation(ResponseBody.class);
            if(null != responseBody){
                //请求json数据
                ResultInfo info = new ResultInfo();
                info.setCode(300);
                info.setMsg(ex.getMessage());
                if(ex instanceof ParamsException){
                    ParamsException e = (ParamsException) ex;
                    info.setMsg(e.getMsg());
                }
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                PrintWriter pw =null;
                try {
                    pw = response.getWriter();
                    pw.write(JSON.toJSONString(info));
                    pw.flush();
                    pw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(null != pw){
                        pw.close();
                    }
                }
            } else {
                if(ex instanceof ParamsException){
                    ParamsException e = (ParamsException) ex;
                    mv.addObject("errorMag",e.getMsg());
                }
            }
        }
        return mv;
    }

    private ModelAndView createDefaultModelAndView(HttpServletRequest request, Exception ex) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error");
        mv.addObject("errorMsg",ex.getMessage());
        mv.addObject("ctx",request.getContextPath());
        return mv;
    }
}
