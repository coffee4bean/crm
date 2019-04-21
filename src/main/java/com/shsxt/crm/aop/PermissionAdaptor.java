package com.shsxt.crm.aop;

import com.shsxt.crm.annotations.RequestPermission;
import com.shsxt.crm.constant.CrmConstant;
import com.shsxt.crm.utils.AssertUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.List;

@Component
@Aspect
public class PermissionAdaptor {
    @Autowired
    private HttpSession session;
    @Pointcut("@annotation(com.shsxt.crm.annotations.RequestPermission)")
    public void cut(){}

    @Around("cut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        Object result = null;
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        RequestPermission permission = method.getAnnotation(RequestPermission.class);
        String aclValue = permission.aclValue();//权限码
        //获取权限列表
        List<String> permissions = (List<String>) session.getAttribute(CrmConstant.USER_PERMISSIONS);
        /*
         * 权限判断
         */
        AssertUtil.isTrue(CollectionUtils.isEmpty(permissions) || !permissions.contains(aclValue),"没有权限");
        result = pjp.proceed();//程序继续执行
        return result;
    }
}
