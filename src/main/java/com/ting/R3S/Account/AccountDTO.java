package com.ting.R3S.Account;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@NoArgsConstructor
public class AccountDTO {

    private Long accountId;
    private String email;
    private String password;
    private String role;
    private String restaurantName;
    private Time openTime;
    private Time closeTime;
    private String status;

    public AccountDTO(Long accountId, String email, String password, String role, String restaurantName, Time openTime, Time closeTime, String status) {
        this.accountId = accountId;
        this.email = email;
        this.password = password;
        this.role = role;
        this.restaurantName = restaurantName;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.status = status;
    }


    @Override
    public String toString() {
        return "AccountDTO{" +
                "accountId=" + accountId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", openTime=" + openTime +
                ", closeTime=" + closeTime +
                ", status='" + status + '\'' +
                '}';
    }
}
