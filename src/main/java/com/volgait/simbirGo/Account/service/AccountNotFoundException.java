package com.volgait.simbirGo.Account.service;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long id) {
        super(String.format("Account with id = '%d' not found", id));
    }

    public AccountNotFoundException(String username) {
        super(String.format("Account with username = '%s' not found", username));
    }

    public AccountNotFoundException(String username, String password) {
        super(String.format("Password = '%s' not correct for '%s'", password, username));
    }
}
