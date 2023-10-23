package com.volgait.simbirGo.Transport.DTO;

import com.volgait.simbirGo.Transport.model.Transport;

public class TransportDto {
    private final Long id;
    private final boolean canBeRented;
    private final String transportType;
    private final String model;
    private final String color;
    private final String identifier;
    private final String description;
    private final double latitude;
    private final double longitude;
    private final double minutePrice;
    private final double dayPrice;

    public TransportDto(Transport transport) {
        this.id = transport.getId();
        this.canBeRented = transport.isCanBeRented();
        this.transportType = transport.getTransportType();
        this.model = transport.getModel();
        this.color = transport.getColor();
        this.identifier = transport.getIdentifier();
        this.description = transport.getDescription();
        this.latitude = transport.getLatitude();
        this.longitude = transport.getLongitude();
        this.minutePrice = transport.getMinutePrice();
        this.dayPrice = transport.getDayPrice();
    }

    public TransportDto() {
        this.id = -1L;
        this.canBeRented = false;
        this.transportType = "";
        this.model = "";
        this.color = "";
        this.identifier = "";
        this.description = null;
        this.latitude = 0;
        this.longitude = 0;
        this.minutePrice = 0;
        this.dayPrice = 0;
    }

    public Long getId() {
        return id;
    }

    public boolean isCanBeRented() {
        return canBeRented;
    }

    public String getTransportType() {
        return transportType;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getMinutePrice() {
        return minutePrice;
    }

    public double getDayPrice() {
        return dayPrice;
    }
}
