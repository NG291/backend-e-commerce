package com.casestudy5.repo;

import com.casestudy5.model.entity.user.SellerRequest;
import com.casestudy5.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISellerRequestRepository extends JpaRepository<SellerRequest, Long> {
    List<SellerRequest> findAllByStatus(String status);

    boolean existsByUser(User user);
}
