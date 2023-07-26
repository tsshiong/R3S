package com.ting.R3S.Actualsales;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface ActualsalesService {

    Double getActualsalesByDateRange(Long restaurantId, LocalDate startDate, LocalDate endDate);

    Top5RestaurantDTO getTop5Restaurants(LocalDate startDate, LocalDate endDate);

    List<SalesDTO> getSalesByMonth(LocalDate adjustedStartDate, LocalDate endDate, Long restaurantId);

    Top5SplhDTO getTop5SplhByDateRange(LocalDate startDate, LocalDate endDate);

    boolean importCsvData(MultipartFile file);

}
