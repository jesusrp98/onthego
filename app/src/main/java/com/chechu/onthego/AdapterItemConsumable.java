package com.chechu.onthego;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class AdapterItemConsumable extends RecyclerView.Adapter<AdapterItemConsumable.ViewHolder> implements Filterable{
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ItemConsumable item = itemList.get(position);

        //set info to ui
        holder.consumiblePhoto.setImageDrawable(photoArray.getDrawable(item.getConsumibleId()));
        holder.consumibleTitle.setText(item.getConsumibleName());
        holder.consumableQuantity.setText(String.format(context.getString(R.string.display_consumible_price), item.getConsumiblePrice()));
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
            consumableQuantity = itemView.findViewById(R.id.consumablePrice);
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        //filters the array adapter when search is activated
        return new Filter() {
            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemList.clear();
                itemList.addAll((ArrayList<ItemConsumable>)results.values);
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final ArrayList<ItemConsumable> filteredArrayList = new ArrayList<>();
                final String string = constraint.toString().toLowerCase();
                final FilterResults results = new FilterResults();

                //checks if search textbox is empty
                if (string.isEmpty()) {
                    results.count = itemList.size();
                    results.values = itemList;
                } else {
                    for (ItemConsumable item : itemList) {
                        if (item.getConsumibleName().toLowerCase().contains(string))
                            filteredArrayList.add(item);
                    }
                    results.count = filteredArrayList.size();
                    results.values = filteredArrayList;
                }
                return results;
            }
        };
    }
}

