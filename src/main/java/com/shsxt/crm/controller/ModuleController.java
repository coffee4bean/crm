package com.shsxt.crm.controller;

import com.shsxt.crm.base.BaseController;
import com.shsxt.crm.constant.CrmConstant;
import com.shsxt.crm.dto.ModuleDto;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.po.Module;
import com.shsxt.crm.query.ModuleQuery;
import com.shsxt.crm.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {
    @Autowired
    private ModuleService moduleService;

    @RequestMapping("index")
    public String index(){
        return "module";
    }
    @RequestMapping("queryAllModulesByRoleId")
    @ResponseBody
    public List<ModuleDto> queryAllModulesByRoleId(Integer roleId){
        return moduleService.queryAllModulesByRoleId(roleId);
    }
    @RequestMapping("queryModulesByParams")
    @ResponseBody
    public Map<String,Object> queryModulesByParams(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer rows,
            ModuleQuery query){
        query.setPageNum(page);
        query.setPageSize(rows);
        return moduleService.queryForPage(query);
    }
    @RequestMapping("saveOrUpdateModule")
    @ResponseBody
    public ResultInfo saveOrUpdateModule(Module module){
        moduleService.saveOrUpdateModule(module);
        return success(CrmConstant.OPS_SUCCESS_MSG);
    }
    @RequestMapping("queryModulesByGrade")
    @ResponseBody
    public List<Map> queryModulesByGrade(Integer grade){
        return moduleService.queryModulesByGrade(grade);
    }
    @RequestMapping("deleteModule")
    @ResponseBody
    public ResultInfo deleteModule(String optValue){
        moduleService.deleteModule(optValue);
        return success(CrmConstant.OPS_SUCCESS_CODE);
    }
}
