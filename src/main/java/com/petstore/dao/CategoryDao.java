package com.petstore.dao;

import com.petstore.model.Category;
import com.petstore.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类数据访问对象
 * 封装分类表的JDBC操作
 */
public class CategoryDao {

    /**
     * 查询所有分类
     * @return 分类列表
     */
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM category ORDER BY id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categories.add(extractCategory(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    /**
     * 根据分类ID查询分类
     * @param categoryId 分类ID
     * @return 分类对象，不存在返回null
     */
    public Category findById(int categoryId) {
        String sql = "SELECT * FROM category WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractCategory(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从ResultSet中提取分类对象
     */
    private Category extractCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getLong("id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        return category;
    }
}