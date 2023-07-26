package com.ting.R3S.Restaurant;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class RestaurantProjectedsalesDTO {

    private LocalDate businessDate;
    private Double projectedSales;
    private Double projectedLabourHour;
    private String restaurantName;
    private Time openTime;
    private Time closeTime;

    public RestaurantProjectedsalesDTO(LocalDate businessDate, Double projectedSales, Double projectedLabourHour, String restaurantName, Time openTime, Time closeTime) {
        this.businessDate = businessDate;
        this.projectedSales = projectedSales;
        this.projectedLabourHour = projectedLabourHour;
        this.restaurantName = restaurantName;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    @Override
    public String toString() {
        return "RestaurantProjectedsalesDTO{" +
                "businessDate=" + businessDate +
                ", projectedSales=" + projectedSales +
                ", projectedLabourHour=" + projectedLabourHour +
                ", restaurantName='" + restaurantName + '\'' +
                ", openTime=" + openTime +
                ", closeTime=" + closeTime +
                '}';
    }
}
