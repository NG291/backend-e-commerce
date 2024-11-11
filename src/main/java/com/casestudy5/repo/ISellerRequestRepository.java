package com.casestudy5.repo;

import com.casestudy5.model.entity.user.SellerRequest;
import com.casestudy5.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ISellerRequestRepository extends JpaRepository<SellerRequest, Long> {
    boolean existsByUser(User user);

    Optional<SellerRequest> findByUserId(Long userId);
}
