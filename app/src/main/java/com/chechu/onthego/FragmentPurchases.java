package com.chechu.onthego;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class FragmentPurchases extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_purchases, container, false);

        final ListView listView = view.findViewById(R.id.purchases_listview);
        final AdapterItemPurchase adapter = new AdapterItemPurchase(getActivity());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ItemPurchase item = (ItemPurchase) parent.getItemAtPosition(position);
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.dialog_done_purchase_title)
                        .setMessage(String.format(getString(R.string.dialog_purchase_body), item.getId(), item.getId(), item.getId()))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            }
        });

        listView.setAdapter(adapter);

        return view;
    }
}