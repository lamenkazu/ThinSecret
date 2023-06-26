package com.daedrii.bodyapp.model.user;

public class UserInfo {

    private String name;
    private String email;
//    private String senha;
    private String phone;
    private String birthDate;
    private BodyInfo bodyInfo;


    public UserInfo(String name, String email, String phone, String birthDate, BodyInfo bodyInfo){

        this.name = name;
        this.bodyInfo = bodyInfo;
//        this.senha = senha;
        this.email = email;
        this.phone = phone;
    }

    public UserInfo(){}

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", bodyInfo=" + bodyInfo +
                '}';
    }

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

//    public String getSenha() {
//        return senha;
//    }
//
//    public void setSenha(String senha) {
//        this.senha = senha;
//    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
    public BodyInfo getBodyInfo() {
        return bodyInfo;
    }

    public void setBodyInfo(BodyInfo bodyInfo) {
        this.bodyInfo = bodyInfo;
    }



}
