package com.casestudy5.service.product;

import com.casestudy5.model.entity.order.Order;
import com.casestudy5.model.entity.orderItem.OrderItem;
import com.casestudy5.model.entity.product.Product;
import com.casestudy5.model.entity.product.ProductRevenueDTO;
import com.casestudy5.repo.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class RevenueService {

    @Autowired
    private IOrderRepository orderRepository;


    public List<ProductRevenueDTO> getRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = orderRepository.findOrdersByDateRange(startDate, endDate);
        List<ProductRevenueDTO> revenueData = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateRange = startDate.format(formatter) + " - " + endDate.format(formatter);

        for (Order order : orders) {
            for (OrderItem orderItem : order.getOrderItems()) {
                Product product = orderItem.getProduct();
                BigDecimal totalRevenue = orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));

                // Create ProductRevenueDTO including the orderId
                ProductRevenueDTO dto = new ProductRevenueDTO(
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        orderItem.getQuantity(),
                        totalRevenue,
                        dateRange,
                        order.getId(),
                        order.getOrderDate()
                        // Include the orderId
                );
                revenueData.add(dto);
            }
        }
        return revenueData;
    }
}
