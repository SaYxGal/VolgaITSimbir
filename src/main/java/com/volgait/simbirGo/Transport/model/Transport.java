package com.volgait.simbirGo.Transport.model;

import com.volgait.simbirGo.Account.model.Account;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "transports")
public class Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Account account;
    @Column(nullable = false)
    private boolean canBeRented;
    @Column(nullable = false)
    private String transportType;
    @Column(nullable = false)
    private String model;
    @Column(nullable = false)
    private String color;
    @Column(nullable = false)
    private String identifier;
    private String description;
    @Column(nullable = false)
    private double latitude;
    @Column(nullable = false)
    private double longitude;
    private double minutePrice;
    private double dayPrice;

    public Transport() {
    }

    public Transport(boolean canBeRented, String transportType, String model,
                     String color, String identifier, String description,
                     double latitude, double longitude,
                     double minutePrice, double dayPrice) {
        this.canBeRented = canBeRented;
        this.transportType = transportType;
        this.model = model;
        this.color = color;
        this.identifier = identifier;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.minutePrice = minutePrice;
        this.dayPrice = dayPrice;
    }

    public Long getId() {
        return id;
    }

    public boolean isCanBeRented() {
        return canBeRented;
    }

    public void setCanBeRented(boolean canBeRented) {
        this.canBeRented = canBeRented;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getMinutePrice() {
        return minutePrice;
    }

    public void setMinutePrice(double minutePrice) {
        this.minutePrice = minutePrice;
    }

    public double getDayPrice() {
        return dayPrice;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setDayPrice(double dayPrice) {
        this.dayPrice = dayPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transport transport = (Transport) o;
        return Objects.equals(id, transport.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
