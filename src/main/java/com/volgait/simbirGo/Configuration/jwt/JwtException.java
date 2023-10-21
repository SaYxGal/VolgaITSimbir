package com.volgait.simbirGo.Configuration.jwt;

public class JwtException extends RuntimeException {
    public JwtException(Throwable throwable) {
        super(throwable);
    }

    public JwtException(String message) {
        super(message);
    }
}
