package com.example.employeetask.service;

import com.example.employeetask.model.Status;
import com.example.employeetask.model.StatusEnum;
import com.example.employeetask.model.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StatusService {
    void createStatus(Status status);

    List<Status> getAll();

    Optional<Status> findById(Long id);

    void update(Status status);

    List<Status> findByDueDate(LocalDate date);

    List<Status> findByStatus(StatusEnum todo);
}
