package com.hodolog.api.exception;

import lombok.Getter;

import javax.persistence.criteria.CriteriaBuilder;

@Getter
public class InvalidRequest extends HodologException{

    private static final String MESSAGE = "잘못된 요청입니다";

//    public String fieldName;
//    public String message;


    public InvalidRequest() {
        super(MESSAGE);
    }

//    public InvalidRequest(String fieldName, String message) {
//        super(MESSAGE);
//        this.fieldName = fieldName;
//        this.message = message;
//    }
    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }


    @Override
    public int getStatusCode() {
        return 400;
    }
}
