package com.example.employeetask.service;

import com.example.employeetask.model.Employee;
import com.example.employeetask.model.Status;
import com.example.employeetask.model.StatusEnum;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StatusService {
    void createStatus(Status status);

    List<Status> getAll();

    Optional<Status> findByTaskId(Long id);

    void update(Status status);

    List<Status> findByDueDate(LocalDate date);

    List<Status> findByStatus(StatusEnum todo);

    void delete(Status status);

    List<Status> findTaskByEmployee(Employee employee);
}
