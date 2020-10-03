package com.Abe.service;

import com.Abe.domain.ConditionQueryBean;
import com.Abe.domain.PageBean;
import com.Abe.domain.User;

import java.util.List;
import java.util.Map;

/**
 *  客户管理业务接口
 */
public interface UserService {

    /**
     * 查询所有客户信息
     * @return 客户信息List集合
     */
    List<User> findAll();

    /**
     * 添加新的客户户记录
     * @param user User Bean 对象
     */
    void add(User user);

    /**
     * 删除客户记录
     * @param id 客户id
     */
    void delete(String id);

    /**
     * 查询客户信息
     * @param id 客户id
     * @return
     *      查询成功：User Bean 对象
     *      查询失败：null
     */
    User findUserById(String id);

    /**
     * 修改用户信息
     * @param user User Bean 对象
     */
    void update(User user);

    /**
     * 批量删除客户记录
     * @param ids 客户id数组
     */
    void deleteSelected(String[] ids);

    /**
     * 分页条件查询
     * @param currentPage 当前页码
     * @param rows 每页显示的记录数
     * @param conditionMap 条件查询Map集合
     * @return 单页客户数据
     */
    PageBean<User> findUserByPage(String currentPage, String rows, Map<String, String[]> conditionMap);

    /**
     * 自定义分页条件查询
     *      利用反射获取queryBean对象的fields，然后遍历fields，
     *      调用对应的get方法取值，避免与request域中的其它属性重复传参和额外的验证
     * @param currentPage 当前页码
     * @param rows 每页显示的记录数
     * @param queryBean 条件查询 Bean 包装类
     * @return 单页客户数据
     */
    PageBean<User> findUserByPage(String currentPage, String rows, ConditionQueryBean queryBean);

    /**
     * 重复记录校验
     * @param user User Bean 对象
     * @return
     *      查询成功：User实例，表示存在重复记录
     *      查询失败：null，表示没有重复记录
     */
    User checkDuplicated(User user);
}
