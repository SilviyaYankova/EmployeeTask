package com.example.employeetask.view;

import com.example.employeetask.model.Employee;
import com.example.employeetask.service.EmployeeService;
import com.example.employeetask.service.StatusService;
import com.example.employeetask.service.TaskService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.employeetask.view.Menu.SCANNER;

public class EmployeeDialog {
    private EmployeeService employeeService;
    private TaskService taskService;
    private StatusService statusService;
    private Employee employee;

    public EmployeeDialog(EmployeeService employeeService, TaskService taskService, StatusService statusService) {
        this.employeeService = employeeService;
        this.taskService = taskService;
        this.statusService = statusService;
    }

    private String readLn() {
        return SCANNER.nextLine();
    }

    public Menu employeeMenu = new Menu(
            new MenuItem(1, "Create employee", () -> this.createNewEmployee(() -> this.employeeMenu)),
            new MenuItem(2, "Read all employees", () -> this.readAllEmployees(() -> this.employeeMenu)),
            new MenuItem(3, "Read an employee", () -> this.readAnEmployee(() -> this.employeeMenu)),
            new MenuItem(4, "Update employee", () -> this.updateEmployee(() -> this.employeeMenu)),
            new MenuItem(5, "Delete employee", () -> this.deleteEmployee(() -> this.employeeMenu)),
            new MenuItem(0, "Back", () -> new Dialog(employeeService, taskService, statusService).getMainMenu())
    );

    Menu updateMenu = new Menu(
            new MenuItem(1, "Full name", () -> this.updateFullName(() -> this.updateMenu)),
            new MenuItem(2, "Email", () -> this.updateEmail(() -> this.updateMenu)),
            new MenuItem(3, "Phone number", () -> this.updatePhoneNumber(() -> this.updateMenu)),
            new MenuItem(4, "Date of birth", () -> this.updateDateOfBirth(() -> this.updateMenu)),
            new MenuItem(5, "Salary", () -> this.updateSalary(() -> this.updateMenu)),
            new MenuItem(6, "Save", () -> this.saveUpdatedEmployee(() -> this.employeeMenu)),
            new MenuItem(0, "Cancel", employeeMenu)
    );

    private State deleteEmployee(State next) {
        System.out.println("Enter employee id you want to delete:");
        String input = readLn();
        Employee employee = findEmployeeById(input);
        if (employee != null) {
            Long id = employee.getId();
            employeeService.deleteEmployee(employee);
            System.out.println("Employee with id \"" + id + "\" successfully deleted.");
        }
        return next;
    }

    private State updateSalary(State next) {
        System.out.println("Update employee's monthly salary from \"" + employee.getSalary() + "\" to:");
        BigDecimal oldSalary = employee.getSalary();
        setSalary(employee);
        if (!oldSalary.equals(employee.getSalary())) {
            System.out.println("Old monthly salary: " + oldSalary);
            System.out.println("New monthly salary: " + employee.getSalary());
        }
        return next;
    }

    private State updateDateOfBirth(State next) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate oldDateOfBirth = employee.getDateOfBirth();
        System.out.println("Update employee's date of birth from \""
                                   + dateTimeFormatter.format(oldDateOfBirth)
                                   + "\" to:");
        setDateOfBirth(employee);
        if (oldDateOfBirth != employee.getDateOfBirth()) {
            System.out.println("Old date of birth: " + dateTimeFormatter.format(oldDateOfBirth));
            System.out.println("New date of birth: " + dateTimeFormatter.format(employee.getDateOfBirth()));
        }
        return next;
    }

    private State updatePhoneNumber(State next) {
        System.out.println("Update employee's phone number from \"" + employee.getPhoneNumber() + "\" to:");
        String oldPhoneNumber = employee.getPhoneNumber();
        setPhoneNumber(employee);
        if (!oldPhoneNumber.equals(employee.getPhoneNumber())) {
            System.out.println("Old phone number: " + oldPhoneNumber);
            System.out.println("New phone number: " + employee.getPhoneNumber());
        }
        return next;
    }

    private State updateEmail(State next) {
        System.out.println("Update employee's email from \"" + employee.getEmail() + "\" to:");
        String newEmail = readLn();
        String oldEmail = employee.getEmail();
        if (checkIfEmailIsValid(employee, newEmail)) {
            System.out.println("Old email: " + oldEmail);
            System.out.println("New email: " + employee.getEmail());
        }
        return next;
    }

    private State updateFullName(State next) {
        System.out.println("Update employee's full name from \"" + employee.getFullName() + "\" to:");
        String oldName = employee.getFullName();
        String input = readLn();
        setFullName(employee, input);
        if (!oldName.equals(employee.getFullName())){
            System.out.println("Old full name: " + oldName);
            System.out.println("New full name: " + employee.getFullName());
        }
        return next;
    }

    private State saveUpdatedEmployee(State next) {
        employeeService.updateEmployee(employee);
        return next;
    }

    private State updateEmployee(State next) {
        System.out.println("Enter employee id you want to update:");
        String input = readLn();
        Employee employeeById = findEmployeeById(input);
        if (employeeById != null) {
            System.out.println("Choose what to update. When you are finished press \"Save\".");
            employee = employeeById;
            return () -> this.updateMenu;
        }
        return next;
    }

    private State readAnEmployee(State next) {
        System.out.println("Enter employee id you want to see:");
        String input = readLn();
        Employee employee = findEmployeeById(input);
        if (employee != null) {
            tableHeader();
            System.out.println(employee);
        }
        return next;
    }

    private State readAllEmployees(State next) {
        List<Employee> employees = employeeService.getAll();
        if (employees.size() > 0) {
            tableHeader();
            employees.forEach(System.out::println);
            System.out.println("Employees count: " + employeeService.employeesCount());
        } else {
            System.out.println("There is no employees in the system.");
        }
        return next;
    }

    private State createNewEmployee(State next) {
        Employee employee = new Employee();

        while (employee.getFullName() == null) {
            System.out.println("Enter full name between 2 and 20 characters:");
            String input = readLn();
            setFullName(employee, input);
        }

        while (employee.getEmail() == null) {
            System.out.println("Enter email:");
            String email = readLn();
            checkIfEmailIsValid(employee, email);
        }

        while (employee.getPhoneNumber() == null) {
            System.out.println("Enter phone number:");
            setPhoneNumber(employee);
        }

        while (employee.getDateOfBirth() == null) {
            setDateOfBirth(employee);
        }

        while (employee.getSalary() == null) {
            System.out.println("Enter monthly salary.");
            setSalary(employee);
        }

        employeeService.createEmployee(employee);
        return next;
    }

    private static void setFullName(Employee employee, String input) {
        if (input.length() < 2 || input.length() > 20) {
            System.out.println("Full name must be between 2 and 20 characters.");
        } else {
            employee.setFullName(input);
        }
    }

    private void setSalary(Employee employee) {
        try {
            BigDecimal salary = new BigDecimal(readLn());
            if (salary.compareTo(BigDecimal.ZERO) > 0) {
                employee.setSalary(salary);
            } else {
                System.out.println("Salary must be a positive number.");
            }
        } catch (Exception e) {
            System.out.println("Please enter a valid number.");
        }
    }

    private void setPhoneNumber(Employee employee) {
        String phoneNumber = readLn();
        if (phoneNumber.length() < 8 || phoneNumber.length() > 10) {
            System.out.println("Error: PhoneNumber should be between 8 and 10 characters long.");
        } else {
            employee.setPhoneNumber(phoneNumber);
        }
    }

    private void setDateOfBirth(Employee employee) {
        System.out.println("Enter date of birth in format \"dd.MM.yyyy\". Example: \"08.08.1988\":");
        String input = readLn();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            LocalDate date = LocalDate.parse(input, formatter);
            employee.setDateOfBirth(date);
        } catch (Exception e) {
            System.out.println("Invalid date format.");
        }
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

    private Employee findEmployeeById(String input) {
        return getEmployee(input, employeeService);
    }

    private static void tableHeader() {
        System.out.printf("| %-10s | %-20s | %-20s | %-15s | %-15s | %-10s |%n",
                          "Id", "Ful lName", "Email", "Phone Number", "Date Of Birth", "Salary");
        System.out.println("-".repeat(109));
    }

    public static Employee getEmployee(String input, EmployeeService employeeService) {
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
