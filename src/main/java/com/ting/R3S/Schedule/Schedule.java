package com.ting.R3S.Schedule;

import com.ting.R3S.Employee.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate businessDate;
    private String workingPeriod;
    private String breakPeriod;
    private Double overtime;
    private Double totalWorkingHour;

    @ManyToOne()
    @JoinColumn(name="employee_id", nullable=false)
    private Employee employee;

}
