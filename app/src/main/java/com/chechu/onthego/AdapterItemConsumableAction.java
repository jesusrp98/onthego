package com.chechu.onthego;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class AdapterItemConsumableAction extends RecyclerView.Adapter<AdapterItemConsumableAction.ViewHolder> {
    private ArrayList<ItemConsumableAction> constItemList;
    private ArrayList<ItemConsumableAction> itemList;
    private TypedArray photoArray;
    private Context context;
    private AlertDialog editTextDialog;
    private TextView totalTextView;

    AdapterItemConsumableAction(Context context, ArrayList<ItemConsumableAction> arrayList, TextView totalTextView) {
        this.photoArray = context.getResources().obtainTypedArray(R.array.icon_view);
        this.itemList = new ArrayList<>();
        this.constItemList = arrayList;
        this.context = context;
        this.totalTextView = totalTextView;
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
        final ItemConsumableAction item = itemList.get(position);

        //set info to ui
        holder.consumableIcon.setImageResource(photoArray.getResourceId(item.getConsumibleId() - 1, -1));
        holder.consumableTitle.setText(item.getConsumibleName());
        holder.consumableAmount.setText(String.format(context.getString(R.string.display_consumible_amount), item.getQuantity()));
        holder.consumablePrice.setText(String.format(context.getString(R.string.display_consumible_price), item.getConsumiblePrice()));

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

        final EditText dialogEditText = dialogView.findViewById(R.id.text_input);
        dialogEditText.setHint(context.getString(R.string.display_hint_number));
        dialogEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //if pressed enter
                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                    enterEditTextDialog(dialogEditText.getText().toString(), i);
                    return true;
                }
                return false;
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_edit_product_title)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enterEditTextDialog(dialogEditText.getText().toString(), i);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        editTextDialog = builder.create();
        editTextDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        editTextDialog.show();
    }

    private void enterEditTextDialog(String string, final int i) {
        int quantity = Integer.parseInt(string);
        if (quantity == 0)
            Toast.makeText(context, R.string.error_stock_0, Toast.LENGTH_LONG).show();
        else if (itemList.get(i).getStock() > quantity) {
            itemList.get(i).setQuantity(quantity);
            updateTotal();
            notifyDataSetChanged();
        } else
            Toast.makeText(context, R.string.error_stock, Toast.LENGTH_LONG).show();
        editTextDialog.dismiss();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private void dialogDelete(final int i) {
        itemList.remove(i);
        notifyDataSetChanged();
    }

    public void addItem(int i, int quantity) {
        itemList.add(constItemList.get(i));
        itemList.get(itemList.size() - 1).setQuantity(quantity);
        updateTotal();
        notifyDataSetChanged();
    }

    public void addRandomItem() {
        itemList.add(constItemList.get(new Random().nextInt(49)));
        updateTotal();
        notifyDataSetChanged();
    }

    private void updateTotal() {
        if (getTotalPrice() != 0)
            totalTextView.setText(String.format(context.getString(R.string.display_shopping_total), getTotalPrice()));
        else
            totalTextView.setText(context.getString(R.string.error_no_product));
    }

    public String getItemList() {
        String aux = "";
        for (ItemConsumableAction item : itemList)
            aux += " " + item.getConsumibleName() + ": " + item.getQuantity() + " unidades.\n";
        return aux;
    }

    public ArrayList<String> getNameArray() {
        final ArrayList<String> arrayList = new ArrayList<>();
        for(ItemConsumableAction item : constItemList)
            arrayList.add(item.getConsumibleName());
        return arrayList;
    }

    public ArrayList<Double> getPriceArray() {
        final ArrayList<Double> arrayList = new ArrayList<>();
        for(ItemConsumableAction item : constItemList)
            arrayList.add(item.getConsumiblePrice());
        return arrayList;
    }

    public float getTotalPrice() {
        float aux = 0;
        for (ItemConsumableAction item : itemList)
            aux += item.getQuantity() * item.getConsumiblePrice();
        return aux;
    }

    public ArrayList<Integer> getStockArray() {
        final ArrayList<Integer> arrayList = new ArrayList<>();
        for(ItemConsumableAction item : constItemList)
            arrayList.add(item.getStock());
        return arrayList;
    }

    //save xml ui into to cache
    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView consumableIcon;
        private final TextView consumableTitle;
        private final TextView consumableAmount;
        private final TextView consumablePrice;
        private final Button deleteButton;
        private final Button editButton;

        private ViewHolder(View itemView) {
            super(itemView);

            consumableIcon = itemView.findViewById(R.id.consumableIcon);
            consumableTitle = itemView.findViewById(R.id.consumableTitle);
            consumableAmount = itemView.findViewById(R.id.consumableAmount);
            consumablePrice = itemView.findViewById(R.id.consumablePrice);
            deleteButton = itemView.findViewById(R.id.action_delete);
            editButton = itemView.findViewById(R.id.action_edit);
        }
    }
}