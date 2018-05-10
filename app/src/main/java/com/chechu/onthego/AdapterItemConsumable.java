package com.chechu.onthego;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class AdapterItemConsumable extends RecyclerView.Adapter<AdapterItemConsumable.ViewHolder> {
    private ArrayList<ItemConsumable> itemList;
    private TypedArray idArray;
    private Context context;

    AdapterItemConsumable(Context context) {
        this.idArray = context.getResources().obtainTypedArray(R.array.icon_view);
        this.itemList = getItems();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consumable_card, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ItemConsumable item = itemList.get(position);

        //set info to ui
        holder.consumiblePhoto.setImageDrawable(item.getConsumiblePhoto());
        holder.consumibleTitle.setText(item.getConsumibleName());
        holder.consumableQuantity.setText(String.format(context.getString(R.string.display_consumible_price), item.getConsumiblePrice()));
    }

    //TODO realizar las requests a la bd
    private static ArrayList<ItemConsumable> getItems() {
        final ArrayList<ItemConsumable> arrayList = new ArrayList<>();

        //

        return  arrayList;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    //save xml ui into to cache
    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView consumiblePhoto;
        private final TextView consumibleTitle;
        private final TextView consumableQuantity;

        private ViewHolder(View itemView) {
            super(itemView);

            consumiblePhoto = itemView.findViewById(R.id.consumableIcon);
            consumibleTitle = itemView.findViewById(R.id.consumableTitle);
            consumableQuantity = itemView.findViewById(R.id.consumableDescription);
        }
    }
}