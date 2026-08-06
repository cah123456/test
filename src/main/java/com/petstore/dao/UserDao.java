package com.petstore.dao;

import com.petstore.model.User;
import com.petstore.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据访问对象
 * 封装用户表的JDBC操作
 */
public class UserDao {

    /**
     * 插入用户记录
     * @param user 用户对象
     * @return 自增ID，失败返回-1
     */
    public int insert(User user) {
        String sql = "INSERT INTO user (username, password, nickname, email, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getNickname());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getRole());
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
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象，不存在返回null
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据用户ID查询用户
     * @param userId 用户ID
     * @return 用户对象，不存在返回null
     */
    public User findById(int userId) {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从ResultSet中提取用户对象
     */
    private User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setNickname(rs.getString("nickname"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }
}