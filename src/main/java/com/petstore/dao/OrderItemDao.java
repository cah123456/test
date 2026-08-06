package com.petstore.dao;

import com.petstore.model.OrderItem;
import com.petstore.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单项数据访问对象
 * 封装订单项表的JDBC操作
 */
public class OrderItemDao {

    /**
     * 批量插入订单项记录
     * @param orderItems 订单项列表
     * @return 每行影响行数数组
     */
    public int[] insertBatch(List<OrderItem> orderItems) {
        String sql = "INSERT INTO order_item (order_id, product_id, product_name, product_price, quantity, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (OrderItem item : orderItems) {
                ps.setLong(1, item.getOrderId());
                ps.setLong(2, item.getProductId());
                ps.setString(3, item.getProductName());
                ps.setBigDecimal(4, item.getProductPrice());
                ps.setInt(5, item.getQuantity());
                ps.setBigDecimal(6, item.getSubtotal());
                ps.addBatch();
            }
            return ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new int[0];
    }

    /**
     * 根据订单ID查询订单项列表
     * @param orderId 订单ID
     * @return 订单项列表
     */
    public List<OrderItem> findByOrderId(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM order_item WHERE order_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orderItems.add(extractOrderItem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItems;
    }

    /**
     * 从ResultSet中提取订单项对象
     */
    private OrderItem extractOrderItem(ResultSet rs) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(rs.getLong("id"));
        orderItem.setOrderId(rs.getLong("order_id"));
        orderItem.setProductId(rs.getLong("product_id"));
        orderItem.setProductName(rs.getString("product_name"));
        orderItem.setProductPrice(rs.getBigDecimal("product_price"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setSubtotal(rs.getBigDecimal("subtotal"));
        return orderItem;
    }
}