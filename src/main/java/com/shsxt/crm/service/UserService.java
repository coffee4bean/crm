package com.shsxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shsxt.crm.base.BaseQuery;
import com.shsxt.crm.base.BaseService;
import com.shsxt.crm.constant.CrmConstant;
import com.shsxt.crm.dao.UserMapper;
import com.shsxt.crm.dao.UserRoleMapper;
import com.shsxt.crm.dto.UserDto;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.model.UserInfo;
import com.shsxt.crm.po.User;
import com.shsxt.crm.po.UserRole;
import com.shsxt.crm.query.UserQuery;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.utils.Md5Util;
import com.shsxt.crm.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService extends BaseService<UserDto> {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 删除用户
     * @param ids
     */
    public void deleteUsers(Integer[] ids){
        /**
         * 1 删除用户 把isValid=0
         * 2 删除用户角色信息
         */
        if(null!=ids && ids.length>0){
            for(Integer id : ids){
                AssertUtil.isTrue(userMapper.delete(id)<1,CrmConstant.OPS_FAILED_MSG);
                /**
                 * 角色更新:
                 * 1 查询当前用户是否有角色
                 * 2 有删除,没有直接添加
                 */
                Integer num = userRoleMapper.queryRolesByUserId(id);
                if(null!=num && num>0){
                    AssertUtil.isTrue(userRoleMapper.deleteRolesByUserId(id)<num,CrmConstant.OPS_FAILED_MSG);
                }
            }
        }
    }

    public void saveOrUpdateUser(UserDto user,Integer[] roleIds){

        /**
         * 1 参数校验
         * 2 参数补全
         * 3 区分添加和更新
         * 4 执行操作
         */
        checkUserParams(user);
        user.setUpdateDate(new Date());
        Integer id = user.getId();
        if(null==id){
            User user2 = userMapper.queryUserByName(user.getUserName());
            AssertUtil.isTrue(null!=user2,"用户名已存在");
            /**
             * 1 补全初始密码 123456
             */
            user.setUserPwd(Md5Util.encode("1234566"));
            user.setIsValid(1);
            user.setCreateDate(new Date());
            AssertUtil.isTrue(userMapper.save(user)<1, CrmConstant.OPS_FAILED_MSG);
        }else{
            /**
             * 1 用户更新
             * 2 角色更新
             */
            /**
             * 不允许修改用户名
             */
            UserDto userDto =  userMapper.queryById(id);
            AssertUtil.isTrue(!userDto.getUserName().equals(user.getUserName()),"用户名不允许修改");
            AssertUtil.isTrue(userMapper.update(user)<1,CrmConstant.OPS_FAILED_MSG);
            /**
             * 角色更新:
             * 1 查询当前用户是否有角色
             * 2 有就删除再添加,没有就直接添加
             */
            Integer num = userRoleMapper.queryRolesByUserId(id);
            if(null!=num && num>0){
                AssertUtil.isTrue(userRoleMapper.deleteRolesByUserId(id)<num,CrmConstant.OPS_FAILED_MSG);
            }
        }
        /**
         * 添加用户角色
         */
        if(null!=roleIds && roleIds.length>0){
            List<UserRole> userRoleList = new ArrayList<>();
            for (Integer roleId : roleIds){
                UserRole userRole = new UserRole();
                userRole.setRoleId(roleId);
                userRole.setUserId(user.getId());
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoleList.add(userRole);
            }
            AssertUtil.isTrue(userRoleMapper.saveBatch(userRoleList)<userRoleList.size(),CrmConstant.OPS_FAILED_MSG);
        }
    }

    private void checkUserParams(UserDto user) {
        /**
         * 1 非空校验
         * 2 用户名唯一校验
         */
        String userName = user.getUserName();
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名为空");
        AssertUtil.isTrue(StringUtils.isBlank(user.getTrueName()), "真实姓名为空");
        AssertUtil.isTrue(StringUtils.isBlank(user.getEmail()), "邮箱为空");
        AssertUtil.isTrue(StringUtils.isBlank(user.getPhone()), "手机号为空");

    }

    /**
     * 修改参数密码
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @param userId
     */
    public void updateUserPwd(String oldPassword,String newPassword,String confirmPassword,Integer userId){
        /**
         * 1 参数校验
         * 2 判断两次密码是否一致
         * 3 判断旧密码是否正确
         * 4 修改密码
         */
        checkUserPwdParams(oldPassword,newPassword,confirmPassword);
        User user = userMapper.queryById(userId);
        AssertUtil.isTrue(null==user,"用户不存在");
        /**
         * 判断密码是否正确
         */
        AssertUtil.isTrue(!Md5Util.encode(oldPassword).equals(user.getUserPwd()),"原密码错误");
        /**
         * 存入加密密码
         */
        AssertUtil.isTrue(userMapper.updateUserPwd(userId,Md5Util.encode(newPassword))<1,"密码修改失败");
    }

    private void checkUserPwdParams(String oldPassword, String newPassword, String confirmPassword) {
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"旧密码为空");
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"新密码为空");
        AssertUtil.isTrue(!confirmPassword.equals(newPassword),"两次密码不一致");
    }


    public UserInfo userLogin(String userName, String userPwd){
        /**
         * 1 校验参数
         */
       // ResultInfo info = new ResultInfo();
        /**
         * 使用AssertUtil 简化判断
         */
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"密码为空");
        User user = userMapper.queryUserByName(userName);
        AssertUtil.isTrue(null==user,"用户不存在或已经注销");
        /**
         * 前台传的密码加密后与后台进行对比
         */
        AssertUtil.isTrue(!Md5Util.encode(userPwd).equals(user.getUserPwd()),"密码错误");
        /*info.setCode(200);
        info.setMsg("登录成功!");*/
        UserInfo userInfo = new UserInfo();
        userInfo.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));//id加密
        userInfo.setUserName(user.getUserName());
        userInfo.setRealName(user.getTrueName());
        return userInfo;
    }
    public List<Map> queryCustomerManagers(){
        //System.out.println(userMapper.queryCustomerManagers());
        return userMapper.queryCustomerManagers();
    }
    //重写分页方法
    public Map<String,Object> queryForPage(UserQuery baseQuery) throws DataAccessException {
        PageHelper.startPage(baseQuery.getPageNum(),baseQuery.getPageSize());
        List<UserDto> entities=userMapper.queryByParams(baseQuery);
        PageInfo<UserDto> pageInfo=new PageInfo<UserDto>(entities);
        /*获取分页结果中的数据*/
        List<UserDto> userDtoList = pageInfo.getList();
        for(UserDto userDto : userDtoList){
            String roleIdStr = userDto.getRoleIdStr();// 获取role id的字符串
            if(!StringUtils.isBlank(roleIdStr)){
                String[] roleIdArr = roleIdStr.split(",");
                List<Integer> roleIdList = new ArrayList<>();
                for(String roleId : roleIdArr){
                    roleIdList.add(Integer.valueOf(roleId));
                }
                userDto.setRoleIds(roleIdList);//设置roleId 集合
            }
        }

        Map<String,Object> map=new HashMap<String,Object>();
        map.put("total",pageInfo.getTotal());
        map.put("rows",pageInfo.getList());
        return map;
    }
    /**
     * 查询授权码
     */
    public List<String> queryAllOptValueByUserId(Integer userId){
        return userMapper.queryAllOptValueByUserId(userId);
    }
}
