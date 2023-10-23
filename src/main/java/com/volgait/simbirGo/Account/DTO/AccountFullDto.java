package com.volgait.simbirGo.Account.DTO;

import com.volgait.simbirGo.Account.model.Account;

public class AccountFullDto {
    private final Long id;
    private final String username;
    private final String password;
    private final boolean isAdmin;
    private final double balance;

    public AccountFullDto(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.isAdmin = account.isAdmin();
        this.balance = account.getBalance();
    }

    public AccountFullDto() {
        this.id = -1L;
        this.username = "Error";
        this.password = "Error";
        this.isAdmin = false;
        this.balance = -1;
    }

    public Long getId() {
        return id;
    }

    public boolean isAdmin() {
        return isAdmin;
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
