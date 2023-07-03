package com.daedrii.bodyapp;

import static org.junit.Assert.*;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.daedrii.bodyapp.controller.home.HomeController;
import com.daedrii.bodyapp.controller.sign.SignUpController;
import com.daedrii.bodyapp.model.fatsecret.FoodDetails;
import com.daedrii.bodyapp.model.fatsecret.Serving;
import com.daedrii.bodyapp.model.user.BodyInfo;
import com.daedrii.bodyapp.model.user.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HomeControllerInstrumentedTest {

    private UserInfo testUser;
    private BodyInfo testBody;
    private static String currentDate;
    private final Integer CALORIES_AMOUNT = 100;
    private final Double CARBS_AMOUNT = 20.6;
    private final Double FAT_AMOUNT = 35.1;
    private final Double PROTEIN_AMOUNT = 10.3;
    private final Double DELTA_ASSERT_DOUBLE = 0.0001;


    @Test
    public void Teste1_setSeedData(){

        currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());

        testUser = new UserInfo();
        testBody = new BodyInfo();

        testBody.setWeight(55);
        testBody.setActLevel(BodyInfo.ActLevel.ACTIVE);
        testBody.setHeight(160);
        testBody.setGoal(BodyInfo.DietGoal.KEEP);
        testBody.setDiet(BodyInfo.DietType.MidCarb);
        testBody.setGender(BodyInfo.Sex.FEMININO);
        testBody.setIMC(18.0);
        testBody.setIDR(2000.0);
        testBody.setAge(23);

        testUser.setPhone("123456789");
        testUser.setName("Mariazinha");
        testUser.setEmail("mariazinha@mail.com");
        testUser.setBirthDate("01/01/2000");
        testUser.setBodyInfo(testBody);

        SignUpController.setNewBodyInfo(testBody);
        SignUpController.setNewUserInfo(testUser);

        AtomicBoolean callbackResult = new AtomicBoolean(false);
        Consumer<Boolean> callback = result -> callbackResult.set(result);

        SignUpController.handleUserDataSignUp(testUser.getName(), testUser.getPhone(), testUser.getEmail(), "123456789", callback);


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            assertTrue(callbackResult.get());

        }

    }

    @Test
    public void Teste2_ShouldInitDatabaseLinkProperly(){

        HomeController.initDatabaseLink();

        assertEquals(FirebaseDatabase.getInstance(), HomeController.getDatabase());
        assertEquals(FirebaseAuth.getInstance(), HomeController.getUserInstance());
        assertEquals(new UserInfo().getName(), HomeController.getUserInfo().getName());
        assertEquals(new BodyInfo().getGender(), HomeController.getUserBodyInfo().getGender());

    }

    @Test
    public void Teste3_ShouldGetUserDataCorrectly(){

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<UserInfo> callbackResult = new AtomicReference<UserInfo>();

        HomeController.getUserData(userInfo -> {
            callbackResult.set(userInfo);
            latch.countDown();

        });

        try {
            // Aguarda até que a operação de exclusão seja concluída
            latch.await();
            //Log.d("teste doido", callbackResult.get().getName());
            //TODO conferir todos os dados do usuário
            assertEquals("Mariazinha", callbackResult.get().getName());
            assertEquals("mariazinha@mail.com", callbackResult.get().getEmail());
            assertEquals(BodyInfo.Sex.FEMININO, callbackResult.get().getBodyInfo().getGender());
            assertEquals(BodyInfo.ActLevel.ACTIVE, callbackResult.get().getBodyInfo().getActLevel());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void Teste4_ShouldAddFoodListToDBCorrectly(){
        ArrayList<FoodDetails> foodList = new ArrayList<>();
        List<Serving> servings = new ArrayList<>();
        servings.add(new Serving(CALORIES_AMOUNT, CARBS_AMOUNT, PROTEIN_AMOUNT, FAT_AMOUNT));
        foodList.add(new FoodDetails("0", "Comida Teste", "La comida del tieste", "especial", "link.com", servings));

        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean addResult = new AtomicBoolean(false);

        HomeController.addFoodListToDB(foodList, servings.get(0).getCalories(), success -> {
            addResult.set(success);
            latch.countDown();

        });

        try {
            latch.await();
            assertTrue(addResult.get());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void Teste5_ShouldGetIRDayCorrectly(){

        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger irResult = new AtomicInteger();

        HomeController.getIRDay(irValue -> {
            irResult.set(irValue);
            latch.countDown();
        });

        try{
            latch.await();
            assertEquals(CALORIES_AMOUNT.intValue(), irResult.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void Teste6_ShouldGetBarChartDataSuccessfully(){


        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<List<Serving>> servingsOfDayResult = new AtomicReference<List<Serving>>();

        Log.d("Data Atual", currentDate.toString());

        HomeController.getBarChartData(currentDate, servings -> {
            servingsOfDayResult.set(servings);
            latch.countDown();
        });

        try{
            Integer totalCaloriesTest = 0;
            Double totalCarbsTest = 0.0, totalFatTest = 0.0, totalProteinTest = 0.0;
            latch.await();
            for(Serving serving: servingsOfDayResult.get()){
                totalCaloriesTest += serving.getCalories();
                totalCarbsTest += serving.getCarbohydrate();
                totalFatTest += serving.getFat();
                totalProteinTest += serving.getProtein();
            }
            assertEquals(CALORIES_AMOUNT.intValue(), totalCaloriesTest.intValue());
            assertEquals(CARBS_AMOUNT, totalCarbsTest, DELTA_ASSERT_DOUBLE);
            assertEquals(PROTEIN_AMOUNT, totalProteinTest, DELTA_ASSERT_DOUBLE);
            assertEquals(FAT_AMOUNT, totalFatTest, DELTA_ASSERT_DOUBLE);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void Teste7_ShouldThrowAnExceptionWhenTryToAddFoodListToDBWhenFoodListIsEmpty(){
        ArrayList<FoodDetails> foodList = new ArrayList<>();

        CountDownLatch latch = new CountDownLatch(1);

        AtomicBoolean callbackResult = new AtomicBoolean(false);
        Consumer<Boolean> callback = result -> {
            callbackResult.set(result);
            latch.countDown();
        };

        assertThrows(RuntimeException.class, () -> {
            HomeController.addFoodListToDB(foodList, 0, callback);
            latch.await();
        });

    }


    @Test
    public void Teste8_clearSeedData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            CountDownLatch latch = new CountDownLatch(1);
            AtomicBoolean deleteResult = new AtomicBoolean(false);

            //Código para deletar a Lista do banco de dados.
            DatabaseReference foodListsRef = FirebaseDatabase.getInstance().getReference()
                    .child("FoodList Per Day")
                    .child(FirebaseAuth.getInstance().getUid());

            foodListsRef.removeValue().addOnCompleteListener(task -> {
                assertTrue(task.isSuccessful());
            });

            SignUpController.deleteUserData(userId, success -> {
                deleteResult.set(success);
                latch.countDown();
            });

            try {
                // Aguarda até que a operação de exclusão seja concluída
                latch.await();
                assertTrue(deleteResult.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
