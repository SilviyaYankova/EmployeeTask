package com.example.employeetask.repository;

import com.example.employeetask.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findAllBySalaryBetween(BigDecimal min, BigDecimal max);
}
