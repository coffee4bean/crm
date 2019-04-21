package com.shsxt.crm.dao;

import com.shsxt.crm.base.BaseDao;
import com.shsxt.crm.dto.UserDto;
import com.shsxt.crm.po.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper extends BaseDao<UserDto> {
     User queryUserByName(String userName);
     Integer updateUserPwd(@Param("id")Integer id,@Param("userPwd")String userPwd);
     List<Map> queryCustomerManagers();

    List<String> queryAllOptValueByUserId(Integer userId);
}