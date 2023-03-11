package com.example.employeetask.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue
    Long id;
    @Column
    String fullName;
    @Column
    String email;
    @Column
    String phoneNumber;
    @Column
    LocalDate dateOfBirth;
    @Column
    BigDecimal salary;

    @Override
    public String toString() {
        return String.format("| %-10s | %-20s | %-20s | %-15s | %-15s | %-10s |",
                             id, fullName, email, phoneNumber, dateOfBirth, salary);
    }
}
