package com.daedrii.bodyapp.model;

public class User {

    private String name;

    private String email;

    private String senha;

    private BodyInfo bodyInfo;

    public User(String name, String email, String senha, BodyInfo bodyInfo){

        this.name = name;
        this.bodyInfo = bodyInfo;
        this.senha = senha;
        this.email = email;
    }

    public User(){}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


    public BodyInfo getBodyInfo() {
        return bodyInfo;
    }

    public void setBodyInfo(BodyInfo bodyInfo) {
        this.bodyInfo = bodyInfo;
    }



}
