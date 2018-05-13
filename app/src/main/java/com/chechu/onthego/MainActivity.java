package com.chechu.onthego;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private String userId;
    private String userName;
    private String userEmail;
    private String userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        //google api stuff
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        //ui init
        viewPager = findViewById(R.id.activity_main_viewpager);
        tabLayout = findViewById(R.id.activity_main_tablayout);

        //display purchase dialog if exists
        final Intent intent = getIntent();
        if (intent.getStringExtra("id") != null)
            purchaseDialog(intent.getStringExtra("id"), intent.getStringExtra("items"),
                    intent.getFloatExtra("amount", 0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                startActivity(new Intent(this, ProfileActivity.class)
                        .putExtra("userId", userId)
                        .putExtra("userName", userName)
                        .putExtra("userEmail", userEmail)
                        .putExtra("userPhoto", userPhoto)
                );
                break;

            case R.id.action_logout:
                logoutDialog();
                break;

            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> optionalPendingResult = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (optionalPendingResult.isDone())
            handleSignInResult(optionalPendingResult.get());
        else {
            optionalPendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            userId = Objects.requireNonNull(result.getSignInAccount()).getId();
            userName = result.getSignInAccount().getDisplayName();
            userEmail = result.getSignInAccount().getEmail();
            userPhoto = Objects.requireNonNull(result.getSignInAccount().getPhotoUrl()).toString();
            initViewPager();
            showTutorial();
        } else
            gotoLogin();
    }

    private void logoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_logout_title)
                .setMessage(R.string.dialog_logout_body)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().remove("key_tutorial").apply();
                        logout();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    private void purchaseDialog(String id, String items, float amount) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_purchase_title)
                .setMessage(String.format(getString(R.string.dialog_purchase_body), id, items, amount))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create().show();
    }

    private void showTutorial() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sharedPreferences.getBoolean(getString(R.string.key_tutorial), false)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_tutorial_title)
                    .setMessage(R.string.dialog_tutorial_body)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            sharedPreferences.edit().putBoolean(getString(R.string.key_tutorial), true).apply();
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .create().show();
        }
    }

    private void logout() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess())
                    gotoLogin();
                else
                    Toast.makeText(getApplicationContext(), R.string.error_account, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void gotoLogin() {
        startActivity(new Intent(this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void initViewPager() {
        final String[] titleArray = getResources().getStringArray(R.array.display_maintab);
        final Fragment[] fragments = { new FragmentRecommended(), new FragmentQR(), new FragmentPurchases() };
        final AdapterTabLayout adapter = new AdapterTabLayout(getSupportFragmentManager());
        final Bundle bundles[] = {new Bundle(), new Bundle()};

        bundles[0].putString("userName", userName);
        fragments[1].setArguments(bundles[0]);

        bundles[1].putString("id", userId);
        fragments[2].setArguments(bundles[1]);

        //add fragments to fragment adapter
        for (int i = 0; i < 3; i++)
            adapter.addFragment(fragments[i], titleArray[i]);

        //viewpager & tablayout setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(3);
    }
}
