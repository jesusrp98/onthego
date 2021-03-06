package com.chechu.onthego;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentQR extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_qr, container, false);

        final ImageView qr = view.findViewById(R.id.qr);
        final TextView welcomeTextView = view.findViewById(R.id.qr_welcome);

        final Bundle bundle = this.getArguments();

        //display user name on fragment
        if (this.getArguments() != null) {
            welcomeTextView.setText(String.format(getString(R.string.display_welcome_screen),
                    bundle.getString("userName")));
        }

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShoppingListActivity.class).putExtra("id_cliente", bundle.getString("userId"))
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        return view;
    }
}