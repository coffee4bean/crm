package com.shsxt.crm.controller;

import com.shsxt.crm.base.BaseController;
import com.shsxt.crm.constant.CrmConstant;
import com.shsxt.crm.dto.UserDto;
import com.shsxt.crm.exceptions.ParamsException;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.model.UserInfo;
import com.shsxt.crm.query.UserQuery;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @RequestMapping("index")
    public String index(){
        return "user";
    }
    @RequestMapping("login")
    @ResponseBody
    public ResultInfo login(String userName,String userPwd){
        UserInfo userInfo = userService.userLogin(userName,userPwd);
        return success(200,"登录成功",userInfo);
    }
    @RequestMapping("updateUserPwd")
    @ResponseBody
    public ResultInfo updateUserPwd(String oldPassword, String newPassword, String confirmPassword, HttpServletRequest request){
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        userService.updateUserPwd(oldPassword,newPassword,confirmPassword,userId);
/*        ResultInfo info = new ResultInfo();
        info.setCode(200);
        info.setMsg("密码修改成功");
        return info;*/
        return success(200,"修改密码成功");
    }
    @RequestMapping("queryCustomerManagers")
    @ResponseBody
    public List<Map> queryCustomerManagers(){
        return userService.queryCustomerManagers();
    }
    @RequestMapping("queryUsersByParams")
    @ResponseBody
    public Map<String,Object> queryUsersByParams(
            @RequestParam(defaultValue = "1")Integer page,
            @RequestParam(defaultValue = "10")Integer rows,
            UserQuery query){
        query.setPageNum(page);
        query.setPageSize(rows);
        return userService.queryForPage(query);
    }
    @RequestMapping("saveOrUpdateUser")
    @ResponseBody
    public ResultInfo saveOrUpdateUser(UserDto userDto,Integer[] roleIds){
        userService.saveOrUpdateUser(userDto,roleIds);
        return success(CrmConstant.OPS_SUCCESS_CODE);
    }
    @RequestMapping("deleteUsers")
    @ResponseBody
    public ResultInfo deleteUsers(Integer[] ids){
        userService.deleteUsers(ids);
        return success(CrmConstant.OPS_SUCCESS_MSG);
    }
}
