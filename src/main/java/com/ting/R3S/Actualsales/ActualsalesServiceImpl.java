package com.ting.R3S.Actualsales;

import com.ting.R3S.Restaurant.Restaurant;
import com.ting.R3S.Restaurant.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActualsalesServiceImpl implements ActualsalesService{

    @Autowired
    public ActualsalesRepository actualsalesRepository;

    @Autowired
    public RestaurantRepository restaurantRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Double getActualsalesByDateRange(Long restaurantId, LocalDate startDate, LocalDate endDate) {
        return actualsalesRepository.getActualsalesByDateRange(restaurantId, startDate, endDate);
    }

    @Override
    public Top5RestaurantDTO getTop5Restaurants(LocalDate startDate, LocalDate endDate) {
        List<Object[]> top5Data = actualsalesRepository.findTop5Restaurants(startDate, endDate);

        List<String> restaurantNames = new ArrayList<>();
        List<Double> actualSales = new ArrayList<>();

        for (Object[] data : top5Data) {
            String restaurantName = (String) data[0];
            Double sales = (Double) data[1];

            restaurantNames.add(restaurantName);
            actualSales.add(sales);
        }

        return new Top5RestaurantDTO(restaurantNames, actualSales);
    }

    @Override
    public List<SalesDTO> getSalesByMonth(LocalDate adjustedStartDate, LocalDate endDate, Long restaurantId) {
        return actualsalesRepository.getSalesByMonth(adjustedStartDate, endDate, restaurantId);
    }

    @Override
    public Top5SplhDTO getTop5SplhByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = actualsalesRepository.getTop5SplhByDateRange(startDate, endDate);

        List<String> restaurantNames = new ArrayList<>();
        List<Double> splhs = new ArrayList<>();

        for (Object[] row : results) {
            String restaurantName = (String) row[0];
            Double splh = (Double) row[1];

            restaurantNames.add(restaurantName);
            splhs.add(splh);
        }

        return new Top5SplhDTO(restaurantNames, splhs);
    }

    @Override
    public boolean importCsvData(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;

            // Define the expected header
            String expectedHeader = "actual_sales,business_date,restaurant_id,actual_labour_hour";

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    // Check if the header matches the expected format
                    if (!line.equals(expectedHeader)) {
                        // Invalid header format
                        return false;
                    }
                    // Skip the header line
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(",");
                if (data.length != 4) {
                    // Invalid data format
                    return false;
                }

                String businessDateStr = data[1];
                String actualSalesStr = data[0];
                String actualLabourHourStr = data[3];
                String restaurantIdStr = data[2];

                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(businessDateStr, dateFormat);
                java.sql.Date businessDate = java.sql.Date.valueOf(date);
                double actualSales = Double.parseDouble(actualSalesStr);
                double actualLabourHour = Double.parseDouble(actualLabourHourStr);
                Long restaurantId = Long.valueOf(restaurantIdStr);

//                Actualsales actualsales = new Actualsales();
//                actualsales.setBusinessDate(businessDate);
//                actualsales.setActualSales(actualSales);
//                actualsales.setActualLabourHour(actualLabourHour);

                // Disable foreign key constraint checks
                jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");

//                Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
//                if (restaurant != null) {
//                    actualsales.setRestaurant(restaurant);
//                    actualsalesRepository.save(actualsales);
//                }

                // Check if the data already exists in the database based on businessDate and restaurantId
                Actualsales existingData = actualsalesRepository.findByBusinessDateAndRestaurantId(businessDate, restaurantId);

                if (existingData != null) {
                    // Update the existing data
                    existingData.setActualSales(actualSales);
                    existingData.setActualLabourHour(actualLabourHour);
                    actualsalesRepository.save(existingData);
                } else {
                    Actualsales actualsales = new Actualsales();
                    actualsales.setBusinessDate(businessDate);
                    actualsales.setActualSales(actualSales);
                    actualsales.setActualLabourHour(actualLabourHour);

                    Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
                    if (restaurant != null) {
                        actualsales.setRestaurant(restaurant);
                        actualsalesRepository.save(actualsales);
                    }
                }


                // Enable foreign key constraint checks
                jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1;");

            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
