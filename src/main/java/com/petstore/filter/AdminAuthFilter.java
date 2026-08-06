package com.petstore.filter;

import com.petstore.model.User;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 管理员权限过滤器
 * 检查当前用户是否为管理员，非管理员则返回403
 */
@WebFilter("/admin/*")
public class AdminAuthFilter implements Filter {

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
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/user/login.jsp");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (!"admin".equals(user.getRole())) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "无权限访问");
            return;
        }
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}