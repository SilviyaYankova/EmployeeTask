package com.example.employeetask.service;

import com.example.employeetask.model.Employee;
import com.example.employeetask.model.Task;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TaskService {

    void createTask(Task task);

    List<Task> getAll();

    void updateTask(Task taskToUpdate);

    void deleteTask(Task task);

    Optional<Task> findById(long id);

    Map<Employee, Long> topFiveEmployeesCount();
}
