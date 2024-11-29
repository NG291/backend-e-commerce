package com.casestudy5.service.order;

import com.casestudy5.model.entity.cart.CartItem;

import com.casestudy5.model.entity.order.Order;
import com.casestudy5.model.entity.orderItem.OrderItem;
import com.casestudy5.model.entity.order.OrderStatus;
import com.casestudy5.model.entity.order.OrderDTO;
import com.casestudy5.model.entity.orderItem.OrderItemDTO;
import com.casestudy5.model.entity.image.Image;
import com.casestudy5.model.entity.notification.Notification;
import com.casestudy5.model.entity.product.Product;
import com.casestudy5.model.entity.user.User;
import com.casestudy5.repo.*;
import com.casestudy5.service.email.EmailService;
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
    private IProductRepository productRepository;
    @Autowired
    private ICartItemRepository cartItemRepository;
    @Autowired
    private INotificationRepository notificationRepository;
    @Autowired
    private EmailService emailService;

    public Order createOrder(Long userId) throws Exception {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            throw new Exception("No items in cart for the given user.");
        }
        BigDecimal totalAmount = cartItems.stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found with id: " + userId));

        Order order = new Order();
        order.setUser(user); // Trực tiếp gán userId
        order.setStatus(OrderStatus.PENDING); // Đơn hàng bắt đầu với trạng thái "PENDING"
        order.setTotalAmount(totalAmount);
        order.setOrderDate(LocalDateTime.now());

        return orderRepository.save(order);
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
                            imageUrl = "/images/" + firstImage.getFileName();
                        }

                        return new OrderItemDTO(
                                item.getProduct().getId(),
                                item.getProduct().getName(),
                                item.getQuantity(),
                                item.getPrice(),
                                imageUrl
                        );
                    })
                    .collect(Collectors.toList());

            BigDecimal totalAmountForSeller = orderItemDTOs.stream()
                    .map(item -> BigDecimal.valueOf(item.getQuantity()).multiply(item.getPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return new OrderDTO(
                    order.getId(),
                    totalAmountForSeller,
                    order.getOrderDate(),
                    order.getStatus(),
                    orderItemDTOs
            );
        }).collect(Collectors.toList());
    }

    public List<OrderDTO> getPendingOrdersForMerchant(Long sellerId) throws Exception {
        List<OrderItem> pendingOrderItems = orderItemRepository.findByOrderStatusAndProduct_UserId(OrderStatus.PENDING, sellerId);
        if (pendingOrderItems.isEmpty()) {
            throw new Exception("No pending orders found for this merchant.");
        }

        Set<Order> sellerPendingOrders = new HashSet<>();
        for (OrderItem orderItem : pendingOrderItems) {
            sellerPendingOrders.add(orderItem.getOrder());
        }

        return sellerPendingOrders.stream().map(order -> {
            String buyerName = order.getUser().getName();

            BigDecimal totalAmountForSeller = order.getOrderItems().stream()
                    .filter(item -> item.getProduct() != null
                            && item.getProduct().getUser() != null
                            && item.getProduct().getUser().getId().equals(sellerId))
                    .map(item -> BigDecimal.valueOf(item.getQuantity()).multiply(item.getPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return new OrderDTO(
                    order.getId(),
                    totalAmountForSeller,
                    order.getOrderDate(),
                    order.getStatus(),
                    buyerName
            );
        }).collect(Collectors.toList());
    }

    public List<OrderDTO> getPendingOrdersForUser(Long userId) throws Exception {
        List<OrderItem> pendingOrderItems = orderItemRepository.findByOrderStatusAndOrder_UserId(OrderStatus.PENDING, userId);

        if (pendingOrderItems.isEmpty()) {
            throw new Exception("No pending orders found for this user.");
        }

        Set<Order> userPendingOrders = new HashSet<>();
        for (OrderItem orderItem : pendingOrderItems) {
            userPendingOrders.add(orderItem.getOrder());
        }

        return userPendingOrders.stream().map(order -> {
            String sellerName = order.getUser().getName();

            return new OrderDTO(
                    order.getId(),
                    order.getTotalAmount(),
                    order.getOrderDate(),
                    order.getStatus(),
                    sellerName
            );
        }).collect(Collectors.toList());
    }


    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        orderRepository.save(order);

        if (status == OrderStatus.SUCCESS) {
            for (OrderItem orderItem : order.getOrderItems()) {
                Product product = orderItem.getProduct();

                int newQuantity = product.getQuantity() - orderItem.getQuantity();
                if (newQuantity < 0) {
                    throw new RuntimeException("Not enough stock for product: " + product.getName());
                }

                product.setQuantity(newQuantity);
                productRepository.save(product);
            }
        }
    }
    public void updateOrderStatus(Long orderId, OrderStatus status, String rejectionReason) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == OrderStatus.REJECT) {
            return; // Dừng lại
        }

        order.setStatus(status);
        orderRepository.save(order);

        if (status == OrderStatus.REJECT) {
            Notification notification = new Notification();
            notification.setUser(order.getUser());
            notification.setMessage("Đơn hàng của bạn đã bị từ chối. Lý do: " + rejectionReason);
            notificationRepository.save(notification);

            sendNotificationToCustomer(notification);
        }
    }

    private void sendNotificationToCustomer(Notification notification) {
        String emailContent = notification.getMessage();
        String recipientEmail = notification.getUser().getEmail();
        emailService.sendEmail(recipientEmail, "Thông báo từ hệ thống", emailContent);
    }


}
