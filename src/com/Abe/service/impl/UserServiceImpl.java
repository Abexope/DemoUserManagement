package com.Abe.service.impl;

import com.Abe.dao.UserDao;
import com.Abe.dao.impl.UserDaoImpl;
import com.Abe.domain.ConditionQueryBean;
import com.Abe.domain.PageBean;
import com.Abe.domain.User;
import com.Abe.service.UserService;

import java.util.List;
import java.util.Map;

/**
 * UserService实现类
 */
public class UserServiceImpl implements UserService {

    private UserDao dao = new UserDaoImpl();    // 实例化 User Dao 对象

    /**
     * 查询所有用户信息
     * @return 全部用户信息组成的List集合
     */
    @Override
    public List<User> findAll() {
        // 调用 UserDao 完成查询
        return dao.findAll();
    }

    /**
     * 添加新的客户记录
     * @param user User Bean 对象
     */
    @Override
    public void add(User user) {
        // 调用 UserDao 完成添加
        dao.add(user);
    }

    /**
     * 删除客户记录
     * @param id 客户id
     */
    @Override
    public void delete(String id) {
        dao.delete(Integer.parseInt(id));
    }

    /**
     * 查询客户信息
     * @param id 客户id
     * @return
     *      查询成功：User Bean 对象
     *      查询失败：null
     */
    @Override
    public User findUserById(String id) {
        return dao.findById(Integer.parseInt(id));
    }

    /**
     * 修改用户信息
     * @param user User Bean 对象
     */
    @Override
    public void update(User user) {
        dao.update(user);
    }

    /**
     * 批量删除客户记录
     * @param ids 客户id数组
     */
    @Override
    public void deleteSelected(String[] ids) {
        for (String id : ids) {                 // 遍历
            dao.delete(Integer.parseInt(id));   // 删除
        }
    }

    /**
     * 分页条件查询
     *  （此部分代码作者已采用其它思路改写，参考下方重载方法，为了保证接口的完整性，因此代码内容仅作注释处理）
     * @param _currentPage 当前页码
     * @param _rows 每页显示的记录数
     * @param conditionMap 条件查询Map集合
     * @return 单页客户数据
     */
    @Override
    public PageBean<User> findUserByPage(String _currentPage, String _rows, Map<String, String[]> conditionMap) {

        /*int currentPage = Integer.parseInt(_currentPage);
        int rows = Integer.parseInt(_rows);

        // 健壮性判断，避免不合理的页面跳转
        if (currentPage <= 0) currentPage = 1;

        // 1.实例化空PageBean对象
        PageBean<User> pb = new PageBean<>();

        // 2.设置参数
        pb.setCurrentPage(currentPage);
        pb.setRows(rows);

        // 3.调用dao查询符合条件的总记录数
        int totalCount = dao.findTotalCount(conditionMap);
        pb.setTotalCount(totalCount);

        // 4.计算totalPage
        int totalPage = (totalCount % rows == 0) ? (totalCount / rows) : (totalCount / rows + 1);
        // 健壮性判断，避免不合理的翻页跳转
        if (currentPage > totalPage) currentPage = totalPage;
        pb.setTotalPage(totalPage);

        // 5.调用dao查询符合条件的 User List 集合
        int start = (currentPage - 1) * rows;   // 计算开始的记录索引
        List<User> userList = dao.findByPage(start, rows, conditionMap);
        pb.setList(userList);

        return pb;*/

        return null;
    }

    /**
     * 自定义分页条件查询
     *      利用反射获取queryBean对象的fields，然后遍历fields，
     *      调用对应的get方法取值，避免与request域中的其它属性重复传参和额外的验证
     * @param _currentPage 当前页码
     * @param _rows 每页显示的记录数
     * @param queryBean 条件查询 Bean 包装类
     * @return 单页客户数据
     */
    @Override
    public PageBean<User> findUserByPage(String _currentPage, String _rows, ConditionQueryBean queryBean) {

        int currentPage = Integer.parseInt(_currentPage);
        int rows = Integer.parseInt(_rows);

        // 健壮性判断，避免不合理的页面跳转
        if (currentPage <= 0) currentPage = 1;

        // 1.实例化空PageBean对象
        PageBean<User> pb = new PageBean<>();

        // 2.设置参数
        pb.setCurrentPage(currentPage);
        pb.setRows(rows);

        // 3.调用dao查询符合条件的总记录数
        int totalCount = dao.findTotalCount(queryBean);
        pb.setTotalCount(totalCount);

        // 4.计算totalPage
        int totalPage = (totalCount % rows == 0) ? (totalCount / rows) : (totalCount / rows + 1);
        // 健壮性判断，避免不合理的翻页跳转
        if (currentPage > totalPage) currentPage = totalPage;
        pb.setTotalPage(totalPage);

        // 5.调用dao查询符合条件的 User List 集合
        int start = (currentPage - 1) * rows;   // 计算开始的记录索引
        List<User> userList = dao.findByPage(start, rows, queryBean);
        pb.setList(userList);

        return pb;
    }

    /**
     * 重复记录校验
     *      暂未实现，不定期回来扩展此功能，思路类似登录校验
     *      1.获取 User Bean 对象，即 user
     *      2.调用UserDaoImpl中的 findUserByBean() 方法（未定义）
     *      3.在DAO的对应方法中，利用反射获取 Bean 对象的 fields 并遍历
     *          正常情况下：除了 id 成员以外都应该有值，因为 id 在数据库中是 PRIMARY KEY 且是 AUTO_INCREMENT 的
     *          那么，对于 id 的处理思路：
     *              （1）循环中校验并continue，操作比较轻，需要测试可行性；
     *              （2）参考模糊查询，在domain包中定义专门用于校验的 Bean，循环中无需校验，但是新建Bean类操作比较重
     *          动态构造sql：
     *              初始sql："SELECT * FROM user WHERE 1 = 1";
     *              动态构造：利用StringBuilder
     *                  stringBuilder.append(" AND ").append(field.getName()).append(" = ? ");      // 精确查询使用 `=`
     *          参数列表：
     *              List<Object> list = new ArrayList<>();
     *      4.查询
     *          template.query(sql, new BeanPropertyRowMapper<>(User.class), params.toArray());
     *
     * @param user User Bean 对象
     * @return
     *      查询成功：User实例，表示存在重复记录
     *      查询失败：null，表示没有重复记录
     */
    @Override
    public User checkDuplicated(User user) {
        String sql = "SELECT";
        return null;
    }

}
