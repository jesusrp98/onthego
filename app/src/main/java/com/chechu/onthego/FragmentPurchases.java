package com.chechu.onthego;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Objects;

public class FragmentPurchases extends Fragment {
    private ListView listView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_purchases, container, false);
        listView = view.findViewById(R.id.purchases_listview);

        //TODO revisar el formato
        //display dialog with purchase info
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ItemPurchase item = (ItemPurchase) parent.getItemAtPosition(position);
                new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                        .setTitle(R.string.dialog_purchase_title)
                        .setMessage(String.format(getString(R.string.dialog_purchase_body), item.getId(), item.getAmount(), item.getAmount()))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            }
        });

        if (this.getArguments() != null)
            setAdapter(this.getArguments().getString("id"));
        else
            Toast.makeText(getContext(), R.string.error_account, Toast.LENGTH_LONG).show();

        return view;
    }

    private void setAdapter(String id) {
        //TODO corregir en un futuro la url
        final String URL = "http://178.62.36.19:8000/get_compras_cliente?id=10017";
        final ArrayList<ItemPurchase> arrayList = new ArrayList<>();

        final RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        final JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); ++i)
                                arrayList.add(new ItemPurchase(i + 1, response.getJSONObject(i)));
                            listView.setAdapter(new AdapterItemPurchase(getContext(), arrayList));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), R.string.error_account, Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(arrayRequest);
    }
}