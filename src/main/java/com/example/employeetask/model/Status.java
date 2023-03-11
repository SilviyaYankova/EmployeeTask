package com.example.employeetask.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task_status")
public class Status {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Task task;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    public Status(Task task, StatusEnum status) {
        this.task = task;
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("| %-10s | %-15s | %-15s | %-10s | %-54s | %-12s |",
                             task.getId(), task.getTitle(), task.getDueDate(),
                             task.getAssignee().getId(), task.getDescription(), getStatus().toString());
    }
}
