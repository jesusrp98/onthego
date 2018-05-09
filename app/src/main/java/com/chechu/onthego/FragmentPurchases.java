package com.chechu.onthego;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentPurchases extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_purchases, container, false);

        final ListView listView = view.findViewById(R.id.purchases_listview);
        final AdapterItemPurchase adapter = new AdapterItemPurchase(getActivity());

        listView.setAdapter(adapter);

        return view;
    }
}