package com.ting.R3S.Restaurant;

import com.ting.R3S.Account.Account;
import com.ting.R3S.Account.AccountRepository;
import com.ting.R3S.Actualsales.Actualsales;
import com.ting.R3S.Actualsales.ActualsalesRepository;
import com.ting.R3S.Employee.Employee;
import com.ting.R3S.Employee.EmployeeRepository;
import com.ting.R3S.Projectedsales.Projectedsales;
import com.ting.R3S.Projectedsales.ProjectedsalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService{

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ActualsalesRepository actualsalesRepository;

    @Autowired
    private ProjectedsalesRepository projectedsalesRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public long countRestaurants() {
        return restaurantRepository.count();
    }

    @Override
    public Page<RestaurantDataTableDTO> searchRestaurants(String searchTerm, String status, Pageable pageable, String sortColumn, String sortDirection) {
        return restaurantRepository.searchRestaurants(searchTerm, status, pageable, sortColumn, sortDirection);
    }

    @Override
    public List<Object> getRestaurantById(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new IllegalArgumentException("Invalid restaurant ID"));
        if (restaurant != null) {
            List<Object> restaurantDetails = new ArrayList<>();
            restaurantDetails.add(restaurant.getRestaurantId());
            restaurantDetails.add(restaurant.getRestaurantName());
            restaurantDetails.add(restaurant.getOpenTime());
            restaurantDetails.add(restaurant.getCloseTime());
            restaurantDetails.add(restaurant.getStatus());
            return restaurantDetails;
        }
        return null;
    }

    @Override
    public void updateRestaurantById(String editedResName, Time editedResOpen, Time editedResClose, String editedResStatus, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new IllegalArgumentException("Invalid restaurant ID"));
        restaurant.setRestaurantName(editedResName);
        restaurant.setOpenTime(editedResOpen);
        restaurant.setCloseTime(editedResClose);
        restaurant.setStatus(editedResStatus);
        restaurantRepository.save(restaurant);
    }

    @Override
    @Transactional
    public void deleteRestaurants(List<Long> restaurantIds) {
        // Disable foreign key constraint checks
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");

        for (Long restaurantId : restaurantIds) {
            List<Employee> employees = employeeRepository.findByRestaurantRestaurantId(restaurantId);
            employeeRepository.deleteAll(employees);
            List<Actualsales> actualsales = actualsalesRepository.findByRestaurantRestaurantId(restaurantId);
            actualsalesRepository.deleteAll(actualsales);
            List<Projectedsales> projectedsales = projectedsalesRepository.findByRestaurantRestaurantId(restaurantId);
            projectedsalesRepository.deleteAll(projectedsales);
        }

        restaurantRepository.deleteAllById(restaurantIds);

        // Enable foreign key constraint checks
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1;");
    }

    @Override
    public void updateRestaurantStatusById(Long restaurantId,String status) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new IllegalArgumentException("Invalid restaurant ID"));
        restaurant.setStatus(status);
        restaurantRepository.save(restaurant);
    }

    @Override
    public void saveRestaurant(Long ResAccountId, String ResName, Time ResOpen, Time ResClose, String ResStatus) {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName(ResName);
        restaurant.setOpenTime(ResOpen);
        restaurant.setCloseTime(ResClose);
        restaurant.setStatus(ResStatus);
        Account account = accountRepository.findById(ResAccountId).orElse(null); // Fetch the related account entity using the account repository
        restaurant.setAccount(account);
        restaurantRepository.save(restaurant);
    }

    @Override
    public List<Object[]> getAllRestaurants() {
        return restaurantRepository.findAllRestaurants();
    }

    @Override
    public List<Object> findRestaurantByName(String restaurantName) {
        Restaurant restaurant = restaurantRepository.findByRestaurantName(restaurantName);
        if (restaurant != null) {
            List<Object> restaurantDetails = new ArrayList<>();
            restaurantDetails.add(restaurant.getRestaurantId());
            restaurantDetails.add(restaurant.getRestaurantName());
            return restaurantDetails;
        }

        return null;
    }

    @Override
    public RestaurantProjectedsalesDTO getSalesData(LocalDate businessDate, Long restaurantId) {
        return restaurantRepository.findByBusinessDateAndRestaurantId(businessDate, restaurantId);
    }

    @Override
    public RestaurantProjectsalesWeekDTO getRestaurantProjectedSales(Long restaurantId, LocalDate businessStartDate, LocalDate businessEndDate) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with ID: " + restaurantId));

        List<RestaurantProjectedsalesDailyDTO> projectedsalesweek = restaurantRepository.findProjectedSalesByDateRange(restaurantId, businessStartDate, businessEndDate);

        RestaurantProjectsalesWeekDTO result = new RestaurantProjectsalesWeekDTO();
        result.setRestaurantName(restaurant.getRestaurantName());
        result.setOpenTime(restaurant.getOpenTime());
        result.setCloseTime(restaurant.getCloseTime());

        // Fill missing dates with zero values
        List<LocalDate> dateRange = getDatesInRange(businessStartDate, businessEndDate);
        Map<LocalDate, RestaurantProjectedsalesDailyDTO> projectedSalesMap = projectedsalesweek.stream()
                .collect(Collectors.toMap(RestaurantProjectedsalesDailyDTO::getBusinessDate, Function.identity()));

        for (LocalDate date : dateRange) {
            if (!projectedSalesMap.containsKey(date)) {
                RestaurantProjectedsalesDailyDTO missingData = new RestaurantProjectedsalesDailyDTO(date, 0.0, 0.0);
                projectedSalesMap.put(date, missingData);
            }
        }

        List<RestaurantProjectedsalesDailyDTO> filledProjectedsalesweek = dateRange.stream()
                .map(projectedSalesMap::get)
                .collect(Collectors.toList());

        result.setProjectsalesweek(filledProjectedsalesweek);

        return result;
    }

    // Helper method to get a list of dates within a specified range
    private List<LocalDate> getDatesInRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> datesInRange = new ArrayList<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            datesInRange.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
        return datesInRange;
    }


    @Override
    public boolean importCsvData(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;

            // Define the expected header
            String expectedHeader = "restaurant_name,open_time,close_time,status,account_id";

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

                String restaurantName = data[0];
                String openTimeStr = data[1];
                String closeTimeStr = data[2];
                String status = data[3];
                String id = data[4];

                // Convert the openTime and closeTime strings to Time objects
                LocalTime openTimeDb = LocalTime.parse(openTimeStr);
                LocalTime closeTimeDb = LocalTime.parse(closeTimeStr);
                Time openTime = Time.valueOf(openTimeDb);
                Time closeTime = Time.valueOf(closeTimeDb);
                Long accountId = Long.valueOf(id);

                // Disable foreign key constraint checks
                jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");

                // Restaurant does not exist, create a new Restaurant object and save it to the database
                Restaurant restaurant = new Restaurant(restaurantName, openTime, closeTime, status, accountId);
                restaurantRepository.save(restaurant);

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
    public List<RestaurantAccountDTO> getRestaurantsByAccountId(Long accountId) {
        return restaurantRepository.getRestaurantsByAccountId(accountId);
    }

    @Override
    public List<RestaurantSettingDTO> getRestaurantDetails(Long accountId) {
        return restaurantRepository.getRestaurantDetails(accountId);
    }

    @Override
    public boolean updateRestaurantSettings(Long accountId, UpdateRequest request) {
        Restaurant restaurant = restaurantRepository.findByAccountAccountId(accountId);
        Account account = accountRepository.findById(accountId).orElse(null);

        if (restaurant != null && account != null) {
            restaurant.setOpenTime(Time.valueOf(request.getOpenTime()));
            restaurant.setCloseTime(Time.valueOf(request.getCloseTime()));
            restaurant.setStatus(request.getStatus());

            account.setEmail(request.getEmail());
            account.setPassword(request.getPassword());

            restaurantRepository.save(restaurant);
            accountRepository.save(account);

            return true;
        }
        return false;
    }
}
