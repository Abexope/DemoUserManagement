package com.Abe.web.servlet;

import com.Abe.domain.User;
import com.Abe.service.UserService;
import com.Abe.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 查询所有用户信息业务
 */
@WebServlet("/userListServlet")
public class UserListServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1.调用UserService完成查询
        UserService service = new UserServiceImpl();
        List<User> users = service.findAll();

        // 2.将List对象存入request域
        request.setAttribute("users", users);

        // 3.转发到list.jsp
        request.getRequestDispatcher("/list.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

}
