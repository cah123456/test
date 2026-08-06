package com.petstore.model;

import java.math.BigDecimal;

/**
 * 订单项实体类
 * 对应数据库中的order_item表
 */
public class OrderItem {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;
    private BigDecimal subtotal;

    public OrderItem() {
    }

    public OrderItem(Long orderId, Long productId, String productName, BigDecimal productPrice, Integer quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.subtotal = productPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                '}';
    }
}