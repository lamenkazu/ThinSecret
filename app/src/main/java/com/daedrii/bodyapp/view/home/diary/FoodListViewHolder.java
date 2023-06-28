package com.daedrii.bodyapp.view.home.diary;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daedrii.bodyapp.R;
import com.google.android.material.textview.MaterialTextView;

public class FoodListViewHolder extends RecyclerView.ViewHolder {

    public MaterialTextView getFoodName() {
        return foodName;
    }

    public void setFoodName(MaterialTextView foodName) {
        this.foodName = foodName;
    }

    public MaterialTextView getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(MaterialTextView foodDescription) {
        this.foodDescription = foodDescription;
    }

    public MaterialTextView getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(MaterialTextView searchTitle) {
        this.searchTitle = searchTitle;
    }

    MaterialTextView foodName, foodDescription, searchTitle;


    public FoodListViewHolder(@NonNull View itemView) {
        super(itemView);

        foodName = itemView.findViewById(R.id.lbl_item_title);
        foodDescription = itemView.findViewById(R.id.lbl_item_description);
        searchTitle = itemView.findViewById(R.id.search_title);
    }
}
