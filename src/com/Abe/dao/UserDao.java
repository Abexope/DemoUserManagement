package com.Abe.dao;

import com.Abe.domain.ConditionQueryBean;
import com.Abe.domain.User;

import java.util.List;
import java.util.Map;

/**
 * 用户DAO接口
 */
public interface UserDao {

    /**
     * 查询所有客户信息
     * @return 全部客户信息组成的List集合
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
    void delete(int id);

    /**
     * 查询客户信息
     * @param id 客户id
     * @return
     *      查询成功：User Bean 对象
     *      查询失败：null
     */
    User findById(int id);

    /**
     * 修改用户信息
     * @param user User Bean 对象
     */
    void update(User user);

    /**
     * 条件查询当前客户表中的总记录数
     * @param conditionMap 条件查询Map集合
     * @return 总记录数
     */
    int findTotalCount(Map<String, String[]> conditionMap);

    /**
     * 自定义条件查询
     *      利用反射获取queryBean对象的fields，然后遍历fields，
     *      调用对应的get方法取值，避免与request域中的其它属性重复传参和额外的验证
     * @param queryBean 条件查询 Bean 包装类
     * @return 总记录数
     */
    int findTotalCount(ConditionQueryBean queryBean);

    /**
     * 分页条件查询当前分页的客户记录
     * @param start 开始索引
     * @param rows 单页最大记录数
     * @param conditionMap 条件查询Map集合
     * @return 泛型是 User Bean 对象的List集合
     */
    List<User> findByPage(int start, int rows, Map<String, String[]> conditionMap);

    /**
     * 自定义分页条件查询当前分页的客户记录
     *      利用反射获取queryBean对象的fields，然后遍历fields，
     *      调用对应的get方法取值，避免与request域中的其它属性重复传参和额外的验证
     * @param start 开始索引
     * @param rows 单页最大记录数
     * @param queryBean 条件查询 Bean 包装类
     * @return 泛型是 User Bean 对象的List集合
     */
    List<User> findByPage(int start, int rows, ConditionQueryBean queryBean);

    /**
     * 重复记录校验
     * @param user User Bean 对象
     * @return
     *      查询成功：User实例，表示存在重复记录
     *      查询失败：null，表示没有重复记录
     */
    User findUserByBean(User user);

}
