package com.shsxt.crm.service;

import com.shsxt.crm.base.BaseService;
import com.shsxt.crm.constant.CrmConstant;
import com.shsxt.crm.dao.ModuleMapper;
import com.shsxt.crm.dao.PermissionMapper;
import com.shsxt.crm.dto.ModuleDto;
import com.shsxt.crm.po.Module;
import com.shsxt.crm.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.Inet4Address;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Module> {
    @Autowired
    private ModuleMapper moduleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    public List<ModuleDto> queryAllModulesByRoleId(Integer roleId){
        return moduleMapper.queryAllModulesByRoleId(roleId);
    }

    public void saveOrUpdateModule(Module module){
        /**
         * 1 校验参数
         * 2 区分添加和更新
         * 3 补全参数
         */
        checkModuleParams(module);

        Integer id = module.getId();
        module.setUpdateDate(new Date());
        if(null==id){
            module.setCreateDate(new Date());
            module.setIsValid((byte)1);
            AssertUtil.isTrue(moduleMapper.save(module)<1, CrmConstant.OPS_FAILED_MSG);
        }else{

        }
    }

    private void checkModuleParams(Module module) {
        /**
         * 1 模块名称非空和唯一校验
         * 2 权限码非空和格式判断
         * 3 判断当前菜单层级和父级菜单层级关系
         */
        String moduleName = module.getModuleName();
        AssertUtil.isTrue(StringUtils.isBlank(moduleName),"模块名称为空");
        AssertUtil.isTrue(null!=moduleMapper.queryModuleByName(moduleName),"模块名已经存在");
        /***
         * 权限非空和格式判断
         * 0 两位
         * 1 四位
         * 2 六位
         */
        String optValue = module.getOptValue();
        AssertUtil.isTrue(StringUtils.isBlank(optValue),"权限码为空");
        //获取层级
        Integer grade = module.getGrade();
        AssertUtil.isTrue(null==grade,"菜单层级为空");
        //判断位数是否正确
        //规律: (grade + 1)*2
        int optLen = (grade + 1)*2;
        AssertUtil.isTrue(optLen!=optValue.length(),"权限码必须为"+optLen+"位");
        /**
         * 权限码的唯一性
         */
        AssertUtil.isTrue(null!=moduleMapper.queryModuleByOptValue(optValue),"权限码已经存在");
        /**
         * 判断当前菜单层级和父级菜单层级关系
         * 0 没有父级
         * 1 父级必须为0
         * 2 父级必须为1
         */
        if(grade>0){
            //通过父id查询,找到父grade
            Integer parentId = module.getParentId();
            AssertUtil.isTrue(null==parentId,"父级为空");

            Module parModule = moduleMapper.queryById(parentId);
            Integer parGrade = parModule.getGrade();
            AssertUtil.isTrue(grade-parGrade!=1,"菜单层级不正确");
            /**
             * 如果父权限码为:
             * 10 ->10xx
             * 20 ->20xx
             * 1010 ->1010xx
             */
            String parOptValue = parModule.getOptValue();//父权限码
            AssertUtil.isTrue(!optValue.startsWith(parOptValue),"权限码格式不正确,应为"+parOptValue+"xx形式");
        }else{
            module.setParentId(null);//设置父id为null
        }
    }
    public List<Map> queryModulesByGrade(Integer grade){
        return moduleMapper.queryModulesByGrade(grade);
    }

    public void deleteModule(String optValue){
        /**
         * 1 模块表级联删除
         * 2 权限表级联删除
         */
        Integer num = moduleMapper.queryTotalByOptValue(optValue);
        if(null !=num && num>0){
            AssertUtil.isTrue(moduleMapper.deleteByOptValue(optValue)<num,CrmConstant.OPS_FAILED_MSG);
        }
        Integer num2 = permissionMapper.queryModulesByOptValue(optValue);
        if(null!=num2 && num2>0){
            AssertUtil.isTrue(permissionMapper.deleteModulesByOptValue(optValue)<num2,CrmConstant.OPS_FAILED_MSG);
        }
    }
}
