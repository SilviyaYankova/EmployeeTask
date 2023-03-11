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

    Menu updateTaskMenu = new Menu(
            new MenuItem(1, "Title", () -> this.updateTaskTitle(() -> this.updateTaskMenu)),
            new MenuItem(2, "Description", () -> this.updateTaskDescription(() -> this.updateTaskMenu)),
            new MenuItem(3, "Assignee", () -> this.updateTaskAssignee(() -> this.updateTaskMenu)),
            new MenuItem(4, "Due date", () -> this.updateTaskDueDate(() -> this.updateTaskMenu)),
            new MenuItem(5, "Save", () -> this.saveUpdatedTask(() -> this.taskMenu)),
            new MenuItem(0, "Cancel", () -> taskMenu)
    );

    private State deleteTask(State next) {
        System.out.println("Enter task id you want to delete:");
        String input = readLn();
        Task task = findTaskById(input);
        if (task != null) {
            Long id = task.getId();
            taskService.deleteTask(task);
            System.out.println("Task with id \"" + id + "\" successfully deleted.");
        }
        return next;
    }

    private State updateTaskDueDate(State next) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate oldDueDate = task.getDueDate();
        System.out.println("Update task's due date from \""
                                   + dateTimeFormatter.format(oldDueDate) + "\" to:");
        String input = readLn();
        setDueDate(task, input);
        System.out.println("Old due date: " + dateTimeFormatter.format(oldDueDate));
        System.out.println("New due date: " + dateTimeFormatter.format(task.getDueDate()));
        return next;
    }

    private State updateTaskAssignee(State next) {
        System.out.println("Update task's assignee from \"" + task.getAssignee().getId() + "\" to:");
        String newAssignee = readLn();
        System.out.println("Old assignee id: " + task.getAssignee().getId());
        Employee assignee = findAssignee(task, newAssignee);
        if (assignee != null) {
            task.setAssignee(assignee);
            System.out.println("New assignee id: " + task.getAssignee().getId());
        }
        return next;
    }

    private State updateTaskDescription(State next) {
        System.out.println("Update task's description from \"" + task.getDescription() + "\" to:");
        String newDescription = readLn();
        System.out.println("Old description: " + task.getDescription());
        task.setDescription(newDescription);
        System.out.println("New description: " + task.getDescription());
        return next;
    }

    private State updateTaskTitle(State next) {
        System.out.println("Update task's title from \"" + task.getTitle() + "\" to:");
        String newTitle = readLn();
        System.out.println("Old title: " + task.getTitle());
        task.setTitle(newTitle);
        System.out.println("New title: " + task.getTitle());
        return next;
    }

    private State saveUpdatedTask(State next) {
        taskService.updateTask(task);
        return next;
    }

    private State updateTask(State next) {
        System.out.println("Enter task id you want to update:");
        String input = readLn();
        Task taskById = findTaskById(input);
        if (taskById != null) {
            System.out.println("Choose what to update. When you are finished press \"Save\"");
            task = taskById;
            return () -> this.updateTaskMenu;
        }
        return next;
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
            findAssignee(task, inputId);
        }

        while (task.getDueDate() == null) {
            System.out.println("Enter due date in format \"dd.MM.yyyy\". Example: \"20.03.2023\":");
            String input = readLn();
            setDueDate(task, input);
        }

        taskService.createTask(task);
        return next;
    }

    private Employee findAssignee(Task task, String inputId) {
        Employee employee = null;
        try {
            int assigneeId = Integer.parseInt(inputId);
            Optional<Employee> byId = Optional.of(employeeService.findById(assigneeId).orElseThrow());
            employee = byId.get();
            task.setAssignee(employee);
        } catch (NumberFormatException e) {
            System.out.println("Please, enter a valid number.");
        } catch (Exception e) {
            System.out.println("Employee with id \"" + inputId + "\" does not exists.");
        }
        return employee;
    }

    private void setDueDate(Task task, String input) {
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
