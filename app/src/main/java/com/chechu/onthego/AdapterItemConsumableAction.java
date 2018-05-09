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

public class AdapterItemConsumableAction extends RecyclerView.Adapter<AdapterItemConsumableAction.ViewHolder> {
    private ArrayList<ItemConsumableAction> itemList;
    private TypedArray idArray;
    private Context context;

    AdapterItemConsumableAction(Context context) {
        this.idArray = context.getResources().obtainTypedArray(R.array.icon_view);
        this.itemList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consumable_action, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ItemConsumableAction item = itemList.get(position);

        holder.consumableIcon.setImageDrawable(item.getConsumiblePhoto());
        holder.consumableTitle.setText(item.getConsumibleName());
        holder.consumableDescription.setText(String.format(context.getString(R.string.display_consumible_amount), item.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView consumableIcon;
        private final TextView consumableTitle;
        private final TextView consumableDescription;

        private ViewHolder(View itemView) {
            super(itemView);

            consumableIcon = itemView.findViewById(R.id.consumableIcon);
            consumableTitle = itemView.findViewById(R.id.consumableTitle);
            consumableDescription = itemView.findViewById(R.id.consumableDescription);
        }
    }
}