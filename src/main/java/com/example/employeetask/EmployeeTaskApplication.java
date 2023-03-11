package com.example.employeetask;

import com.example.employeetask.service.EmployeeService;
import com.example.employeetask.service.StatusService;
import com.example.employeetask.service.TaskService;
import com.example.employeetask.view.Dialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class EmployeeTaskApplication implements CommandLineRunner {
    private final EmployeeService employeeService;
    private final TaskService taskService;
    private final StatusService statusService;

    public EmployeeTaskApplication(EmployeeService employeeService, TaskService taskService, StatusService statusService) {
        this.employeeService = employeeService;
        this.taskService = taskService;
        this.statusService = statusService;
    }

    public static void main(String[] args) {
        SpringApplication.run(EmployeeTaskApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("EXECUTING : command line runner");
        System.out.println();
        new Dialog(employeeService, taskService, statusService).run();
    }
}
