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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import org.json.JSONArray;
import org.json.JSONException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ShoppingListActivity extends AppCompatActivity {
    private static final String PAYPAL_CLIENT_ID = "AZXq8rssVj9vrxys4mEgAxaoj6WZUdIzCUDTqc7yoh7gD2XB_dErcSf27Z-m0MorfolToVpdfpTCA9lh";
    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration configuration;

    private AdapterItemConsumableAction adapter;
    private RecyclerView recyclerView;

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
        recyclerView = findViewById(R.id.shoppingRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkout();
            }
        });

        setAdapter();
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
                    postPurchase();
                    startActivity(new Intent(this, MainActivity.class)
                            .putExtra("id", confirmation.toJSONObject().getJSONObject("response").getString("id"))
                            .putExtra("items", adapter.getItemList())
                            .putExtra("amount", adapter.getTotalPrice())
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
        final Spinner dialogSpinner = dialogView.findViewById(R.id.addProductSpinner);
        final TextView dialogPrice = dialogView.findViewById(R.id.addProductPrice);
        final ImageView dialogPhoto = dialogView.findViewById(R.id.addProductPhoto);
        final EditText dialogEditText = dialogView.findViewById(R.id.addProductQuantity);

        dialogSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, adapter.getNameArray()));
        dialogSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dialogPrice.setText(String.format(getString(R.string.display_consumible_price), adapter.getPriceArray().get(position)));
                dialogPhoto.setImageResource(getApplicationContext().getResources()
                        .obtainTypedArray(R.array.icon_view).getResourceId(position, -1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_add_product_title)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int i = dialogSpinner.getSelectedItemPosition();
                        String text = dialogEditText.getText().toString();
                        if (!text.equals("")) {
                            if (adapter.getStockArray().get(i) > Integer.parseInt(text))
                                adapter.addItem(i, Integer.parseInt(dialogEditText.getText().toString()));
                            else
                                Toast.makeText(getApplicationContext(), R.string.error_stock, Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(getApplicationContext(), R.string.error_stock_0, Toast.LENGTH_LONG).show();
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

    private void setAdapter() {
        final String URL = "http://onthego.myddns.me:8000/get_productos";
        final ArrayList<ItemConsumableAction> arrayList = new ArrayList<>();

        //api rest request
        final RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        final JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); ++i)
                                arrayList.add(new ItemConsumableAction(response.getJSONObject(i)));
                            //set recycler & adapter
                            adapter = new AdapterItemConsumableAction(getApplicationContext(), arrayList, (TextView) findViewById(R.id.totalText));
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_account, Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(arrayRequest);
    }

    //TODO hacer esto
    private void postPurchase() {
        final String URL = "http://onthego.myddns.me:8000/enviar_compra";
        final ArrayList<ItemConsumableAction> arrayList = new ArrayList<>();

        //api rest request
        final RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        final StringRequest arrayRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), R.string.error_account, Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();


                return params;
            }
        };
        requestQueue.add(arrayRequest);
    }

    private void checkout() {
        if (adapter.getTotalPrice() > 0) {
            final Intent intent = new Intent(this, PaymentActivity.class);
            final PayPalPayment payment = new PayPalPayment(new BigDecimal(adapter.getTotalPrice()), "EUR",
                    getString(R.string.display_paypal_message), PayPalPayment.PAYMENT_INTENT_SALE);

            //start paypal checkout activity
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
            startActivityForResult(intent, PAYPAL_REQUEST_CODE);
        } else
            Toast.makeText(getApplicationContext(), R.string.error_no_product, Toast.LENGTH_LONG).show();
    }
}
