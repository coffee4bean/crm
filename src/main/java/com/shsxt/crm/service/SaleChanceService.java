package com.shsxt.crm.service;

import com.shsxt.crm.base.BaseController;
import com.shsxt.crm.base.BaseDao;
import com.shsxt.crm.base.BaseService;
import com.shsxt.crm.dao.SaleChanceMapper;
import com.shsxt.crm.dao.UserMapper;
import com.shsxt.crm.po.SaleChance;
import com.shsxt.crm.po.User;
import com.shsxt.crm.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SaleChanceService extends BaseService<SaleChance> {
    @Autowired
    private SaleChanceMapper saleChanceMapper;
    @Autowired
    private UserMapper userMapper;

    public void saveOrUpdateSaleChance(SaleChance saleChance, Integer userId) {
        /**
         * 1 校验参数
         * 2 补全参数
         * 3执行保存操作
         */
        AssertUtil.isTrue(StringUtils.isBlank(saleChance.getCustomerName()), "客户名称为空");
        AssertUtil.isTrue(StringUtils.isBlank(saleChance.getLinkMan()), "联系人为空");
        AssertUtil.isTrue(StringUtils.isBlank(saleChance.getLinkPhone()), "联系电话为空");

        Integer id = saleChance.getId();
        saleChance.setUpdateDate(new Date());//更新时间
        if (null == id) {
            //添加
            User user = userMapper.queryById(userId);
            saleChance.setCreateMan(user.getUserName());//创建人
            /**
             * 判断是否选择分配人
             * 如果已经分配为1
             * 如果未分配为0
             */
            String assignMan = saleChance.getAssignMan();
            if (StringUtils.isBlank(assignMan)) {
                //未分配
                saleChance.setState(0);
            } else {
                saleChance.setState(1);
                saleChance.setAssignTime(new Date());//分配时间
            }
            saleChance.setDevResult(0);// 0 未开发
            saleChance.setIsValid(1);// 0 无效 1 有效
            AssertUtil.isTrue(saleChanceMapper.save(saleChance) < 1, "营销机会添加失败");
        } else {
            //更新
            AssertUtil.isTrue(saleChanceMapper.update(saleChance) < 1, "营销机会添加失败");

        }
    }
   public Integer updateDevResult(SaleChance saleChance){
        return saleChanceMapper.updateDevResult(saleChance);
   }
}
