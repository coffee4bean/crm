package com.shsxt.crm.dao;

import com.shsxt.crm.base.BaseDao;
import com.shsxt.crm.dto.ModuleDto;
import com.shsxt.crm.po.Module;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ModuleMapper extends BaseDao<Module> {
     List<ModuleDto> queryAllModulesByRoleId(Integer roleId);
     List<Map> queryModulesByGrade(Integer grade);
     Module queryModuleByName(String moduleName);
     Module queryModuleByOptValue(String optValue);
     Integer deleteByOptValue(String optValue);
     Integer queryTotalByOptValue(String optValue);

}