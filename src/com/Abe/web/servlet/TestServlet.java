package com.Abe.web.servlet;

import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 敏感词过滤功能开发的测试Servlet
 *  测试： String value = request.getParameter(String str)
 *         String[] values = request.getParameterValues(String str)
 *         Map<String, String[]> parameterMap = request.getParameterMap()
 */
@WebServlet("/testServlet")
public class TestServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("GBK");
//        String name = request.getParameter("name");
//        String msg = request.getParameter("msg");
//        System.out.println(name + ": " + msg);
//        String[] values = request.getParameterValues("name");
//        System.out.println(Arrays.toString(values));

        // 提取request对象中的ParameterMap
        Map<String, String[]> parameterMap = request.getParameterMap();
        System.out.println(parameterMap);
        for (String key : parameterMap.keySet()) {
            System.out.println(key + ": " + Arrays.toString(parameterMap.get(key)));
            parameterMap.put(key, new String[]{"1111"});
        }

        // 创建自定义HashMap，遍历ParameterMap，将ParameterMap中的key，value转移到HashMap中
        HashMap<String, String[]> hashMap = new HashMap<>();
        for (String key : parameterMap.keySet()) {
            hashMap.put(key, parameterMap.get(key));
        }
        System.out.println(hashMap);

        // 利用BeanUtils将HashMap转化为ParameterMap
//        try {
//            BeanUtils.populate(request.getParameterMap().getClass(), hashMap);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        System.out.println(hashMap);



//        request.getParameterMap()
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
