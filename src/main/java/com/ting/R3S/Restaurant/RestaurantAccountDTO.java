package com.ting.R3S.Restaurant;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@NoArgsConstructor
public class RestaurantAccountDTO {

    private Long restaurantId;
    private String restaurantName;
    private Time openTime;
    private Time closeTime;
    private String status;

    public RestaurantAccountDTO(Long restaurantId, String restaurantName, Time openTime, Time closeTime, String status) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.status = status;
    }

    @Override
    public String toString() {
        return "RestaurantAccountDTO{" +
                "restaurantId=" + restaurantId +
                ", restaurantName='" + restaurantName + '\'' +
                ", openTime=" + openTime +
                ", closeTime=" + closeTime +
                ", status='" + status + '\'' +
                '}';
    }
}
