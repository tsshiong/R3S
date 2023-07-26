package com.ting.R3S.Employee;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RestaurantScheduleDTO {

    private List<EmployeeScheduleDTO> schedule;
    private List<RestaurantEmployeeDTO> employee;

    public RestaurantScheduleDTO(List<EmployeeScheduleDTO> schedule, List<RestaurantEmployeeDTO> employee ) {
        this.schedule = schedule;
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "RestaurantScheduleDTO{" +
                "employee=" + employee +
                ", schedule=" + schedule +
                '}';
    }
}
