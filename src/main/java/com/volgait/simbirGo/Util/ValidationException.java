package com.volgait.simbirGo.Util;

import java.util.Set;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Set<String> errors) {
        super(String.join("\n", errors));
    }
}
