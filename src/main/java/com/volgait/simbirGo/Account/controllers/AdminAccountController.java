package com.volgait.simbirGo.Account.controllers;

import com.volgait.simbirGo.Account.DTO.AccountAdminCreateUpdateDto;
import com.volgait.simbirGo.Account.DTO.AccountFullDto;
import com.volgait.simbirGo.Account.model.Account;
import com.volgait.simbirGo.Account.model.AccountRole;
import com.volgait.simbirGo.Account.service.AccountExistsException;
import com.volgait.simbirGo.Account.service.AccountNotFoundException;
import com.volgait.simbirGo.Account.service.AccountService;
import com.volgait.simbirGo.Configuration.OpenAPI30Configuration;
import com.volgait.simbirGo.Util.ValidationException;
import org.springframework.data.util.Pair;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(OpenAPI30Configuration.API_PREFIX + "/Admin/Account")
public class AdminAccountController {
    private final AccountService accountService;

    public AdminAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("")
    @Secured(AccountRole.AsString.ADMIN)
    public List<AccountFullDto> getAccounts(int start, int count) {
        return accountService.findAccountsInRange(start, count).stream().map(AccountFullDto::new).toList();
    }

    @GetMapping("/{id}")
    @Secured(AccountRole.AsString.ADMIN)
    public AccountFullDto getAccount(@PathVariable Long id) {
        try {
            return new AccountFullDto(accountService.findAccount(id));
        } catch (AccountNotFoundException e) {
            return new AccountFullDto();
        }
    }

    @PostMapping("")
    @Secured(AccountRole.AsString.ADMIN)
    public String registry(@RequestBody AccountAdminCreateUpdateDto createInfo) {
        try {
            Account account = accountService.createAccount(createInfo.getUsername(),
                    createInfo.getPassword(), createInfo.isAdmin(), createInfo.getBalance());
            return account.getUsername() + " was created";
        } catch (ValidationException | AccountExistsException e) {
            return e.getMessage();
        }
    }

    @PutMapping("/{id}")
    @Secured(AccountRole.AsString.ADMIN)
    public String update(@PathVariable Long id, @RequestBody AccountAdminCreateUpdateDto updateInfo) {
        try {
            Pair<Account, String> info = accountService.updateAccount(id, updateInfo.getUsername(),
                    updateInfo.getPassword(), updateInfo.isAdmin(), updateInfo.getBalance());
            return info.getFirst().getUsername() + " was updated.\nNew token - " + info.getSecond();
        } catch (ValidationException | AccountExistsException e) {
            return e.getMessage();
        }
    }

    @DeleteMapping("/{id}")
    @Secured(AccountRole.AsString.ADMIN)
    public String delete(@PathVariable Long id) {
        try {
            Account account = accountService.deleteAccount(id);
            return account.getUsername() + " was deleted";
        } catch (ValidationException | AccountNotFoundException e) {
            return e.getMessage();
        }
    }
}
