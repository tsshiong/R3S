package com.ting.R3S.Actualsales;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SalesDTO {

    private int month;
    private double actualSales;
    private double projectedSales;
    private double projectedLabourHour;
    private double actualLabourHour;

    public SalesDTO(int month, double actualSales, double projectedSales, double projectedLabourHour, double actualLabourHour) {
        this.month = month;
        this.actualSales = actualSales;
        this.projectedSales = projectedSales;
        this.projectedLabourHour = projectedLabourHour;
        this.actualLabourHour = actualLabourHour;
    }

    @Override
    public String toString() {
        return "SalesDTO{" +
                "month=" + month +
                ", actualSales=" + actualSales +
                ", projectedSales=" + projectedSales +
                ", projectedLabourHour=" + projectedLabourHour +
                ", actualLabourHour=" + actualLabourHour +
                '}';
    }
}
