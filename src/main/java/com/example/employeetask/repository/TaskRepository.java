package com.example.employeetask.repository;

import com.example.employeetask.model.Employee;
import com.example.employeetask.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = """
            SELECT t.assignee, count(*) FROM Task as t
            WHERE t.dueDate BETWEEN :start AND :end
            GROUP BY t.assignee
            ORDER BY count(*) desc
            LIMIT 5
            """)
    List<Employee> findTopFiveEmployees(LocalDate start, LocalDate end);

    @Query(value = """
            SELECT count(*) FROM Task as t
            WHERE t.dueDate BETWEEN :start AND :end
            GROUP BY t.assignee
            ORDER BY count(*) desc
            LIMIT 5
            """)
    List<Long> findTopFiveEmployeesCount(LocalDate start, LocalDate end);
}