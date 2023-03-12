package com.example.employeetask.view;

import com.example.employeetask.model.Employee;
import com.example.employeetask.model.Status;
import com.example.employeetask.service.EmployeeService;
import com.example.employeetask.service.StatusService;
import com.example.employeetask.service.TaskService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.employeetask.model.StatusEnum.*;
import static com.example.employeetask.view.Menu.SCANNER;

public class StatisticsDialog {
    private EmployeeService employeeService;
    private TaskService taskService;
    private StatusService statusService;

    public StatisticsDialog(EmployeeService employeeService, TaskService taskService, StatusService statusService) {
        this.employeeService = employeeService;
        this.taskService = taskService;
        this.statusService = statusService;
    }

    private String readLn() {
        return SCANNER.nextLine();
    }

    public Menu statisticsMenu = new Menu(
            new MenuItem(1, "Top five employees", () -> this.topFiveEmployees(() -> this.statisticsMenu)),
            new MenuItem(2, "Employees salary in range", () -> this.employeesSalaryInRange(() -> this.statisticsMenu)),
            new MenuItem(3, "Employees count", () -> this.employeesCount(() -> this.statisticsMenu)),
            new MenuItem(4, "Tasks of employee", () -> this.tasksOfEmployee(() -> this.statisticsMenu)),
            new MenuItem(5, "Task by due date", () -> this.taskByDueDate(() -> this.statisticsMenu)),
            new MenuItem(6, "Task by status", () -> this.tasksByStatus(() -> this.statusMenu)),
            new MenuItem(7, "Tasks count", () -> this.tasksCount(() -> this.statisticsMenu)),
            new MenuItem(0, "Back", () -> new Dialog(employeeService, taskService, statusService).getMainMenu())
    );

    Menu statusMenu = new Menu(
            new MenuItem(1, "Todo", () -> this.todoStatus(() -> this.statisticsMenu)),
            new MenuItem(2, "In progress", () -> this.inProgressStatus(() -> this.statisticsMenu)),
            new MenuItem(3, "Review", () -> this.reviewStatus(() -> this.statisticsMenu)),
            new MenuItem(4, "Done", () -> this.doneStatus(() -> this.statisticsMenu))
    );

    private State doneStatus(State next) {
        List<Status> list = statusService.findByStatus(DONE);
        tableHeaderWithStatus();
        list.forEach(System.out::println);
        return next;
    }

    private State reviewStatus(State next) {
        List<Status> list = statusService.findByStatus(REVIEW);
        tableHeaderWithStatus();
        list.forEach(System.out::println);
        return next;
    }

    private State inProgressStatus(State next) {
        List<Status> list = statusService.findByStatus(IN_PROGRESS);
        tableHeaderWithStatus();
        list.forEach(System.out::println);
        return next;
    }

    private State todoStatus(State next) {
        List<Status> list = statusService.findByStatus(TODO);
        tableHeaderWithStatus();
        list.forEach(System.out::println);
        return next;
    }

    private State tasksByStatus(State next) {
        System.out.println("Select status:");
        return next;
    }

    private State taskByDueDate(State next) {
        System.out.println("Enter due date in format \"dd.MM.yyyy\". Example: \"20.03.2023\":");
        String input = readLn();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse(input, formatter);
        List<Status> tasks = statusService.findByDueDate(date);
        tableHeaderWithStatus();
        tasks.forEach(System.out::println);
        return next;
    }

    private State tasksCount(State next) {
        System.out.println("Count of all tasks: " + taskService.tasksCount());
        return next;
    }

    private State employeesSalaryInRange(State next) {
        System.out.println("Enter minimum amount");
        BigDecimal min = new BigDecimal(readLn());
        System.out.println("Enter maximum amount");
        BigDecimal max = new BigDecimal(readLn());
        List<Employee> list = employeeService.findBySalaryInRange(min, max);
        tableHeaderEmployeeWithCount();
        list.forEach(System.out::println);
        return next;
    }

    private State tasksOfEmployee(State next) {
        System.out.println("Enter employee id you want to see:");
        String input = readLn();
        Employee employee = findEmployeeById(input);
        if (employee != null) {
            List<Status> tasks = statusService.findTaskByEmployee(employee);
            if (tasks.isEmpty()) {
                System.out.println("Employee with id \"" + employee.getId() + "\" does not have tasks.");
            } else {
                tableHeaderWithStatus();
                tasks.forEach(System.out::println);
            }
        }
        return next;
    }

    private State employeesCount(State next) {
        System.out.println("Count of all Employees: " + employeeService.employeesCount());
        return next;
    }

    private State topFiveEmployees(State next) {
        Map<Employee, Long> map = taskService.topFiveEmployeesCount();
        tableHeaderEmployeeWithCount();
        for (Map.Entry<Employee, Long> entry : map.entrySet()) {
            Employee employee = entry.getKey();
            Long count = entry.getValue();
            printEmployees(employee, count);
        }
        return next;
    }

    private void printEmployees(Employee employee, Long count) {
        System.out.printf("| %-10s | %-20s | %-20s | %-15s | %-15s | %-10s | %-10s |%n",
                          employee.getId(), employee.getFullName(), employee.getEmail(),
                          employee.getPhoneNumber(), employee.getDateOfBirth(),
                          employee.getSalary(), count);
    }

    private void tableHeaderEmployeeWithCount() {
        System.out.printf("| %-10s | %-20s | %-20s | %-15s | %-15s | %-10s | %-10s |%n",
                          "Id", "Ful lName", "Email", "Phone Number", "Date Of Birth", "Salary", "Count");
        System.out.println("-".repeat(122));
    }

    private void tableHeaderWithStatus() {
        System.out.printf("| %-10s | %-15s | %-15s | %-10s | %-54s | %-12s |%n",
                          "Id", "Title", "Due Date", "Assignee", "Description", "Status");
        System.out.println("-".repeat(135));
    }

    private Employee findEmployeeById(String input) {
        Employee employee = null;
        try {
            long id = Integer.parseInt(input);
            Optional<Employee> byId = Optional.of(employeeService.findById(id).orElseThrow());
            employee = byId.get();
        } catch (NumberFormatException e) {
            System.out.println("Please, enter a valid number.");
        } catch (Exception e) {
            System.out.println("Employee with id \"" + input + "\" does not exists.");
        }
        return employee;
    }

}
