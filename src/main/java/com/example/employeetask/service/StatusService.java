package com.example.employeetask.service;

import com.example.employeetask.model.Status;

import java.util.List;

public interface StatusService {
    void createStatus(Status status);

    List<Status> getAll();
}
