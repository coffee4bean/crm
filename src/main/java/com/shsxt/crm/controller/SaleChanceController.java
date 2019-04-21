package com.shsxt.crm.controller;

import com.shsxt.crm.annotations.RequestPermission;
import com.shsxt.crm.base.BaseController;
import com.shsxt.crm.constant.CrmConstant;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.po.SaleChance;
import com.shsxt.crm.query.SaleChanceQuery;
import com.shsxt.crm.service.SaleChanceService;
import com.shsxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("saleChance")
public class SaleChanceController extends BaseController {
    @Autowired
    private SaleChanceService saleChanceService;

    @RequestMapping("index/{state}")
    public String index(@PathVariable Integer state) {
        if (state == 0) {
            return "sale_chance";
        } else if (state == 1) {
            return "cus_dev_plan";
        }
        return "error";
    }

    @RequestPermission(aclValue = "101001")
    @RequestMapping("querySaleChancesByParams")
    @ResponseBody
    public Map<String, Object> querySaleChancesByParams(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer rows, SaleChanceQuery query) {
        query.setPageNum(page);
        query.setPageSize(rows);
        return saleChanceService.queryForPage(query);
    }

    @RequestMapping("saveOrUpdateSaleChance")
    @ResponseBody
    public ResultInfo saveOrUpdateSaleChance(SaleChance saleChance, HttpServletRequest request) {
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        saleChanceService.saveOrUpdateSaleChance(saleChance, userId);
        return success("操作成功");
    }

    @RequestMapping("updateDevResult")
    @ResponseBody
    public ResultInfo updateDevResult(SaleChance saleChance) {
        Integer i = saleChanceService.updateDevResult(saleChance);
        System.out.println(i);
        return success(CrmConstant.OPS_SUCCESS_MSG);
    }
}
