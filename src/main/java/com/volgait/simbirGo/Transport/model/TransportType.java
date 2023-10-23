package com.volgait.simbirGo.Transport.model;

public enum TransportType {
    Car("Car"),
    Bike("Bike"),
    Scooter("Scooter");
    private final String text;

    TransportType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
