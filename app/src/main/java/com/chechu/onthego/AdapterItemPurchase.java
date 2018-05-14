package com.chechu.onthego;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.amulyakhare.textdrawable.TextDrawable;
import java.util.ArrayList;

public class AdapterItemPurchase extends ArrayAdapter<ItemPurchase> {
    AdapterItemPurchase(Context context, ArrayList<ItemPurchase> arrayList) {
        super(context, R.layout.item_purchase, arrayList);
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final ItemPurchase itemPurchase = getItem(position);
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_purchase, parent, false);
            viewHolder = new ViewHolder();

            //find view items by id
            viewHolder.photo = convertView.findViewById(R.id.item_purchase_icon);
            viewHolder.name = convertView.findViewById(R.id.item_purchase_title);
            viewHolder.description = convertView.findViewById(R.id.item_purchase_description);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        //assign objects to viewholder
        viewHolder.photo.setImageDrawable(
                TextDrawable.builder().beginConfig().textColor(getContext().getResources().getColor(android.R.color.white))
                        .endConfig().buildRound(String.format("#%d", position + 1),
                        getContext().getResources().getColor(R.color.colorAccent))
        );
        viewHolder.name.setText(itemPurchase.getId());
        viewHolder.description.setText(String.format(getContext().getString(R.string.dialog_purchase_date), itemPurchase.getDate()));

        return convertView;
    }

    //view holder cache
    private static class ViewHolder {
        ImageView photo;
        TextView name;
        TextView description;
    }
}