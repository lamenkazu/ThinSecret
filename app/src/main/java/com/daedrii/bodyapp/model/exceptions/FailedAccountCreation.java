package com.daedrii.bodyapp.model.exceptions;

public class FailedAccountCreation extends Exception{
    public FailedAccountCreation(String err){
        super(err);
    }
}
