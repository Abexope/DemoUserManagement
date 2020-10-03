package com.Abe.web.servlet;

import com.Abe.domain.User;
import com.Abe.service.UserService;
import com.Abe.service.impl.UserServiceImpl;
import com.Abe.utils.MapBeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 添加用户业务
 */
@WebServlet("/addUserServlet")
public class AddUserServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1.设置编码
        request.setCharacterEncoding("UTF-8");

        /*// 2.获取参数
        Map<String, String[]> parameterMap = request.getParameterMap();

        // 3.封装对象
        User user = new User();
        try {
            BeanUtils.populate(user, parameterMap);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }*/

        // 自定义通用BeanUtils类将2，3步封装
        User user = new User();
        MapBeanUtils.populate(request, user);

        // 4.调用 UserService 执行添加用户
        UserService service = new UserServiceImpl();
        service.add(user);

        // 5.跳转到findUserByPageServlet
        response.sendRedirect(request.getContextPath() + "/findUserByPageServlet");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

}
