package com.chechu.onthego;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class AdapterItemConsumable extends ArrayAdapter<ItemConsumable> implements Filterable {
    private ArrayList<ItemConsumable> auxItemList;
    private Context context;

    AdapterItemConsumable(Context context) {
        super(context, R.layout.item_consumable, getItems());
        this.context = context;
    }

    //view holder cache
    private static class ViewHolder {
        ImageView iconView;
        TextView titleView;
        TextView descriptionView;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final ItemConsumable itemConsumable = getItem(position);
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_consumable, parent, false);
            viewHolder = new ViewHolder();

            //get item id
            viewHolder.iconView = convertView.findViewById(R.id.consumableIcon);
            viewHolder.titleView = convertView.findViewById(R.id.consumableTitle);
            viewHolder.descriptionView = convertView.findViewById(R.id.consumableDescription);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        //assign objects to viewholder
        //viewHolder.iconView.setImageDrawable(itemConsumable.getPhoto());
        viewHolder.titleView.setText(itemConsumable.getName());
        viewHolder.descriptionView.setText(itemConsumable.getDescription());

        return convertView;
    }

    private static ArrayList<ItemConsumable> getItems() {
        final ArrayList<ItemConsumable> arrayList = new ArrayList<>();

        arrayList.add(new ItemConsumable("Lechuga", "Es verde"));
        arrayList.add(new ItemConsumable("Cocholate", "Color mierda"));
        arrayList.add(new ItemConsumable("Platano", "¡Corre!"));
        arrayList.add(new ItemConsumable("Coco", "Como la peli"));
        arrayList.add(new ItemConsumable("Lejia", "Chupitazo"));
        arrayList.add(new ItemConsumable("Manzana", "Roja || Verde"));
        arrayList.add(new ItemConsumable("Cocretas", "De bechamel"));
        arrayList.add(new ItemConsumable("Mataratas", "Pa la topasio"));

        return  arrayList;
    }
}