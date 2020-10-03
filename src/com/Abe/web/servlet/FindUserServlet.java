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

/**
 * 查询客户信息业务
 */
@WebServlet("/findUserServlet")
public class FindUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1.获取id
        String id = request.getParameter("id");

        // 2.调用 UserService 执行查询
        UserService service = new UserServiceImpl();
        User user = service.findUserById(id);

        // 3.将 user 存入 request
        request.setAttribute("user", user);

        // 4.转发到update.jsp
        request.getRequestDispatcher("/update.jsp").forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
