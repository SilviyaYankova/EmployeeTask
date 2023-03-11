package com.example.employeetask.service.impl;

import com.example.employeetask.model.Employee;
import com.example.employeetask.model.Task;
import com.example.employeetask.repository.TaskRepository;
import com.example.employeetask.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public List<Task> getAll() {
        return taskRepository.findAll();
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
    public Optional<Task> findById(long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Map<Employee, Long> topFiveEmployeesCount() {
        Map<Employee, Long> map = new LinkedHashMap<>();
        List<Employee> employees = taskRepository.findTopFiveEmployees();
        List<Long> tasksCount = taskRepository.findTopFiveEmployeesCount();
        for (int i = 0; i < employees.size(); i++) {
            map.put(employees.get(i), tasksCount.get(i));
        }
        return map;
    }
}
