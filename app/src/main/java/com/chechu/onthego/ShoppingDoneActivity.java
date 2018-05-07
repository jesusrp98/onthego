package com.chechu.onthego;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

public class ShoppingDoneActivity extends AppCompatActivity {
    private TextView idTextView;
    private TextView statusTextView;
    private TextView amountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_done);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        idTextView = findViewById(R.id.idTextView);
        statusTextView = findViewById(R.id.statusTextView);
        amountTextView = findViewById(R.id.amountTextView);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        final Intent intent = getIntent();
        try {
            showDetails(new JSONObject(intent.getStringExtra("paymentDetails"))
                    .getJSONObject("response"), intent.getStringExtra("paymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDetails(JSONObject response, String paymentAmount) {
        try {
            idTextView.setText(response.getString("id"));
            statusTextView.setText(response.getString("state"));
            amountTextView.setText(String.format("%s€", paymentAmount));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
