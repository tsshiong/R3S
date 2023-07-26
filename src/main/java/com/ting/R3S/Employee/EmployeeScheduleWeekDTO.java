package com.ting.R3S.Employee;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class EmployeeScheduleWeekDTO {

    private Long scheduleId;
    private Long employeeId;
    private String workingPeriod;
    private String breakPeriod;
    private LocalDate businessDate;

    public EmployeeScheduleWeekDTO(Long scheduleId, Long employeeId, String workingPeriod, String breakPeriod, LocalDate businessDate) {
        this.scheduleId = scheduleId;
        this.employeeId = employeeId;
        this.workingPeriod = workingPeriod;
        this.breakPeriod = breakPeriod;
        this.businessDate = businessDate;
    }

    @Override
    public String toString() {
        return "EmployeeScheduleWeekDTO{" +
                "scheduleId=" + scheduleId +
                ", employeeId=" + employeeId +
                ", workingPeriod='" + workingPeriod + '\'' +
                ", breakPeriod='" + breakPeriod + '\'' +
                ", businessDate=" + businessDate +
                '}';
    }
}
