package com.ting.R3S.Schedule;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Data
@NoArgsConstructor
public class ScheduleDTO {

    private Double projectedSales;
    private Double projectedLabourSales;
    private Date businessDate;
    private Long restaurantId;
    private String restaurantName;
    private Time openTime;
    private Time closeTime;
    private Long employeeId;
    private String employeeName;
    private String position;
    private String employeeType;
    private Long id;
    private String workingPeriod;

    // Constructor
    public ScheduleDTO(Double projectedSales, Double projectedLabourSales, Date businessDate, Long restaurantId,
                       String restaurantName, Time openTime, Time closeTime, Long employeeId,
                       String employeeName, String position, String employeeType, Long id, String workingPeriod) {
        this.projectedSales = projectedSales;
        this.projectedLabourSales = projectedLabourSales;
        this.businessDate = businessDate;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.position = position;
        this.employeeType = employeeType;
        this.id = id;
        this.workingPeriod = workingPeriod;
    }

    @Override
    public String toString() {
        return "ScheduleDTO{" +
                "projectedSales=" + projectedSales +
                ", projectedLabourSales=" + projectedLabourSales +
                ", businessDate=" + businessDate +
                ", restaurantId=" + restaurantId +
                ", restaurantName='" + restaurantName + '\'' +
                ", openTime=" + openTime +
                ", closeTime=" + closeTime +
                ", employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", position='" + position + '\'' +
                ", employeeType='" + employeeType + '\'' +
                ", scheduleId=" + id +
                ", workingPeriod='" + workingPeriod + '\'' +
                '}';
    }
}
