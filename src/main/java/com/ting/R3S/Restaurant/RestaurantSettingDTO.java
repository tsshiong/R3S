package com.ting.R3S.Restaurant;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@NoArgsConstructor
public class RestaurantSettingDTO {

    private Long restaurantId;
    private String restaurantName;
    private Time openTime;
    private Time closeTime;
    private String status;
    private String email;
    private String password;


    public RestaurantSettingDTO(Long restaurantId, String restaurantName, Time openTime, Time closeTime, String status, String email, String password) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.status = status;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "RestaurantSettingDTO{" +
                "restaurantId=" + restaurantId +
                ", restaurantName='" + restaurantName + '\'' +
                ", openTime=" + openTime +
                ", closeTime=" + closeTime +
                ", status='" + status + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
