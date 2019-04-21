package com.shsxt.crm.service;

import com.shsxt.crm.base.BaseService;
import com.shsxt.crm.constant.CrmConstant;
import com.shsxt.crm.dao.CusDevPlanMapper;
import com.shsxt.crm.po.CusDevPlan;
import com.shsxt.crm.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class CusDevPlanService extends BaseService<CusDevPlan> {
    @Autowired
    private CusDevPlanMapper cusDevPlanMapper;
    /**
     * 添加或者保存
     */
    public void saveOrUpdateCusDevPlan(CusDevPlan cusDevPlan,Integer sid){
        /**
         * 1 检验参数
         * 2 补全参数
         * 3 判断是添加还是修改
         * 4 执行操作
         */
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()),"计划任务为空");
        AssertUtil.isTrue(null==cusDevPlan.getPlanDate(),"计划时间为空");
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getExeAffect()),"执行效率为空");
        Integer id = cusDevPlan.getId();
        cusDevPlan.setUpdateDate(new Date());//更新

        if(id==null){
            //添加
            cusDevPlan.setSaleChanceId(sid);
            cusDevPlan.setCreateDate(new Date());
            cusDevPlan.setIsValid(1);
            AssertUtil.isTrue(cusDevPlanMapper.save(cusDevPlan)<1, CrmConstant.OPS_FAILED_MSG);
        }else{
            //更新
            AssertUtil.isTrue(cusDevPlanMapper.update(cusDevPlan)<1,CrmConstant.OPS_FAILED_MSG);
        }
    }
}
