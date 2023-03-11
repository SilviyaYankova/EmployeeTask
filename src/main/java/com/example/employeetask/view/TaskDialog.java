package com.example.employeetask.view;

import com.example.employeetask.model.Employee;
import com.example.employeetask.model.Task;
import com.example.employeetask.service.EmployeeService;
import com.example.employeetask.service.TaskService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
        System.out.println("Enter task id you want to see:");
        String input = readLn();
        Task task = findTaskById(input);
        if (task != null) {
            tableHeader();
            System.out.println(task);
        }
        return next;
    }

    private State readAllTasks(State next) {
        List<Task> tasks = taskService.getAll();
        if (tasks.size() > 0) {
            tableHeader();
            tasks.forEach(System.out::println);
        } else {
            System.out.println("There is no employees in the system.");
        }
        return next;
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

    private static void tableHeader() {
        System.out.printf("| %-10s | %-15s | %-15s | %-10s | %-54s |%n",
                          "id", "title", "dueDate", "assignee", "description");
        System.out.println("_".repeat(120));
    }

    private Task findTaskById(String input) {
        Task task = null;
        try {
            long id = Integer.parseInt(input);
            Optional<Task> byId = Optional.of(taskService.findById(id).orElseThrow());
            task = byId.get();
        } catch (NumberFormatException e) {
            System.out.println("Please, enter a valid number.");
        } catch (Exception e) {
            System.out.println("Task with id \"" + input + "\" does not exists.");
        }
        return task;
    }
}
