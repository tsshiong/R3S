package com.ting.R3S.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost")
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employee-table")
    public ResponseEntity<EmployeeTableDTO> getEmployees(@RequestParam(value = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "size", defaultValue = "10") int size,
                                                             @RequestParam(value = "search", required = false, defaultValue = "") String search,
                                                             @RequestParam(value = "sortColumn", required = false, defaultValue = "employeeId") String sortColumn,
                                                             @RequestParam(value = "sortDirection", required = false,defaultValue = "asc") String sortDirection,
                                                             @RequestParam(value = "position", required = false, defaultValue = "all") String position,
                                                             @RequestParam(value = "employeeType", required = false, defaultValue = "all") String employeeType) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<EmployeeDTO> employees = employeeService.searchEmployees(search, position, employeeType, pageRequest, sortColumn, sortDirection);

        long recordsTotal = employeeService.countEmployees();

        EmployeeTableDTO employeeTableDTO = new EmployeeTableDTO(
                employees.getNumber() * employees.getSize(),
                employees.getSize(),
                employees.getNumber(),
                employees.getTotalPages(),
                employees.getTotalElements(),
                recordsTotal,
                employees.getTotalElements(),
                true,
                employees.getNumber() * employees.getSize() + 1,
                employees.getContent()
        );

        return ResponseEntity.ok().body(employeeTableDTO);
    }

    @GetMapping("/getemployee/{employeeId}")
    public ResponseEntity<List<Object>> getEmployeeById(@PathVariable("employeeId") Long employeeId) {
        List<Object> employeeDetail = employeeService.getEmployeeById(employeeId);
        return new ResponseEntity<>(employeeDetail, HttpStatus.OK);
    }


    @PutMapping("/update_employee/{employeeId}")
    public ResponseEntity<Void> updateEmployee(@PathVariable("employeeId") Long employeeId, @RequestBody EmployeeDTO employeeDTO) {
        employeeService.updateEmployee(employeeId, employeeDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add_employee")
    public ResponseEntity<Void> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.addEmployee(employeeDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete-employees")
    public ResponseEntity<String> deleteEmployees(@RequestBody List<Long> employeeIds) {
        try {
            employeeService.deleteEmployees(employeeIds);
            return ResponseEntity.ok("Employees deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting employees");
        }
    }

    @GetMapping("/employeeschedules")
    public ResponseEntity<RestaurantScheduleDTO> getRestaurantSchedules(@RequestParam Long restaurant_id, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate business_date) {
        RestaurantScheduleDTO schedules = employeeService.findRestaurantSchedules(restaurant_id, business_date);

        List<RestaurantEmployeeDTO> restaurantEmployees = employeeService.findRestaurantEmployees(restaurant_id);

        if (schedules == null) {
            schedules = new RestaurantScheduleDTO(new ArrayList<>(), restaurantEmployees);
        } else {
            schedules.setEmployee(restaurantEmployees);
        }

        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/employeeschedulesweek")
    public ResponseEntity<RestaurantScheduleWeekDTO> getRestaurantSchedulesWeek(@RequestParam Long restaurantId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate businessStartDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate businessEndDate) {
        RestaurantScheduleWeekDTO schedulesWeek = employeeService.findEmployeeSchedulesWeek(restaurantId, businessStartDate, businessEndDate);

        List<RestaurantEmployeeDTO> restaurantEmployees = employeeService.findRestaurantEmployees(restaurantId);

        if (schedulesWeek == null) {
            schedulesWeek = new RestaurantScheduleWeekDTO(new ArrayList<>(), restaurantEmployees);
        } else {
            schedulesWeek.setEmployeeWeek(restaurantEmployees);
        }

        return ResponseEntity.ok(schedulesWeek);
    }

    @GetMapping("/availableemployees")
    public List<AvailableEmployeeDTO> getAvailableEmployees(@RequestParam Long restaurantId) {
        return employeeService.getAvailableEmployees(restaurantId);
    }

    @GetMapping("/totalemployee")
    @ResponseBody
    public int[] getTotalEmployeesByRestaurantId(@RequestParam @Nullable Long restaurantId) {
        int[] totalEmployees = employeeService.getTotalEmployeesByRestaurantId(restaurantId);
        return totalEmployees;
    }

    @PostMapping("/import-csv")
    public ResponseEntity<String> importCsvData(@RequestParam("file") MultipartFile file) {
        boolean importSuccess = employeeService.importCsvData(file);
        if (importSuccess) {
            return ResponseEntity.ok("CSV data imported successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to import CSV data");
        }
    }

    @GetMapping("/employee-table/{selectedRestaurantId}")
    public ResponseEntity<EmployeeTableDTO> getEmployees(@RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "size", defaultValue = "10") int size,
                                                         @RequestParam(value = "search", required = false, defaultValue = "") String search,
                                                         @RequestParam(value = "sortColumn", required = false, defaultValue = "employeeId") String sortColumn,
                                                         @RequestParam(value = "sortDirection", required = false,defaultValue = "asc") String sortDirection,
                                                         @RequestParam(value = "position", required = false, defaultValue = "all") String position,
                                                         @RequestParam(value = "employeeType", required = false, defaultValue = "all") String employeeType,
                                                         @PathVariable("selectedRestaurantId") Long selectedRestaurantId) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<EmployeeDTO> employees = employeeService.searchEmployeesByRestaurantId(search, position, employeeType, pageRequest, sortColumn, sortDirection, selectedRestaurantId);

        long recordsTotal = employeeService.countEmployees();

        EmployeeTableDTO employeeTableDTO = new EmployeeTableDTO(
                employees.getNumber() * employees.getSize(),
                employees.getSize(),
                employees.getNumber(),
                employees.getTotalPages(),
                employees.getTotalElements(),
                recordsTotal,
                employees.getTotalElements(),
                true,
                employees.getNumber() * employees.getSize() + 1,
                employees.getContent()
        );

        return ResponseEntity.ok().body(employeeTableDTO);
    }
}
