package com.volgait.simbirGo.Account.repository;

import com.volgait.simbirGo.Account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findOneByUsernameIgnoreCase(String username);

    @Query(value = "SELECT * FROM accounts LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Account> getAccountsInRange(int start, int count);
}
