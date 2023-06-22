package com.daedrii.bodyapp.view.home;

import static com.daedrii.bodyapp.model.fatsecret.XmlParser.parseFoodDetails;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.model.fatsecret.FoodDetails;
import com.daedrii.bodyapp.model.fatsecret.FatSecretApiService;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class DiaryFragment extends Fragment {

    private ExecutorService executorService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        executorService = Executors.newSingleThreadExecutor();

        searchFoodsAsync("linguiça");
        getFoodDetailsAsync("3085");

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void searchFoodsAsync(String query) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return FatSecretApiService.searchFoods(query);
            } catch (IOException | OAuthException e) {
                Log.e("TAG", "Erro na solicitação da API: " + e.getMessage());
                return null;
            }
        }, executorService).thenAccept(result -> {
            if (result != null) {
                // Processar os resultados da busca de alimentos
                Log.d("TAG", "Resultados da busca de alimentos: " + result);
                // Não chame getFoodDetailsAsync("food_id_here") aqui
            }
        });
    }

    private void getFoodDetailsAsync(String foodId) {
        CompletableFuture.supplyAsync(() -> {
            try {
                String xmlData = FatSecretApiService.getFoodDetails(foodId);

                return xmlData;

            } catch (IOException | OAuthMessageSignerException | OAuthExpectationFailedException |
                     OAuthCommunicationException e) {
                Log.e("TAG", "Erro na solicitação da API: " + e.getMessage());
                return null;
            }
        }, executorService).thenAccept(result -> {
            if (result != null) {
                // Processar os detalhes do alimento
                FoodDetails foodDetails = parseFoodDetails(result);
                if (foodDetails != null) {
                    Log.d("TAG1", "Detalhes do alimento: " + foodDetails);
                }
            }
        });
    }
}
