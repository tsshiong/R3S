package com.ting.R3S.Restaurant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost")
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/restaurant-table")
    public ResponseEntity<RestaurantTableDTO> getRestaurants(@RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "size", defaultValue = "10") int size,
                                                       @RequestParam(value = "search", required = false, defaultValue = "") String search,
                                                       @RequestParam(value = "sortColumn", required = false, defaultValue = "restaurantId") String sortColumn,
                                                       @RequestParam(value = "sortDirection", required = false,defaultValue = "asc") String sortDirection,
                                                       @RequestParam(value = "status", required = false, defaultValue = "all") String status) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<RestaurantDataTableDTO> restaurants = restaurantService.searchRestaurants(search, status, pageRequest, sortColumn, sortDirection);

        long recordsTotal = restaurantService.countRestaurants();

        RestaurantTableDTO restaurantTableDTO = new RestaurantTableDTO(
                restaurants.getNumber() * restaurants.getSize(),
                restaurants.getSize(),
                restaurants.getNumber(),
                restaurants.getTotalPages(),
                restaurants.getTotalElements(),
                recordsTotal,
                restaurants.getTotalElements(),
                true,
                restaurants.getNumber() * restaurants.getSize() + 1,
                restaurants.getContent()
        );

        return ResponseEntity.ok().body(restaurantTableDTO);
    }

    @GetMapping("/getrestaurant/{restaurantId}")
    public ResponseEntity<List<Object>> getRestaurantById(@PathVariable("restaurantId") Long restaurantId) {
        List<Object> restaurantDetail = restaurantService.getRestaurantById(restaurantId);
        return new ResponseEntity<>(restaurantDetail, HttpStatus.OK);
    }

    @PutMapping("/update_restaurant/{restaurantId}")
    public ResponseEntity<Void> updateRestaurantById(@RequestBody RestaurantDTO restaurantDTO, @PathVariable("restaurantId") Long restaurantId) {
        String editedResName = restaurantDTO.getRestaurantName();
        Time editedResOpen = restaurantDTO.getOpenTime();
        Time editedResClose = restaurantDTO.getCloseTime();
        String editedResStatus = restaurantDTO.getStatus();
        restaurantService.updateRestaurantById(editedResName, editedResOpen, editedResClose, editedResStatus, restaurantId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete-restaurants")
    public ResponseEntity<String> deleteRestaurants(@RequestBody List<Long> restaurantIds) {
        restaurantService.deleteRestaurants(restaurantIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/updatestatus/{restaurantId}/{status}")
    public ResponseEntity<Void> updateRestaurantStatusById(@PathVariable("restaurantId") Long restaurantId, @PathVariable("status") String status) {
        restaurantService.updateRestaurantStatusById(restaurantId, status);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add_restaurant")
    public void addRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        restaurantService.saveRestaurant(
                restaurantDTO.getAccountId(),
                restaurantDTO.getRestaurantName(),
                restaurantDTO.getOpenTime(),
                restaurantDTO.getCloseTime(),
                restaurantDTO.getStatus()

        );
    }

    @GetMapping("/all_restaurants")
    public ResponseEntity<List<Object[]>> getAllRestaurants() {
        List<Object[]> restaurants = restaurantService.getAllRestaurants();
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }

    @GetMapping("/find_by_name/{restaurantName}")
    public ResponseEntity<List<Object>> findRestaurantByName(@PathVariable String restaurantName) {
        List<Object> restaurantDetail = restaurantService.findRestaurantByName(restaurantName);
        return new ResponseEntity<>(restaurantDetail, HttpStatus.OK);
    }

    @GetMapping("/getprojectedsalesdata")
    public ResponseEntity<RestaurantProjectedsalesDTO> getSalesData(@RequestParam(value = "businessDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate businessDate,
                                                          @RequestParam(value = "restaurantId", defaultValue = "1") Long restaurantId) {
        RestaurantProjectedsalesDTO restaurantProjectedsalesDTO = restaurantService.getSalesData(businessDate, restaurantId);
        if (restaurantProjectedsalesDTO == null || restaurantProjectedsalesDTO.getBusinessDate() == null) {
            restaurantProjectedsalesDTO.setBusinessDate(businessDate);
            restaurantProjectedsalesDTO.setProjectedSales(Double.valueOf(0.0));
            restaurantProjectedsalesDTO.setProjectedLabourHour(Double.valueOf(0.0));
            return ResponseEntity.ok(restaurantProjectedsalesDTO);
        }
        return ResponseEntity.ok(restaurantProjectedsalesDTO);
    }

    @GetMapping("/getprojectedsalesdataweek")
    public ResponseEntity<RestaurantProjectsalesWeekDTO> getRestaurantProjectedSales(@RequestParam(value = "businessStartDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate businessStartDate,
                                                                    @RequestParam(value = "businessEndDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate businessEndDate,
                                                                    @RequestParam(value = "restaurantId", defaultValue = "1") Long restaurantId) {
        RestaurantProjectsalesWeekDTO result = restaurantService.getRestaurantProjectedSales(restaurantId, businessStartDate, businessEndDate);
        return ResponseEntity.ok(result);

    }

    @PostMapping("/import-csv")
    public ResponseEntity<String> importCsvData(@RequestParam("file") MultipartFile file) {
        boolean importSuccess = restaurantService.importCsvData(file);
        if (importSuccess) {
            return ResponseEntity.ok("CSV data imported successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to import CSV data");
        }
    }

    @GetMapping("/getrestaurantbyaccid/{accountId}")
    public List<RestaurantAccountDTO> getRestaurantsByAccountId(@PathVariable Long accountId) {
        return restaurantService.getRestaurantsByAccountId(accountId);
    }

    @GetMapping("/setting/{accountId}")
    public List<RestaurantSettingDTO> getRestaurantDetails(@PathVariable Long accountId) {
        return restaurantService.getRestaurantDetails(accountId);
    }

    @PostMapping("/updatesetting/{accountId}")
    public ResponseEntity<String> updateRestaurantSettings(@PathVariable Long accountId, @RequestBody UpdateRequest request) {
        boolean updated = restaurantService.updateRestaurantSettings(accountId, request);
        if (updated) {
            return ResponseEntity.ok("Restaurant settings updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to update restaurant settings.");
        }
    }
}