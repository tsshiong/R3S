package com.ting.R3S.Restaurant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public interface RestaurantService {

    long countRestaurants();

    Page<RestaurantDataTableDTO> searchRestaurants(String searchTerm, String status, Pageable pageable, String sortColumn, String sortDirection);

    List<Object> getRestaurantById(Long restaurantId);

    void updateRestaurantById(String editedResName, Time editedResOpen, Time editedResClose, String editedResStatus, Long restaurantId);

    void deleteRestaurants(List<Long> restaurantIds);

    void updateRestaurantStatusById(Long restaurantId,String status);

    void saveRestaurant(Long ResAccountId, String ResName, Time ResOpen, Time ResClose, String ResStatus);

    List<Object[]> getAllRestaurants();

    List<Object> findRestaurantByName(String name);

    RestaurantProjectedsalesDTO getSalesData(LocalDate businessDate, Long restaurantId);

    RestaurantProjectsalesWeekDTO getRestaurantProjectedSales(Long restaurantId, LocalDate businessStartDate, LocalDate businessEndDate);

    boolean importCsvData(MultipartFile file);

    List<RestaurantAccountDTO> getRestaurantsByAccountId(Long accountId);

    List<RestaurantSettingDTO> getRestaurantDetails(Long accountId);

    boolean updateRestaurantSettings(Long accountId, UpdateRequest request);
}
