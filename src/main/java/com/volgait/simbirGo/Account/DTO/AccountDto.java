package com.volgait.simbirGo.Account.DTO;

import com.volgait.simbirGo.Account.model.Account;

public class AccountDto {
    private final Long id;
    private final String username;
    private final String password;
    private final double balance;

    public AccountDto(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.balance = account.getBalance();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }
}
