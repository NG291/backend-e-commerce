package com.casestudy5.repo;

import com.casestudy5.model.entity.cart.Enum.OrderStatus;
import com.casestudy5.model.entity.cart.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByStatus(OrderStatus status);
}
