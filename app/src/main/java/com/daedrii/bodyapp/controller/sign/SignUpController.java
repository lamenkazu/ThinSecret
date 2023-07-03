package com.daedrii.bodyapp.controller.sign;

import androidx.annotation.NonNull;

import com.daedrii.bodyapp.model.user.BodyInfo;
import com.daedrii.bodyapp.model.user.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.function.Consumer;

public class SignUpController {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference userRef = database.getReference("UserInfo");
    private static DatabaseReference bodyRef = database.getReference("BodyInfo");
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static BodyInfo newBodyInfo = new BodyInfo();
    public static UserInfo newUserInfo = new UserInfo();

    
    /*
    * Método que lida com gerar a autenticação do novo usuário
    * criado durante o SignUp no FirebaseAuth */
    public static void handleUserDataSignUp(String userName, String userPhone,
                                            String userMail, String userPassword, Consumer<Boolean> callback) {

        newUserInfo.setEmail(userMail);
        newUserInfo.setName(userName);
        newUserInfo.setPhone(userPhone);
        newUserInfo.setBodyInfo(newBodyInfo);

        mAuth.createUserWithEmailAndPassword(userMail, userPassword) //cria autenticação para o usuario.
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            
                            if (setUserData(newUserInfo)) {//Chama método que insere os dados do usuário no banco de dados
                                
                                callback.accept(true);
                            }

                        } else {
                            callback.accept(false);
                        }
                    }
                });
    }

    /*
     * Método que lida com Inserir os dados do usuário inseridos
     * durante o SignUp no banco de dados Firebase */
    public static Boolean setUserData(UserInfo actualUserInfo) {
        userRef.child(mAuth.getCurrentUser().getUid())
                .setValue(actualUserInfo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        bodyRef.child(mAuth.getCurrentUser().getUid())
                                .setValue(actualUserInfo.getBodyInfo());
                    }
                });

        return true;
    }

    
    /*
    * Método que lida com deletar os dados e a autenticação de 
    * um usuário do sistema permanentemente. */
    public static void deleteUserData(String userId, Consumer<Boolean> callback) {

        mAuth.getCurrentUser().delete() //Deleta a autenticação do usuário.

                //Ao deletar a autenticação, remove os dados das Coleções UserInfo e BodyInfo
                // utilizando a UUID do usuário que acabou de ser removido do sistema
                .addOnCompleteListener(taskAuth -> {
                            if (taskAuth.isSuccessful()) {
                                userRef.child(userId).removeValue() // Excluir os dados do UserInfo do Realtime Database

                                        //Ao excluir os dados de UserInfo do Realtime Database,
                                        //remove os dados corporais de BodyInfo
                                        .addOnCompleteListener(taskUserInfo -> {
                                                    if(taskUserInfo.isSuccessful()){
                                                        bodyRef.child(userId).removeValue()

                                                                //Ao excluir todos os dados, retorna true para
                                                                // caso tenha excluído com sucesso recursivamente.
                                                                .addOnCompleteListener(taskBodyInfo -> {
                                                                            callback.accept(taskBodyInfo.isSuccessful());
                                                                        }
                                                                );
                                                    }

                                                }
                                        );
                            } else {
                                callback.accept(false);
                            }
                        }
                );
    }

    /*
    * Método que lida com o calculo do IDR */
    public static void handleIDRCalculation(BodyInfo newBodyInfo) {
        //Finaliza Calculos Corporais
        BodyInfo bodyInfo = newBodyInfo;
        Double metBasal = bodyInfo.getActLevel().getMetBasal();
        Double step1, step2, step3, somaSteps;

        if (bodyInfo.getGender() == BodyInfo.Sex.MASCULINO) {
            step1 = 13.75 * bodyInfo.getWeight();
            step2 = 5.003 * bodyInfo.getHeight();
            step3 = 6.755 * bodyInfo.getAge();
            somaSteps = 66.5 + step1 + step2 - step3;

        } else {
            step1 = 9.563 * bodyInfo.getWeight();
            step2 = 1.85 * bodyInfo.getHeight();
            step3 = 4.676 * bodyInfo.getAge();
            somaSteps = 655.1 + step1 + step2 - step3;
        }

        if (bodyInfo.getGoal() == BodyInfo.DietGoal.LOSS) {
            somaSteps -= 500; // Objetivo de perda de peso
            
        } else if (bodyInfo.getGoal() == BodyInfo.DietGoal.GAIN) {
            somaSteps += 500; // Objetivo de ganho de peso
        }

        newBodyInfo.setIDR(metBasal * somaSteps);

    }

    public static void handleAgeInfos(Long idade, String date) {
        newBodyInfo.setAge(idade.intValue());
        newUserInfo.setBirthDate(date);
        handleIDRCalculation(newBodyInfo);
    }

    public static void setActLevel(BodyInfo.ActLevel level){
        newBodyInfo.setActLevel(level);
    }
   public static void setBodyData(Integer height, Integer weight, Double IMC){
        newBodyInfo.setIMC(IMC);
        newBodyInfo.setHeight(height);
        newBodyInfo.setWeight(weight);
    }

    public static void setGoal(BodyInfo.DietGoal goal){
        newBodyInfo.setGoal(goal);
    }

    public static void setDiet(BodyInfo.DietType diet){
        newBodyInfo.setDiet(diet);
    }

    public static void handleGenderChange(BodyInfo.Sex gender) {
        newBodyInfo.setSex(gender);
    }

    public static BodyInfo getNewBodyInfo() {
        return newBodyInfo;
    }

    public static UserInfo getNewUserInfo() {
        return newUserInfo;
    }

    public static void setNewBodyInfo(BodyInfo newBodyInfo) {
        SignUpController.newBodyInfo = newBodyInfo;
    }

    public static void setNewUserInfo(UserInfo newUserInfo) {
        SignUpController.newUserInfo = newUserInfo;
    }
}
