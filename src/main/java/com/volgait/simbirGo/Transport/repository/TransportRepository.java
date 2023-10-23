package com.volgait.simbirGo.Transport.repository;

import com.volgait.simbirGo.Transport.model.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransportRepository extends JpaRepository<Transport, Long> {
    @Query(value = "SELECT * FROM transports WHERE transport_type = ?3 LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Transport> getTransportsInRangeFiltered(int start, int count, String transportType);

    @Query(value = "SELECT * FROM transports LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Transport> getTransportsInRange(int start, int count);
}
