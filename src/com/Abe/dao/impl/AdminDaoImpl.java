package com.Abe.dao.impl;

import com.Abe.dao.AdminDao;
import com.Abe.domain.Admin;
import com.Abe.utils.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class AdminDaoImpl implements AdminDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 登陆验证
     * @param username 登陆用户名
     * @param password 登录用户密码
     * @return
     *      查询成功：User实例
     *      查询失败：null
     */
    @Override
    public Admin findUserByUsernameAndPassword(String username, String password) {
        try {
            String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";     // OK
            return template.queryForObject(sql, new BeanPropertyRowMapper<>(Admin.class), username, password);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
