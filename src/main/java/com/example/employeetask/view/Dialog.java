package com.example.employeetask.view;

import com.example.employeetask.service.EmployeeService;
import com.example.employeetask.service.TaskService;

public class Dialog {
    private EmployeeService employeeService;
    private TaskService taskService;

    public Dialog(EmployeeService employeeService, TaskService taskService) {
        this.employeeService = employeeService;
        this.taskService = taskService;
    }

    public void run() {
        State state = mainMenu;
        while (state != null) {
            state = state.get();
        }
    }

    Menu mainMenu = new Menu(
            new MenuItem(1, "Employee", () -> new EmployeeDialog(employeeService, taskService).employeeMenu),
            new MenuItem(2, "Task", () -> new TaskDialog(employeeService, taskService).taskMenu),
            new MenuItem(3, "Statistics", () -> new StatisticsDialog(employeeService, taskService).statisticsMenu)
    );

    public Menu getMainMenu() {
        return mainMenu;
    }
}
