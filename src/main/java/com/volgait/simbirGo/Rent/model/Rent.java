package com.volgait.simbirGo.Rent.model;

import com.volgait.simbirGo.Account.model.Account;
import com.volgait.simbirGo.Transport.model.Transport;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "rents")
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Account account;
    @ManyToOne
    @JoinColumn(name = "transport_id", nullable = false)
    private Transport transport;
    @Column(nullable = false)
    private ZonedDateTime timeStart;
    private ZonedDateTime timeEnd;
    @Column(nullable = false)
    private double priceOfUnit;
    @Column(nullable = false)
    private String priceType;
    private double finalPrice;

    public Rent() {
    }

    public Rent(ZonedDateTime timeStart, ZonedDateTime timeEnd,
                double priceOfUnit, String priceType, double finalPrice) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.priceOfUnit = priceOfUnit;
        this.priceType = priceType;
        this.finalPrice = finalPrice;
    }

    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public ZonedDateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(ZonedDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public ZonedDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(ZonedDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public double getPriceOfUnit() {
        return priceOfUnit;
    }

    public void setPriceOfUnit(double priceOfUnit) {
        this.priceOfUnit = priceOfUnit;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rent rent = (Rent) o;
        return Objects.equals(id, rent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
