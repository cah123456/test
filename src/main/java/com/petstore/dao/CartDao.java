package com.petstore.dao;

import com.petstore.model.CartItem;
import com.petstore.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车数据访问对象
 * 封装购物车项表的JDBC操作
 */
public class CartDao {

    /**
     * 插入购物车项记录
     * @param cartItem 购物车项对象
     * @return 自增ID，失败返回-1
     */
    public int insert(CartItem cartItem) {
        String sql = "INSERT INTO cart_item (user_id, product_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, cartItem.getUserId());
            ps.setLong(2, cartItem.getProductId());
            ps.setInt(3, cartItem.getQuantity());
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
     * 根据用户ID查询购物车列表
     * @param userId 用户ID
     * @return 购物车项列表
     */
    public List<CartItem> findByUserId(Long userId) {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT ci.*, p.name as product_name, p.image_url as product_image_url, p.price as product_price " +
                     "FROM cart_item ci LEFT JOIN product p ON ci.product_id = p.id WHERE ci.user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cartItems.add(extractCartItem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    /**
     * 更新购物车项数量
     * @param cartItemId 购物车项ID
     * @param quantity 新数量
     * @return 影响行数
     */
    public int updateQuantity(int cartItemId, int quantity) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, cartItemId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据购物车项ID删除记录
     * @param cartItemId 购物车项ID
     * @return 影响行数
     */
    public int deleteById(int cartItemId) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartItemId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 删除用户所有购物车项（下单后清空）
     * @param userId 用户ID
     * @return 影响行数
     */
    public int deleteByUserId(Long userId) {
        String sql = "DELETE FROM cart_item WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 从ResultSet中提取购物车项对象
     */
    private CartItem extractCartItem(ResultSet rs) throws SQLException {
        CartItem cartItem = new CartItem();
        cartItem.setId(rs.getLong("id"));
        cartItem.setUserId(rs.getLong("user_id"));
        cartItem.setProductId(rs.getLong("product_id"));
        cartItem.setQuantity(rs.getInt("quantity"));
        cartItem.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            cartItem.setProductName(rs.getString("product_name"));
            cartItem.setProductImageUrl(rs.getString("product_image_url"));
            cartItem.setProductPrice(rs.getBigDecimal("product_price"));
        } catch (SQLException e) {
            // 如果没有关联查询，忽略
        }
        return cartItem;
    }
}