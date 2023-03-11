package com.example.employeetask.service.impl;

import com.example.employeetask.model.Employee;
import com.example.employeetask.repository.EmployeeRepository;
import com.example.employeetask.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void createEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @Override
    public void updateEmployee(Employee employeeToUpdate) {
        employeeRepository.save(employeeToUpdate);
    }

    @Override
    public void deleteEmployee(Employee employee) {
        employeeRepository.delete(employee);
    }

    @Override
    public Optional<Employee> findById(long id) {
        return employeeRepository.findById(id);
    }
}
