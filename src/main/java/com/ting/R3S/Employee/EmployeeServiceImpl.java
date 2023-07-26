package com.ting.R3S.Employee;

import com.ting.R3S.Restaurant.Restaurant;
import com.ting.R3S.Restaurant.RestaurantRepository;
import com.ting.R3S.Schedule.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public long countEmployees() {
        return employeeRepository.count();
    }

    @Override
    public List<Object> getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new IllegalArgumentException("Invalid employee ID"));
        if (employee != null) {
            List<Object> employeeDetails = new ArrayList<>();
            employeeDetails.add(employee.getEmployeeId());
            employeeDetails.add(employee.getEmployeeName());
            employeeDetails.add(employee.getPosition());
            employeeDetails.add(employee.getHireDate());
            employeeDetails.add(employee.getEmployeeType());
            employeeDetails.add(employee.getRestaurant().getRestaurantId());
            employeeDetails.add(employee.getRestaurant().getRestaurantName());
            return employeeDetails;
        }
        return null;
    }

    @Override
    public Page<EmployeeDTO> searchEmployees(String searchTerm, String position, String employeeType, Pageable pageable, String sortColumn, String sortDirection) {
        return employeeRepository.searchEmployees(searchTerm, position, employeeType, pageable, sortColumn, sortDirection);
    }

    @Override
    public void updateEmployee(Long employeeId, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee with id " + employeeId + " not found"));

        Restaurant restaurant = restaurantRepository.findById(employeeDTO.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id " + employeeDTO.getRestaurantId() + " not found"));

        employee.setEmployeeName(employeeDTO.getEmployeeName());
        employee.setPosition(employeeDTO.getPosition());
        employee.setHireDate(employeeDTO.getHireDate());
        employee.setEmployeeType(employeeDTO.getEmployeeType());
        employee.setRestaurant(restaurant);

        employeeRepository.save(employee);
    }

    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setEmployeeName(employeeDTO.getEmployeeName());
        employee.setPosition(employeeDTO.getPosition());
        employee.setHireDate(employeeDTO.getHireDate());
        employee.setEmployeeType(employeeDTO.getEmployeeType());
        // Get the restaurant entity from the database based on the ID
        Restaurant restaurant = restaurantRepository.findById(employeeDTO.getRestaurantId()).orElse(null);
        if (restaurant != null) {
            employee.setRestaurant(restaurant);
            employeeRepository.save(employee);
        }
    }

    @Override
    public void deleteEmployees(List<Long> employeeIds) {
        for (Long id : employeeIds) {
            Optional<Employee> employeeOptional = employeeRepository.findById(id);
            employeeOptional.ifPresent(employee -> employeeRepository.delete(employee));
        }
    }

    @Override
    public RestaurantScheduleDTO findRestaurantSchedules(Long restaurantId, LocalDate businessDate) {
        List<EmployeeScheduleDTO> employeeSchedules = employeeRepository.findEmployeeSchedules(restaurantId, businessDate);

        if (employeeSchedules.isEmpty()) {
            return null;
        } else {
            List<RestaurantEmployeeDTO> restaurantEmployees = findRestaurantEmployees(restaurantId);
            return new RestaurantScheduleDTO(employeeSchedules, restaurantEmployees);
        }
    }

    @Override
    public List<RestaurantEmployeeDTO> findRestaurantEmployees(Long restaurantId) {
        return employeeRepository.findRestaurantEmployees(restaurantId);
    }

    @Override
    public RestaurantScheduleWeekDTO findEmployeeSchedulesWeek(Long restaurantId, LocalDate businessStartDate, LocalDate businessEndDate) {
        List<EmployeeScheduleWeekDTO> employeeSchedulesWeek = employeeRepository.findEmployeeSchedulesWeek(restaurantId, businessStartDate, businessEndDate);

        if (employeeSchedulesWeek.isEmpty()) {
            return null;
        } else {
            List<RestaurantEmployeeDTO> restaurantEmployees = findRestaurantEmployees(restaurantId);
            return new RestaurantScheduleWeekDTO(employeeSchedulesWeek, restaurantEmployees);
        }
    }

//    @Override
//    public List<AvailableEmployeeDTO> getAvailableEmployees(Long restaurantId, LocalDate businessDate) {
//        List<AvailableEmployeeDTO> availableEmployees = employeeRepository.getAllEmployees(restaurantId);
//        List<WorkingEmployeeDTO> workingEmployees = scheduleRepository.getWorkingEmployees(restaurantId, businessDate);
//
//        // Remove working employees from available employees
//        availableEmployees.removeIf(availableEmployee ->
//                workingEmployees.stream().anyMatch(workingEmployee ->
//                        workingEmployee.getEmployeeId().equals(availableEmployee.getEmployeeId())));
//
//        return availableEmployees;
//    }

    @Override
    public List<AvailableEmployeeDTO> getAvailableEmployees(Long restaurantId) {
        return employeeRepository.getAllEmployees(restaurantId);
    }

    @Override
    public int[] getTotalEmployeesByRestaurantId(Long restaurantId) {
        List<Object[]> results = employeeRepository.getTotalEmployeesByRestaurantId(restaurantId);

        int fullTimeEmployeeCount = 0;
        int partTimeEmployeeCount = 0;

        for (Object[] row : results) {
            if (row[0] != null) {
                fullTimeEmployeeCount = ((Number) row[0]).intValue();
            }
            if (row[1] != null) {
                partTimeEmployeeCount = ((Number) row[1]).intValue();
            }
        }

        return new int[]{fullTimeEmployeeCount, partTimeEmployeeCount};
    }

    @Override
    public boolean importCsvData(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;

            // Define the expected header
            String expectedHeader = "employee_name,position,hire_date,employee_type,restaurant_id";

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
                if (data.length != 5) {
                    // Invalid data format
                    return false;
                }

                String employeeName = data[0];
                String position = data[1];
                String hireDateStr = data[2];
                String employeeType = data[3];
                String restaurantIdStr = data[4];

                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(hireDateStr, dateFormat);
                java.sql.Date hireDate = java.sql.Date.valueOf(date);
                Long restaurantId = Long.valueOf(restaurantIdStr);

                // Create a new Restaurant object and save it to the database
                Employee employee = new Employee();
                employee.setEmployeeName(employeeName);
                employee.setPosition(position);
                employee.setHireDate(hireDate);
                employee.setEmployeeType(employeeType);

                // Disable foreign key constraint checks
                jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");

                Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
                if (restaurant != null) {
                    employee.setRestaurant(restaurant);
                    employeeRepository.save(employee);
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

    @Override
    public Page<EmployeeDTO> searchEmployeesByRestaurantId(String searchTerm, String position, String employeeType, Pageable pageable, String sortColumn, String sortDirection, Long selectedRestaurantId) {
        return employeeRepository.searchEmployeesByRestaurantId(searchTerm, position, employeeType, pageable, sortColumn, sortDirection,selectedRestaurantId);
    }
}
