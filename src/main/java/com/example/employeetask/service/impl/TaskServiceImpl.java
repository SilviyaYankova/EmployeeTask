package com.example.employeetask.service.impl;

import com.example.employeetask.model.Employee;
import com.example.employeetask.model.Task;
import com.example.employeetask.repository.TaskRepository;
import com.example.employeetask.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository repository) {
        this.taskRepository = repository;
    }

    @Override
    public void createTask(Task task) {
        taskRepository.save(task);
    }

    @Override
    public void updateTask(Task taskToUpdate) {
        taskRepository.save(taskToUpdate);
    }

    @Override
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    @Override
    public Map<Employee, Long> topFiveEmployeesCount() {
        Map<Employee, Long> map = new LinkedHashMap<>();
        LocalDate start = LocalDate.now().minusMonths(1);
        LocalDate end = LocalDate.now();
        List<Employee> employees = taskRepository.findTopFiveEmployees(start, end);
        List<Long> tasksCount = taskRepository.findTopFiveEmployeesCount(start, end);
        for (int i = 0; i < employees.size(); i++) {
            map.put(employees.get(i), tasksCount.get(i));
        }
        return map;
    }

    @Override
    public Long tasksCount() {
        return taskRepository.count();
    }
}
