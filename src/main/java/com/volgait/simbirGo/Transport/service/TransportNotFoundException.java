package com.volgait.simbirGo.Transport.service;

public class TransportNotFoundException extends RuntimeException {
    public TransportNotFoundException(Long id) {
        super(String.format("Transport with id = '%d' not found", id));
    }
}
