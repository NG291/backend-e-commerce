package com.casestudy5.controller.cart;

import com.casestudy5.config.UserPrinciple;
import com.casestudy5.model.entity.cart.Enum.OrderStatus;
import com.casestudy5.model.entity.cart.dto.OrderDTO;
import com.casestudy5.service.OrderItem.OrderItemService;
import com.casestudy5.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;
    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        try {
            orderService.updateOrderStatus(orderId, OrderStatus.CANCEL);
            return ResponseEntity.ok("Order has been canceled successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/reject/{orderId}")
    public ResponseEntity<String> rejectOrder(@PathVariable Long orderId) {
        try {
            orderService.updateOrderStatus(orderId, OrderStatus.REJECT);
            return ResponseEntity.ok("Order has been rejected successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/success/{orderId}")
    public ResponseEntity<String> SuccessOrder(@PathVariable Long orderId) {
        try {
            orderService.updateOrderStatus(orderId, OrderStatus.SUCCESS);
            return ResponseEntity.ok("Order has been rejected successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getOrdersForUser(@AuthenticationPrincipal UserPrinciple userPrinciple) {
        try {

            Long userId = userPrinciple.getId();
            List<OrderDTO> orders = orderService.getOrdersForUser(userId);
            return ResponseEntity.ok(orders);

        } catch (Exception e) {
            // Nếu có lỗi, trả về thông báo lỗi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<?> getOrdersForMerchant(@PathVariable Long sellerId) {
        try {
            List<OrderDTO> orderDTOs = orderService.getOrdersForMerchant(sellerId);

            return ResponseEntity.ok(orderDTOs);
        } catch (Exception e) {
            // Nếu có lỗi (ví dụ: không có đơn hàng nào), trả về thông báo lỗi
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No orders found for this merchant.");
        }
    }

    @GetMapping("/pending/{sellerId}")
    public ResponseEntity<List<OrderDTO>> getPendingOrdersForMerchant(@PathVariable Long sellerId) {
        try {
            List<OrderDTO> pendingOrders = orderService.getPendingOrdersForMerchant(sellerId);
            return ResponseEntity.ok(pendingOrders);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }
}
