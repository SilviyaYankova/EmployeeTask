package com.example.employeetask.service;

import com.example.employeetask.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    void createEmployee(Employee employee);

    List<Employee> getAll();

    void updateEmployee(Employee employeeToUpdate);

    void deleteEmployee(Employee employee);

    Optional<Employee> findById(long id);

    long employeesCount();
}
