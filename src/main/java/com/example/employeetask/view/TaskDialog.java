package com.example.employeetask.view;

import com.example.employeetask.model.Employee;
import com.example.employeetask.model.Task;
import com.example.employeetask.service.EmployeeService;
import com.example.employeetask.service.TaskService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.example.employeetask.view.Menu.SCANNER;

public class TaskDialog {
    private EmployeeService employeeService;
    private TaskService taskService;
    private Task task;

    public TaskDialog(EmployeeService employeeService, TaskService taskService) {
        this.employeeService = employeeService;
        this.taskService = taskService;
    }

    private String readLn() {
        return SCANNER.nextLine();
    }

    Menu taskMenu = new Menu(
            new MenuItem(1, "Create task", () -> this.createNewTask(() -> this.taskMenu)),
            new MenuItem(2, "Read all task", () -> this.readAllTasks(() -> this.taskMenu)),
            new MenuItem(3, "Read an task", () -> this.readAnTask(() -> this.taskMenu)),
            new MenuItem(4, "Update task", () -> this.updateTask(() -> this.taskMenu)),
            new MenuItem(5, "Delete task", () -> this.deleteTask(() -> this.taskMenu)),
            new MenuItem(0, "Back", () -> new Dialog(employeeService, taskService).getMainMenu())
    );

    private State deleteTask(State next) {
        return null;
    }

    private State updateTask(State next) {
        return null;
    }

    private State readAnTask(State next) {
        return null;
    }

    private State readAllTasks(State next) {
        return null;
    }

    private State createNewTask(State next) {
        Task task = new Task();
        while (task.getTitle() == null) {
            System.out.println("Enter title:");
            task.setTitle(readLn());
        }

        while (task.getDescription() == null) {
            System.out.println("Enter description:");
            task.setDescription(readLn());
        }

        while (task.getAssignee() == null) {
            System.out.println("Enter assignee id:");
            String inputId = readLn();
            try {
                int assigneeId = Integer.parseInt(inputId);
                Optional<Employee> employee = Optional.of(employeeService.findById(assigneeId).orElseThrow());
                task.setAssignee(employee.get());
            } catch (NumberFormatException e) {
                System.out.println("Please, enter a valid number.");
            } catch (Exception e) {
                System.out.println("Employee with id \"" + inputId + "\" does not exists.");
            }
        }

        while (task.getDueDate() == null) {
            System.out.println("Enter due date in format \"dd.MM.yyyy\". Example: \"20.03.2023\":");
            setDueDate(task);
        }

        taskService.createTask(task);
        return next;
    }

    private void setDueDate(Task task) {
        String input = readLn();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse(input, formatter);
        task.setDueDate(date);
    }
}
