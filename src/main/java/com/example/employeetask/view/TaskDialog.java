package com.example.employeetask.view;

import com.example.employeetask.model.Employee;
import com.example.employeetask.model.Status;
import com.example.employeetask.model.Task;
import com.example.employeetask.service.EmployeeService;
import com.example.employeetask.service.StatusService;
import com.example.employeetask.service.TaskService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.example.employeetask.model.StatusEnum.*;
import static com.example.employeetask.view.Menu.SCANNER;

public class TaskDialog {
    private EmployeeService employeeService;
    private TaskService taskService;
    private StatusService statusService;
    private Task task;
    private Status status;

    public TaskDialog(EmployeeService employeeService, TaskService taskService, StatusService statusService) {
        this.employeeService = employeeService;
        this.taskService = taskService;
        this.statusService = statusService;
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
            new MenuItem(0, "Back", () -> new Dialog(employeeService, taskService, statusService).getMainMenu())
    );

    Menu updateTaskMenu = new Menu(
            new MenuItem(1, "Title", () -> this.updateTitle(() -> this.updateTaskMenu)),
            new MenuItem(2, "Description", () -> this.updateDescription(() -> this.updateTaskMenu)),
            new MenuItem(3, "Assignee", () -> this.updateAssignee(() -> this.updateTaskMenu)),
            new MenuItem(4, "Due date", () -> this.updateDueDate(() -> this.updateTaskMenu)),
            new MenuItem(5, "Status", () -> this.updateStatus(() -> this.updateStatusMenu)),
            new MenuItem(6, "Save", () -> this.saveUpdatedTask(() -> this.taskMenu)),
            new MenuItem(0, "Cancel", () -> taskMenu)
    );

    Menu updateStatusMenu = new Menu(
            new MenuItem(1, "Todo", () -> this.todoStatus(() -> this.updateTaskMenu)),
            new MenuItem(2, "In progress", () -> this.inProgressStatus(() -> this.updateTaskMenu)),
            new MenuItem(3, "Review", () -> this.reviewStatus(() -> this.updateTaskMenu)),
            new MenuItem(4, "Done", () -> this.doneStatus(() -> this.updateTaskMenu)),
            new MenuItem(0, "Cancel", () -> taskMenu)
    );

    private State deleteTask(State next) {
        System.out.println("Enter task id you want to delete:");
        String input = readLn();
        Status status = findTaskById(input);
        if (status != null) {
            Long id = status.getId();
            statusService.delete(status);
            taskService.deleteTask(status.getTask());
            System.out.println("Task with id \"" + id + "\" successfully deleted.");
        }
        return next;
    }

    private State doneStatus(State next) {
        status.setStatus(DONE);
        System.out.println("Task's status updated to \"Done\".");
        return next;
    }

    private State reviewStatus(State next) {
        status.setStatus(REVIEW);
        System.out.println("Task's status updated to \"Review\".");
        return next;
    }

    private State inProgressStatus(State next) {
        status.setStatus(IN_PROGRESS);
        System.out.println("Task's status updated to \"In progress\".");
        return next;
    }

    private State todoStatus(State next) {
        status.setStatus(TODO);
        System.out.println("Task's status updated to \"Todo\".");
        return next;
    }

    private State updateStatus(State next) {
        System.out.println("Update task's status from \"" + status.getStatus() + "\" to:");
        return next;
    }

    private State updateDueDate(State next) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate oldDueDate = task.getDueDate();
        System.out.println("Update task's due date from \""
                                   + dateTimeFormatter.format(oldDueDate)
                                   + "\" to:");
        String input = readLn();
        setDueDate(task, input);
        if (!oldDueDate.equals(task.getDueDate())) {
            System.out.println("Old due date: " + dateTimeFormatter.format(oldDueDate));
            System.out.println("New due date: " + dateTimeFormatter.format(task.getDueDate()));
        }
        return next;
    }

    private State updateAssignee(State next) {
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

    private State updateDescription(State next) {
        String oldDescription = task.getDescription();
        System.out.println("Update task's description from \"" + oldDescription + "\" to:");
        String input = readLn();
        setDescription(task, input);
        if (!oldDescription.equals(task.getDescription())) {
            System.out.println("Old description: " + oldDescription);
            System.out.println("New description: " + task.getDescription());
        }
        return next;
    }

    private State updateTitle(State next) {
        String oldTitle = task.getTitle();
        System.out.println("Update task's title from \"" + oldTitle + "\" to:");
        String input = readLn();
        setTitle(task, input);
        if (!oldTitle.equals(task.getTitle())) {
            System.out.println("Old title: " + oldTitle);
            System.out.println("New title: " + task.getTitle());
        }
        return next;
    }

    private State saveUpdatedTask(State next) {
        taskService.updateTask(task);
        statusService.update(status);
        return next;
    }

    private State updateTask(State next) {
        System.out.println("Enter task id you want to update:");
        String input = readLn();
        Status taskById = findTaskById(input);
        if (taskById != null) {
            System.out.println("Choose what to update. When you are finished press \"Save\"");
            status = taskById;
            task = taskById.getTask();
            return () -> this.updateTaskMenu;
        }
        return next;
    }

    private State readAnTask(State next) {
        System.out.println("Enter task id you want to see:");
        String input = readLn();
        Status taskById = findTaskById(input);
        if (taskById != null) {
            taskTableHeader();
            System.out.println(taskById);
        }
        return next;
    }

    private State readAllTasks(State next) {
        List<Status> tasks = statusService.getAll();
        if (tasks.size() > 0) {
            taskTableHeader();
            tasks.forEach(System.out::println);
            System.out.println("Tasks count: " + taskService.tasksCount());
        } else {
            System.out.println("There is no task in the system.");
        }
        return next;
    }

    private State createNewTask(State next) {
        Task task = new Task();
        while (task.getTitle() == null) {
            System.out.println("Enter title between 2 and 20 characters:");
            String input = readLn();
            setTitle(task, input);
        }

        while (task.getDescription() == null) {
            System.out.println("Enter description between 10 and 50 characters:");
            String input = readLn();
            setDescription(task, input);
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
        Status status = new Status(task, TODO);
        statusService.createStatus(status);
        return next;
    }

    private static void setDescription(Task task, String input) {
        if (input.length() < 10 || input.length() > 50) {
            System.out.println("Description must be between 2 and 20 characters.");
        } else {
            task.setDescription(input);
        }
    }

    private static void setTitle(Task task, String input) {
        if (input.length() < 2 || input.length() > 20) {
            System.out.println("Title must be between 2 and 20 characters.");
        } else {
            task.setTitle(input);
        }
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
        try {
            LocalDate date = LocalDate.parse(input, formatter);
//            LocalDate now = LocalDate.now();
//            if (date.isAfter(now)) {
                task.setDueDate(date);
//            } else {
//                System.out.println("Due date can not be from the past.");
//            }
        } catch (Exception e) {
            System.out.println("Invalid date format.");
        }
    }

    private Status findTaskById(String input) {
        Status statusById = null;
        try {
            long id = Integer.parseInt(input);
            Optional<Status> byId = Optional.of(statusService.findByTaskId(id).orElseThrow());
            statusById = byId.get();
        } catch (NumberFormatException e) {
            System.out.println("Please, enter a valid number.");
        } catch (Exception e) {
            System.out.println("Task with id \"" + input + "\" does not exists.");
        }
        return statusById;
    }

    private static void taskTableHeader() {
        System.out.printf("| %-10s | %-20s | %-15s | %-10s | %-50s | %-12s |%n",
                          "Id", "Title", "Due Date", "Assignee", "Description", "Status");
        System.out.println("-".repeat(136));
    }
}
