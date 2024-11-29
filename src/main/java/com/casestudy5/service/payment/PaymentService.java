package com.casestudy5.service.payment;

import com.casestudy5.model.entity.cart.*;
import com.casestudy5.model.entity.notification.Notification;
import com.casestudy5.model.entity.payment.PaymentMethodStatus;
import com.casestudy5.model.entity.payment.PaymentStatus;
import com.casestudy5.model.entity.order.Order;
import com.casestudy5.model.entity.payment.Payment;
import com.casestudy5.model.entity.user.User;
import com.casestudy5.repo.*;
import com.casestudy5.service.email.IEmailService;
import com.casestudy5.service.orderItem.OrderItemService;
import com.casestudy5.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private ICartItemRepository cartItemRepository;

    @Autowired
    private IPaymentRepository paymentRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private INotificationRepository notificationRepository;
    @Autowired
    private IEmailService emailService;

    @Transactional
    public Payment processPayment(Long userId, double paymentAmount, PaymentMethodStatus paymentMethod) throws Exception {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new Exception("Your cart is empty.");
        }
        BigDecimal totalAmount = cartItems.stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (BigDecimal.valueOf(paymentAmount).compareTo(totalAmount) != 0) {
            throw new Exception("Payment amount does not match the total amount.");
        }
        Order order = orderService.createOrder(userId);

        orderItemService.createOrderItems(order.getId(), userId);

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentAmount(paymentAmount);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);

        cartItemRepository.deleteAll(cartItems);

        Set<Long> sellerUserIds = cartItems.stream()
                .map(cartItem -> cartItem.getProduct().getUser().getId())
                .collect(Collectors.toSet());
        for (Long sellerUserId : sellerUserIds) {
            User seller = userRepository.findById(sellerUserId)
                    .orElseThrow(() -> new Exception("Seller not found"));

            sendOrderNotification(seller, order.getId());
        }
        return payment;
    }

    private void sendOrderNotification(User seller, Long orderId) {
        Notification notification = new Notification();
        notification.setUser(seller);
        notification.setMessage("Bạn có một đơn hàng mới. ID đơn hàng: " + orderId);

        notificationRepository.save(notification);

        sendNotificationToSeller(notification);
    }
    private void sendNotificationToSeller(Notification notification) {
        String emailContent = notification.getMessage();
        String recipientEmail = notification.getUser().getEmail();

        // Gửi email cho người bán
        emailService.sendEmail(recipientEmail, "Thông báo đơn hàng mới", emailContent);
    }

}
