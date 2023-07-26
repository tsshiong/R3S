package com.ting.R3S.Restaurant;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
public class UpdateRequest {
    private LocalTime openTime;
    private LocalTime closeTime;
    private String status;
    private String email;
    private String password;

    public UpdateRequest(LocalTime openTime, LocalTime closeTime, String status, String email, String password) {
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.status = status;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UpdateRequest{" +
                "openTime=" + openTime +
                ", closeTime=" + closeTime +
                ", status='" + status + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
