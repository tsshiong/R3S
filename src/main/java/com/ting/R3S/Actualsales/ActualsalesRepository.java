package com.ting.R3S.Actualsales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActualsalesRepository extends JpaRepository<Actualsales, Long> {

    List<Actualsales> findByRestaurantRestaurantId(Long restaurantId);

    @Query("SELECT p FROM Actualsales p WHERE p.businessDate = :businessDate AND p.restaurant.restaurantId = :restaurantId")
    Actualsales findByBusinessDateAndRestaurantId(@Param("businessDate") Date businessDate, @Param("restaurantId") Long restaurantId);

    @Query(
            value = "SELECT SUM(s.actual_sales) FROM Actualsales s WHERE (?1 IS NULL OR s.restaurant_id = ?1) AND (s.business_date BETWEEN ?2 AND ?3)",
            nativeQuery = true
    )
    Double getActualsalesByDateRange(Long restaurantId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT r.restaurantName, SUM(a.actualSales) " +
            "FROM Actualsales a " +
            "JOIN a.restaurant r " +
            "WHERE a.businessDate BETWEEN :startDate AND :endDate " +
            "GROUP BY r.restaurantName " +
            "ORDER BY SUM(a.actualSales) DESC " +
            "LIMIT 5")
    List<Object[]> findTop5Restaurants(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT NEW com.ting.R3S.Actualsales.SalesDTO(MONTH(a.businessDate), " +
            "COALESCE(SUM(a.actualSales), 0), COALESCE(SUM(p.projectedSales), 0), " +
            "COALESCE(SUM(p.projectedLabourHour), 0), COALESCE(SUM(a.actualLabourHour), 0)) " +
            "FROM Actualsales a " +
            "LEFT JOIN Projectedsales p ON (a.businessDate = p.businessDate AND a.restaurant.restaurantId = p.restaurant.restaurantId)" +
            "WHERE a.businessDate BETWEEN :adjustedStartDate AND :endDate " +
            "AND (:restaurantId IS NULL OR (a.restaurant.restaurantId = :restaurantId AND p.restaurant.restaurantId = :restaurantId)) " +
            "GROUP BY MONTH(a.businessDate)")
    List<SalesDTO> getSalesByMonth(LocalDate adjustedStartDate, LocalDate endDate, Long restaurantId);


    @Query("SELECT r.restaurantName, " +
            "ROUND(SUM(a.actualSales) / SUM(a.actualLabourHour), 2) AS splh " +
            "FROM Actualsales a " +
            "JOIN a.restaurant r ON a.restaurant.restaurantId = r.restaurantId " +
            "WHERE a.businessDate BETWEEN :startDate AND :endDate " +
            "GROUP BY r.restaurantId " +
            "ORDER BY splh DESC " +
            "LIMIT 5")
    List<Object[]> getTop5SplhByDateRange(LocalDate startDate, LocalDate endDate);
}
