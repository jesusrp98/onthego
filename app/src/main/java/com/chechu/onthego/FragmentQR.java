package com.chechu.onthego;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentQR extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_qr, container, false);

        final Bundle bundle = this.getArguments();

        //ui init
        final ImageView qr = view.findViewById(R.id.qr);
        final TextView welcomeTextView = view.findViewById(R.id.qr_welcome);

        //welcomeTextView.setText(String.format("Welcome back %s!", bundle.getString("userName")));

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(this, ShoppingList.class));
                Toast.makeText(getContext(), "I'm a QR code :)", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}