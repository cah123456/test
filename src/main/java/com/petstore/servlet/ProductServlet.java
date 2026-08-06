package com.petstore.servlet;

import com.petstore.model.Category;
import com.petstore.model.Product;
import com.petstore.service.ProductService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 商品模块的控制器
 * 处理商品列表、详情、搜索、分类列表等请求
 * 映射路径：/product/*
 */
@WebServlet("/product/*")
public class ProductServlet extends HttpServlet {

    private ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if ("/list".equals(pathInfo) || pathInfo == null || pathInfo.equals("/")) {
            // 商品列表
            String categoryIdStr = request.getParameter("categoryId");
            String pageStr = request.getParameter("page");
            
            Integer categoryId = null;
            if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
                categoryId = Integer.parseInt(categoryIdStr);
            }
            
            int page = 1;
            if (pageStr != null && !pageStr.isEmpty()) {
                page = Integer.parseInt(pageStr);
            }
            
            int pageSize = 12;
            List<Product> products = productService.getProductList(categoryId, page, pageSize);
            List<Category> categories = productService.getAllCategories();
            
            request.setAttribute("products", products);
            request.setAttribute("categories", categories);
            request.setAttribute("currentCategory", categoryId);
            request.setAttribute("currentPage", page);
            request.getRequestDispatcher("/product/list.jsp").forward(request, response);
            
        } else if ("/detail".equals(pathInfo)) {
            // 商品详情
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
            
            request.setAttribute("product", product);
            request.getRequestDispatcher("/product/detail.jsp").forward(request, response);
            
        } else if ("/search".equals(pathInfo)) {
            // 搜索商品
            String keyword = request.getParameter("keyword");
            if (keyword == null) {
                keyword = "";
            }
            
            List<Product> products = productService.searchProducts(keyword);
            request.setAttribute("products", products);
            request.setAttribute("keyword", keyword);
            request.getRequestDispatcher("/product/search.jsp").forward(request, response);
            
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}