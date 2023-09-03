package com.springboot.crud.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private String resourceName;
    private String fieldName;
    private Long fiedlValue;

    public ResourceNotFoundException(String resourceName,String fieldName, Long fiedlValue)
    {
        super(String.format("%s not found with %s : '%s'",resourceName,fieldName,fiedlValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fiedlValue = fiedlValue;
    }
}
