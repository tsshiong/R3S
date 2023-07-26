package com.ting.R3S.Projectedsales;

import com.ting.R3S.Restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "projectedsales")
public class Projectedsales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "business_date")
    private LocalDate businessDate;

    @Column(name = "projected_sales")
    private Double projectedSales;

    @Column(name = "projected_labour_hour")
    private Double projectedLabourHour;

    @ManyToOne()
    @JoinColumn(name="restaurant_id", nullable=false)
    private Restaurant restaurant;

    public Projectedsales (LocalDate businessDate, Double projectedSales, Double projectedLabourHour, Long restaurantId) {
        this.businessDate = businessDate;
        this.projectedSales = projectedSales;
        this.projectedLabourHour = projectedLabourHour;
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(restaurantId);
        this.restaurant = restaurant;
    }
}
