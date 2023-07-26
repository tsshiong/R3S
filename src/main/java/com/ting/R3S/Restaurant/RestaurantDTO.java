package com.ting.R3S.Restaurant;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@NoArgsConstructor
public class RestaurantDTO {

    private Long accountId;
    private String restaurantName;
    private Time openTime;
    private Time closeTime;
    private String status;
    private String employeeName;

    public RestaurantDTO(Long accountId, String restaurantName, Time openTime, Time closeTime, String status, String employeeName) {
        this.accountId = accountId;
        this.restaurantName = restaurantName;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.status = status;
        this.employeeName = employeeName;
    }


    @Override
    public String toString() {
        return "RestaurantDTO{" +
                "accountId=" + accountId +
                ", restaurantName='" + restaurantName + '\'' +
                ", openTime=" + openTime +
                ", closeTime=" + closeTime +
                ", status='" + status + '\'' +
                ", employeeName='" + employeeName + '\'' +
                '}';
    }
}
