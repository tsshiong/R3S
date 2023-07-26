package com.ting.R3S.Schedule;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class DeleteScheduleRequest {
    private LocalDate selectedDate;
    private List<Long> employeeIds;

    public DeleteScheduleRequest(LocalDate selectedDate, List<Long> employeeIds) {
        this.selectedDate = selectedDate;
        this.employeeIds = employeeIds;
    }

    @Override
    public String toString() {
        return "DeleteScheduleRequest{" +
                "selectedDate=" + selectedDate +
                ", employeeIds=" + employeeIds +
                '}';
    }
}
