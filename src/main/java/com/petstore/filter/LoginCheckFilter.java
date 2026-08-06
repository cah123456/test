package com.petstore.filter;

import com.petstore.model.User;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录检查过滤器
 * 检查用户是否已登录，未登录则重定向到登录页面
 */
@WebFilter("/cart/*")
public class LoginCheckFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // 未登录，重定向到登录页面
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/user/login.jsp");
            return;
        }
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}