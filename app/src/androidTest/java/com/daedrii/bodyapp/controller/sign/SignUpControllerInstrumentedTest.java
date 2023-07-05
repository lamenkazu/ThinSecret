package com.daedrii.bodyapp.controller.sign;

import static org.junit.Assert.*;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.daedrii.bodyapp.model.user.BodyInfo;
import com.daedrii.bodyapp.model.user.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignUpControllerInstrumentedTest {

    private UserInfo testUser;
    private BodyInfo testeBody;

    @Before
    public void setSeedData(){

        testUser = new UserInfo();
        testeBody = new BodyInfo();

        testeBody.setWeight(55);
        testeBody.setActLevel(BodyInfo.ActLevel.ACTIVE);
        testeBody.setHeight(160);
        testeBody.setGoal(BodyInfo.DietGoal.KEEP);
        testeBody.setDiet(BodyInfo.DietType.MidCarb);
        testeBody.setGender(BodyInfo.Sex.FEMININO);
        testeBody.setIMC(18.0);
        testeBody.setIDR(2000.0);
        testeBody.setAge(23);

        testUser.setPhone("123456789");
        testUser.setName("Mariazinha");
        testUser.setEmail("mariazinha@mail.com");
        testUser.setBirthDate("01/01/2000");
        testUser.setBodyInfo(testeBody);

        SignUpController.setNewBodyInfo(testeBody);

    }

    @Test
    public void Teste1_ShouldHandleUserDataSignUpSuccessfully() {


        AtomicBoolean callbackResult = new AtomicBoolean(false);
        Consumer<Boolean> callback = result -> callbackResult.set(result);


        SignUpController.handleUserDataSignUp(testUser.getName(), testUser.getPhone(), testUser.getEmail(), "123456789", callback);


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        assertTrue(callbackResult.get());
    }

    @Test
    public void Teste2_ShouldSetUserDataSuccessfully() {
        // Mock user info

        testUser.setName("Nicolau Flamel");
        testUser.setPhone("31996585478");

        // Call the method
        boolean result = SignUpController.setUserData(testUser);

        // Assert the result
        assertTrue(result);
    }

    @Test
    public void Teste3_ShouldDeleteUserData() {
        // Certifique-se de ter o usuário Jhon Doe criado antes de executar esse teste
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            CountDownLatch latch = new CountDownLatch(1);
            AtomicBoolean deleteResult = new AtomicBoolean(false);

            SignUpController.deleteUserData(userId, success -> {
                deleteResult.set(success);
                latch.countDown();
            });

            try {
                // Aguarda até que a operação de exclusão seja concluída
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            assertTrue("Failed to delete test user.", deleteResult.get());
        }
    }

    @Test
    public void ShouldHandleAgeInfosProperly() {
        SignUpController.handleAgeInfos(23L, "1999/12/01");
        assertEquals(23, SignUpController.getNewBodyInfo().getAge().intValue());
        assertEquals("1999/12/01", SignUpController.getNewUserInfo().getBirthDate());
    }

    @Test
    public void ShouldSetActLevelProperly() {
        SignUpController.setActLevel(BodyInfo.ActLevel.SEDENTARY);
        assertEquals(BodyInfo.ActLevel.SEDENTARY, SignUpController.getNewBodyInfo().getActLevel());
    }

    @Test
    public void ShouldSetBodyDataProperly() {
        SignUpController.setBodyData(170, 65, 22.5);
        assertEquals(170, SignUpController.getNewBodyInfo().getHeight().intValue());
        assertEquals(65, SignUpController.getNewBodyInfo().getWeight().intValue());
        assertEquals(22.5, SignUpController.getNewBodyInfo().getIMC(), 0.001);
    }

    @Test
    public void ShouldSetDietGoalProperly() {
        SignUpController.setGoal(BodyInfo.DietGoal.GAIN);
        assertEquals(BodyInfo.DietGoal.GAIN, SignUpController.getNewBodyInfo().getGoal());
    }

    @Test
    public void ShouldSetDietTypeProperly(){
        SignUpController.setDiet(BodyInfo.DietType.LowCarb);
        assertEquals(BodyInfo.DietType.LowCarb, SignUpController.getNewBodyInfo().getDiet());
    }

    @Test
    public void ShouldHandleGenderChangeProperly(){

        assertEquals(testeBody.getGender(), SignUpController.getNewBodyInfo().getGender());
        Log.d("Gender: ", testeBody.getGender().toString()); //Pelo Seed deveria ser Feminino

        SignUpController.handleGenderChange(
                testeBody.getGender() == BodyInfo.Sex.MASCULINO ?
                        BodyInfo.Sex.FEMININO :
                        BodyInfo.Sex.MASCULINO);

        assertEquals(testeBody.getGender(), SignUpController.getNewBodyInfo().getGender());
        Log.d("Gender: ", testeBody.getGender().toString()); //Pelo Seed deveria ser Masculino


    }

    @Test
    public void ShouldGetAndSetNewBodyInfoProperly() {

        //primeiro teste
        BodyInfo newBodyInfo = SignUpController.getNewBodyInfo();
        assertEquals(newBodyInfo, testeBody);

        //seta outros dados para re-teste
        BodyInfo otherBody = new BodyInfo();
        otherBody.setWeight(90);
        otherBody.setActLevel(BodyInfo.ActLevel.SEDENTARY);
        otherBody.setHeight(160);
        otherBody.setGoal(BodyInfo.DietGoal.LOSS);
        otherBody.setDiet(BodyInfo.DietType.LowCarb);
        otherBody.setGender(BodyInfo.Sex.FEMININO);
        otherBody.setIMC(35.0);
        otherBody.setIDR(2000.0);
        otherBody.setAge(23);

        //segundo teste
        SignUpController.setNewBodyInfo(otherBody);
        newBodyInfo = SignUpController.getNewBodyInfo();
        assertEquals(newBodyInfo, otherBody);

    }
}
