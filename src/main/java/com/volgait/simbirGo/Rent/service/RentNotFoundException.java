package com.volgait.simbirGo.Rent.service;

public class RentNotFoundException extends RuntimeException {
    public RentNotFoundException(Long id) {
        super(String.format("Rent with id = '%d' not found", id));
    }
}
