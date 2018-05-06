package com.chechu.onthego;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        //ui init
        viewPager = findViewById(R.id.activity_main_viewpager);
        tabLayout = findViewById(R.id.activity_main_tablayout);
        welcomeTextView = findViewById(R.id.qr_welcome);

//        welcomeTextView.setText(getString(R.string.display_welcome_qr) + "user");

        initViewPager();
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
                Intent intent = new Intent(this, Login.class);
                intent.putExtra("isLogOut", true);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViewPager() {
        final String[] titleArray = getResources().getStringArray(R.array.display_maintab);
        final Fragment[] fragments = {new FragmentRecommended(), new FragmentQR(), new FragmentShopping()};
        final AdapterTabLayout adapter = new AdapterTabLayout(getSupportFragmentManager());

        for (int i = 0; i < 3; i++)
            adapter.addFragment(fragments[i], titleArray[i]);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);
    }
}