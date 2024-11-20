package com.casestudy5.repo;

import com.casestudy5.model.entity.cart.CartItem;
import com.casestudy5.model.entity.product.Product;
import com.casestudy5.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByUserAndProduct(User user, Product product);

    List<CartItem> findByUser(User user);

    List<CartItem> findByUserId(Long userId);

    void deleteAllByUserId(Long userId);

}
