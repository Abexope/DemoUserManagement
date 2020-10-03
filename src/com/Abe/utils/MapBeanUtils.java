package com.Abe.utils;

import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 通用Map表单封装类
 */
public class MapBeanUtils {
    public static<T> void populate(HttpServletRequest request, T beanObject) {
        // 1.获取Map表单
        Map<String, String[]> parameterMap = request.getParameterMap();
        // 2.封装Bean对象
        try {
            BeanUtils.populate(beanObject, parameterMap);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
