package com.petstore.servlet;

import com.petstore.model.CartItem;
import com.petstore.model.User;
import com.petstore.service.CartService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 购物车模块的控制器
 * 处理添加、列表、更新、删除购物车项等请求
 * 映射路径：/cart/*
 */
@WebServlet("/cart/*")
public class CartServlet extends HttpServlet {

    private CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if ("/list".equals(pathInfo) || pathInfo == null || pathInfo.equals("/")) {
            // 购物车列表
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            
            List<CartItem> cartItems = cartService.getCartItems(user.getId());
            request.setAttribute("cartItems", cartItems);
            request.getRequestDispatcher("/cart/list.jsp").forward(request, response);
            
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
        
        if ("/add".equals(pathInfo)) {
            // 添加商品到购物车
            String productIdStr = request.getParameter("productId");
            String quantityStr = request.getParameter("quantity");
            
            if (productIdStr == null || quantityStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "参数不完整");
                return;
            }
            
            Long productId = Long.parseLong(productIdStr);
            int quantity = Integer.parseInt(quantityStr);
            
            if (quantity <= 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "数量无效");
                return;
            }
            
            boolean success = cartService.addCartItem(user.getId(), productId, quantity);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/cart/list");
            } else {
                request.setAttribute("error", "添加购物车失败");
                request.getRequestDispatcher("/product/detail.jsp?id=" + productId).forward(request, response);
            }
            
        } else if ("/update".equals(pathInfo)) {
            // 更新购物车商品数量
            String cartItemIdStr = request.getParameter("cartItemId");
            String quantityStr = request.getParameter("quantity");
            
            if (cartItemIdStr == null || quantityStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "参数不完整");
                return;
            }
            
            int cartItemId = Integer.parseInt(cartItemIdStr);
            int quantity = Integer.parseInt(quantityStr);
            
            if (quantity <= 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "数量无效");
                return;
            }
            
            cartService.updateCartItem(cartItemId, quantity);
            response.sendRedirect(request.getContextPath() + "/cart/list");
            
        } else if ("/delete".equals(pathInfo)) {
            // 删除购物车中的商品
            String cartItemIdStr = request.getParameter("cartItemId");
            
            if (cartItemIdStr == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少购物车项ID");
                return;
            }
            
            int cartItemId = Integer.parseInt(cartItemIdStr);
            cartService.deleteCartItem(cartItemId);
            response.sendRedirect(request.getContextPath() + "/cart/list");
            
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}