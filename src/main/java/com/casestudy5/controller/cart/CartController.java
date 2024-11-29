package com.casestudy5.controller.cart;

import com.casestudy5.model.entity.cart.CartItem;
import com.casestudy5.model.entity.user.User;
import com.casestudy5.service.cart.CartItemService;
import com.casestudy5.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private UserService userService;

    @PostMapping("/add/{productId}")
    public ResponseEntity<String> addToCart(@PathVariable Long productId,
                                            @RequestParam int quantity,
                                            Principal principal) {
        try {
            User user = getUserFromPrincipal(principal)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            cartItemService.addToCart(user, productId, quantity);
            return ResponseEntity.ok("Product added to cart");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding product to cart: " + e.getMessage());
        }
    }

    private Optional<User> getUserFromPrincipal(Principal principal) {
        String username = principal.getName();
        return userService.findByUsername(username);
    }

    @GetMapping("/view")
    public ResponseEntity<?> viewCart(Principal principal) {
        try {
            Optional<User> user = getUserFromPrincipal(principal);
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            List<CartItem> cartItems = cartItemService.getCartItems(user.get());
            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching cart items: " + e.getMessage());
        }
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<String> updateCart(@PathVariable Long productId,
                                             @RequestParam int quantity,
                                             Principal principal) {
        try {
            Optional<User> user = getUserFromPrincipal(principal);
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            cartItemService.updateCart(user.get(), productId, quantity);
            return ResponseEntity.ok("Cart updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating cart: " + e.getMessage());
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long productId,
                                                 Principal principal) {
        try {
            Optional<User> user = getUserFromPrincipal(principal);
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            cartItemService.removeFromCart(user.get(), productId);
            return ResponseEntity.ok("Product removed from cart");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error removing product from cart: " + e.getMessage());
        }
    }
}
