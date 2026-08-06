package com.petstore.service;

import com.petstore.dao.CartDao;
import com.petstore.model.CartItem;
import java.util.List;

/**
 * 购物车业务逻辑服务
 * 管理购物车项的增删改查
 */
public class CartService {

    private CartDao cartDao = new CartDao();

    /**
     * 添加商品到购物车
     * @param userId 用户ID
     * @param productId 商品ID
     * @param quantity 数量
     * @return 成功返回true，失败返回false
     */
    public boolean addCartItem(Long userId, Long productId, int quantity) {
        CartItem cartItem = new CartItem(userId, productId, quantity);
        return cartDao.insert(cartItem) > 0;
    }

    /**
     * 获取用户购物车列表
     * @param userId 用户ID
     * @return 购物车项列表
     */
    public List<CartItem> getCartItems(Long userId) {
        return cartDao.findByUserId(userId);
    }

    /**
     * 修改购物车商品数量
     * @param cartItemId 购物车项ID
     * @param quantity 新数量
     * @return 成功返回true，失败返回false
     */
    public boolean updateCartItem(int cartItemId, int quantity) {
        return cartDao.updateQuantity(cartItemId, quantity) > 0;
    }

    /**
     * 删除购物车中的商品
     * @param cartItemId 购物车项ID
     * @return 成功返回true，失败返回false
     */
    public boolean deleteCartItem(int cartItemId) {
        return cartDao.deleteById(cartItemId) > 0;
    }
}