package com.ting.R3S.Actualsales;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Top5SplhDTO {

    private List<String> restaurantName;
    private List<Double> splh;


    public Top5SplhDTO(List<String> restaurantName, List<Double> splh) {
        this.restaurantName = restaurantName;
        this.splh = splh;
    }

    @Override
    public String toString() {
        return "Top5SplhDTO{" +
                "restaurantName=" + restaurantName +
                ", splh=" + splh +
                '}';
    }
}
