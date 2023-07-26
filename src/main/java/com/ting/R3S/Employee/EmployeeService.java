package com.ting.R3S.Employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {

    Page<EmployeeDTO> searchEmployees(String searchTerm, String position, String employeeType, Pageable pageable, String sortColumn, String sortDirection);

    long countEmployees();

    List<Object> getEmployeeById(Long employeeId);

    void updateEmployee(Long employeeId, EmployeeDTO employeeDTO);

    void addEmployee(EmployeeDTO employeeDTO);

    void deleteEmployees(List<Long> employeeIds);

    RestaurantScheduleDTO findRestaurantSchedules(Long restaurantId, LocalDate businessDate);

    List<RestaurantEmployeeDTO> findRestaurantEmployees(Long restaurantId);

    RestaurantScheduleWeekDTO findEmployeeSchedulesWeek(Long restaurantId, LocalDate businessStartDate, LocalDate businessEndDate);

    List<AvailableEmployeeDTO> getAvailableEmployees(Long restaurantId);

    int[] getTotalEmployeesByRestaurantId(Long restaurantId);

    boolean importCsvData(MultipartFile file);

    Page<EmployeeDTO> searchEmployeesByRestaurantId(String searchTerm, String position, String employeeType, Pageable pageable, String sortColumn, String sortDirection, Long selectedRestaurantId);
}
