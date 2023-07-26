package com.ting.R3S.Employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT new com.ting.R3S.Employee.EmployeeDTO(e.employeeId, e.employeeName, e.position, e.hireDate, e.employeeType, COALESCE(r.restaurantName, ''), r.restaurantId) " +
            "FROM Employee e INNER JOIN e.restaurant r " +
            "WHERE (CAST(e.employeeId AS string) LIKE %:searchTerm% OR e.employeeName LIKE %:searchTerm% OR r.restaurantName LIKE %:searchTerm%) " +
            "AND (IFNULL(:position, '') = 'all' OR e.position = :position) " +
            "AND (IFNULL(:employeeType, '') = 'all' OR e.employeeType = :employeeType) " +
            "ORDER BY " +
            "CASE WHEN (:sortColumn is null OR :sortDirection is null) THEN e.employeeId END ASC, " +
            "CASE WHEN :sortColumn = 'employeeId' AND :sortDirection = 'asc' THEN e.employeeId END ASC, " +
            "CASE WHEN :sortColumn = 'employeeId' AND :sortDirection = 'desc' THEN e.employeeId END DESC," +
            "CASE WHEN :sortColumn = 'employeeName' AND :sortDirection = 'asc' THEN e.employeeName END ASC, " +
            "CASE WHEN :sortColumn = 'employeeName' AND :sortDirection = 'desc' THEN e.employeeName END DESC, " +
            "CASE WHEN :sortColumn = 'hireDate' AND :sortDirection = 'asc' THEN e.hireDate END ASC, " +
            "CASE WHEN :sortColumn = 'hireDate' AND :sortDirection = 'desc' THEN e.hireDate END DESC, " +
            "CASE WHEN :sortColumn = 'restaurantName' AND :sortDirection = 'asc' THEN r.restaurantName END ASC, " +
            "CASE WHEN :sortColumn = 'restaurantName' AND :sortDirection = 'desc' THEN r.restaurantName END DESC")
    Page<EmployeeDTO> searchEmployees(
            @Param("searchTerm") String searchTerm,
            @Param("position") String position,
            @Param("employeeType") String employeeType,
            Pageable pageable,
            @Param("sortColumn") String sortColumn,
            @Param("sortDirection") String sortDirection);


    @Query("SELECT new com.ting.R3S.Employee.EmployeeScheduleDTO(s.id, s.employee.employeeId, s.workingPeriod, s.breakPeriod) "
            + "FROM Employee e JOIN e.schedule s "
            + "WHERE e.restaurant.restaurantId = :restaurantId AND s.businessDate = :businessDate "
            + "GROUP BY s.id, s.employee.employeeId, s.workingPeriod")
    List<EmployeeScheduleDTO> findEmployeeSchedules(@Param("restaurantId") Long restaurantId, @Param("businessDate") LocalDate businessDate);

    @Query("SELECT new com.ting.R3S.Employee.RestaurantEmployeeDTO(e.employeeId, e.employeeName, e.position, e.employeeType) "
            + "FROM Employee e "
            + "WHERE e.restaurant.restaurantId = :restaurantId "
            + "GROUP BY e.employeeId, e.employeeName, e.position, e.employeeType")
    List<RestaurantEmployeeDTO> findRestaurantEmployees(@Param("restaurantId") Long restaurantId);

    @Query("SELECT new com.ting.R3S.Employee.EmployeeScheduleWeekDTO(s.id, s.employee.employeeId, s.workingPeriod, s.breakPeriod, s.businessDate) "
            + "FROM Employee e JOIN e.schedule s "
            + "WHERE e.restaurant.restaurantId = :restaurantId AND (s.businessDate BETWEEN :businessStartDate AND :businessEndDate) "
            + "GROUP BY s.id, s.employee.employeeId, s.workingPeriod")
    List<EmployeeScheduleWeekDTO> findEmployeeSchedulesWeek(@Param("restaurantId") Long restaurantId, @Param("businessStartDate") LocalDate businessStartDate, @Param("businessEndDate") LocalDate businessEndDate);

    @Query("SELECT new com.ting.R3S.Employee.AvailableEmployeeDTO(e.employeeId, e.employeeName, e.employeeType) "
            + "FROM Employee e "
            + "WHERE e.restaurant.restaurantId = :restaurantId")
    List<AvailableEmployeeDTO> getAllEmployees(@Param("restaurantId") Long restaurantId);

    @Query("SELECT SUM(CASE WHEN e.employeeType = 'Full Time' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN e.employeeType = 'Part Time' THEN 1 ELSE 0 END) " +
            "FROM Employee e " +
            "WHERE (:restaurantId IS NULL OR e.restaurant.restaurantId = :restaurantId)")
    List<Object[]> getTotalEmployeesByRestaurantId(@Param("restaurantId") Long restaurantId);

    List<Employee> findByRestaurantRestaurantId(Long restaurantId);

    @Query("SELECT new com.ting.R3S.Employee.EmployeeDTO(e.employeeId, e.employeeName, e.position, e.hireDate, e.employeeType, COALESCE(r.restaurantName, ''), r.restaurantId) " +
            "FROM Employee e INNER JOIN e.restaurant r " +
            "WHERE e.restaurant.restaurantId = :selectedRestaurantId " +
            "AND (CAST(e.employeeId AS string) LIKE %:searchTerm% OR e.employeeName LIKE %:searchTerm% OR r.restaurantName LIKE %:searchTerm%) " +
            "AND (IFNULL(:position, '') = 'all' OR e.position = :position) " +
            "AND (IFNULL(:employeeType, '') = 'all' OR e.employeeType = :employeeType) " +
            "ORDER BY " +
            "CASE WHEN (:sortColumn is null OR :sortDirection is null) THEN e.employeeId END ASC, " +
            "CASE WHEN :sortColumn = 'employeeId' AND :sortDirection = 'asc' THEN e.employeeId END ASC, " +
            "CASE WHEN :sortColumn = 'employeeId' AND :sortDirection = 'desc' THEN e.employeeId END DESC," +
            "CASE WHEN :sortColumn = 'employeeName' AND :sortDirection = 'asc' THEN e.employeeName END ASC, " +
            "CASE WHEN :sortColumn = 'employeeName' AND :sortDirection = 'desc' THEN e.employeeName END DESC, " +
            "CASE WHEN :sortColumn = 'hireDate' AND :sortDirection = 'asc' THEN e.hireDate END ASC, " +
            "CASE WHEN :sortColumn = 'hireDate' AND :sortDirection = 'desc' THEN e.hireDate END DESC, " +
            "CASE WHEN :sortColumn = 'restaurantName' AND :sortDirection = 'asc' THEN r.restaurantName END ASC, " +
            "CASE WHEN :sortColumn = 'restaurantName' AND :sortDirection = 'desc' THEN r.restaurantName END DESC")
    Page<EmployeeDTO> searchEmployeesByRestaurantId(
            @Param("searchTerm") String searchTerm,
            @Param("position") String position,
            @Param("employeeType") String employeeType,
            Pageable pageable,
            @Param("sortColumn") String sortColumn,
            @Param("sortDirection") String sortDirection,
            @Param("selectedRestaurantId") Long selectedRestaurantId);
}
