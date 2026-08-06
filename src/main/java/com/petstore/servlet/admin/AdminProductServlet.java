package com.petstore.servlet.admin;

import com.petstore.model.Category;
import com.petstore.model.Product;
import com.petstore.service.ProductService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * 后台管理商品模块的控制器
 * 处理商品列表、新增、编辑、删除等请求
 * 映射路径：/admin/product/*
 */
@WebServlet("/admin/product/*")
public class AdminProductServlet extends HttpServlet {

    private ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if ("/list".equals(pathInfo) || pathInfo == null || pathInfo.equals("/")) {
            // 商品列表
            String pageStr = request.getParameter("page");
            int page = 1;
            if (pageStr != null && !pageStr.isEmpty()) {
                page = Integer.parseInt(pageStr);
            }
            
            List<Product> products = productService.getProductList(null, page, 20);
            List<Category> categories = productService.getAllCategories();
            
            request.setAttribute("products", products);
            request.setAttribute("categories", categories);
            request.setAttribute("currentPage", page);
            request.getRequestDispatcher("/admin/product/list.jsp").forward(request, response);
            
        } else if ("/add".equals(pathInfo)) {
            // 新增商品页面
            List<Category> categories = productService.getAllCategories();
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/admin/product/add.jsp").forward(request, response);
            
        } else if ("/edit".equals(pathInfo)) {
            // 编辑商品页面
            String productIdStr = request.getParameter("id");
            if (productIdStr == null || productIdStr.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少商品ID");
                return;
            }
            
            int productId = Integer.parseInt(productIdStr);
            Product product = productService.getProductById(productId);
            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "商品不存在");
                return;
            }
            
            List<Category> categories = productService.getAllCategories();
            request.setAttribute("product", product);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/admin/product/edit.jsp").forward(request, response);
            
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if ("/add".equals(pathInfo)) {
            // 新增商品
            Product product = extractProductFromRequest(request);
            boolean success = productService.addProduct(product);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/product/list");
            } else {
                request.setAttribute("error", "新增商品失败");
                List<Category> categories = productService.getAllCategories();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/admin/product/add.jsp").forward(request, response);
            }
            
        } else if ("/edit".equals(pathInfo)) {
            // 编辑商品
            String productIdStr = request.getParameter("id");
            if (productIdStr == null || productIdStr.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少商品ID");
                return;
            }
            
            Product product = extractProductFromRequest(request);
            product.setId(Long.parseLong(productIdStr));
            boolean success = productService.updateProduct(product);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/product/list");
            } else {
                request.setAttribute("error", "编辑商品失败");
                List<Category> categories = productService.getAllCategories();
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("/admin/product/edit.jsp").forward(request, response);
            }
            
        } else if ("/delete".equals(pathInfo)) {
            // 删除商品
            String productIdStr = request.getParameter("id");
            if (productIdStr == null || productIdStr.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "缺少商品ID");
                return;
            }
            
            int productId = Integer.parseInt(productIdStr);
            productService.deleteProduct(productId);
            response.sendRedirect(request.getContextPath() + "/admin/product/list");
            
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * 从请求中提取商品对象
     */
    private Product extractProductFromRequest(HttpServletRequest request) {
        Product product = new Product();
        product.setName(request.getParameter("name"));
        product.setDescription(request.getParameter("description"));
        product.setPrice(new BigDecimal(request.getParameter("price")));
        product.setStock(Integer.parseInt(request.getParameter("stock")));
        product.setImageUrl(request.getParameter("imageUrl"));
        product.setBreed(request.getParameter("breed"));
        String ageStr = request.getParameter("age");
        if (ageStr != null && !ageStr.isEmpty()) {
            product.setAge(Integer.parseInt(ageStr));
        }
        product.setCategoryId(Long.parseLong(request.getParameter("categoryId")));
        return product;
    }
}