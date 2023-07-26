package com.ting.R3S.Account;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
public class AccountTableDTO {

    private int end;
    private int length;
    private int page;
    private int pages;
    private long recordsDisplay;
    private long recordsTotal;
    private long recordsFiltered;
    private boolean serverSide;
    private int start;

    private List<AccountDTO> accounts;


    public AccountTableDTO(int end, int length, int page, int pages, long recordsDisplay, long recordsTotal, long recordsFiltered, boolean serverSide, int start, List<AccountDTO> accounts) {
        this.end = end;
        this.length = length;
        this.page = page;
        this.pages = pages;
        this.recordsDisplay = recordsDisplay;
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.serverSide = serverSide;
        this.start = start;
        this.accounts = accounts;
    }
}

