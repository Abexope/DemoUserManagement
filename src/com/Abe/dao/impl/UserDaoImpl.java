package com.Abe.dao.impl;

import com.Abe.dao.UserDao;
import com.Abe.domain.ConditionQueryBean;
import com.Abe.domain.User;
import com.Abe.utils.ConcatSqlAndParamsUtils;
import com.Abe.utils.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户DAO实现类
 *      使用JDBC操作数据库
 */
public class UserDaoImpl implements UserDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 查询所有客户信息
     * @return 全部客户信息组成的List集合
     */
    @Override
    public List<User> findAll() {
        // 1.定义sql
        String sql = "SELECT * FROM user";
        // 2.执行sql
        return template.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public void add(User user) {
        // 1.定义sql
        String sql = "INSERT INTO user VALUES(NULL, ?, ?, ?, ?, ?, ?)";
        // 2.执行sql
        template.update(sql,
                user.getName(),         // 姓名
                user.getGender(),       // 性别
                user.getAge(),          // 年龄
                user.getAddress(),      // 籍贯
                user.getQq(),           // QQ
                user.getEmail()         // 邮箱
        );
    }

    /**
     * 删除客户记录
     * @param id 客户id
     */
    @Override
    public void delete(int id) {
        // 1.定义sql
        String sql = "DELETE FROM user WHERE id = ?";
        // 2.执行sql
        template.update(sql, id);
    }

    /**
     * 查询客户信息
     * @param id 客户id
     * @return
     *      查询成功：User Bean 对象
     *      查询失败：null
     */
    @Override
    public User findById(int id) {
        // 1.定义sql
        String sql = "SELECT * FROM user WHERE id = ?";
        // 2.执行sql
        try {
            return template.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);    // 查到了
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;    // 没查到
        }
    }

    /**
     * 修改用户信息
     * @param user User Bean 对象
     */
    @Override
    public void update(User user) {
        // 1.定义sql
        String sql = "UPDATE user SET name = ?, gender = ?, age = ?, address = ?, qq = ?, email = ? WHERE id = ?";
        // 2.执行sql
        template.update(sql,
                user.getName(),
                user.getGender(),
                user.getAge(),
                user.getAddress(),
                user.getQq(),
                user.getEmail(),
                user.getId()
        );
    }

    /**
     * 条件查询当前客户表中的总记录数
     *  （此部分代码作者已采用其它思路改写，参考下方重载方法，为了保证接口的完整性，因此代码内容仅作注释处理）
     * @return 总记录数
     * @param conditionMap 条件查询Map集合
     */
    @Override
    public int findTotalCount(Map<String, String[]> conditionMap) {

        /*// 1.定义模板sql
        String sql = "SELECT COUNT(*) FROM user WHERE 1 = 1";
        StringBuilder stringBuilder = new StringBuilder(sql);

        // 2.遍历conditionMap，根据Map内容动态修改sql语句
        // 定义有效参数List集合
        List<Object> params = new ArrayList<>();
        for (String key : conditionMap.keySet()) {

            // 排除分页条件参数
            if ("currentPage".equals(key) || "rows".equals(key)) continue;

                *//*老师采用在循环中验证排除法实现
                todo: 自己考虑定义ConditionQueryBean类，利用MapBeanUtils封装Bean对象，
                    然后利用反射获取对象的Fields，
                    最后通过遍历Fields书写sql*//*


            String value = conditionMap.get(key)[0];
            if (value != null && !"".equals(value)) {   // 有值
                stringBuilder.append(" AND ").append(key).append(" LIKE ? ");
                params.add("%" + value + "%");      // 记录有效参数的value
            }
        }

        // 3.执行sql
        sql = stringBuilder.toString();     // 将stringBuilder拼接成新的sql语句
        return template.queryForObject(sql, Integer.class, params.toArray());*/
        return 0;
    }

    /**
     * 自定义条件查询
     *      利用反射获取queryBean对象的fields，然后遍历fields，
     *      调用对应的get方法取值，避免与request域中的其它属性重复传参和额外的验证
     * @param queryBean 条件查询 Bean 包装类
     * @return 总记录数
     */
    @Override
    public int findTotalCount(ConditionQueryBean queryBean) {

        /*// 定义模板sql
        String sql = "SELECT COUNT(*) FROM user WHERE 1 = 1";
        StringBuilder stringBuilder = new StringBuilder(sql);

        // 1.获取queryBean对象的class和fields
        Class<? extends ConditionQueryBean> queryBeanClass = queryBean.getClass();
        Field[] fields = queryBeanClass.getDeclaredFields();

        // 2.遍历fields
        List<Object> params = new ArrayList<>();
        for (Field field : fields) {
            try {
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), queryBeanClass);
                Method getMethod = pd.getReadMethod();      // 获取对应field的get方法
                String value = (String) getMethod.invoke(queryBean);    // 利用对应field的get方法从queryBean中取值
                if (value != null && !"".equals(value)) {   // 有值
                    stringBuilder.append(" AND ").append(field.getName()).append(" LIKE ? ");
                    params.add("%" + value + "%");      // 记录有效参数的value
                }
            } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }*/

        // 利用Utils类封装反射与动态sql过程，Utils代码在com.Abe.utils包中
        ConcatSqlAndParamsUtils.concatCount(queryBean);    // 构造sql和params
        String sql = ConcatSqlAndParamsUtils.getCountSql();
        List<Object> params = ConcatSqlAndParamsUtils.getParams();
        ConcatSqlAndParamsUtils.reset();    // 重置内部静态成员

        System.out.println("findTotalCount sql: " + sql);
        System.out.println("findTotalCount params: " + params);

        // 3.执行sql
        /*sql = stringBuilder.toString();*/     // 将stringBuilder拼接成新的sql语句
        return template.queryForObject(sql, Integer.class, params.toArray());
    }

    /**
     * 分页条件查询当前分页的客户记录
     * （此部分代码作者已采用其它思路改写，参考下方重载方法，为了保证接口的完整性，因此代码内容仅作注释处理）
     * @param start 开始索引
     * @param rows 单页最大记录数
     * @param conditionMap 条件查询Map集合
     * @return 泛型是 User Bean 对象的List集合
     */
    @Override
    public List<User> findByPage(int start, int rows, Map<String, String[]> conditionMap) {
        /*// 1.定义sql
        String sql = "SELECT * FROM user WHERE 1 = 1 ";
        StringBuilder stringBuilder = new StringBuilder(sql);   // todo:重复代码功能抽取

        // 2.遍历conditionMap，根据Map内容动态修改sql语句
        // 定义有效参数List集合
        List<Object> params = new ArrayList<>();
        for (String key : conditionMap.keySet()) {

            // 排除分页条件参数
            if ("currentPage".equals(key) || "rows".equals(key)) continue;

//                老师采用在循环中验证排除法实现
//                todo: 自己考虑定义ConditionQueryBean类，利用MapBeanUtils封装Bean对象，
//                    然后利用反射获取对象的Fields，
//                    最后通过遍历Fields书写sql


            String value = conditionMap.get(key)[0];
            if (value != null && !"".equals(value)) {   // 有值
                stringBuilder.append(" AND ").append(key).append(" LIKE ? ");
                params.add("%" + value + "%");      // 记录有效参数的value
            }
        }

        // 添加分页查询
        stringBuilder.append("LIMIT ? , ?");

        // 添加分页查询参数
        params.add(start);
        params.add(rows);

        // 3.执行sql
        sql = stringBuilder.toString();     // 将stringBuilder拼接成新的sql语句
        return template.query(sql, new BeanPropertyRowMapper<>(User.class), params.toArray());*/
        return null;
    }

    /**
     * 自定义分页条件查询当前分页的客户记录
     *      利用反射获取queryBean对象的fields，然后遍历fields，
     *      调用对应的get方法取值，避免与request域中的其它属性重复传参和额外的验证
     * @param start 开始索引
     * @param rows 单页最大记录数
     * @param queryBean 条件查询 Bean 包装类
     * @return 泛型是 User Bean 对象的List集合
     */
    @Override
    public List<User> findByPage(int start, int rows, ConditionQueryBean queryBean) {

        /*// 1.定义sql
        String sql = "SELECT * FROM user WHERE 1 = 1 ";
        StringBuilder stringBuilder = new StringBuilder(sql);

        // 1.获取queryBean对象的class和fields
        Class<? extends ConditionQueryBean> queryBeanClass = queryBean.getClass();
        Field[] fields = queryBeanClass.getDeclaredFields();

        // 2.遍历fields
        List<Object> params = new ArrayList<>();
        for (Field field : fields) {
            try {
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), queryBeanClass);
                Method getMethod = pd.getReadMethod();      // 获取对应field的get方法
                String value = (String) getMethod.invoke(queryBean);    // 利用对应field的get方法从queryBean中取值
                if (value != null && !"".equals(value)) {   // 有值
                    stringBuilder.append(" AND ").append(field.getName()).append(" LIKE ? ");
                    params.add("%" + value + "%");      // 记录有效参数的value
                }
            } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        // 添加分页查询
        stringBuilder.append("LIMIT ? , ?");

        // 添加分页查询参数
        params.add(start);
        params.add(rows);

        // 3.执行sql
        sql = stringBuilder.toString();     // 将stringBuilder拼接成新的sql语句*/

        // 利用Utils类封装反射与动态sql过程，Utils代码在com.Abe.utils包中
        ConcatSqlAndParamsUtils.concatQuery(start, rows, queryBean);       // 构造sql和params
        String sql = ConcatSqlAndParamsUtils.getQuerySql();
        List<Object> params = ConcatSqlAndParamsUtils.getParams();
        ConcatSqlAndParamsUtils.reset();      // 重置内部静态成员

        System.out.println("findByPage sql: " + sql);
        System.out.println("findByPage params: " + params);

        return template.query(sql, new BeanPropertyRowMapper<>(User.class), params.toArray());
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
    public User findUserByBean(User user) {
        return null;
    }

}
