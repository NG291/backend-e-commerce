package com.casestudy5.repo;

import com.casestudy5.model.entity.cart.Enum.OrderStatus;
import com.casestudy5.model.entity.cart.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderItemRepository extends JpaRepository<OrderItem,Long> {
    List<OrderItem> findByOrderUserId(Long userId);
    List<OrderItem> findByProduct_UserId(Long sellerId);
    List<OrderItem> findByOrderStatusAndProduct_UserId(OrderStatus status, Long sellerId);
}
