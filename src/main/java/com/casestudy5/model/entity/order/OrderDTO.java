package com.casestudy5.model.entity.order;

import com.casestudy5.model.entity.orderItem.OrderItemDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    private Long id;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private List<OrderItemDTO> orderItems;
    private String buyerName;

    // Constructor có tham số
    public OrderDTO(Long id, BigDecimal totalAmount, LocalDateTime orderDate, OrderStatus status, List<OrderItemDTO> orderItems, String buyerName) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
        this.orderItems = orderItems;
        this.buyerName = buyerName;
    }
    public OrderDTO(Long id, BigDecimal totalAmount, LocalDateTime orderDate, OrderStatus status, List<OrderItemDTO> orderItems) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
        this.orderItems = orderItems;
    }
    public OrderDTO(Long id, BigDecimal totalAmount, LocalDateTime orderDate, OrderStatus status, String buyerName) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
        this.buyerName = buyerName;
    }

    // Getters và setters (nếu cần)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }
}

