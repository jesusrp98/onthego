package com.chechu.onthego;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Welcome extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishTutorial();
            }
        });
    }

    private void finishTutorial() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(getString(R.string.key_tutorial), true).apply();
        startActivity(new Intent(this, MainActivity.class));
    }
}