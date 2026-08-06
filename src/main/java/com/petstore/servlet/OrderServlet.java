package com.petstore.servlet;

import com.petstore.model.Order;
import com.petstore.model.User;
import com.petstore.service.OrderService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 订单模块的控制器
 * 处理订单创建、列表、详情、状态查询等请求
 * 映射路径：/order/*
 */
@WebServlet("/order/*")
public class OrderServlet extends HttpServlet {

    private OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if ("/list".equals(pathInfo) || pathInfo == null || pathInfo.equals("/")) {
            // 订单列表
            String status = request.getParameter("status");
            List<Order> orders = orderService.getOrdersByUserId(user.getId(), status);
            request.setAttribute("orders", orders);
            request.setAttribute("currentStatus", status);
            request.getRequestDispatcher("/order/list.jsp").forward(request, response);
            
        } else if ("/detail".equals(pathInfo)) {
            // 订单详情
            String orderIdStr = request.getParameter("id");
            if (orderIdStr == null || orderIdStr.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少订单ID");
                return;
            }
            
            int orderId = Integer.parseInt(orderIdStr);
            Order order = orderService.getOrderById(orderId);
            
            if (order == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "订单不存在");
                return;
            }
            
            // 验证订单归属
            if (!order.getUserId().equals(user.getId())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "无权查看此订单");
                return;
            }
            
            request.setAttribute("order", order);
            request.getRequestDispatcher("/order/detail.jsp").forward(request, response);
            
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if ("/create".equals(pathInfo)) {
            // 创建订单
            String shippingAddress = request.getParameter("shippingAddress");
            String contactPhone = request.getParameter("contactPhone");
            
            if (shippingAddress == null || contactPhone == null ||
                shippingAddress.isEmpty() || contactPhone.isEmpty()) {
                request.setAttribute("error", "收货地址和联系电话不能为空");
                request.getRequestDispatcher("/cart/checkout.jsp").forward(request, response);
                return;
            }
            
            int orderId = orderService.createOrder(user.getId(), shippingAddress, contactPhone);
            
            if (orderId > 0) {
                response.sendRedirect(request.getContextPath() + "/order/detail?id=" + orderId);
            } else {
                request.setAttribute("error", "创建订单失败，请检查库存或购物车");
                request.getRequestDispatcher("/cart/checkout.jsp").forward(request, response);
            }
            
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}