package com.chechu.onthego;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //openTutorial();
        checkLogIn();
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_account:
                startActivity(new Intent(this, Login.class));
                break;

            case R.id.action_logout:
                startActivity(new Intent(this, Login.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openTutorial() {
        //opens tutorial activity if it hasn't opened yet
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.key_tutorial), false))
            startActivity(new Intent(this, Tutorial.class));
    }

    private void checkLogIn() {
        //checks if user has logged in
        //if not it opens login activity
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.key_login), false))
            startActivity(new Intent(this, Login.class));
    }
}