package com.example.employeetask.service.impl;

import com.example.employeetask.model.Employee;
import com.example.employeetask.model.Status;
import com.example.employeetask.model.StatusEnum;
import com.example.employeetask.repository.StatusRepository;
import com.example.employeetask.service.StatusService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StatusServiceImpl implements StatusService {
    private final StatusRepository statusRepository;

    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public void createStatus(Status status) {
        statusRepository.save(status);
    }

    @Override
    public List<Status> getAll() {
        return statusRepository.findAll();
    }

    @Override
    public Optional<Status> findByTaskId(Long id) {
        return statusRepository.findByTask_Id(id);
    }

    @Override
    public void update(Status status) {
        statusRepository.save(status);
    }

    @Override
    public List<Status> findByDueDate(LocalDate date) {
        return statusRepository.findAllByTask_DueDate(date);
    }

    @Override
    public List<Status> findByStatus(StatusEnum status) {
        return statusRepository.findAllByStatus(status);
    }

    @Override
    public void delete(Status status) {
        statusRepository.delete(status);
    }

    @Override
    public List<Status> findTaskByEmployee(Employee employee) {
        return statusRepository.findAllByTask_Assignee(employee);
    }
}
