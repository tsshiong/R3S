package com.ting.R3S.Employee;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@Builder
public class EmployeeDTO {

    private Long employeeId;
    private String employeeName;
    private String position;
    private Date hireDate;
    private String employeeType;
    private String restaurantName;
    private Long restaurantId;


    public EmployeeDTO(Long employeeId, String employeeName, String position, Date hireDate, String employeeType, String restaurantName, Long restaurantId) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.position = position;
        this.hireDate = hireDate;
        this.employeeType = employeeType;
        this.restaurantName = restaurantName;
        this.restaurantId = restaurantId;
    }


    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", position='" + position + '\'' +
                ", hireDate=" + hireDate +
                ", employeeType='" + employeeType + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
