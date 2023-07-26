package com.ting.R3S.Actualsales;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Top5RestaurantDTO {

    private List<String> restaurantName;
    private List<Double> actualSales;


    public Top5RestaurantDTO(List<String> restaurantName, List<Double> actualSales) {
        this.restaurantName = restaurantName;
        this.actualSales = actualSales;
    }

    @Override
    public String toString() {
        return "Top5RestaurantDTO{" +
                "restaurantName=" + restaurantName +
                ", actualSales=" + actualSales +
                '}';
    }
}
