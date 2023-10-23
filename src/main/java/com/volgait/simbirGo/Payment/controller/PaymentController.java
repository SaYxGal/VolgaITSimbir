package com.volgait.simbirGo.Payment.controller;

import com.volgait.simbirGo.Account.model.Account;
import com.volgait.simbirGo.Account.service.AccountService;
import com.volgait.simbirGo.Configuration.OpenAPI30Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(OpenAPI30Configuration.API_PREFIX + "/Payment")
public class PaymentController {
    private final AccountService accountService;

    public PaymentController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/Hesoyam/{accountId}")
    public String hesoyam(@PathVariable Long accountId) {
        Account account = accountService.findAccount(accountId);
        final Account currentAccount = accountService.findCurrentAccount();
        if (!currentAccount.isAdmin() && !Objects.equals(account.getId(), currentAccount.getId())) {
            return "Your account don't have permission to do this action";
        }
        accountService.updateAccount(account.getId(), account.getUsername(), account.getPassword(),
                account.isAdmin(), account.getBalance() + 250000);
        return "Balance of " + account.getUsername() + " was increased by 250000";
    }
}
