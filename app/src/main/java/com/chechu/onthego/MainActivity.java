package com.chechu.onthego;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient;

    private ViewPager viewPager;
    private TabLayout tabLayout;

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
        try {
            final String paymentDetails = intent.getStringExtra("paymentDetails");
            if (paymentDetails != null) {
                final JSONObject purchase = new JSONObject(paymentDetails).getJSONObject("response");

                purchaseDialog(purchase.getString("id"), purchase.getString("state")
                        , intent.getStringExtra("paymentAmount"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                final Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("userName", userName);
                intent.putExtra("userEmail", userEmail);
                intent.putExtra("userPhoto", userPhoto);
                startActivity(intent);
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            userName = result.getSignInAccount().getDisplayName();
            userEmail = result.getSignInAccount().getEmail();
            userPhoto = result.getSignInAccount().getPhotoUrl().toString();
            initViewPager();

        } else
            gotoLogin();
    }

    private void logoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_logout_title)
                .setMessage(R.string.dialog_logout_body)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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

    private void purchaseDialog(String id, String status, String amount) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_purchase_title)
                .setMessage(String.format(getString(R.string.dialog_purchase_body), id, status, amount))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create().show();
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

        //add userName to QR fragment
        final Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        fragments[1].setArguments(bundle);

        //add fragments to fragment adapter
        for (int i = 0; i < 3; i++)
            adapter.addFragment(fragments[i], titleArray[i]);

        //viewpager & tablayout setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);
    }
}
