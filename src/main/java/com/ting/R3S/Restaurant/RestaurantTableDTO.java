package com.ting.R3S.Restaurant;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
public class RestaurantTableDTO {

    private int end;
    private int length;
    private int page;
    private int pages;
    private long recordsDisplay;
    private long recordsTotal;
    private long recordsFiltered;
    private boolean serverSide;
    private int start;

    private List<RestaurantDataTableDTO> restaurants;

    public RestaurantTableDTO(int end, int length, int page, int pages, long recordsDisplay, long recordsTotal, long recordsFiltered, boolean serverSide, int start, List<RestaurantDataTableDTO> restaurants) {
        this.end = end;
        this.length = length;
        this.page = page;
        this.pages = pages;
        this.recordsDisplay = recordsDisplay;
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.serverSide = serverSide;
        this.start = start;
        this.restaurants = restaurants;
    }

    @Override
    public String toString() {
        return "RestaurantTableDTO{" +
                "end=" + end +
                ", length=" + length +
                ", page=" + page +
                ", pages=" + pages +
                ", recordsDisplay=" + recordsDisplay +
                ", recordsTotal=" + recordsTotal +
                ", recordsFiltered=" + recordsFiltered +
                ", serverSide=" + serverSide +
                ", start=" + start +
                ", restaurants=" + restaurants +
                '}';
    }
}
