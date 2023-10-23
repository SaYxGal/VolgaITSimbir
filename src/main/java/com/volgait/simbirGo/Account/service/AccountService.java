package com.volgait.simbirGo.Account.service;

import com.volgait.simbirGo.Account.model.Account;
import com.volgait.simbirGo.Account.model.AccountRole;
import com.volgait.simbirGo.Account.repository.AccountRepository;
import com.volgait.simbirGo.Configuration.jwt.JwtException;
import com.volgait.simbirGo.Configuration.jwt.JwtProvider;
import com.volgait.simbirGo.Util.ValidatorUtil;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidatorUtil validatorUtil;
    private final JwtProvider jwtProvider;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, ValidatorUtil validatorUtil, JwtProvider jwtProvider) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.validatorUtil = validatorUtil;
        this.jwtProvider = jwtProvider;
    }

    public Account createAccount(String username, String password) {
        return createAccount(username, password, false, 0);
    }

    @Transactional
    public Account createAccount(String username, String password, boolean isAdmin, double balance) {
        if (findByUsername(username) != null) {
            throw new AccountExistsException(username);
        }
        final Account account = new Account(username, passwordEncoder.encode(password),
                isAdmin, balance);
        validatorUtil.validate(account);
        return accountRepository.save(account);
    }

    public Pair<Account, String> updateAccount(String username, String password) {
        Account currentAccount = findCurrentAccount();
        if (currentAccount != null) {
            return updateAccount(currentAccount.getId(), username,
                    password, currentAccount.isAdmin(), currentAccount.getBalance());
        }
        return null;
    }

    @Transactional
    public Pair<Account, String> updateAccount(Long id, String username, String password, boolean isAdmin, double balance) {
        if (findByUsername(username) != null) {
            throw new AccountExistsException(username);
        }
        final Account currentAccount = findAccount(id);
        currentAccount.setUsername(username);
        currentAccount.setPassword(passwordEncoder.encode(password));
        currentAccount.setAdmin(isAdmin);
        currentAccount.setBalance(balance);
        validatorUtil.validate(currentAccount);
        String token = jwtProvider.generateToken(currentAccount.getUsername());
        return Pair.of(accountRepository.save(currentAccount), token);
    }

    @Transactional
    public Account deleteAccount(Long id) {
        final Account currentAccount = findAccount(id);
        accountRepository.delete(currentAccount);
        return currentAccount;
    }

    @Transactional(readOnly = true)
    public Account findAccount(Long id) {
        final Optional<Account> account = accountRepository.findById(id);
        return account.orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Account> findAccountsInRange(int start, int count) {
        return accountRepository.getAccountsInRange(start - 1, count);
    }

    @Transactional(readOnly = true)
    public Account findByUsername(String username) {
        return accountRepository.findOneByUsernameIgnoreCase(username);
    }

    public Account findCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            return findByUsername(currentUsername);
        }
        return null;
    }

    public String signIn(String username, String password) {
        final Account account = findByUsername(username);
        if (account == null) {
            throw new AccountNotFoundException(username);
        }
        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new AccountNotFoundException(password);
        }
        return jwtProvider.generateToken(account.getUsername());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Account accountEntity = findByUsername(username);
        if (accountEntity == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(
                accountEntity.getUsername(), accountEntity.getPassword(), accountEntity.isAdmin()
                ? Collections.singleton(AccountRole.ADMIN)
                : Collections.singleton(AccountRole.USER));
    }

    public UserDetails loadUserByToken(String token) throws UsernameNotFoundException {
        if (!jwtProvider.isTokenValid(token)) {
            throw new JwtException("Bad token");
        }
        final String accountLogin = jwtProvider.getLoginFromToken(token)
                .orElseThrow(() -> new JwtException("Token is not contain Login"));
        return loadUserByUsername(accountLogin);
    }
}
