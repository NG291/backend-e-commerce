package com.casestudy5.service.employee;

import com.casestudy5.model.entity.employee.Employee;
import com.casestudy5.service.IGenerateService;

import java.util.List;

public interface IEmployeeService extends IGenerateService<Employee> {
    void resetPassword(Long id);

    List<Employee> searchByUsernameOrName(String query);

}
