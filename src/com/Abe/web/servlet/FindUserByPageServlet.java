package com.Abe.web.servlet;

import com.Abe.domain.ConditionQueryBean;
import com.Abe.domain.PageBean;
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
import java.util.Map;

@WebServlet("/findUserByPageServlet")
public class FindUserByPageServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 1.获取参数
        String currentPage = request.getParameter("currentPage");       // 当前页码
        String rows = request.getParameter("rows");                     // 每页显示的记录数

        // 健壮性判断，针对首页展示
        if (currentPage == null || "".equals(currentPage)) {
            currentPage = "1";
        }
        if (rows == null || "".equals(rows)) {
            rows = "5";
        }

        // 获取条件查询参数
        Map<String, String[]> conditionMap = request.getParameterMap();

        // 自己的思路：使用queryBean包装查询条件
        ConditionQueryBean queryBean = new ConditionQueryBean();
        MapBeanUtils.populate(request, queryBean);

        // 2.调用service执行查询
        UserService service = new UserServiceImpl();
//        PageBean<User> pb = service.findUserByPage(currentPage, rows, conditionMap);
        PageBean<User> pb = service.findUserByPage(currentPage, rows, queryBean);

        // 3.将pageBean对象和查询条件存入request
        request.setAttribute("pb", pb);
        request.setAttribute("condition", conditionMap);    // 回写查询条件

        // 4.转发到list.jsp
        request.getRequestDispatcher("/list.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

}
