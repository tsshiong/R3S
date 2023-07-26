package com.ting.R3S.Actualsales;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost")
@RequestMapping("/sales")
public class ActualsalesController {

    @Autowired
    private ActualsalesService actualsalesService;

    @GetMapping("/totalactualsales")
    public ResponseEntity<Map<String, Double>> getActualsalesByDateRange(
            @RequestParam @Nullable Long restaurantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Double totalSales = actualsalesService.getActualsalesByDateRange(restaurantId, startDate, endDate);
        if (totalSales == null) {
            totalSales = 0.0; // Set totalSales to 0 if data is not available
        }
        Map<String, Double> response = new HashMap<>();
        response.put("totalSales", totalSales);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/top5restaurants")
    public ResponseEntity<Top5RestaurantDTO> getTop5Restaurants(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Top5RestaurantDTO top5Restaurants = actualsalesService.getTop5Restaurants(startDate, endDate);
        return ResponseEntity.ok(top5Restaurants);
    }

    @GetMapping("/actualvsprojected")
    public ResponseEntity<List<SalesDTO>> getSalesByMonth(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam @Nullable Long restaurantId) {

        // Adjust start date if it's within 3 months or less
        LocalDate adjustedStartDate = startDate;
        LocalDate threeMonthsAgo = endDate.minusMonths(3);
        if (startDate.isAfter(threeMonthsAgo)) {
            adjustedStartDate = threeMonthsAgo.withDayOfMonth(1);
        }

        List<SalesDTO> sales = actualsalesService.getSalesByMonth(adjustedStartDate, endDate, restaurantId);

        // Fill missing months with 0.0 values
        List<SalesDTO> filledSales = fillMissingMonths(sales, adjustedStartDate, endDate);

        return new ResponseEntity<>(filledSales, HttpStatus.OK);
    }

    private List<SalesDTO> fillMissingMonths(List<SalesDTO> sales, LocalDate startDate, LocalDate endDate) {
        List<SalesDTO> filledSales = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            boolean found = false;
            for (SalesDTO sale : sales) {
                if (sale.getMonth() == currentDate.getMonthValue()) {
                    filledSales.add(sale);
                    found = true;
                    break;
                }
            }
            if (!found) {
                filledSales.add(new SalesDTO(currentDate.getMonthValue(), 0.0, 0.0, 0.0, 0.0));
            }
            currentDate = currentDate.plusMonths(1);
        }

        return filledSales;
    }


    @GetMapping("/top5splhrestaurants")
    public ResponseEntity<Top5SplhDTO> getTop5SplhRestaurants(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        Top5SplhDTO top5splhdto = actualsalesService.getTop5SplhByDateRange(startDate, endDate);
        return ResponseEntity.ok(top5splhdto);
    }

    @PostMapping("/import-csv")
    public ResponseEntity<String> importCsvData(@RequestParam("file") MultipartFile file) {
        boolean importSuccess = actualsalesService.importCsvData(file);
        if (importSuccess) {
            return ResponseEntity.ok("CSV data imported successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to import CSV data");
        }
    }
}
