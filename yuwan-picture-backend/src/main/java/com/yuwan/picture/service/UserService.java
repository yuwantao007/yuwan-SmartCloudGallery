package com.yuwan.picture.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuwan.picture.model.dto.user.UserQueryRequest;
import com.yuwan.picture.model.entity.User;
import com.yuwan.picture.model.vo.LoginUserVO;
import com.yuwan.picture.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 30391
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2026-01-28 16:20:01
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);


    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户密码加密
     *
     * @param userPassword
     * @return
     */
    String getEncryptPassword(String userPassword);

    /**
     * 获取当前登录用户信息
     *
     * @param request
     * @return 登录用户信息
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 用户退出登录(注销登录态)
     *
     * @param request
     * @return 是否注销成功
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏后的登录用户信息
     *
     * @param user
     * @return 脱敏后的登录用户信息
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取用户信息
     *
     * @param user
     * @return 脱敏后的用户信息
     */
    UserVO getUserVO(User user);

    /**
     * 获取用户信息列表
     *
     * @param userList
     * @return 脱敏后的用户信息列表
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return 查询条件
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);
}
