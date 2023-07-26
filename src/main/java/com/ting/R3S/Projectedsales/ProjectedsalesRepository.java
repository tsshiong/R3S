package com.ting.R3S.Projectedsales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProjectedsalesRepository extends JpaRepository<Projectedsales, Long> {

    List<Projectedsales> findByRestaurantRestaurantId(Long restaurantId);

    @Query("SELECT p FROM Projectedsales p WHERE p.businessDate = :businessDate AND p.restaurant.restaurantId = :restaurantId")
    Projectedsales findByBusinessDateAndRestaurantId(@Param("businessDate") LocalDate businessDate, @Param("restaurantId") Long restaurantId);
}
