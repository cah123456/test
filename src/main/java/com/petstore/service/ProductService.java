package com.petstore.service;

import com.petstore.dao.CategoryDao;
import com.petstore.dao.ProductDao;
import com.petstore.model.Category;
import com.petstore.model.Product;
import java.util.List;

/**
 * 商品业务逻辑服务
 * 处理商品和分类的查询、管理
 */
public class ProductService {

    private ProductDao productDao = new ProductDao();
    private CategoryDao categoryDao = new CategoryDao();

    /**
     * 获取商品列表，支持按分类筛选和分页
     * @param categoryId 分类ID，为null时查询所有
     * @param page 页码（从1开始）
     * @param pageSize 每页数量
     * @return 商品列表
     */
    public List<Product> getProductList(Integer categoryId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return productDao.findAll(categoryId, offset, pageSize);
    }

    /**
     * 根据商品ID获取商品详情
     * @param productId 商品ID
     * @return 商品对象
     */
    public Product getProductById(int productId) {
        return productDao.findById(productId);
    }

    /**
     * 根据关键字搜索商品
     * @param keyword 关键字
     * @return 商品列表
     */
    public List<Product> searchProducts(String keyword) {
        return productDao.search(keyword);
    }

    /**
     * 获取所有商品分类
     * @return 分类列表
     */
    public List<Category> getAllCategories() {
        return categoryDao.findAll();
    }

    /**
     * 新增商品
     * @param product 商品对象
     * @return 成功返回true，失败返回false
     */
    public boolean addProduct(Product product) {
        return productDao.insert(product) > 0;
    }

    /**
     * 更新商品信息
     * @param product 商品对象
     * @return 成功返回true，失败返回false
     */
    public boolean updateProduct(Product product) {
        return productDao.update(product) > 0;
    }

    /**
     * 删除商品
     * @param productId 商品ID
     * @return 成功返回true，失败返回false
     */
    public boolean deleteProduct(int productId) {
        return productDao.deleteById(productId) > 0;
    }
}