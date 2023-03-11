package com.example.employeetask.view;

import com.example.employeetask.model.Employee;
import com.example.employeetask.service.EmployeeService;
import com.example.employeetask.service.TaskService;

import java.util.Map;

import static com.example.employeetask.view.Menu.SCANNER;

public class StatisticsDialog {
    private EmployeeService employeeService;
    private TaskService taskService;

    public StatisticsDialog(EmployeeService employeeService, TaskService taskService) {
        this.employeeService = employeeService;
        this.taskService = taskService;
    }

    private String readLn() {
        return SCANNER.nextLine();
    }

    public Menu statisticsMenu = new Menu(
            new MenuItem(1, "Top five employees", () -> this.topFiveEmployees(() -> this.statisticsMenu)),
            new MenuItem(0, "Back", () -> new Dialog(employeeService, taskService).getMainMenu())
    );

    private State topFiveEmployees(State next) {
        Map<Employee, Long> map = taskService.topFiveEmployeesCount();
        tableHeader();
        for (Map.Entry<Employee, Long> entry : map.entrySet()) {
            Employee employee = entry.getKey();
            Long count = entry.getValue();
            System.out.printf("| %-10s | %-20s | %-20s | %-15s | %-15s | %-10s | %-10s |%n",
                              employee.getId(), employee.getFullName(), employee.getEmail(),
                              employee.getPhoneNumber(), employee.getDateOfBirth(),
                              employee.getSalary(), count);
        }
        return next;
    }

    private static void tableHeader() {
        System.out.printf("| %-10s | %-20s | %-20s | %-15s | %-15s | %-10s | %-10s |%n",
                          "Id", "Ful lName", "Email", "Phone Number", "Date Of Birth", "Salary", "Count");
        System.out.println("_".repeat(122));
    }
}
