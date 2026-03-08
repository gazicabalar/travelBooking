package com.travelbooking.travelbooking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    String resourceName;
    String field;
    Long fieldId;

    public ResourceNotFoundException(String resourceName, String field, Long fieldId){
        super(String.format("%s not found with %s : '%d'", resourceName, field, fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
