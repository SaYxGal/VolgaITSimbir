package com.volgait.simbirGo.Account.controllers;

import com.volgait.simbirGo.Account.DTO.AccountDto;
import com.volgait.simbirGo.Account.model.Account;
import com.volgait.simbirGo.Account.service.AccountExistsException;
import com.volgait.simbirGo.Account.service.AccountService;
import com.volgait.simbirGo.Configuration.OpenAPI30Configuration;
import com.volgait.simbirGo.Util.ValidationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(OpenAPI30Configuration.API_PREFIX + "/Account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/Me")
    public AccountDto getCurrentAccount() {
        Account currentAccount = accountService.findCurrentAccount();
        if (currentAccount != null) {
            return new AccountDto(currentAccount);
        }
        return null;
    }

    @PostMapping("/SignIn")
    public String login(String username, String password) {
        return accountService.signIn(username, password);
    }

    @PostMapping("/SignUp")
    public String registry(String username, String password) {
        try {
            Account account = accountService.createAccount(username, password);
            return account.getUsername() + " was created";
        } catch (ValidationException | AccountExistsException e) {
            return e.getMessage();
        }
    }

    @PutMapping("/Update")
    public String update(String username, String password) {
        try {
            Account account = accountService.updateAccount(username, password);
            return account.getUsername() + " was updated";
        } catch (ValidationException | AccountExistsException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/SignOut")
    public String logout() {
        //To do
        return "";
    }
}
