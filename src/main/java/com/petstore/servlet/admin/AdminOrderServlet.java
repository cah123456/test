package com.petstore.servlet.admin;

import com.petstore.model.Order;
import com.petstore.service.OrderService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 后台管理订单模块的控制器
 * 处理订单列表、详情、状态更新等请求
 * 映射路径：/admin/order/*
 */
@WebServlet("/admin/order/*")
public class AdminOrderServlet extends HttpServlet {

    private OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if ("/list".equals(pathInfo) || pathInfo == null || pathInfo.equals("/")) {
            // 订单列表
            String status = request.getParameter("status");
            List<Order> orders;
            if (status != null && !status.isEmpty()) {
                // 按状态筛选（简化处理，实际应该从service层支持）
                orders = orderService.getAllOrders();
            } else {
                orders = orderService.getAllOrders();
            }
            
            request.setAttribute("orders", orders);
            request.setAttribute("currentStatus", status);
            request.getRequestDispatcher("/admin/order/list.jsp").forward(request, response);
            
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
            
            request.setAttribute("order", order);
            request.getRequestDispatcher("/admin/order/detail.jsp").forward(request, response);
            
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if ("/updateStatus".equals(pathInfo)) {
            // 更新订单状态
            String orderIdStr = request.getParameter("orderId");
            String status = request.getParameter("status");
            
            if (orderIdStr == null || status == null || orderIdStr.isEmpty() || status.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "参数不完整");
                return;
            }
            
            int orderId = Integer.parseInt(orderIdStr);
            
            // 验证状态流转合法性
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "订单不存在");
                return;
            }
            
            // 检查状态流转是否合法
            if (!isValidStatusTransition(order.getStatus(), status)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "订单状态无效");
                return;
            }
            
            boolean success = orderService.updateOrderStatus(orderId, status);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/order/detail?id=" + orderId);
            } else {
                request.setAttribute("error", "更新订单状态失败");
                doGet(request, response);
            }
            
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * 验证订单状态流转是否合法
     * 状态流转规则：待支付 -> 已支付 -> 已发货 -> 已完成
     */
    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        switch (currentStatus) {
            case "待支付":
                return "已支付".equals(newStatus) || "已取消".equals(newStatus);
            case "已支付":
                return "已发货".equals(newStatus);
            case "已发货":
                return "已完成".equals(newStatus);
            case "已完成":
                return false;
            default:
                return false;
        }
    }
}