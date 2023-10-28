package com.volgait.simbirGo.Rent.repository;

import com.volgait.simbirGo.Account.model.Account;
import com.volgait.simbirGo.Rent.model.Rent;
import com.volgait.simbirGo.Transport.model.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long> {
    @Query("SELECT r FROM Rent r WHERE r.account = :account")
    List<Rent> getAccountRents(@Param("account") Account account);

    @Query("SELECT r FROM Rent r WHERE r.transport = :transport")
    List<Rent> getTransportRents(@Param("transport") Transport transport);
}
