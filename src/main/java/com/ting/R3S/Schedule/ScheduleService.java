package com.ting.R3S.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    void deleteSchedule(LocalDate selectedDate, List<Long> employeeIds);

    Double getActualTotalWorkingHourByDateRange(Long restaurantId, LocalDate startDate, LocalDate endDate);
    Double getActualTotalOvertimeByDateRange(Long restaurantId, LocalDate startDate, LocalDate endDate);

    void saveOrUpdateSchedules(List<EventData> eventData, Long restaurantId);

    void deleteScheduleData(List<EventData> eventDataList);
}
