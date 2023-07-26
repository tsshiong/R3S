package com.ting.R3S.Schedule;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class EventData {
    private String breakPeriod;
    private LocalDate businessDate;
    private Double overtime;
    private String workingPeriod;
    private Long employeeId;
    private Double totalWorkingHour;

    public EventData(String breakPeriod, LocalDate businessDate, Double overtime, String workingPeriod, Long employeeId, Double totalWorkingHour) {
        this.breakPeriod = breakPeriod;
        this.businessDate = businessDate;
        this.overtime = overtime;
        this.workingPeriod = workingPeriod;
        this.employeeId = employeeId;
        this.totalWorkingHour = totalWorkingHour;
    }

    public String getBreakPeriod() {
        return breakPeriod;
    }

    public void setBreakPeriod(String breakPeriod) {
        this.breakPeriod = breakPeriod;
    }

    public LocalDate getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(LocalDate businessDate) {
        this.businessDate = businessDate;
    }

    public Double getOvertime() {
        return overtime;
    }

    public void setOvertime(Double overtime) {
        this.overtime = overtime;
    }

    public String getWorkingPeriod() {
        return workingPeriod;
    }

    public void setWorkingPeriod(String workingPeriod) {
        this.workingPeriod = workingPeriod;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Double getTotalWorkingHour() {
        return totalWorkingHour;
    }

    public void setTotalWorkingHour(Double totalWorkingHour) {
        this.totalWorkingHour = totalWorkingHour;
    }

    @Override
    public String toString() {
        return "{" +
                "\"employeeId\":" + employeeId +
                ", \"workingPeriod\":\"" + workingPeriod + '\"' +
                ", \"breakPeriod\":" + (breakPeriod != null ? "\"" + breakPeriod + "\"" : null) +
                ", \"businessDate\":\"" + businessDate + '\"' +
                ", \"totalworkinghour\":" + (totalWorkingHour != null ? totalWorkingHour.toString() : null) +
                ", \"overtime\":" + (overtime != null ? overtime.toString() : null) +
                "}";
    }
}
