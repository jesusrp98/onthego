package com.chechu.onthego;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import org.json.JSONException;
import java.math.BigDecimal;

public class ShoppingListActivity extends AppCompatActivity {
    private static final String PAYPAL_CLIENT_ID = "AZXq8rssVj9vrxys4mEgAxaoj6WZUdIzCUDTqc7yoh7gD2XB_dErcSf27Z-m0MorfolToVpdfpTCA9lh";
    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration configuration;
    private static final int AMOUNT = 1;

    private AdapterItemConsumableAction adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        //paypal stuff
        configuration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)
                .clientId(PAYPAL_CLIENT_ID);
        final Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        startService(intent);

        //ui init
        final RecyclerView recyclerView = findViewById(R.id.shoppingRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkout();
            }
        });

        //recyclerView & adapter init
        adapter = new AdapterItemConsumableAction(getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if payment is SUCCesful
        if (resultCode == RESULT_OK) {
            final PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

            if (confirmation != null) {
                try {
                    //pass payment info to display in MainActivity
                    startActivity(new Intent(this, MainActivity.class)
                            .putExtra("id", confirmation.toJSONObject().getJSONObject("response").getString("id"))
                            .putExtra("items", adapter.getItemList())
                            .putExtra("amount", String.valueOf(AMOUNT))
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED || resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, getString(R.string.error_account), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shopping, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_quit_shopping:
                quitDialog();
                break;

            case R.id.action_catalog:
                startActivity(new Intent(this, CatalogueActivity.class));
                break;

            case R.id.action_add_product:
                dialogAddProduct();
                break;

            case R.id.action_add_product_random:
                adapter.addRandomItem();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        quitDialog();
    }

    private void quitDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_shopping_title)
                .setMessage(R.string.dialog_shopping_body)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //start MainActivity cleanly
                        startActivity(new Intent(getApplicationContext(), MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
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

    @SuppressLint("InflateParams")
    private void dialogAddProduct() {
        final View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_add_product, null);
        //TODO hacer addDialog

        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_add_product_title)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create().show();
    }

    private void checkout() {
        final Intent intent = new Intent(this, PaymentActivity.class);
        final PayPalPayment payment = new PayPalPayment(new BigDecimal(AMOUNT), "EUR",
                getString(R.string.display_paypal_message), PayPalPayment.PAYMENT_INTENT_SALE);

        //start paypal checkout activity
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }
}
