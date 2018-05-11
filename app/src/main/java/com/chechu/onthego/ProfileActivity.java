package com.chechu.onthego;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //ui init
        final ImageView photoImageView = findViewById(R.id.photoImageView);
        final TextView nameTextView = findViewById(R.id.nameTextView);
        final TextView emailTextView = findViewById(R.id.emailTextView);

        //get & display account info
        final Intent intent = getIntent();
        nameTextView.setText(intent.getStringExtra("userName"));
        emailTextView.setText(intent.getStringExtra("userEmail"));
        Glide.with(this).load(Uri.parse(intent.getStringExtra("userPhoto"))).into(photoImageView);

        //telegram button
        findViewById(R.id.telegramButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://t.me/onthego_mojabot?start=" + intent.getStringExtra("userId");
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
