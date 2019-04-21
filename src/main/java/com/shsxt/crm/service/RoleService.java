package com.shsxt.crm.service;

import com.shsxt.crm.base.BaseService;
import com.shsxt.crm.constant.CrmConstant;
import com.shsxt.crm.dao.PermissionMapper;
import com.shsxt.crm.dao.RoleMapper;
import com.shsxt.crm.dao.UserRoleMapper;
import com.shsxt.crm.po.Permission;
import com.shsxt.crm.po.Role;
import com.shsxt.crm.utils.AssertUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role> {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    public List<Map> queryAllRoles(){
        return roleMapper.queryAllRoles();
    }

    public void saveOrUpdateRole(Role role){
        String roleName = role.getRoleName();
        AssertUtil.isTrue(StringUtils.isBlank(roleName),"角色名为空");
        Integer id = role.getId();
        role.setUpdateDate(new Date());
        if(null == id){
            /**
             * 角色名唯一判断
             */
            AssertUtil.isTrue(null != roleMapper.queryByRoleName(roleName),"用户名已经存在");
            role.setIsValid(1);
            role.setCreateDate(new Date());
            AssertUtil.isTrue(roleMapper.save(role)<1, CrmConstant.OPS_FAILED_MSG);
        } else {
            /**
             * 判断是否是修改名字的操作
             * 1 如果通过查询的角色名字和当前角色名字一致,代表不修改角色名字 ,不需要做唯一校验
             * 2 不一致,代表修改角色名字,要做 唯一校验
             */
            Role roleDB = roleMapper.queryById(id);
            if(!roleDB.getRoleName().equals(roleName)){
                AssertUtil.isTrue(null != roleMapper.queryByRoleName(roleName),"角色已经存在");
            }
            AssertUtil.isTrue(roleMapper.update(role)<1,CrmConstant.OPS_FAILED_MSG);
        }
    }

    /**
     * 授权
     * @param roleId
     * @param moduleIds
     */
    public void doGrant(Integer roleId, Integer[] moduleIds) {
        /**
         * 1 查询权限
         * 2 删除权限
         * 3 添加权限
         */
        Integer num = permissionMapper.queryModulesByRoleId(roleId);
        if(num>0){
            AssertUtil.isTrue(permissionMapper.deleteModulesByRoleId(roleId)<1,CrmConstant.OPS_FAILED_MSG);
        }
        if(ArrayUtils.isNotEmpty(moduleIds)){
            List<Permission> permissionList = new ArrayList<>();
            for(Integer moduleId:moduleIds){
                Permission permission = new Permission();
                permission.setRoleId(roleId);
                permission.setModuleId(moduleId);
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permission.setAclValue("000000");// 权限码
                permissionList.add(permission);
            }
            AssertUtil.isTrue(permissionMapper.saveBatch(permissionList)<permissionList.size(),CrmConstant.OPS_FAILED_MSG);
        }
    }
    /**
     * 删除角色
     */
    public void deleteRoles(Integer[] ids){
        /**
         * 1 删除角色
         * 2 删除用户角色中间表
         * 3 删除角色权限中间
         */
        if(ArrayUtils.isNotEmpty(ids)){
            for(Integer roleId : ids){
                //1 删除角色
                AssertUtil.isTrue(roleMapper.delete(roleId)<1,CrmConstant.OPS_FAILED_MSG);
                //2 删除用户角色中间表
                Integer num1 = userRoleMapper.queryRolesByUserId(roleId);
                if(num1!=null &&num1 >0){
                    AssertUtil.isTrue(userRoleMapper.deleteRolesByUserId(roleId)<num1,CrmConstant.OPS_FAILED_MSG);
                //3 删除角色权限中间表
                    Integer num2 = permissionMapper.queryModulesByRoleId(roleId);
                    if(null!=num2 && num2 >0){
                        AssertUtil.isTrue(permissionMapper.deleteModulesByRoleId(roleId)<num2,CrmConstant.OPS_FAILED_MSG);
                    }
                }
            }
            throw new RuntimeException("测试");
        }
    }
}
