package com.ting.R3S.Employee;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmployeeScheduleDTO {

    private Long scheduleId;
    private Long employeeId;
    private String workingPeriod;
    private String breakPeriod;

    public EmployeeScheduleDTO(Long scheduleId, Long employeeId, String workingPeriod, String breakPeriod) {
        this.scheduleId = scheduleId;
        this.employeeId = employeeId;
        this.workingPeriod = workingPeriod;
        this.breakPeriod = breakPeriod;
    }

    @Override
    public String toString() {
        return "EmployeeScheduleDTO{" +
                "scheduleId=" + scheduleId +
                ", employeeId=" + employeeId +
                ", workingPeriod='" + workingPeriod + '\'' +
                ", breakPeriod='" + breakPeriod + '\'' +
                '}';
    }
}
