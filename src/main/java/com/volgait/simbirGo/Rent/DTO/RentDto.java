package com.volgait.simbirGo.Rent.DTO;

import com.volgait.simbirGo.Rent.model.PriceType;
import com.volgait.simbirGo.Rent.model.Rent;

import java.time.ZonedDateTime;

public class RentDto {
    private final Long id;
    private final Long transportId;
    private final Long userId;
    private final ZonedDateTime timeStart;
    private final ZonedDateTime timeEnd;
    private final double priceOfUnit;
    private final String priceType;
    private final double finalPrice;

    public RentDto(Rent rent) {
        this.id = rent.getId();
        this.transportId = rent.getTransport().getId();
        this.userId = rent.getAccount().getId();
        this.timeStart = rent.getTimeStart();
        this.timeEnd = rent.getTimeEnd();
        this.priceOfUnit = rent.getPriceOfUnit();
        this.priceType = rent.getPriceType();
        this.finalPrice = rent.getFinalPrice();
    }

    public RentDto() {
        this.id = -1L;
        this.userId = -1L;
        this.transportId = -1L;
        this.timeStart = ZonedDateTime.now();
        this.timeEnd = null;
        this.priceOfUnit = 0;
        this.priceType = PriceType.Days.toString();
        this.finalPrice = 0;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTransportId() {
        return transportId;
    }

    public ZonedDateTime getTimeStart() {
        return timeStart;
    }

    public ZonedDateTime getTimeEnd() {
        return timeEnd;
    }

    public double getPriceOfUnit() {
        return priceOfUnit;
    }

    public String getPriceType() {
        return priceType;
    }

    public double getFinalPrice() {
        return finalPrice;
    }
}
