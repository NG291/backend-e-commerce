package com.casestudy5.controller.cart;

import com.casestudy5.model.entity.cart.CartItem;
import com.casestudy5.model.entity.user.User;
import com.casestudy5.service.Cart.CartItemService;
import com.casestudy5.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private UserService userService;

    @PostMapping("/add/{productId}")
    public ResponseEntity<?> addToCart(@PathVariable Long productId,
                                       @RequestParam int quantity,
                                       Principal principal) {
        try {
            User user = getUserFromPrincipal(principal);
            cartItemService.addToCart(user, productId, quantity);
            return ResponseEntity.ok("Product added to cart");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding product to cart");
        }
    }

    private User getUserFromPrincipal(Principal principal) {
        String username = principal.getName();
        return userService.findByUsername(username);
    }
    @GetMapping("/view")
    public ResponseEntity<?> viewCart(Principal principal) {
        try {

            User user = getUserFromPrincipal(principal);
            List<CartItem> cartItems = cartItemService.getCartItems(user);
            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching cart items");
        }
    }
    @PutMapping("/update/{productId}")
    public ResponseEntity<?> updateCart(@PathVariable Long productId,
                                        @RequestParam int quantity,
                                        Principal principal) {
        try {
            User user = getUserFromPrincipal(principal);
            cartItemService.updateCart(user, productId, quantity);
            return ResponseEntity.ok("Cart updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating cart");
        }
    }
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long productId,
                                            Principal principal) {
        try {
            User user = getUserFromPrincipal(principal);
            cartItemService.removeFromCart(user, productId);
            return ResponseEntity.ok("Product removed from cart");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing product from cart");
        }
    }

}
