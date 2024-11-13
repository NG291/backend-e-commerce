package com.casestudy5.service.employee;

import com.casestudy5.model.entity.employee.Employee;
import com.casestudy5.service.IGenerateService;

public interface IEmployeeService extends IGenerateService<Employee> {
    void resetPassword(Long id);
}
