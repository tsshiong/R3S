package com.ting.R3S.Restaurant;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class RestaurantProjectedsalesDailyDTO {

    private LocalDate businessDate;
    private Double projectedSales;
    private Double projectedLabourHour;


    public RestaurantProjectedsalesDailyDTO(LocalDate businessDate, Double projectedSales, Double projectedLabourHour) {
        this.businessDate = businessDate;
        this.projectedSales = projectedSales;
        this.projectedLabourHour = projectedLabourHour;
    }

    @Override
    public String toString() {
        return "RestaurantProjectedsalesDailyDTO{" +
                "businessDate=" + businessDate +
                ", projectedSales=" + projectedSales +
                ", projectedLabourHour=" + projectedLabourHour +
                '}';
    }
}
