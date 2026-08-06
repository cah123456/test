package com.petstore.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * 密码加密工具类
 * 使用MD5加盐方式对密码进行加密
 */
public class PasswordUtil {

    /**
     * 生成随机盐值
     * @return 随机盐值字符串
     */
    public static String generateSalt() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append((char) ('a' + random.nextInt(26)));
        }
        return sb.toString();
    }

    /**
     * 对密码进行MD5加盐加密
     * @param password 原始密码
     * @param salt 盐值
     * @return 加密后的密码字符串
     */
    public static String encryptPassword(String password, String salt) {
        String saltedPassword = password + salt;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(saltedPassword.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5加密算法不可用", e);
        }
    }

    /**
     * 验证密码
     * @param inputPassword 输入的密码
     * @param salt 盐值
     * @param encryptedPassword 数据库中存储的加密密码
     * @return 是否匹配
     */
    public static boolean verifyPassword(String inputPassword, String salt, String encryptedPassword) {
        String encryptedInput = encryptPassword(inputPassword, salt);
        return encryptedInput.equals(encryptedPassword);
    }
}