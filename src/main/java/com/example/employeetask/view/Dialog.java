package com.example.employeetask.view;

import com.example.employeetask.service.EmployeeService;
import com.example.employeetask.service.StatusService;
import com.example.employeetask.service.TaskService;

public class Dialog {
    private EmployeeService employeeService;
    private TaskService taskService;
    private StatusService statusService;

    public Dialog(EmployeeService employeeService, TaskService taskService, StatusService statusService) {
        this.employeeService = employeeService;
        this.taskService = taskService;
        this.statusService = statusService;
    }

    public void run() {
        State state = mainMenu;
        while (state != null) {
            state = state.get();
        }
    }

    Menu mainMenu = new Menu(
            new MenuItem(1, "Employee", () -> new EmployeeDialog(employeeService, taskService, statusService).employeeMenu),
            new MenuItem(2, "Task", () -> new TaskDialog(employeeService, taskService, statusService).taskMenu),
            new MenuItem(3, "Statistics", () -> new StatisticsDialog(employeeService, taskService, statusService).statisticsMenu)
    );

    public Menu getMainMenu() {
        return mainMenu;
    }
}
