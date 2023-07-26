package com.ting.R3S.Employee;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AvailableEmployeeDTO {

    private Long employeeId;
    private String employeeName;
    private String employeeType;

    public AvailableEmployeeDTO(Long employeeId, String employeeName, String employeeType) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeType = employeeType;
    }

    @Override
    public String toString() {
        return "AvailableEmployeeDTO{" +
                "employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", employeeType='" + employeeType + '\'' +
                '}';
    }
}
