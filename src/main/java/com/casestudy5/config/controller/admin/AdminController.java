package com.casestudy5.config.controller.admin;

import com.casestudy5.model.entity.employee.Employee;
import com.casestudy5.model.entity.user.SellerRequest;
import com.casestudy5.model.entity.user.User;
import com.casestudy5.service.employee.IEmployeeService;
import com.casestudy5.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
//@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IEmployeeService iEmployeeService;

    // Các endpoint liên quan đến Seller Request
    @GetMapping("/seller-requests")
    public List<SellerRequest> getSellerRequests() {
        return userService.getAllSellerRequests();
    }

    @PostMapping("/approve-seller/{userId}")
    public ResponseEntity<String> approveSeller(@PathVariable Long userId) {
        String result = userService.approveSeller(userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/reject-seller/{userId}")
    public ResponseEntity<String> rejectSeller(@PathVariable Long userId) {
        String result = userService.rejectSeller(userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Các endpoint liên quan đến quản lý nhân viên
    @GetMapping("/employees")
    public ResponseEntity<Iterable<Employee>> getAllEmployees() {
        return new ResponseEntity<>(iEmployeeService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        try {
            iEmployeeService.save(employee);
            return new ResponseEntity<>(employee, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = iEmployeeService.findById(id);
        return employee.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

//    @PutMapping("/employees/{id}")
//    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
//        Optional<Employee> existingEmployee = iEmployeeService.findById(id);
//        if (existingEmployee.isPresent()) {
//            employee.setId(id);
//            try {
//                iEmployeeService.save(employee);
//                return new ResponseEntity<>(employee, HttpStatus.OK);
//            } catch (IllegalArgumentException e) {
//                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//            }
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable Long id) {
        Optional<Employee> employee = iEmployeeService.findById(id);
        if (employee.isPresent()) {
            iEmployeeService.remove(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/employees/{id}/reset-password")
    public ResponseEntity<HttpStatus> resetPassword(@PathVariable Long id) {
        try {
            iEmployeeService.resetPassword(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/employees/search")
    public ResponseEntity<Iterable<Employee>> searchEmployees(@RequestParam("query") String query) {
        List<Employee> employees = iEmployeeService.searchByUsernameOrName(query);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> getAllUsers() {
        Iterable<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }



}
