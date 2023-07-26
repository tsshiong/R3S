package com.ting.R3S.Restaurant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {


    @Query("SELECT new com.ting.R3S.Restaurant.RestaurantDataTableDTO(r.restaurantId, r.restaurantName, r.openTime, r.closeTime, r.status, COALESCE(e.employeeName, '')) " +
            "FROM Restaurant r INNER JOIN r.employee e " +
            "WHERE (CAST(r.restaurantId AS string) LIKE %:searchTerm% OR r.restaurantName LIKE %:searchTerm% OR e.employeeName LIKE %:searchTerm%) " +
            "AND (IFNULL(:status, '') = 'all' OR r.status = :status) " +
            "AND e.position = 'manager' " +
            "ORDER BY " +
            "CASE WHEN (:sortColumn is null OR :sortDirection is null) THEN r.restaurantId END ASC, " +
            "CASE WHEN :sortColumn = 'restaurantId' AND :sortDirection = 'asc' THEN r.restaurantId END ASC, " +
            "CASE WHEN :sortColumn = 'restaurantId' AND :sortDirection = 'desc' THEN r.restaurantId END DESC," +
            "CASE WHEN :sortColumn = 'restaurantName' AND :sortDirection = 'asc' THEN r.restaurantName END ASC, " +
            "CASE WHEN :sortColumn = 'restaurantName' AND :sortDirection = 'desc' THEN r.restaurantName END DESC, " +
            "CASE WHEN :sortColumn = 'employeeName' AND :sortDirection = 'asc' THEN e.employeeName END ASC, " +
            "CASE WHEN :sortColumn = 'employeeName' AND :sortDirection = 'desc' THEN e.employeeName END DESC")
    Page<RestaurantDataTableDTO> searchRestaurants(
            @Param("searchTerm") String searchTerm,
            @Param("status") String status,
            Pageable pageable,
            @Param("sortColumn") String sortColumn,
            @Param("sortDirection") String sortDirection);

    @Query("SELECT r.restaurantId, r.restaurantName FROM Restaurant r")
    List<Object[]> findAllRestaurants();

    Restaurant findByRestaurantName(String restaurantName);

    @Query("SELECT new com.ting.R3S.Restaurant.RestaurantProjectedsalesDTO(p.businessDate, p.projectedSales, p.projectedLabourHour, r.restaurantName, r.openTime, r.closeTime) " +
            "FROM Restaurant r LEFT JOIN r.projectedsales p ON p.businessDate = :businessDate " +
            "WHERE r.restaurantId = :restaurantId ")
    RestaurantProjectedsalesDTO findByBusinessDateAndRestaurantId(@Param("businessDate") LocalDate businessDate, @Param("restaurantId") Long restaurantId);


    @Query("SELECT new com.ting.R3S.Restaurant.RestaurantProjectedsalesDailyDTO(p.businessDate, p.projectedSales, p.projectedLabourHour) " +
            "FROM Restaurant r LEFT JOIN r.projectedsales p ON (p.businessDate BETWEEN :businessStartDate AND :businessEndDate) " +
            "WHERE r.restaurantId = :restaurantId")
    List<RestaurantProjectedsalesDailyDTO> findProjectedSalesByDateRange(@Param("restaurantId") Long restaurantId,
                                                                         @Param("businessStartDate") LocalDate businessStartDate,
                                                                         @Param("businessEndDate") LocalDate businessEndDate);

    @Query("SELECT new com.ting.R3S.Restaurant.RestaurantAccountDTO(r.restaurantId, r.restaurantName, r.openTime, r.closeTime, r.status) " +
            "FROM Restaurant r " +
            "WHERE r.account.accountId = :accountId")
    List<RestaurantAccountDTO> getRestaurantsByAccountId(Long accountId);

    @Query("SELECT new com.ting.R3S.Restaurant.RestaurantSettingDTO(r.restaurantId, r.restaurantName, r.openTime, r.closeTime, r.status, a.email, a.password) FROM Restaurant r JOIN Account a ON r.account.accountId = a.accountId WHERE r.account.accountId = :accountId")
    List<RestaurantSettingDTO> getRestaurantDetails(@Param("accountId") Long accountId);

    Restaurant findByAccountAccountId(Long accountId);
}
