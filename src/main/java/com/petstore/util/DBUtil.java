package com.petstore.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 * 使用C3P0连接池管理数据库连接
 */
public class DBUtil {

    private static final ComboPooledDataSource dataSource = new ComboPooledDataSource();

    /**
     * 获取数据库连接
     * @return Connection 数据库连接
     * @throws SQLException 如果获取连接失败
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * 获取数据源
     * @return DataSource 数据源对象
     */
    public static DataSource getDataSource() {
        return dataSource;
    }

    /**
     * 关闭数据库连接（归还到连接池）
     * @param conn 数据库连接
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}