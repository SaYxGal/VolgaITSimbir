package com.volgait.simbirGo.Account.DTO;

import com.volgait.simbirGo.Account.model.Account;

public class AccountInfoDto {
    private final String username;
    private final String password;
    private final double balance;

    public AccountInfoDto(Account account) {
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.balance = account.getBalance();
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
