package com.Abe.dao;

import com.Abe.domain.Admin;

/**
 * 管理员DAO接口
 */
public interface AdminDao {

    /**
     * 登陆验证
     * @param username 登陆用户名
     * @param password 登录用户密码
     * @return Admin实例 or null
     */
    Admin findUserByUsernameAndPassword(String username, String password);

}
