package com.ting.R3S.Projectedsales;

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

@Service
public class ProjectedsalesServiceImpl implements ProjectedsalesService{

    @Autowired
    private ProjectedsalesRepository projectedsalesRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean importCsvData(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;

            // Define the expected header
            String expectedHeader = "business_date,projected_sales,projected_labour_hour,restaurant_id";

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

                String businessDateStr = data[0];
                String projectedSalesStr = data[1];
                String projectedLabourHourStr = data[2];
                String restaurantIdStr = data[3];

                // Convert the openTime and closeTime strings to Time objects
                LocalDate businessDate = LocalDate.parse(businessDateStr);
                Double projectedSales = Double.valueOf(projectedSalesStr);
                Double projectedLabourHour = Double.valueOf(projectedLabourHourStr);
                Long restaurantId = Long.valueOf(restaurantIdStr);

//                Projectedsales projectedsales = new Projectedsales();
//                projectedsales.setBusinessDate(businessDate);
//                projectedsales.setProjectedSales(projectedSales);
//                projectedsales.setProjectedLabourHour(projectedLabourHour);

                // Disable foreign key constraint checks
                jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");

//                Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
//                if (restaurant != null) {
//                    projectedsales.setRestaurant(restaurant);
//                    projectedsalesRepository.save(projectedsales);
//                }
                // Check if the data already exists in the database based on businessDate and restaurantId
                Projectedsales existingData = projectedsalesRepository.findByBusinessDateAndRestaurantId(businessDate, restaurantId);

                if (existingData != null) {
                    // Update the existing data
                    existingData.setProjectedSales(projectedSales);
                    existingData.setProjectedLabourHour(projectedLabourHour);
                    projectedsalesRepository.save(existingData);
                } else {
                    Projectedsales projectedsales12 = new Projectedsales();
                    projectedsales12.setBusinessDate(businessDate);
                    projectedsales12.setProjectedSales(projectedSales);
                    projectedsales12.setProjectedLabourHour(projectedLabourHour);

                    Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
                    if (restaurant != null) {
                        projectedsales12.setRestaurant(restaurant);
                        projectedsalesRepository.save(projectedsales12);
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
