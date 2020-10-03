package com.Abe.web.servlet;

import com.Abe.service.UserService;
import com.Abe.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/deleteUserServlet")
public class DeleteUserServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1.获取id
        String id = request.getParameter("id");

        // 2.调用service执行删除业务
        UserService service = new UserServiceImpl();
        service.delete(id);

        // 3.跳转到findUserByPageServlet重新查询所有客户（重定向）
        response.sendRedirect(request.getContextPath() + "/findUserByPageServlet");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

}
