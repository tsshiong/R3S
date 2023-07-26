package com.ting.R3S.Restaurant;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@NoArgsConstructor
public class RestaurantDataTableDTO {

    private Long restaurantId;
    private String restaurantName;
    private Time openTime;
    private Time closeTime;
    private String status;
    private String employeeName;


    public RestaurantDataTableDTO(Long restaurantId, String restaurantName, Time openTime, Time closeTime, String status, String employeeName) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.status = status;
        this.employeeName = employeeName;
    }

    @Override
    public String toString() {
        return "RestaurantDataTableDTO{" +
                "restaurantId=" + restaurantId +
                ", restaurantName='" + restaurantName + '\'' +
                ", openTime=" + openTime +
                ", closeTime=" + closeTime +
                ", status='" + status + '\'' +
                ", employeeName='" + employeeName + '\'' +
                '}';
    }
}
