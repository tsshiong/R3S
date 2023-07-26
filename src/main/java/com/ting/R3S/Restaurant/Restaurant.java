package com.ting.R3S.Restaurant;

import com.ting.R3S.Account.Account;
import com.ting.R3S.Actualsales.Actualsales;
import com.ting.R3S.Employee.Employee;
import com.ting.R3S.Projectedsales.Projectedsales;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "account")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;
    private String restaurantName;
    private Time openTime;
    private Time closeTime;
    private String status;

    @OneToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "account_id",
            referencedColumnName = "accountId"
    )
    private Account account;

    @OneToMany(
        mappedBy = "restaurant"
    )
    private List<Employee> employee;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @JoinColumn(name = "restaurant_id")
    private List<Actualsales> actualSalesList;

    @OneToMany(
            mappedBy = "restaurant"
    )
    private List<Projectedsales> projectedsales;

    public Restaurant(String restaurantName, Time openTime, Time closeTime, String status, Long accountId) {
        this.restaurantName = restaurantName;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.status = status;
        Account account = new Account();
        account.setAccountId(accountId);
        this.account = account;
    }
}
