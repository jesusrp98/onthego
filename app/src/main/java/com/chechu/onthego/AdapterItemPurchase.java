package com.chechu.onthego;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.amulyakhare.textdrawable.TextDrawable;
import java.util.ArrayList;

public class AdapterItemPurchase extends ArrayAdapter<ItemPurchase> {
    private Context context;

    AdapterItemPurchase(Context context) {
        super(context, R.layout.item_purchase, getItems());
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
        final ItemPurchase itemPurchase = getItem(position);
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_purchase, parent, false);
            viewHolder = new ViewHolder();

            //get item id
            viewHolder.iconView = convertView.findViewById(R.id.item_purchase_icon);
            viewHolder.titleView = convertView.findViewById(R.id.item_purchase_title);
            viewHolder.descriptionView = convertView.findViewById(R.id.item_purchase_description);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        //assign objects to viewholder
        viewHolder.iconView.setImageDrawable(
                TextDrawable.builder().beginConfig().textColor(context.getResources().getColor(android.R.color.white))
                        .endConfig().buildRound("#" + itemPurchase.getNumber(), context.getResources().getColor(R.color.colorAccent))
        );
        viewHolder.titleView.setText(itemPurchase.getId());
        viewHolder.descriptionView.setText(Html.fromHtml(itemPurchase.getDate()));

        return convertView;
    }

    private static ArrayList<ItemPurchase> getItems() {
        final ArrayList<ItemPurchase> aux = new ArrayList<>();

        aux.add(new ItemPurchase(1,"1-9-9", "puta madre", "123123"));
        aux.add(new ItemPurchase(2, "2-9-9", "puta madre socio", "123123"));
        aux.add(new ItemPurchase(3, "3-9-9", "puta madre socio xd", "1212312313123"));
        aux.add(new ItemPurchase(4, "4-9-9", "puta madre socio xd gg gente", "12123123"));

        return aux;
    }
}