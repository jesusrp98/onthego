package com.chechu.onthego;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class AdapterItemConsumableAction extends RecyclerView.Adapter<AdapterItemConsumableAction.ViewHolder> {
    private ArrayList<ItemConsumableAction> catalogue;
    private ItemPurchase itemPurchase;

    private AlertDialog editTextDialog;
    private TextView totalTextView;

    private TypedArray photoArray;
    private Context context;

    AdapterItemConsumableAction(Context context, ArrayList<ItemConsumableAction> arrayList, TextView totalTextView) {
        this.photoArray = context.getResources().obtainTypedArray(R.array.icon_view);
        this.itemPurchase = new ItemPurchase();
        this.totalTextView = totalTextView;
        this.catalogue = arrayList;
        this.context = context;

        updateTotal();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consumable_action, parent, false));
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final ItemConsumableAction item = getItems().get(position);

        //set info to ui
        holder.photo.setImageResource(photoArray.getResourceId(item.getId() - 1, -1));
        holder.name.setText(item.getName());
        holder.quantity.setText(String.format(context.getString(R.string.display_consumible_amount), item.getQuantity()));
        holder.price.setText(String.format(context.getString(R.string.display_consumible_price), item.getPrice()));

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDelete(position);
            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEdit(position);
            }
        });
    }

    @SuppressLint("InflateParams")
    private void dialogEdit(final int i) {
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_text, null);

        editTextDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_edit_product_title)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int quantity = Integer.parseInt(((EditText) dialogView.findViewById(R.id.text_input)).getText().toString());
                        if (quantity == 0)
                            Toast.makeText(context, R.string.error_stock_0, Toast.LENGTH_LONG).show();
                        else if (getItems().get(i).getStock() > quantity) {
                            itemPurchase.editItem(i, quantity);
                            updateTotal();
                            notifyDataSetChanged();
                        } else
                            Toast.makeText(context, R.string.error_stock, Toast.LENGTH_LONG).show();
                        editTextDialog.dismiss();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        editTextDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        editTextDialog.show();
    }

    @Override
    public int getItemCount() {
        return getItems().size();
    }

    private void dialogDelete(int i) {
        itemPurchase.removeItem(i);
        updateTotal();
        notifyDataSetChanged();
    }

    private void updateTotal() {
        if (getTotalPrice() != 0)
            totalTextView.setText(String.format(context.getString(R.string.display_shopping_total), getTotalPrice()));
        else
            totalTextView.setText(context.getString(R.string.error_no_product));
    }

    public void addItem(int i, int quantity) {
        if (!listHas(i)) {
            itemPurchase.addItem(catalogue.get(i), quantity);
            updateTotal();
            notifyDataSetChanged();
        } else
            Toast.makeText(context, R.string.error_alredy_product, Toast.LENGTH_LONG).show();
    }

    private boolean listHas(int i) {
        for (ItemConsumableAction item : getItems())
            if (item.equals(catalogue.get(i)))
                return true;
        return false;
    }

    public String getItemList() {
        return itemPurchase.getItemList();
    }

    public ArrayList<String> getNameArray() {
        final ArrayList<String> arrayList = new ArrayList<>();
        for(ItemConsumableAction item : catalogue)
            arrayList.add(item.getName());
        return arrayList;
    }

    public ArrayList<Double> getPriceArray() {
        final ArrayList<Double> arrayList = new ArrayList<>();
        for(ItemConsumableAction item : catalogue)
            arrayList.add(item.getPrice());
        return arrayList;
    }

    public ArrayList<Long> getStockArray() {
        final ArrayList<Long> arrayList = new ArrayList<>();
        for(ItemConsumableAction item : catalogue)
            arrayList.add(item.getStock());
        return arrayList;
    }

    public double getTotalPrice() {
        return itemPurchase.getTotalPrice();
    }

    public String getDate() {
        return itemPurchase.getDate();
    }

    public ArrayList<ItemConsumableAction> getItems() {
        return itemPurchase.getItems();
    }

    public int getPhoto(int i) {
        return photoArray.getResourceId(catalogue.get(i).getId() - 1, -1);
    }

    public void initPurchase(String id, String date) {
        itemPurchase.init(id, date);
    }

    //save xml ui into to cache
    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView photo;
        private final TextView name;
        private final TextView quantity;
        private final TextView price;
        private final Button deleteButton;
        private final Button editButton;

        private ViewHolder(View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.consumableIcon);
            name = itemView.findViewById(R.id.consumableTitle);
            quantity = itemView.findViewById(R.id.consumableAmount);
            price = itemView.findViewById(R.id.consumablePrice);
            deleteButton = itemView.findViewById(R.id.action_delete);
            editButton = itemView.findViewById(R.id.action_edit);
        }
    }
}