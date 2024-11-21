package com.casestudy5.service.payment;

import com.casestudy5.model.entity.cart.*;
import com.casestudy5.model.entity.cart.Enum.PaymentMethodStatus;
import com.casestudy5.model.entity.cart.Enum.PaymentStatus;
import com.casestudy5.repo.ICartItemRepository;
import com.casestudy5.repo.IPaymentRepository;
import com.casestudy5.service.orderItem.OrderItemService;
import com.casestudy5.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private ICartItemRepository cartItemRepository;

    @Autowired
    private IPaymentRepository paymentRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

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
        paymentRepository.save(payment); // Lưu Payment vào database

        cartItemRepository.deleteAll(cartItems);
        return payment;
    }
}
