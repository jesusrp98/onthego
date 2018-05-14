package com.chechu.onthego;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class AdapterItemConsumable extends RecyclerView.Adapter<AdapterItemConsumable.ViewHolder> {
    private ArrayList<ItemConsumable> itemList;
    private TypedArray photoArray;
    private Context context;

    AdapterItemConsumable(Context context, ArrayList<ItemConsumable> itemConsumables) {
        this.photoArray = context.getResources().obtainTypedArray(R.array.icon_view);
        this.itemList = itemConsumables;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consumable_card, parent, false));
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ItemConsumable item = itemList.get(position);

        //set info to ui
        holder.photo.setImageResource(photoArray.getResourceId(item.getId() - 1, -1));
        holder.name.setText(item.getName());
        holder.quantity.setText(String.format(context.getString(R.string.display_consumible_price), item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    //save xml ui into to cache
    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView photo;
        private final TextView name;
        private final TextView quantity;

        private ViewHolder(View view) {
            super(view);

            photo = view.findViewById(R.id.consumableIcon);
            name = view.findViewById(R.id.consumableTitle);
            quantity = view.findViewById(R.id.consumablePrice);
        }
    }
}

