package com.ting.R3S.Employee;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantEmployeeDTO {

    private Long employeeId;
    private String employeeName;
    private String position;
    private String employeeType;

    public RestaurantEmployeeDTO(Long employeeId, String employeeName, String position, String employeeType) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.position = position;
        this.employeeType = employeeType;
    }

    @Override
    public String toString() {
        return "RestaurantEmployeeDTO{" +
                "employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", position='" + position + '\'' +
                ", employeeType='" + employeeType + '\'' +
                '}';
    }
}
