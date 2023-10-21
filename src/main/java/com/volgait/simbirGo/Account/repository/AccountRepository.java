package com.volgait.simbirGo.Account.repository;

import com.volgait.simbirGo.Account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findOneByUsernameIgnoreCase(String username);
}
