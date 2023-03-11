package com.example.employeetask.view;

import com.example.employeetask.model.Task;
import com.example.employeetask.service.EmployeeService;
import com.example.employeetask.service.TaskService;

public class TaskDialog {
    private EmployeeService employeeService;
    private TaskService taskService;
    private Task task;

    public TaskDialog(EmployeeService employeeService, TaskService taskService) {
        this.employeeService = employeeService;
        this.taskService = taskService;
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
        // title, description, assignee, due, date
        return null;
    }
}
