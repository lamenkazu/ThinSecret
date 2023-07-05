package com.daedrii.bodyapp.model.exceptions;

public class PasswordNotMatchesException extends Exception {

    public PasswordNotMatchesException(String err){
        super(err);
    }

}
