package com.ting.R3S.Schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableTransactionManagement
@RestController
@CrossOrigin(origins = "http://localhost")
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/delete-schedule")
    public ResponseEntity<String> deleteSchedule(@RequestBody DeleteScheduleRequest request) {
        try {
            scheduleService.deleteSchedule(request.getSelectedDate(), request.getEmployeeIds());
            return ResponseEntity.ok("Data deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting data: " + e.getMessage());
        }
    }

    @GetMapping("/totallabourhours")
    public ResponseEntity<Map<String, Double>> getActualTotalWorkingHourAndOvertimeByDateRange(
            @RequestParam @Nullable Long restaurantId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        Double totalWorkingHour = scheduleService.getActualTotalWorkingHourByDateRange(restaurantId, startDate, endDate);
        Double totalOvertime = scheduleService.getActualTotalOvertimeByDateRange(restaurantId, startDate, endDate);
        Map<String, Double> response = new HashMap<>();
        response.put("totalWorkingHour", totalWorkingHour);
        response.put("totalOvertime", totalOvertime);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/save-schedule")
    public ResponseEntity<String> saveOrUpdateSchedules(@RequestBody List<EventData> eventData, @RequestHeader("restaurantId") Long restaurantId) {
        try {
            scheduleService.saveOrUpdateSchedules(eventData, restaurantId);
            return ResponseEntity.ok("Schedules saved/updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving/updating schedules: " + e.getMessage());
        }
    }

    @PostMapping("/delete-scheduleweek")
    public ResponseEntity<String> deleteScheduleWeek(@RequestBody List<EventData> eventDataList) {
        try {
            scheduleService.deleteScheduleData(eventDataList);
            return ResponseEntity.ok("Data deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete data: " + e.getMessage());
        }
    }


}


