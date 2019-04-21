package com.shsxt.crm.controller;

import com.shsxt.crm.base.BaseController;
import com.shsxt.crm.constant.CrmConstant;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.po.Role;
import com.shsxt.crm.query.RoleQuery;
import com.shsxt.crm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController{
    @Autowired
    private RoleService roleService;

    @RequestMapping("index")
    public String index(){
        return "role";
    }

    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map> queryAllRoles(){
        return roleService.queryAllRoles();
    }

    @RequestMapping("queryRolesByParams")
    @ResponseBody
    public Map<String,Object> queryRolesByParams(
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer rows,
        RoleQuery query){
        return roleService.queryForPage(query);
    }
    @RequestMapping("saveOrUpdateRole")
    @ResponseBody
    public ResultInfo saveOrUpdateRole(Role role){
        roleService.saveOrUpdateRole(role);
        return success(CrmConstant.OPS_SUCCESS_MSG);
    }
    @RequestMapping("doGrant")
    @ResponseBody
    public ResultInfo doGrant(Integer roleId, Integer[] moduleIds){
        roleService.doGrant(roleId,moduleIds);
        return success(CrmConstant.OPS_SUCCESS_MSG);
    }
    @RequestMapping("deleteRoles")
    @ResponseBody
    public ResultInfo deleteRoles(Integer[] ids){
        roleService.deleteRoles(ids);
        return success(CrmConstant.OPS_SUCCESS_MSG);
    }
}
