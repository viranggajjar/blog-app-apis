package com.blogapi.blogappapis.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    String resourceName;
    String fieldName;
    int fieldValue;
    String fieldValue1;


    public ResourceNotFoundException( String resourceName, String fieldName, Integer fieldValue) {
        super(String.format("%s not found with %s : %s",resourceName,fieldName,fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ResourceNotFoundException( String resourceName, String fieldName, String fieldValue1) {
        super(String.format("%s not found with %s : %s",resourceName,fieldName,fieldValue1));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue1 = fieldValue1;
    }

}
