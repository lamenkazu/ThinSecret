package com.daedrii.bodyapp.view.home.diary;

import static com.daedrii.bodyapp.model.fatsecret.XmlParser.parseFoodDetails;
import static com.daedrii.bodyapp.model.fatsecret.XmlParser.parseFoodSearchResults;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.controller.home.FoodListAdapter;
import com.daedrii.bodyapp.controller.home.HomeController;
import com.daedrii.bodyapp.model.fatsecret.FoodDetails;
import com.daedrii.bodyapp.model.fatsecret.FatSecretApiService;
import com.daedrii.bodyapp.model.fatsecret.Serving;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class DiaryFragment extends Fragment {

    private ExecutorService executorService;
    private RecyclerView recyclerView, searchResultsRecyclerView;
    private FoodListAdapter adapterList, adapterSearch;
    private ArrayList<FoodDetails> foodList, searchResultList;
    private SearchView searchView;
    private Handler searchHandler = new Handler();
    private MaterialTextView idrView, irView;
    private MaterialButton btnEmptyList, btnAddRefToDB;

    private Integer IR = 0;


    private void initComponents(View view){

        executorService = Executors.newSingleThreadExecutor();

        //Views de IDR e IR
        irView = view.findViewById(R.id.irView);
        idrView = view.findViewById(R.id.idrView);

        //SearchItems Component
        searchResultsRecyclerView = view.findViewById(R.id.recycler_search_result);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        searchResultList = new ArrayList<>();
        adapterSearch = new FoodListAdapter(searchResultList, "search");
        searchResultsRecyclerView.setAdapter(adapterSearch);

        //Selected FoodList Component
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        foodList = new ArrayList<>();
        adapterList = new FoodListAdapter(foodList, "list");
        recyclerView.setAdapter(adapterList);

        //SearchView
        searchView = view.findViewById(R.id.food_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Não é necessário lidar com a submissão do texto de pesquisa neste exemplo
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchHandler.removeCallbacksAndMessages(null); // Remover qualquer pesquisa pendente
                searchHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        searchFoodsAsync(newText); // Executar a pesquisa após o atraso
                    }
                }, 250);
                return true;
            }
        });

        adapterSearch.setOnItemClickListener(new FoodListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                searchView.setQuery("", false);
                searchView.clearFocus();
                FoodDetails selectedFood = searchResultList.get(position);
                getFoodDetailsAsync(selectedFood.getFoodId());

            }
        });

        //Botoes
        btnEmptyList = view.findViewById(R.id.btn_esvazia_lista);
        btnEmptyList.setOnClickListener(v -> {
            clearFoodList();
        });

        btnAddRefToDB = view.findViewById(R.id.btn_enviar_lista);
        btnAddRefToDB.setOnClickListener(v -> {
            HomeController.addFoodListToDB(foodList, IR, success -> {
                if(success){
                    clearFoodList();
                }
            });
        });

    }

    private void clearFoodList(){
        foodList.clear();
        HomeController.getIRDay(irValue -> {
            Integer IR = irValue; // Atualizar o valor de IR
            irView.setText("IR: " + IR);
            adapterList.notifyDataSetChanged();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        initComponents(view);

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

        DecimalFormat decimalFormat = new DecimalFormat("#");
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            HomeController.getUserData(userInfo -> {
                idrView.setText("IDR: " + decimalFormat.format(userInfo.getBodyInfo().getIDR()));

            });
        }
    }

    private void searchFoodsAsync(String query) {
        CompletableFuture.supplyAsync(() -> {
            try {
                String xmlData = FatSecretApiService.searchFoods(query);

                return xmlData;
            } catch (IOException e) {
                Log.e("TAG", "Erro na solicitação da API: " + e.getMessage());
                return null;
            } catch (OAuthException e) {
                throw new RuntimeException(e);
            }
        }, executorService).thenAccept(result -> {
            if (result != null) {
                List<FoodDetails> foodDetails = parseFoodSearchResults(result);
                // Processar os resultados da busca de alimentos
                Log.d("TAG", "Resultados da busca de alimentos: " + result);
                // Não chame getFoodDetailsAsync("food_id_here") aqui
                this.searchResultList.clear();
                this.searchResultList.addAll(foodDetails);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapterSearch.notifyDataSetChanged();
                    }
                });
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
                    foodList.add(foodDetails);


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Lista", "Tamanho da lista: " + foodList.size());
                            Serving serving = foodDetails.getServings().get(0);
                            IR += serving.getCalories();
                            irView.setText("IR: " + IR);
                            adapterList.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }
}
