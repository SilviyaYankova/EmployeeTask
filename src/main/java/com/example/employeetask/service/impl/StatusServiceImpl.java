package com.example.employeetask.service.impl;

import com.example.employeetask.model.Status;
import com.example.employeetask.model.Task;
import com.example.employeetask.repository.StatusRepository;
import com.example.employeetask.service.StatusService;
import org.springframework.stereotype.Service;

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
    public Optional<Status> findById(Long id) {
        return statusRepository.findById(id);
    }

    @Override
    public void update(Status status) {
        statusRepository.save(status);
    }

}
