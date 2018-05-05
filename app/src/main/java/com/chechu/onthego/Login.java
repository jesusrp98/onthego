package com.chechu.onthego;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.hotspot2.pps.Credential;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;

public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{
    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    private TextView userNameTextView;
    private TextView userEmailTextView;
    private ImageView userPicture;
    private LinearLayout profileLayout;
    private LinearLayout signInLayout;
    private static final int REQ_CODE = 9001;
    private SharedPreferences sharedPreferences;
    private GoogleSignInAccount account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Bundle bundle = getIntent().getExtras();
        boolean logOut = false;

        if (bundle != null)
            logOut = bundle.getBoolean("isLogOut", false);

        signInButton = findViewById(R.id.signInButton);
        userNameTextView = findViewById(R.id.userName);
        userEmailTextView = findViewById(R.id.userEmail);
        userPicture = findViewById(R.id.userPicture);

        getSupportActionBar().setTitle(getString(R.string.activity_login));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        profileLayout = findViewById(R.id.profileLayout);
        signInLayout = findViewById(R.id.signInLayout);
        profileLayout.setVisibility(View.GONE);

        signInButton.setOnClickListener(this);

        if (logOut)
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_logout_title)
                    .setMessage(R.string.dialog_logout_body)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            signOut();
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

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();

        if (!sharedPreferences.getString("key_account", "").equals("")) {
            account = new Gson().fromJson(sharedPreferences.getString("key_account", ""), GoogleSignInAccount.class);
            updateUI(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signInButton)
            signIn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE)
            handleResult(Auth.GoogleSignInApi.getSignInResultFromIntent(data));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn() {
        startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(googleApiClient), REQ_CODE);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                sharedPreferences.edit().remove(getString(R.string.key_user_token)).apply();
                updateUI(false);
            }
        });
    }

    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            account = result.getSignInAccount();
            sharedPreferences.edit().putString("key_account", account.toJson()).apply();
            updateUI(true);
        } else {
            updateUI(false);
        }
    }

    private void updateUI(boolean isLogin) {
        if (isLogin) {
            userNameTextView.setText(account.getDisplayName());
            userEmailTextView.setText(account.getEmail());
            Glide.with(this).load(account.getPhotoUrl()).into(userPicture);
            profileLayout.setVisibility(View.VISIBLE);
            signInLayout.setVisibility(View.GONE);
        } else {
            profileLayout.setVisibility(View.GONE);
            signInLayout.setVisibility(View.VISIBLE);
        }
    }
}
