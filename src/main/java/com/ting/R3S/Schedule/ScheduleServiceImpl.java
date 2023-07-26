package com.ting.R3S.Schedule;

import com.ting.R3S.Employee.Employee;
import com.ting.R3S.Employee.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void deleteSchedule(LocalDate selectedDate, List<Long> employeeIds) {
        // Implement the logic to delete the schedule data from the repository
        scheduleRepository.deleteByBusinessDateAndEmployee_EmployeeIdIn(selectedDate, employeeIds);
    }

    @Override
    public Double getActualTotalWorkingHourByDateRange(Long restaurantId, LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = scheduleRepository.getActualTotalWorkingHourAndOvertimeByDateRange(restaurantId, startDate, endDate);
        if (!results.isEmpty()) {
            Object[] result = results.get(0);
            if (result[0] != null) {
                return ((Number) result[0]).doubleValue();
            }
        }
        return 0.0;
    }

    @Override
    public Double getActualTotalOvertimeByDateRange(Long restaurantId, LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = scheduleRepository.getActualTotalWorkingHourAndOvertimeByDateRange(restaurantId, startDate, endDate);
        if (!results.isEmpty()) {
            Object[] result = results.get(0);
            if (result[1] != null) {
                return ((Number) result[1]).doubleValue();
            }
        }
        return 0.0;
    }

//    @Override
//    public void saveOrUpdateSchedules(List<EventData> eventData) {
//        for (EventData eventDatas : eventData) {
//            Schedule existingSchedule = scheduleRepository.findByBusinessDateAndEmployeeId(eventDatas.getBusinessDate(), eventDatas.getEmployeeId());
//            if (existingSchedule != null) {
//                updateSchedule(existingSchedule, eventDatas);
//            } else {
//                saveNewSchedule(eventDatas);
//            }
//        }
//
//    }
//
//    private void updateSchedule(Schedule existingSchedule, EventData eventDatas) {
//        existingSchedule.setWorkingPeriod(eventDatas.getWorkingPeriod());
//        existingSchedule.setBreakPeriod(eventDatas.getBreakPeriod());
//        existingSchedule.setTotalWorkingHour(eventDatas.getTotalWorkingHour());
//        existingSchedule.setOvertime(eventDatas.getOvertime());
//
//        scheduleRepository.save(existingSchedule);
//    }
//
//    private void saveNewSchedule(EventData eventDatas) {
//        Schedule newSchedule = new Schedule();
//        newSchedule.setWorkingPeriod(eventDatas.getWorkingPeriod());
//        newSchedule.setBreakPeriod(eventDatas.getBreakPeriod());
//        newSchedule.setBusinessDate(eventDatas.getBusinessDate());
//        newSchedule.setTotalWorkingHour(eventDatas.getTotalWorkingHour());
//        newSchedule.setOvertime(eventDatas.getOvertime());
//        Employee employee = employeeRepository.findById(eventDatas.getEmployeeId()).orElse(null);
//        if (employee != null) {
//            newSchedule.setEmployee(employee);
//            scheduleRepository.save(newSchedule);
//        }
//    }

    @Override
    public void saveOrUpdateSchedules(List<EventData> eventDataList, Long restaurantId) {
        // Collect all unique business dates from the eventData list
        Set<LocalDate> businessDates = eventDataList.stream()
                .map(EventData::getBusinessDate)
                .collect(Collectors.toSet());

        for (LocalDate businessDate : businessDates) {
            // Get existing schedules for the current business date
            List<Schedule> existingSchedules = scheduleRepository.findByBusinessDateAndRestaurantId(businessDate, restaurantId);
            List<Long> existingEmployeeIds = existingSchedules.stream()
                    .map(schedule -> schedule.getEmployee().getEmployeeId())
                    .collect(Collectors.toList());

            // Filter the eventData list to include only objects with the current business date
            List<EventData> eventDataForCurrentDate = eventDataList.stream()
                    .filter(eventData -> eventData.getBusinessDate().equals(businessDate))
                    .collect(Collectors.toList());

            for (EventData eventData : eventDataForCurrentDate) {
                Long employeeId = eventData.getEmployeeId();
                List<Schedule> existingSchedulesForEmployee = existingSchedules.stream()
                        .filter(schedule -> schedule.getEmployee().getEmployeeId().equals(employeeId))
                        .collect(Collectors.toList());

                if (!existingSchedulesForEmployee.isEmpty()) {
                    for (Schedule existingSchedule : existingSchedulesForEmployee) {
                        // Update existing schedule
                        updateSchedule(existingSchedule, eventData);
                    }
                } else {
                    // Create new schedule
                    saveNewSchedule(eventData);
                }

                existingEmployeeIds.removeIf(id -> id.equals(employeeId));
            }

            // Delete schedules for employee IDs not present in the filtered eventData list
            for (Long employeeId : existingEmployeeIds) {
                scheduleRepository.deleteByBusinessDateAndEmployeeId(businessDate, employeeId);
            }

        }
    }


    private void updateSchedule(Schedule existingSchedule, EventData eventData) {
        existingSchedule.setWorkingPeriod(eventData.getWorkingPeriod());
        existingSchedule.setBreakPeriod(eventData.getBreakPeriod());
        existingSchedule.setTotalWorkingHour(eventData.getTotalWorkingHour());
        existingSchedule.setOvertime(eventData.getOvertime());

        Schedule updatedSchedule = scheduleRepository.save(existingSchedule);
    }

    private void saveNewSchedule(EventData eventData) {
        Schedule newSchedule = new Schedule();
        newSchedule.setWorkingPeriod(eventData.getWorkingPeriod());
        newSchedule.setBreakPeriod(eventData.getBreakPeriod());
        newSchedule.setBusinessDate(eventData.getBusinessDate());
        newSchedule.setTotalWorkingHour(eventData.getTotalWorkingHour());
        newSchedule.setOvertime(eventData.getOvertime());
        Employee employee = employeeRepository.findById(eventData.getEmployeeId()).orElse(null);
        if (employee != null) {
            newSchedule.setEmployee(employee);
            scheduleRepository.save(newSchedule);
        }
    }


    @Override
    public void deleteScheduleData(List<EventData> eventDataList) {
        for (EventData eventData : eventDataList) {
            Long employeeId = eventData.getEmployeeId();
            LocalDate businessDate = eventData.getBusinessDate();
            scheduleRepository.deleteByEmployeeIdAndBusinessDate(employeeId, businessDate);
        }
    }

}
