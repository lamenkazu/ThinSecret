package com.daedrii.bodyapp.model;

public class UserSingleton {

    private static UserSingleton instance;
    private User user;

    private UserSingleton(){
        BodyInfo seedBodyInfo = new BodyInfo();
        seedBodyInfo.setAge(23);
        seedBodyInfo.setGender(BodyInfo.Sex.MASCULINO);
        seedBodyInfo.setGoal(BodyInfo.DietGoal.GAIN);
        seedBodyInfo.setActLevel(BodyInfo.ActLevel.HIGH_ACTIVE);
        seedBodyInfo.setHeight(160);
        seedBodyInfo.setWeight(60);
        user = new User("Erick", "333@mail.com", "159753", seedBodyInfo);
    }

    public static synchronized UserSingleton getInstance(){
        if(instance == null){
            instance = new UserSingleton();
        }
        return instance;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User newUser) {
        user = newUser;
    }

}
