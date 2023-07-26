package com.ting.R3S.Account;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AvailableAccountDTO {

    private Long accountId;
    private String email;

    public AvailableAccountDTO(Long accountId, String email) {
        this.accountId = accountId;
        this.email = email;
    }

    @Override
    public String toString() {
        return "AvailableAccountDTO{" +
                "accountId=" + accountId +
                ", email='" + email + '\'' +
                '}';
    }
}
