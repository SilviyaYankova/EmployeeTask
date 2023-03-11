package com.example.employeetask;

import com.example.employeetask.service.EmployeeService;
import com.example.employeetask.view.Dialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class EmployeeTaskApplication implements CommandLineRunner {
    private final EmployeeService employeeService;

    public EmployeeTaskApplication(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public static void main(String[] args) {
        SpringApplication.run(EmployeeTaskApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("EXECUTING : command line runner");
        new Dialog(employeeService).run();
    }
}
