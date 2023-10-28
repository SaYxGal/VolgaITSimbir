package com.volgait.simbirGo.Rent.DTO;

import java.time.ZonedDateTime;

public class RentAdminCreateUpdateDto {
    private Long transportId;
    private Long userId;
    private ZonedDateTime timeStart;
    private ZonedDateTime timeEnd;
    private double priceOfUnit;
    private String priceType;
    private double finalPrice;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTransportId() {
        return transportId;
    }

    public void setTransportId(Long transportId) {
        this.transportId = transportId;
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
}
