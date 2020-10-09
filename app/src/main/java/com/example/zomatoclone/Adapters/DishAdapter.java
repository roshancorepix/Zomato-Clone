package com.example.zomatoclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zomatoclone.Model.DishModel;
import com.example.zomatoclone.R;

import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder>{

    private Context context;
    private List<DishModel> dishModelList;

    public DishAdapter(Context context, List<DishModel> dishModelList) {
        this.context = context;
        this.dishModelList = dishModelList;
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dish_item, parent, false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        holder.dishImage.setImageResource(dishModelList.get(position).getDishImage());
    }

    @Override
    public int getItemCount() {
        return dishModelList.size();
    }

    public static class DishViewHolder extends RecyclerView.ViewHolder{

        ImageView dishImage;
        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            dishImage = itemView.findViewById(R.id.dish_image);
        }
    }
}