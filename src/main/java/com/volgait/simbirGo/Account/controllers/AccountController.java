package com.volgait.simbirGo.Account.controllers;

import com.volgait.simbirGo.Account.DTO.AccountInfoDto;
import com.volgait.simbirGo.Account.DTO.AccountSignInUpDto;
import com.volgait.simbirGo.Account.DTO.AccountUpdateDto;
import com.volgait.simbirGo.Account.model.Account;
import com.volgait.simbirGo.Account.service.AccountExistsException;
import com.volgait.simbirGo.Account.service.AccountNotFoundException;
import com.volgait.simbirGo.Account.service.AccountService;
import com.volgait.simbirGo.Configuration.OpenAPI30Configuration;
import com.volgait.simbirGo.Util.ValidationException;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(OpenAPI30Configuration.API_PREFIX + "/Account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/Me")
    public AccountInfoDto getCurrentAccount() {
        try {
            Account currentAccount = accountService.findCurrentAccount();
            if (currentAccount != null) {
                return new AccountInfoDto(currentAccount);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/SignIn")
    public String login(@RequestBody AccountSignInUpDto authDto) {
        try {
            return accountService.signIn(authDto.getUsername(), authDto.getPassword());
        } catch (ValidationException | AccountNotFoundException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/SignUp")
    public String registry(@RequestBody AccountSignInUpDto authDto) {
        try {
            Account account = accountService.createAccount(authDto.getUsername(), authDto.getPassword());
            return account.getUsername() + " was created";
        } catch (ValidationException | AccountExistsException e) {
            return e.getMessage();
        }
    }

    @PutMapping("/Update")
    public String update(@RequestBody AccountUpdateDto updateInfo) {
        try {
            Pair<Account, String> info = accountService.updateAccount(updateInfo.getUsername(), updateInfo.getPassword());
            if (info.getSecond().equals("")) {
                return info.getFirst().getUsername() + " was updated.";
            }
            return info.getFirst().getUsername() + " was updated.\nNew token - " + info.getSecond();
        } catch (ValidationException | AccountExistsException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/SignOut")
    public String logout() {
        //TODO
        return "";
    }
}
