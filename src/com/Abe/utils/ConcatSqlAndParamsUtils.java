package com.Abe.utils;

import com.Abe.domain.ConditionQueryBean;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 拼接条件查询sql语句和查询参数收集封装类
 */
public class ConcatSqlAndParamsUtils {

    private static String countSql = "SELECT COUNT(*) FROM user WHERE 1 = 1";   // 查询计数的初始sql
    private static String querySql = "SELECT * FROM user WHERE 1 = 1";          // 查询记录的初始sql
    private static boolean countFlag = false;               // countSql填充标记
    private static boolean queryFlag = false;               // querySql填充标记
    private static List<Object> params = new ArrayList<>();                     // 初始参数List集合
    private static StringBuilder stringBuilder = new StringBuilder();           // sql填充StringBuilder对象

    /**
     * 填充sql语句
     * @param sql 初始sql语句
     * @param queryBean 条件查询 Bean 包装类
     * @return 填充后的sql语句
     */
    private static String concat(String sql, ConditionQueryBean queryBean) {

        stringBuilder.append(sql);

        // 1.利用反射获取queryBean对象的class和fields
        Class<? extends ConditionQueryBean> queryBeanClass = queryBean.getClass();
        Field[] fields = queryBeanClass.getDeclaredFields();

        // 2.遍历fields
        for (Field field : fields) {
            try {

                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), queryBeanClass);

                // 获取对应field的get方法
                Method getMethod = pd.getReadMethod();

                // 利用对应field的get方法从queryBean中取值
                String value = (String) getMethod.invoke(queryBean);

                if (value != null && !"".equals(value)) {   // 有值
                    // 向sql中添加查询条件
                    stringBuilder.append(" AND ").append(field.getName()).append(" LIKE ? ");
                    params.add("%" + value + "%");      // 同时记录有效参数的value
                }

            } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();     // 获取完成构造的sql
    }

    /**
     * 计数条件查询
     * @param queryBean 条件查询 Bean 包装类
     */
    public static void concatCount(ConditionQueryBean queryBean) {
        countFlag = !countFlag;
        countSql = concat(countSql, queryBean);
    }

    /**
     * 分页条件查询
     * @param start 开始索引
     * @param rows 单页最大记录数
     * @param queryBean 条件查询 Bean 包装类
     */
    public static void concatQuery(int start, int rows, ConditionQueryBean queryBean) {
        // 前面的语句构造与concatCount流程相同，区别在于初始sql语句不同
        queryFlag = !queryFlag;
        querySql = concat(querySql, queryBean);

        // 添加分页查询参数
        params.add(start);
        params.add(rows);

        // 添加分页查询
        stringBuilder.append(" LIMIT ?, ? ");
        querySql = stringBuilder.toString();     // 获取完成构造的sql
    }

    public static String getCountSql() {
        return countSql;
    }

    public static String getQuerySql() {
        return querySql;
    }

    public static List<Object> getParams() {
        return params;
    }

    public static void reset() {
        // 重置stringBuilder等静态成员，避免可能的错误
        if (queryFlag) {
            queryFlag = false;
            querySql = "SELECT * FROM user WHERE 1 = 1";          // 查询记录的初始sql
        }
        if (countFlag) {
            countFlag = false;
            countSql = "SELECT COUNT(*) FROM user WHERE 1 = 1";   // 查询计数的初始sql
        }
        params = new ArrayList<>();
        stringBuilder = new StringBuilder();
    }

}
