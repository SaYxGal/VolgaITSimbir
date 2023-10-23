package com.volgait.simbirGo.Transport.repository;

import com.volgait.simbirGo.Transport.model.Transport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportRepository extends JpaRepository<Transport, Long> {
}
