package com.casestudy5.repo;

import com.casestudy5.model.entity.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Long> {

    // Kiểm tra xem username đã tồn tại hay chưa
    Optional<Employee> findByUsername(String username);

    // Kiểm tra xem email đã tồn tại hay chưa
    Optional<Employee> findByEmail(String email);

    // Kiểm tra xem có nhân viên nào với username này không, trừ trường hợp id hiện tại (hỗ trợ khi cập nhật thông tin)
    boolean existsByUsername(String username);

    // Kiểm tra xem có nhân viên nào với email này không, trừ trường hợp id hiện tại (hỗ trợ khi cập nhật thông tin)
    boolean existsByEmail(String email);

    List<Employee> findByUsernameContainingOrNameContaining(String username, String name);


}