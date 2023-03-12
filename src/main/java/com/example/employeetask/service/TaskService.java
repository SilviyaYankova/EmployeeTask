package com.example.employeetask.service;

import com.example.employeetask.model.Employee;
import com.example.employeetask.model.Task;

import java.util.Map;

public interface TaskService {

    void createTask(Task task);

    void updateTask(Task taskToUpdate);

    void deleteTask(Task task);

    Map<Employee, Long> topFiveEmployeesCount();

    Long tasksCount();
}
