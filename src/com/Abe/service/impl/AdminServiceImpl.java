package com.Abe.service.impl;

import com.Abe.dao.AdminDao;
import com.Abe.dao.impl.AdminDaoImpl;
import com.Abe.domain.Admin;
import com.Abe.service.AdminService;

public class AdminServiceImpl implements AdminService {

    private AdminDao dao = new AdminDaoImpl();    // 实例化 User Dao 对象

    /**
     * 登陆验证
     * @param admin User Bean 对象，已从浏览器中接收提交表单并封装
     * @return
     *      查询成功：User实例
     *      查询失败：null
     */
    @Override
    public Admin login(Admin admin) {
        return dao.findUserByUsernameAndPassword(admin.getUsername(), admin.getPassword());
    }

}
