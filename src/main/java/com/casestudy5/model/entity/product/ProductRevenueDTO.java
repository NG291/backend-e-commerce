package com.casestudy5.model.entity.product;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonPropertyOrder({"orderId", "productName", "description", "price", "quantitySold", "totalRevenue", "dateRange","orderDate"})
public class ProductRevenueDTO {
    private Long orderId;
    private String productName;
    private String description;
    private BigDecimal price;
    private int quantitySold;
    private BigDecimal totalRevenue;
    private String dateRange;
    private LocalDateTime orderDate;

    public ProductRevenueDTO(String productName, String description, BigDecimal price, int quantitySold, BigDecimal totalRevenue, String dateRange, Long orderId, LocalDateTime orderDate) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.quantitySold = quantitySold;
        this.totalRevenue = totalRevenue;
        this.dateRange = dateRange;
        this.orderId = orderId;
        this.orderDate= orderDate;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
