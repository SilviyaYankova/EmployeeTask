package com.example.employeetask.repository;

import com.example.employeetask.model.Employee;
import com.example.employeetask.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("""
            SELECT t.assignee, count(*) FROM Task as t
            GROUP BY t.assignee
            ORDER BY count(*) desc
            LIMIT 5
            """)
    List<Employee> findTopFiveEmployees();

}
