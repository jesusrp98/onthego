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

public class AdapterItemConsumableAction extends RecyclerView.Adapter<AdapterItemConsumableAction.ViewHolder> {
    private ArrayList<ItemConsumableAction> itemList;
    private TypedArray idArray;
    private Context context;

    AdapterItemConsumableAction(Context context) {
        this.idArray = context.getResources().obtainTypedArray(R.array.icon_view);
        this.itemList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consumable_action, parent, false));
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ItemConsumableAction item = itemList.get(position);

        //set info to ui
        holder.consumableIcon.setImageDrawable(item.getConsumiblePhoto());
        holder.consumableTitle.setText(item.getConsumibleName());
        holder.consumableAmount.setText(String.format(context.getString(R.string.display_consumible_amount), item.getQuantity()));
        holder.consumablePrice.setText(String.format(context.getString(R.string.display_consumible_price), item.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addRandomItem() {
        //TODO simulate addItem to shopping cart
    }

    public String getItemList() {
        String aux = "";
        for (ItemConsumableAction item : itemList)
            aux += " " + item.getConsumibleName() + ": " + item.getQuantity() + " unidades.\n";
        return aux;
    }

    //save xml ui into to cache
    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView consumableIcon;
        private final TextView consumableTitle;
        private final TextView consumableAmount;
        private final TextView consumablePrice;

        private ViewHolder(View itemView) {
            super(itemView);

            consumableIcon = itemView.findViewById(R.id.consumableIcon);
            consumableTitle = itemView.findViewById(R.id.consumableTitle);
            consumableAmount = itemView.findViewById(R.id.consumableAmount);
            consumablePrice = itemView.findViewById(R.id.consumablePrice);
        }
    }
}