package com.petstore.service;

import com.petstore.dao.UserDao;
import com.petstore.model.User;
import com.petstore.util.PasswordUtil;

/**
 * 用户业务逻辑服务
 * 处理注册、登录、密码加密、会话管理等
 */
public class UserService {

    private UserDao userDao = new UserDao();

    /**
     * 注册新用户
     * @param user 用户对象（包含用户名、密码、昵称、邮箱）
     * @return 注册成功返回true，失败返回false
     */
    public boolean register(User user) {
        // 检查用户名是否已存在
        User existingUser = userDao.findByUsername(user.getUsername());
        if (existingUser != null) {
            return false;
        }
        
        // 生成盐值并加密密码
        String salt = PasswordUtil.generateSalt();
        String encryptedPassword = PasswordUtil.encryptPassword(user.getPassword(), salt);
        // 存储格式：加密密码:盐值
        user.setPassword(encryptedPassword + ":" + salt);
        
        // 设置默认角色
        user.setRole("user");
        
        // 插入数据库
        int result = userDao.insert(user);
        return result > 0;
    }

    /**
     * 用户登录验证
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回User对象，失败返回null
     */
    public User login(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            return null;
        }
        
        // 从存储的密码中提取加密密码和盐值
        String storedPassword = user.getPassword();
        if (storedPassword == null || !storedPassword.contains(":")) {
            return null;
        }
        String[] parts = storedPassword.split(":");
        String encryptedPassword = parts[0];
        String salt = parts[1];
        
        // 验证密码
        if (PasswordUtil.verifyPassword(password, salt, encryptedPassword)) {
            // 清除密码信息，返回用户对象
            user.setPassword(null);
            return user;
        }
        return null;
    }

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户对象
     */
    public User getUserById(int userId) {
        User user = userDao.findById(userId);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户对象
     */
    public User getUserByUsername(String username) {
        User user = userDao.findByUsername(username);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }
}