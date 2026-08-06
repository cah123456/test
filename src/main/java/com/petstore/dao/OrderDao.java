package com.petstore.dao;

import com.petstore.model.Order;
import com.petstore.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单数据访问对象
 * 封装订单表的JDBC操作
 */
public class OrderDao {

    /**
     * 插入订单记录
     * @param order 订单对象
     * @return 自增ID，失败返回-1
     */
    public int insert(Order order) {
        String sql = "INSERT INTO order_info (order_number, user_id, total_amount, status, shipping_address, contact_phone) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, order.getOrderNumber());
            ps.setLong(2, order.getUserId());
            ps.setBigDecimal(3, order.getTotalAmount());
            ps.setString(4, order.getStatus());
            ps.setString(5, order.getShippingAddress());
            ps.setString(6, order.getContactPhone());
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
     * 根据用户ID查询订单列表，支持按状态筛选
     * @param userId 用户ID
     * @param status 订单状态，为null时查询所有
     * @return 订单列表
     */
    public List<Order> findByUserId(Long userId, String status) {
        List<Order> orders = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM order_info WHERE user_id = ?");
        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
        }
        sql.append(" ORDER BY created_at DESC");
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            ps.setLong(1, userId);
            if (status != null && !status.isEmpty()) {
                ps.setString(2, status);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orders.add(extractOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * 根据订单ID查询订单
     * @param orderId 订单ID
     * @return 订单对象，不存在返回null
     */
    public Order findById(int orderId) {
        String sql = "SELECT o.*, u.username FROM order_info o LEFT JOIN user u ON o.user_id = u.id WHERE o.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractOrderWithUsername(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询所有订单（管理员）
     * @return 订单列表
     */
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.username FROM order_info o LEFT JOIN user u ON o.user_id = u.id ORDER BY o.created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                orders.add(extractOrderWithUsername(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * 更新订单状态
     * @param orderId 订单ID
     * @param status 新状态
     * @return 影响行数
     */
    public int updateStatus(int orderId, String status) {
        String sql = "UPDATE order_info SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 从ResultSet中提取订单对象
     */
    private Order extractOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setOrderNumber(rs.getString("order_number"));
        order.setUserId(rs.getLong("user_id"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setStatus(rs.getString("status"));
        order.setShippingAddress(rs.getString("shipping_address"));
        order.setContactPhone(rs.getString("contact_phone"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        order.setUpdatedAt(rs.getTimestamp("updated_at"));
        return order;
    }

    /**
     * 从ResultSet中提取订单对象（包含用户名）
     */
    private Order extractOrderWithUsername(ResultSet rs) throws SQLException {
        Order order = extractOrder(rs);
        try {
            order.setUsername(rs.getString("username"));
        } catch (SQLException e) {
            // 如果没有关联查询username，忽略
        }
        return order;
    }
}