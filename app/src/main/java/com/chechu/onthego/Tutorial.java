package com.chechu.onthego;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Tutorial extends AppCompatActivity {
    private Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        okButton = findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishTutorial();
            }
        });
    }

    private void nextTutorial() {

    }

    private void finishTutorial() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.key_tutorial), true).apply();
        finish();
    }
}