package com.casestudy5.repo;

import com.casestudy5.model.entity.cart.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderItemRepository extends JpaRepository<OrderItem,Long> {
}
