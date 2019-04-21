package com.shsxt.crm.dao;

import com.shsxt.crm.base.BaseDao;
import com.shsxt.crm.po.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.net.Inet4Address;

@Repository
public interface PermissionMapper extends BaseDao<Permission> {
    Integer queryModulesByRoleId(Integer roleId);
    Integer deleteModulesByRoleId(Integer roleId);
    Integer queryModulesByOptValue(String optValue);
    Integer deleteModulesByOptValue(String optValue);
}