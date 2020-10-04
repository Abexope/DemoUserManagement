package com.Abe.web.servlet;

import com.Abe.domain.Admin;
import com.Abe.domain.User;
import com.Abe.service.AdminService;
import com.Abe.service.impl.AdminServiceImpl;
import com.Abe.service.impl.UserServiceImpl;
import com.Abe.utils.MapBeanUtils;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 系统登陆业务
 */
@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1.设置编码
        request.setCharacterEncoding("UTF-8");

        // 2.验证码校验
        // 2.1 获取用户填写的验证码
        String verify_code = request.getParameter("verifyCode");

        // 2.2 获取 CheckCodeServlet 生成的验证码
        HttpSession session = request.getSession();
        String check_code_server = (String) session.getAttribute("CHECK_CODE_SERVER");
        session.removeAttribute("CHECK_CODE_SERVER");   // 保证验证码的一次性

        // 2.3 验证码比对
        if (check_code_server == null || !check_code_server.equalsIgnoreCase(verify_code)) {   // 验证失败
            // 向浏览器输出提示信息
            request.setAttribute("login_msg", "验证码错误！");
            // 跳转登陆页面
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        /*// 3.获取其它表单数据（用户名、密码）
        Map<String, String[]> parameterMap = request.getParameterMap();

        // 4.封装 Admin 对象
        Admin admin = new Admin();
        try {
            BeanUtils.populate(admin, parameterMap);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }*/

        // 自定义通用BeanUtils类将3，4步封装
        Admin admin = new Admin();
        MapBeanUtils.populate(request, admin);

        // 5.调用AdminService执行登陆验证
        AdminService service = new AdminServiceImpl();
        Admin loginAdmin = service.login(admin);

        // 6.判断是否成功
        if (loginAdmin != null) {    // 登陆成功
            // 将管理员字段存入session
            session.setAttribute("admin", loginAdmin);
            // 跳转页面
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else {    // 登陆失败
            // 向浏览器输出提示信息
            request.setAttribute("login_msg", "用户名或密码错误！");
            // 跳转登陆页面
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
