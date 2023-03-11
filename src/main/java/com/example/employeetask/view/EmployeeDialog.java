package com.example.employeetask.view;


import com.example.employeetask.model.Employee;
import com.example.employeetask.service.EmployeeService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.employeetask.view.Menu.SCANNER;


public class EmployeeDialog {
    private final EmployeeService employeeService;
    private Employee employee;

    public EmployeeDialog(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    private String readLn() {
        return SCANNER.nextLine();
    }

    Menu employeeMenu = new Menu(
            new MenuItem(1, "Create employee", () -> this.createNewEmployee(() -> this.employeeMenu)),
            new MenuItem(2, "Read all employees", () -> this.readAllEmployee(() -> this.employeeMenu)),
            new MenuItem(3, "Read an employee", () -> this.readAnEmployee(() -> this.employeeMenu)),
            new MenuItem(4, "Update employee", () -> this.updateEmployee(() -> this.employeeMenu)),
            new MenuItem(5, "Delete employee", () -> this.deleteEmployee(() -> this.employeeMenu)),
            new MenuItem(0, "Back", new Dialog(null).mainMenu)
    );

    Menu updateEmployeeMenu = new Menu(
            new MenuItem(1, "Full name", () -> this.updateEmployeeName(() -> this.updateEmployeeMenu)),
            new MenuItem(2, "Email", () -> this.updateEmployeeEmail(() -> this.updateEmployeeMenu)),
            new MenuItem(3, "Phone number", () -> this.updateEmployeePhoneNumber(() -> this.updateEmployeeMenu)),
            new MenuItem(4, "Date of birth", () -> this.updateEmployeeDateOfBirth(() -> this.updateEmployeeMenu)),
            new MenuItem(5, "Salary", () -> this.updateEmployeeSalary(() -> this.updateEmployeeMenu)),
            new MenuItem(6, "Save", () -> this.saveUpdatedEmployee(() -> this.employeeMenu)),
            new MenuItem(0, "Cancel", employeeMenu)
    );

    private State deleteEmployee(State next) {
        System.out.println("Enter employee id you want to delete:");
        Employee employee = findEmployeeById();
        if (employee != null) {
            Long id = employee.getId();
            employeeService.deleteEmployee(employee);
            System.out.println("Employee with id \"" + id + "\" successfully deleted.");
        }
        return next;
    }

    private State updateEmployeeSalary(State next) {
        System.out.println("Update employee's monthly salary from \"" + employee.getSalary() + "\" to:");
        String newSalary = readLn();
        System.out.println("Old monthly salary: " + employee.getSalary());
        employee.setSalary(new BigDecimal(newSalary));
        System.out.println("New monthly salary: " + employee.getSalary());
        return next;
    }

    private State updateEmployeeDateOfBirth(State next) {
        System.out.println("Update employee's date of birth from \"" + employee.getDateOfBirth() + "\" to:");
        LocalDate oldDateOfBirth = employee.getDateOfBirth();
        setEmployeeDateOfBirth(employee);
        System.out.println("Old date of birth: " + oldDateOfBirth);
        System.out.println("New date of birth: " + employee.getDateOfBirth());
        return next;
    }

    private State updateEmployeePhoneNumber(State next) {
        System.out.println("Update employee's phone number from \"" + employee.getPhoneNumber() + "\" to:");
        String phoneNumber = readLn();
        System.out.println("Old phone number: " + employee.getPhoneNumber());
        employee.setPhoneNumber(phoneNumber);
        System.out.println("New phone number: " + employee.getPhoneNumber());
        return next;
    }

    private State updateEmployeeEmail(State next) {
        System.out.println("Update employee's email from \"" + employee.getEmail() + "\" to:");
        String newEmail = readLn();
        String oldEmail = employee.getEmail();
        if (checkIfEmailIsValid(employee, newEmail)) {
            System.out.println("Old email: " + oldEmail);
            System.out.println("New email: " + employee.getEmail());
        }
        return next;
    }

    private State updateEmployeeName(State next) {
        System.out.println("Update employee's full name from \"" + employee.getFullName() + "\" to:");
        String newEmail = readLn();
        System.out.println("Old full name: " + employee.getFullName());
        employee.setFullName(newEmail);
        System.out.println("New full name: " + employee.getFullName());
        return next;
    }

    private State saveUpdatedEmployee(State next) {
        employeeService.updateEmployee(employee);
        return next;
    }

    private State updateEmployee(State next) {
        System.out.println("Enter employee id you want to update:");
        State update = updatingEmployee();
        if (update != null) return update;
        return next;
    }

    private State readAnEmployee(State next) {
        System.out.println("Enter employee id you want to see:");
        Employee employee = findEmployeeById();
        if (employee != null) {
            tableHeader();
            System.out.println(employee);
        }
        return next;
    }

    private State readAllEmployee(State next) {
        List<Employee> employees = employeeService.getAll();
        if (employees.size() > 0) {
            tableHeader();
            employees.forEach(System.out::println);
        } else {
            System.out.println("There is no employees in the system.");
        }
        return next;
    }

    private State createNewEmployee(State next) {
        Employee employee = new Employee();

        while (employee.getFullName() == null) {
            System.out.println("Enter full name:");
            employee.setFullName(readLn());
        }

        while (employee.getEmail() == null) {
            System.out.println("Enter email:");
            String email = readLn();
            checkIfEmailIsValid(employee, email);
        }

        while (employee.getPhoneNumber() == null) {
            System.out.println("Enter phone number:");
            String phoneNumber = readLn();
            if (phoneNumber.length() < 8 || phoneNumber.length() > 10) {
                System.out.println("Error: PhoneNumber should be between 8 and 10 characters long.");
            } else {
                employee.setPhoneNumber(phoneNumber);
            }
        }

        while (employee.getDateOfBirth() == null) {
            setEmployeeDateOfBirth(employee);
        }

        while (employee.getSalary() == null) {
            System.out.println("Enter monthly salary");
            employee.setSalary(new BigDecimal(readLn()));
        }

        employeeService.createEmployee(employee);
        return next;
    }

    private void setEmployeeDateOfBirth(Employee employee) {
        System.out.println("Enter date of birth in format \"dd.MM.yyyy\". Example: \"08.08.1988\":");
        String input = readLn();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse(input, formatter);
        employee.setDateOfBirth(date);
    }

    private static boolean checkIfEmailIsValid(Employee employee, String email) {
        Pattern pattern = Pattern.compile("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.find()) {
            System.out.println("Error: Email must be valid.");
            return false;
        }
        employee.setEmail(email);
        return true;
    }

    private State updatingEmployee() {
        long id;
        try {
            id = Integer.parseInt(readLn());
            Optional<Employee> byId = employeeService.findById(id);
            byId.ifPresent(value -> employee = value);
            if (employee != null) {
                System.out.println("Choose what to update. When you are finished press \"Save\"");
                return () -> this.updateEmployeeMenu;
            } else {
                System.out.println("Employee with id \"" + id + "\" does not exists.");
            }
        } catch (Exception e) {
            System.out.println("Please, enter a valid number.");
        }
        return null;
    }

    private Employee findEmployeeById() {
        long id;
        Employee employee = null;
        try {
            id = Integer.parseInt(readLn());
            Optional<Employee> byId = employeeService.findById(id);
            if (byId.isPresent()) {
                employee = byId.get();
            } else {
                System.out.println("Employee with id \"" + id + "\" does not exists.");
            }
        } catch (Exception e) {
            System.out.println("Please, enter a valid number.");
        }
        return employee;
    }

    private static void tableHeader() {
        System.out.printf("| %-10s | %-20s | %-20s | %-15s | %-15s | %-10s |%n",
                          "Id", "Ful lName", "Email", "Phone Number", "Date Of Birth", "Salary");
        System.out.println("_".repeat(109));
    }
}
