package com.daedrii.bodyapp.controller.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daedrii.bodyapp.R;
import com.daedrii.bodyapp.model.fatsecret.FoodDetails;
import com.daedrii.bodyapp.model.fatsecret.Serving;
import com.daedrii.bodyapp.view.home.diary.FoodListViewHolder;

import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListViewHolder> {

    private ArrayList<FoodDetails> items;
    private Boolean viewList, viewSearch;

    private OnItemClickListener itemClickListener;




    public FoodListAdapter(ArrayList<FoodDetails> items, String viewType){
        this.items = items;
        if(viewType.equals("list")){
            viewList = true;
            viewSearch = false;
        }
        else if(viewType.equals("search")){
            viewList = false;
            viewSearch = true;
        }
    }

    @NonNull
    @Override
    public FoodListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if(viewSearch){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_list, parent, false);
        }else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_list, parent, false);


        return new FoodListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListViewHolder holder, int position) {

        FoodDetails foodDetails = items.get(position);
        StringBuilder servingsText = new StringBuilder();


        // Iterar sobre a lista de servidores e criar a representação em string personalizada
        for (Serving serving : foodDetails.getServings()) {
            servingsText
                    .append("Quantidade: ").append(serving.getServingDescription())
                    .append("\nCalorias: ").append(serving.getCalories())
                    .append("\nCarboidratos: ").append(serving.getCarbohydrate())
                    .append(", Proteinas: ").append(serving.getProtein())
                    .append(", Gorduras: ").append(serving.getFat())
                    .append("\nDescrição de medidas: ").append(serving.getMeasurementDescription())
                    .append("\nMetric Serving Amount: ").append(serving.getMetricServingAmount()).append(serving.getMetricServingUnit())
                    .append("\n");
            break;
        }

        if(viewList){
            holder.getFoodName().setText(foodDetails.getFoodName());
            holder.getFoodDescription().setText(servingsText);
        }
        if(viewSearch){

            final int itemPosition = holder.getAdapterPosition();

            holder.getSearchTitle().setText(foodDetails.getFoodName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(itemPosition);
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
