package com.daedrii.bodyapp;

import com.daedrii.bodyapp.controller.home.HomeController;
import com.daedrii.bodyapp.model.fatsecret.FoodDetails;
import com.daedrii.bodyapp.model.fatsecret.Serving;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class HomeControllerTest {
    private final Integer CALORIES_AMOUNT = 100;
    private final Double CARBS_AMOUNT = 20.6;
    private final Double FAT_AMOUNT = 35.1;
    private final Double PROTEIN_AMOUNT = 10.3;
    private final Double DELTA_ASSERT_DOUBLE = 0.0001;

    String currentDate;

    @Mock
    private FirebaseDatabase mockDatabase;

    @Mock
    private FirebaseAuth mockAuth;

    @Mock
    private DatabaseReference mockUserInfoRef;

    @Mock
    private DatabaseReference mockUserBodyRef;

    @Mock
    private DatabaseReference mockFoodListsRef;

    @InjectMocks
    private HomeController homeController;

    @Before
    public void setUp() throws IllegalAccessException, NoSuchFieldException {
        MockitoAnnotations.initMocks(this);

        currentDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());

        String userId = "123456789";

        // Configurar o retorno do método getReference() do mockDatabase
        when(mockDatabase.getReference(anyString())).thenReturn(mockUserInfoRef);
        when(mockDatabase.getReference("UserInfo/" + userId)).thenReturn(mockUserInfoRef);
        when(mockDatabase.getReference("BodyInfo/" + userId)).thenReturn(mockUserBodyRef);
        when(mockDatabase.getReference("FoodList Per Day/" + userId)).thenReturn(mockFoodListsRef);

        // Configurar o retorno do método child() do mockUserInfoRef, mockUserBodyRef e mockFoodListsRef
        when(mockUserInfoRef.child(anyString())).thenReturn(mockUserBodyRef);
        when(mockUserBodyRef.child(anyString())).thenReturn(mockUserBodyRef);

        // Configurar o retorno do método child() para a primeira chamada em mockFoodListsRef
        when(mockFoodListsRef.child(anyString())).thenReturn(mockFoodListsRef);

        // Configurar o retorno do método child() para a segunda chamada em mockFoodListsRef
        when(mockFoodListsRef.push()).thenReturn(mockFoodListsRef);
        when(mockFoodListsRef.getKey()).thenReturn("#333");

        // Configurar o retorno do método setValue() do mockFoodListsRef
        doAnswer(invocation -> {
            // Aqui você pode definir o comportamento esperado do método setValue()
            // Por exemplo, você pode verificar os argumentos passados para o método,
            // armazená-los em uma variável para posterior verificação ou lançar uma exceção
            // se necessário para simular um erro.
            return null; // ou um valor mock se desejar
        }).when(mockFoodListsRef).setValue(anyList());

        homeController = new HomeController();
        Field field = HomeController.class.getDeclaredField("foodListsRef");
        field.setAccessible(true);
        field.set(homeController, mockFoodListsRef);
    }




    @Test
    public void addFoodListToDB_ShouldInvokeCallbacksAndSetValueCorrectly() {

        // Configurar o estado necessário para o teste
        ArrayList<FoodDetails> foodList = new ArrayList<>();
        List<Serving> servings = new ArrayList<>();
        servings.add(new Serving(CALORIES_AMOUNT, CARBS_AMOUNT, PROTEIN_AMOUNT, FAT_AMOUNT));
        foodList.add(new FoodDetails("0", "Comida Teste", "La comida del tieste", "especial", "link.com", servings));

        int IR = 100;

        // Utilize CountDownLatch para sincronizar o teste com a chamada assíncrona do método
        CountDownLatch latch = new CountDownLatch(1);

        AtomicBoolean successResult = new AtomicBoolean(false);
        Consumer<Boolean> successCallback = result -> {
            successResult.set(result);
            latch.countDown(); // Libera o latch quando o callback é invocado
        };

        // Configurar o comportamento do mockFoodListsRef.child(currentDate).push().getKey()
        String newFoodListKey = "#333";
        when(mockFoodListsRef.child(anyString()).push().getKey()).thenReturn(newFoodListKey);

        // Configurar o comportamento do mockFoodListsRef.child(currentDate).child("foodList").child(newFoodListKey).setValue(foodList)
        doAnswer(invocation -> {
            // Aqui você pode definir o comportamento esperado do método setValue()
            // Por exemplo, você pode verificar os argumentos passados para o método,
            // armazená-los em uma variável para posterior verificação ou lançar uma exceção
            // se necessário para simular um erro.
            // No final, você precisa invocar o callback passando o resultado esperado
            ((OnCompleteListener<Void>) invocation.getArgument(0)).onComplete(mock(Task.class));
            return null; // ou um valor mock se desejar
        }).when(mockFoodListsRef.child(currentDate).child("foodList").child(newFoodListKey)).setValue(foodList);

        // Configurar o comportamento do mockFoodListsRef.child(currentDate).child("IDR").setValue(userBodyInfo.getIDR())
        doAnswer(invocation -> {
            // Aqui você pode definir o comportamento esperado do método setValue()
            // e invocar o callback passando o resultado esperado
            ((OnCompleteListener<Void>) invocation.getArgument(0)).onComplete(mock(Task.class));
            return null; // ou um valor mock se desejar
        }).when(mockFoodListsRef.child(currentDate).child("IDR")).setValue(1000);

        // Configurar o comportamento do mockFoodListsRef.child(currentDate).child("IR").setValue(IR)
        doAnswer(invocation -> {
            // Aqui você pode definir o comportamento esperado do método setValue()
            // e invocar o callback passando o resultado esperado
            ((OnCompleteListener<Void>) invocation.getArgument(0)).onComplete(mock(Task.class));
            return null; // ou um valor mock se desejar
        }).when(mockFoodListsRef.child(currentDate).child("IR")).setValue(IR);

        homeController.addFoodListToDB(foodList, IR, successCallback);
        try {
            // Aguardar a conclusão do callback com uma duração máxima
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verificar se as interações esperadas com os mocks ocorreram
        verify(mockDatabase).getReference("FoodList Per Day/123456789");
        verify(mockFoodListsRef).child("2023/07/03");
        verify(mockFoodListsRef).child(currentDate).child("foodList").child(newFoodListKey).setValue(foodList);
        verify(mockFoodListsRef).child(currentDate).child("IDR").setValue(1000);
        verify(mockFoodListsRef).child(currentDate).child("IR").setValue(IR);

        // Verificar se o callback foi invocado com o resultado correto
        assertTrue(successResult.get());
    }

}
