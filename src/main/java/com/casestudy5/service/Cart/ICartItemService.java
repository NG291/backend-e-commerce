package com.casestudy5.service.Cart;

import com.casestudy5.model.entity.cart.CartItem;
import com.casestudy5.model.entity.user.User;

import java.util.List;

public interface ICartItemService {
    void addToCart(User user, Long productId, int quantity);

    List<CartItem> getCartItems(User user);

    void updateCart(User user, Long productId, int quantity);

     void removeFromCart(User user, Long productId);
}
