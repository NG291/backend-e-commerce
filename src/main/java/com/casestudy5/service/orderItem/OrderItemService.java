package com.casestudy5.service.orderItem;

import com.casestudy5.model.entity.cart.CartItem;
import com.casestudy5.model.entity.order.Order;
import com.casestudy5.model.entity.orderItem.OrderItem;
import com.casestudy5.repo.ICartItemRepository;
import com.casestudy5.repo.IOrderItemRepository;
import com.casestudy5.repo.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {

    @Autowired
    private IOrderItemRepository orderItemRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private ICartItemRepository cartItemRepository;

    @Transactional
    public List<OrderItem> createOrderItems(Long orderId, Long UserId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found."));

        List<CartItem> cartItems = cartItemRepository.findByUserId(UserId);

        if (cartItems.isEmpty()) {
            throw new Exception("No items in cart for the given user.");
        }

        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getProduct().getPrice());
                    return orderItem;
                }).collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);
        return orderItems;
    }

    public List<OrderItem> getOrderItemsForUser(Long userId) {
        return orderItemRepository.findByOrderUserId(userId);
    }
}
