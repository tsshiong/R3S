package com.ting.R3S.Schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    void deleteByBusinessDateAndEmployee_EmployeeIdIn(LocalDate businessDate, List<Long> employeeIds);

    @Query(
            value = "SELECT SUM(s.total_working_hour), SUM(s.overtime) " +
                    "FROM schedule s " +
                    "INNER JOIN employee e ON s.employee_id = e.employee_id " +
                    "INNER JOIN restaurant r ON e.restaurant_id = r.restaurant_id " +
                    "WHERE (:restaurantId IS NULL OR r.restaurant_id = :restaurantId) " +
                    "AND s.business_date BETWEEN :startDate AND :endDate",
            nativeQuery = true
    )
    List<Object[]> getActualTotalWorkingHourAndOvertimeByDateRange(Long restaurantId, LocalDate startDate, LocalDate endDate);

    @Modifying
    @Query("DELETE FROM Schedule s WHERE s.businessDate = :businessDate AND s.employee.employeeId = :employeeId")
    void deleteByBusinessDateAndEmployeeId(@Param("businessDate") LocalDate businessDate, @Param("employeeId") Long employeeId);

    @Query("SELECT s FROM Schedule s INNER JOIN Employee e ON s.employee.employeeId = e.employeeId WHERE s.businessDate = :businessDate AND e.restaurant.restaurantId = :restaurantId")
    List<Schedule> findByBusinessDateAndRestaurantId(@Param("businessDate") LocalDate businessDate, @Param("restaurantId") Long restaurantId);

    @Query("SELECT new com.ting.R3S.Schedule.WorkingEmployeeDTO(e.employeeId, e.employeeName) " +
            "FROM Schedule s " +
            "JOIN Employee e ON s.employee.employeeId = e.employeeId " +
            "WHERE s.businessDate = :businessDate AND e.restaurant.restaurantId = :restaurantId")
    List<WorkingEmployeeDTO> getWorkingEmployees(Long restaurantId, LocalDate businessDate);

    @Transactional
    @Modifying
    @Query("DELETE FROM Schedule s WHERE s.employee.employeeId = :employeeId AND s.businessDate = :businessDate")
    void deleteByEmployeeIdAndBusinessDate(@Param("employeeId") Long employeeId, @Param("businessDate") LocalDate businessDate);

}
