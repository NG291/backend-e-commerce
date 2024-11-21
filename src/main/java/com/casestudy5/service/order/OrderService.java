package com.casestudy5.service.order;

import com.casestudy5.model.entity.cart.CartItem;

import com.casestudy5.model.entity.cart.Order;
import com.casestudy5.model.entity.cart.OrderItem;
import com.casestudy5.model.entity.cart.Enum.OrderStatus;
import com.casestudy5.model.entity.cart.dto.OrderDTO;
import com.casestudy5.model.entity.cart.dto.OrderItemDTO;
import com.casestudy5.model.entity.image.Image;
import com.casestudy5.model.entity.user.User;
import com.casestudy5.repo.ICartItemRepository;
import com.casestudy5.repo.IOrderItemRepository;
import com.casestudy5.repo.IOrderRepository;
import com.casestudy5.repo.IUserRepository;
import com.casestudy5.service.OrderItem.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class OrderService {

    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IOrderItemRepository orderItemRepository;
    @Autowired
    private ICartItemRepository cartItemRepository;
    @Autowired
    private OrderItemService orderItemService;
    public Order createOrder(Long userId) throws Exception {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            throw new Exception("No items in cart for the given user.");
        }

        // Tính tổng số tiền
        BigDecimal totalAmount = cartItems.stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found with id: " + userId));

        // Tạo đơn hàng
        Order order = new Order();
        order.setUser(user); // Trực tiếp gán userId
        order.setStatus(OrderStatus.PENDING); // Đơn hàng bắt đầu với trạng thái "PENDING"
        order.setTotalAmount(totalAmount);
        order.setOrderDate(LocalDateTime.now());

        // Lưu đơn hàng vào cơ sở dữ liệu
        return orderRepository.save(order);
    }
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
    }

    public List<OrderDTO> getOrdersForUser(Long userId) throws Exception {
        List<Order> orders = orderRepository.findByUserId(userId);

        if (orders.isEmpty()) {
            throw new Exception("No orders found for this user.");
        }

        return orders.stream().map(order -> {
            List<OrderItemDTO> orderItems = order.getOrderItems().stream()
                    .map(item -> {

                        List<Image> images = item.getProduct().getImages();

                        List<String> imageUrls = images.stream()
                                .map(image -> image.getImageUrl())
                                .collect(Collectors.toList());

                        String firstImage = (imageUrls != null && !imageUrls.isEmpty()) ? imageUrls.get(0) : null;

                        return new OrderItemDTO(
                                item.getProduct().getId(),
                                item.getProduct().getName(),
                                item.getQuantity(),
                                item.getPrice(),
                                firstImage
                        );
                    })
                    .toList();

            // Chuyển đổi Order sang OrderDTO
            return new OrderDTO(
                    order.getId(),
                    order.getTotalAmount(),
                    order.getOrderDate(),
                    order.getStatus(),
                    orderItems
            );
        }).toList();
    }


    public List<OrderDTO> getOrdersForMerchant(Long sellerId) throws Exception {
        List<OrderItem> orderItems = orderItemRepository.findByProduct_UserId(sellerId);

        if (orderItems.isEmpty()) {
            throw new Exception("No orders found for this merchant.");
        }

        Set<Order> sellerOrders = new HashSet<>();
        for (OrderItem orderItem : orderItems) {
            sellerOrders.add(orderItem.getOrder());
        }

        return sellerOrders.stream().map(order -> {
            List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream()
                    .filter(item -> {
                        return item.getProduct() != null && item.getProduct().getUser() != null
                                && item.getProduct().getUser().getId() != null
                                && item.getProduct().getUser().getId().equals(sellerId);
                    })
                    .map(item -> {
                        String imageUrl = null;
                        if (!item.getProduct().getImages().isEmpty()) {
                            Image firstImage = item.getProduct().getImages().get(0);
                            imageUrl =  "/images/"  + firstImage.getFileName();  // Hoặc sử dụng phương thức getImageUrl()
                        }

                        return new OrderItemDTO(
                                item.getProduct().getId(),
                                item.getProduct().getName(),
                                item.getQuantity(),
                                item.getPrice(),
                                imageUrl  // Sử dụng imageUrl
                        );
                    })
                    .collect(Collectors.toList());

            return new OrderDTO(
                    order.getId(),
                    order.getTotalAmount(),
                    order.getOrderDate(),
                    order.getStatus(),
                    orderItemDTOs
            );
        }).collect(Collectors.toList());
    }
}
