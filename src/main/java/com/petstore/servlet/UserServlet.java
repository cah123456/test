package com.petstore.servlet;

import com.petstore.model.User;
import com.petstore.service.UserService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * 用户模块的控制器
 * 处理注册、登录、登出、个人资料等请求
 * 映射路径：/user/*
 */
@WebServlet("/user/*")
public class UserServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if ("/logout".equals(pathInfo)) {
            // 用户登出
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else if ("/profile".equals(pathInfo)) {
            // 查看个人资料
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("user") != null) {
                User user = (User) session.getAttribute("user");
                User fullUser = userService.getUserById(user.getId().intValue());
                request.setAttribute("user", fullUser);
                request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if ("/register".equals(pathInfo)) {
            // 用户注册
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String nickname = request.getParameter("nickname");
            String email = request.getParameter("email");
            
            if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                request.setAttribute("error", "用户名和密码不能为空");
                request.getRequestDispatcher("/user/register.jsp").forward(request, response);
                return;
            }
            
            User user = new User(username, password, nickname, email);
            boolean success = userService.register(user);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/user/login.jsp?registered=true");
            } else {
                request.setAttribute("error", "用户名已存在");
                request.getRequestDispatcher("/user/register.jsp").forward(request, response);
            }
        } else if ("/login".equals(pathInfo)) {
            // 用户登录
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                request.setAttribute("error", "用户名和密码不能为空");
                request.getRequestDispatcher("/user/login.jsp").forward(request, response);
                return;
            }
            
            User user = userService.login(username, password);
            
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                
                // 根据角色跳转到不同页面
                if ("admin".equals(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/admin/index.jsp");
                } else {
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                }
            } else {
                request.setAttribute("error", "用户名或密码错误");
                request.getRequestDispatcher("/user/login.jsp").forward(request, response);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}