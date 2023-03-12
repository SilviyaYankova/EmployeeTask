package com.example.employeetask.repository;

import com.example.employeetask.model.Status;
import com.example.employeetask.model.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Long> {

    List<Status> findAllByTask_DueDate(LocalDate date);

    List<Status> findAllByStatus(StatusEnum status);

    Optional<Status> findByTask_Id(Long id);
}
