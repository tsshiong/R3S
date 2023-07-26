package com.ting.R3S.Actualsales;

import com.ting.R3S.Restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Actualsales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_date")
    private Date businessDate;

    @Column(name = "actual_sales")
    private double actualSales;

    @Column(name = "actual_labour_hour")
    private double actualLabourHour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "restaurantId")
    private Restaurant restaurant;

    public Actualsales (Date businessDate, double actualSales,double actualLabourHour, Long restaurantId) {
        this.businessDate = businessDate;
        this.actualSales = actualSales;
        this.actualLabourHour = actualLabourHour;
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        this.restaurant = restaurant;
    }
}
