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
        super(context, R.layout.item_consumable);
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
            //viewHolder.iconView = convertView.findViewById(R.id.item_operation_icon);
            //viewHolder.titleView = convertView.findViewById(R.id.item_operation_title);
            //viewHolder.descriptionView = convertView.findViewById(R.id.item_operation_description);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        //assign objects to viewholder
        //viewHolder.titleView.setText(itemOperation.getTitle());
        //viewHolder.descriptionView.setText(Html.fromHtml(itemOperation.getDescription()));

        return convertView;
    }
}