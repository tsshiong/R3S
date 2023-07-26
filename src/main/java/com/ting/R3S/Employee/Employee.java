package com.ting.R3S.Employee;

import com.ting.R3S.Restaurant.Restaurant;
import com.ting.R3S.Schedule.Schedule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long employeeId;
    private String employeeName;
    private String position;
    private Date hireDate;
    private String employeeType;

    @ManyToOne()
    @JoinColumn(name="restaurant_id", nullable=false)
    private Restaurant restaurant;

    @OneToMany(
            mappedBy = "employee"
    )
    private List<Schedule> schedule;

    public Employee (String employeeName, String position, Date hireDate, String employeeType, Long restaurantId) {
        this.employeeName = employeeName;
        this.position = position;
        this.hireDate = hireDate;
        this.employeeType = employeeType;
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        this.restaurant = restaurant;
    }
}
