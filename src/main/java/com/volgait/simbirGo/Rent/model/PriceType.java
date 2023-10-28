package com.volgait.simbirGo.Rent.model;

public enum PriceType {
    Minutes("Minutes"),
    Days("Days");
    private final String text;

    PriceType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
