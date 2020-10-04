package com.Abe.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//@WebFilter("/*")
public class LoginFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        // 0.将ServletRequest对象强制向下转型为HttpServletRequest
        HttpServletRequest request = (HttpServletRequest) req;

        // 1.获取资源请求路径
        String uri = request.getRequestURI();

        // 2.判断是否包含登陆相关资源路径
        /*注意
            不要忘记排除 css、js、图片、验证码等资源
         */
        if (
                uri.contains("/login.jsp") ||               // 登陆页面
                uri.contains("/loginServlet") ||            // 登陆业务程序
                uri.contains("/checkCodeServlet") ||        // 验证码生成程序
                uri.contains("/css/") || uri.contains("/js/") || uri.contains("/fonts/")    // 前端渲染资源
        ) {      // 包含登陆资源
            // 用户希望登陆服务器，放行
            chain.doFilter(req, resp);
        } else {    // 不包含登陆资源
            // 验证用户是否已经登陆
            // 3.从session中获取`admin`字段
            Object admin = request.getSession().getAttribute("admin");
            if (admin != null) {    // 用户已登录
                chain.doFilter(req, resp);      // 放行
            } else {    // 用户未登录
                // 向浏览器输出提示信息
                request.setAttribute("login_msg", "您尚未登陆，请登录");
                // 跳转登陆页面
                request.getRequestDispatcher("/login.jsp").forward(request, resp);
            }
        }



    }

    public void destroy() {
    }

}
