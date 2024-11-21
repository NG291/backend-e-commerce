package com.casestudy5.service.employee;

import com.casestudy5.model.entity.employee.Employee;
import com.casestudy5.repo.IEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements IEmployeeService {

    private static final String DEFAULT_PASSWORD = "123456@Abc";

    @Autowired
    private IEmployeeRepository iEmployeeRepository;

    @Override
    public Iterable<Employee> findAll() {
        return iEmployeeRepository.findAll();
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return iEmployeeRepository.findById(id);
    }

    @Override
    public void save(Employee employee) {
        // Kiểm tra tính duy nhất của username và email trước khi lưu
        if (iEmployeeRepository.existsByUsername(employee.getUsername())) {
            throw new IllegalArgumentException("Username đã tồn tại!");
        }
        if (iEmployeeRepository.existsByEmail(employee.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại!");
        }
        // Thiết lập mật khẩu mặc định khi tạo mới
        employee.setPassword(DEFAULT_PASSWORD);
        iEmployeeRepository.save(employee);
    }

    @Override
    public void remove(Long id) {
        iEmployeeRepository.deleteById(id);
    }

    public void updateEmployee(Employee employee) {

        if (!iEmployeeRepository.existsById(employee.getId())) {
            throw new IllegalArgumentException("Employee does not exist!");
        }
        if (iEmployeeRepository.existsByUsername(employee.getUsername()) &&
                !iEmployeeRepository.findByUsername(employee.getUsername()).get().getId().equals(employee.getId())) {
            throw new IllegalArgumentException("Username already existed!");
        }
        if (iEmployeeRepository.existsByEmail(employee.getEmail()) &&
                !iEmployeeRepository.findByEmail(employee.getEmail()).get().getId().equals(employee.getId())) {
            throw new IllegalArgumentException("Email already existed!");
        }
        iEmployeeRepository.save(employee);
    }

    public void resetPassword(Long id) {
        Optional<Employee> optionalEmployee = iEmployeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employee.setPassword(DEFAULT_PASSWORD);
            iEmployeeRepository.save(employee);
        } else {
            throw new IllegalArgumentException("Employee does not exist!");
        }
    }

    @Override
    public List<Employee> searchByUsernameOrName(String query) {
        return iEmployeeRepository.findByUsernameContainingOrNameContaining(query, query);
    }

}

