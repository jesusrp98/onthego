package com.chechu.onthego;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class AdapterItemConsumableAction extends RecyclerView.Adapter<AdapterItemConsumableAction.ViewHolder> {
    private ArrayList<ItemConsumable> itemList;
    private Context context;

    AdapterItemConsumableAction(Context context) {
        itemList = getItems();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consumable_action, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ItemConsumable item = itemList.get(position);

        //set data to result item
        holder.photo.setImageDrawable(item.getPhoto());
        holder.title.setText(item.getName());
        holder.description.setText(item.getDescription());
    }

    private static ArrayList<ItemConsumable> getItems() {
        final ArrayList<ItemConsumable> arrayList = new ArrayList<>();

        arrayList.add(new ItemConsumable("Lechuga", "Es verde"));
        arrayList.add(new ItemConsumable("Cocholate", "Color mierda"));
        arrayList.add(new ItemConsumable("Platano", "¡Corre!"));
        arrayList.add(new ItemConsumable("Coco", "Como la peli"));
        arrayList.add(new ItemConsumable("Lejia", "Chupitazo"));
        arrayList.add(new ItemConsumable("Manzana", "Roja || Verde"));
        arrayList.add(new ItemConsumable("Cocretas", "De cocido"));
        arrayList.add(new ItemConsumable("Mataratas", "Pa la topasio"));

        return  arrayList;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView photo;
        private final TextView title;
        private final TextView description;

        private ViewHolder(View itemView) {
            super(itemView);
            //get item id for cache
            photo = itemView.findViewById(R.id.consumableIcon);
            title = itemView.findViewById(R.id.consumableTitle);
            description = itemView.findViewById(R.id.consumableDescription);
        }
    }
}