package com.petstore.dao;

import com.petstore.model.Product;
import com.petstore.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品数据访问对象
 * 封装商品表的JDBC操作
 */
public class ProductDao {

    /**
     * 查询商品列表，支持按分类筛选和分页
     * @param categoryId 分类ID，为null时查询所有
     * @param offset 偏移量
     * @param limit 每页数量
     * @return 商品列表
     */
    public List<Product> findAll(Integer categoryId, int offset, int limit) {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT p.*, c.name as category_name FROM product p LEFT JOIN category c ON p.category_id = c.id WHERE 1=1");
        if (categoryId != null) {
            sql.append(" AND p.category_id = ?");
        }
        sql.append(" ORDER BY p.created_at DESC LIMIT ?, ?");
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (categoryId != null) {
                ps.setInt(paramIndex++, categoryId);
            }
            ps.setInt(paramIndex++, offset);
            ps.setInt(paramIndex, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                products.add(extractProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * 根据商品ID查询商品
     * @param productId 商品ID
     * @return 商品对象，不存在返回null
     */
    public Product findById(int productId) {
        String sql = "SELECT p.*, c.name as category_name FROM product p LEFT JOIN category c ON p.category_id = c.id WHERE p.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractProduct(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据关键字搜索商品
     * @param keyword 关键字
     * @return 商品列表
     */
    public List<Product> search(String keyword) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.name as category_name FROM product p LEFT JOIN category c ON p.category_id = c.id WHERE p.name LIKE ? OR p.description LIKE ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String likePattern = "%" + keyword + "%";
            ps.setString(1, likePattern);
            ps.setString(2, likePattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                products.add(extractProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * 插入商品记录
     * @param product 商品对象
     * @return 自增ID，失败返回-1
     */
    public int insert(Product product) {
        String sql = "INSERT INTO product (name, description, price, stock, image_url, breed, age, status, category_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setBigDecimal(3, product.getPrice());
            ps.setInt(4, product.getStock());
            ps.setString(5, product.getImageUrl());
            ps.setString(6, product.getBreed());
            if (product.getAge() != null) {
                ps.setInt(7, product.getAge());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            ps.setString(8, product.getStock() > 0 ? "有货" : "缺货");
            ps.setLong(9, product.getCategoryId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 更新商品记录
     * @param product 商品对象
     * @return 影响行数
     */
    public int update(Product product) {
        String sql = "UPDATE product SET name=?, description=?, price=?, stock=?, image_url=?, breed=?, age=?, status=?, category_id=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setBigDecimal(3, product.getPrice());
            ps.setInt(4, product.getStock());
            ps.setString(5, product.getImageUrl());
            ps.setString(6, product.getBreed());
            if (product.getAge() != null) {
                ps.setInt(7, product.getAge());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            ps.setString(8, product.getStock() > 0 ? "有货" : "缺货");
            ps.setLong(9, product.getCategoryId());
            ps.setLong(10, product.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据商品ID删除商品
     * @param productId 商品ID
     * @return 影响行数
     */
    public int deleteById(int productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 从ResultSet中提取商品对象
     */
    private Product extractProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setStock(rs.getInt("stock"));
        product.setImageUrl(rs.getString("image_url"));
        product.setBreed(rs.getString("breed"));
        product.setAge(rs.getObject("age", Integer.class));
        product.setStatus(rs.getString("status"));
        product.setCategoryId(rs.getLong("category_id"));
        product.setCreatedAt(rs.getTimestamp("created_at"));
        product.setUpdatedAt(rs.getTimestamp("updated_at"));
        try {
            product.setCategoryName(rs.getString("category_name"));
        } catch (SQLException e) {
            // 如果没有关联查询category_name，忽略
        }
        return product;
    }
}