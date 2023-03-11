package com.example.employeetask.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.StringJoiner;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue
    Long id;
    @Column
    String title;
    @Column
    String description;
    @ManyToOne
    Employee assignee;
    @Column
    LocalDate dueDate;

    @Override
    public String toString() {
        return String.format("| %-10s | %-15s | %-15s | %-10s | %-54s |",
                             id, title, dueDate, assignee.getId(), description);
    }
}
