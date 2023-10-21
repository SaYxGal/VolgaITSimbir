package com.volgait.simbirGo.Account.service;

public class AccountExistsException extends RuntimeException {
    public AccountExistsException(String username) {
        super(String.format("Account '%s' already exists", username));
    }
}
