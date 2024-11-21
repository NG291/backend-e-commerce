package com.casestudy5.controller.cart;

import com.casestudy5.model.entity.cart.Payment;
import com.casestudy5.model.entity.cart.Enum.PaymentMethodStatus;
import com.casestudy5.service.payment.PaymentService;
;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Phương thức xử lý thanh toán
    @PostMapping("/process")
    public ResponseEntity<Object> processPayment(@RequestParam Long userId,
                                                 @RequestParam double paymentAmount,
                                                 @RequestParam PaymentMethodStatus paymentMethod) {
        try {
            Payment payment = paymentService.processPayment(userId, paymentAmount, paymentMethod);
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
