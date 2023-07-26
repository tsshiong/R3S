package com.ting.R3S.Employee;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RestaurantScheduleWeekDTO {

    private List<EmployeeScheduleWeekDTO> scheduleWeek;
    private List<RestaurantEmployeeDTO> employeeWeek;

    public RestaurantScheduleWeekDTO(List<EmployeeScheduleWeekDTO> scheduleWeek, List<RestaurantEmployeeDTO> employeeWeek) {
        this.scheduleWeek = scheduleWeek;
        this.employeeWeek = employeeWeek;
    }


    @Override
    public String toString() {
        return "RestaurantScheduleWeekDTO{" +
                "scheduleWeek=" + scheduleWeek +
                ", employeeWeek=" + employeeWeek +
                '}';
    }
}
