package com.Abe.web.servlet;

import com.Abe.service.UserService;
import com.Abe.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 批量删除前台勾选的客户数据
 */
@WebServlet("/deleteSelectedUserServlet")
public class DeleteSelectedUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1.获取所有勾选的客户id
        String[] ids = request.getParameterValues("uid");

        // 2.调用service对象执行批量删除
        if (ids != null && ids.length > 0) {      // 后端解决空选误操作
            UserService service = new UserServiceImpl();
            service.deleteSelected(ids);
        }

        // 3.跳转到findUserByPageServlet
        response.sendRedirect(request.getContextPath() + "/findUserByPageServlet");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
