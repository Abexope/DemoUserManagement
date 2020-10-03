package com.Abe.service;

import com.Abe.domain.Admin;

public interface AdminService {

    /**
     * 管理员登陆验证
     * @param admin Admin Bean 对象，已从浏览器中接收提交表单并封装
     * @return User实例 or null
     */
    Admin login(Admin admin);

}
