package com.example.employeetask.view;


import com.example.employeetask.service.EmployeeService;

public class Dialog {
    private EmployeeService employeeService;

    public Dialog(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public void run() {
        State state = mainMenu;
        while (state != null) {
            state = state.get();
        }
    }

    Menu mainMenu = new Menu(
            new MenuItem(1, "Employee", () -> new EmployeeDialog(employeeService).employeeMenu),
            new MenuItem(2, "Task", () -> this.taskMenu)

    );

    Menu taskMenu = new Menu(
            new MenuItem(0, "Back", mainMenu)
    );
}
