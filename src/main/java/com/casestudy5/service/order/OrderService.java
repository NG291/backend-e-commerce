package com.casestudy5.service.order;

import com.casestudy5.model.entity.cart.CartItem;

import com.casestudy5.model.entity.cart.Order;
import com.casestudy5.model.entity.cart.OrderItem;
import com.casestudy5.model.entity.cart.OrderStatus;
import com.casestudy5.model.entity.user.User;
import com.casestudy5.repo.ICartItemRepository;
import com.casestudy5.repo.IOrderItemRepository;
import com.casestudy5.repo.IOrderRepository;
import com.casestudy5.repo.IUserRepository;
import com.casestudy5.service.orderItem.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

    public List<Order> getOrdersForUser(Long userId) throws Exception {
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders.isEmpty()) {
            throw new Exception("No orders found for this user.");
        }
        return orders;
    }
    public List<Order> getOrdersForMerchant(Long sellerId) throws Exception {
        // Lấy tất cả OrderItem mà sản phẩm thuộc về nhà cung cấp này
        List<OrderItem> orderItems = orderItemRepository.findByProduct_UserId(sellerId);

        // Kiểm tra nếu không có đơn hàng nào liên quan đến nhà cung cấp này
        if (orderItems.isEmpty()) {
            throw new Exception("No orders found for this merchant.");
        }

        // Tạo danh sách các đơn hàng của nhà cung cấp
        Set<Order>  SellerOrders = new HashSet<>();

        // Lấy tất cả các Order từ OrderItem
        for (OrderItem orderItem : orderItems) {
            SellerOrders.add(orderItem.getOrder()); // Thêm đơn hàng vào danh sách
        }

        return new ArrayList<>(SellerOrders); // Chuyển đổi Set thành List
    }


}
