package com.petstore.service;

import com.petstore.dao.CartDao;
import com.petstore.dao.OrderDao;
import com.petstore.dao.OrderItemDao;
import com.petstore.dao.ProductDao;
import com.petstore.model.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 订单业务逻辑服务
 * 处理订单创建、查询、状态管理
 */
public class OrderService {

    private OrderDao orderDao = new OrderDao();
    private OrderItemDao orderItemDao = new OrderItemDao();
    private CartDao cartDao = new CartDao();
    private ProductDao productDao = new ProductDao();

    /**
     * 创建订单，从购物车获取商品并生成订单项
     * @param userId 用户ID
     * @param shippingAddress 收货地址
     * @param contactPhone 联系电话
     * @return 订单ID，失败返回-1
     */
    public int createOrder(Long userId, String shippingAddress, String contactPhone) {
        // 获取用户购物车列表
        List<CartItem> cartItems = cartDao.findByUserId(userId);
        if (cartItems == null || cartItems.isEmpty()) {
            return -1;
        }

        // 检查库存并计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (CartItem cartItem : cartItems) {
            Product product = productDao.findById(cartItem.getProductId().intValue());
            if (product == null || product.getStock() < cartItem.getQuantity()) {
                return -1; // 库存不足
            }
            
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalAmount = totalAmount.add(subtotal);
            
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setSubtotal(subtotal);
            orderItems.add(orderItem);
        }

        // 生成订单号
        String orderNumber = generateOrderNumber();

        // 创建订单
        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus("待支付");
        order.setShippingAddress(shippingAddress);
        order.setContactPhone(contactPhone);

        int orderId = orderDao.insert(order);
        if (orderId <= 0) {
            return -1;
        }

        // 批量插入订单项
        for (OrderItem item : orderItems) {
            item.setOrderId((long) orderId);
        }
        orderItemDao.insertBatch(orderItems);

        // 扣减库存
        for (CartItem cartItem : cartItems) {
            Product product = productDao.findById(cartItem.getProductId().intValue());
            if (product != null) {
                product.setStock(product.getStock() - cartItem.getQuantity());
                productDao.update(product);
            }
        }

        // 清空购物车
        cartDao.deleteByUserId(userId);

        return orderId;
    }

    /**
     * 获取用户订单列表，支持按状态筛选
     * @param userId 用户ID
     * @param status 订单状态，为null时查询所有
     * @return 订单列表
     */
    public List<Order> getOrdersByUserId(Long userId, String status) {
        return orderDao.findByUserId(userId, status);
    }

    /**
     * 根据订单ID获取订单详情
     * @param orderId 订单ID
     * @return 订单对象（包含订单项）
     */
    public Order getOrderById(int orderId) {
        Order order = orderDao.findById(orderId);
        if (order != null) {
            List<OrderItem> orderItems = orderItemDao.findByOrderId(orderId);
            order.setOrderItems(orderItems);
        }
        return order;
    }

    /**
     * 获取所有订单列表（管理员）
     * @return 订单列表
     */
    public List<Order> getAllOrders() {
        return orderDao.findAll();
    }

    /**
     * 更新订单状态
     * @param orderId 订单ID
     * @param status 新状态
     * @return 成功返回true，失败返回false
     */
    public boolean updateOrderStatus(int orderId, String status) {
        return orderDao.updateStatus(orderId, status) > 0;
    }

    /**
     * 生成订单号
     * 格式：年月日时分秒+4位随机数
     */
    private String generateOrderNumber() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = sdf.format(new Date());
        Random random = new Random();
        int randomNum = 1000 + random.nextInt(9000);
        return "ORD" + timestamp + randomNum;
    }
}