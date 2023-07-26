package com.ting.R3S.Restaurant;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.List;

@Data
@NoArgsConstructor
public class RestaurantProjectsalesWeekDTO {


    private String restaurantName;
    private Time openTime;
    private Time closeTime;

    private List<RestaurantProjectedsalesDailyDTO> projectsalesweek;


    public RestaurantProjectsalesWeekDTO(String restaurantName, Time openTime, Time closeTime, List<RestaurantProjectedsalesDailyDTO> projectsalesweek) {
        this.restaurantName = restaurantName;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.projectsalesweek = projectsalesweek;
    }

    @Override
    public String toString() {
        return "RestaurantProjectsalesWeekDTO{" +
                "restaurantName='" + restaurantName + '\'' +
                ", openTime=" + openTime +
                ", closeTime=" + closeTime +
                ", projectsalesweek=" + projectsalesweek +
                '}';
    }
}
