package com.chechu.onthego;

import android.annotation.SuppressLint;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Objects;

public class FragmentPurchases extends Fragment {
    private ListView listView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_purchases, container, false);
        listView = view.findViewById(R.id.purchases_listview);

        //display dialog with purchase info
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("StringFormatMatches")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ItemPurchase item = (ItemPurchase) parent.getItemAtPosition(position);
                new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                        .setTitle(R.string.dialog_purchase_title)
                        .setMessage(String.format(getString(R.string.dialog_purchase_body), item.getId(), item.getDate(), item.getItemList(), item.getTotalPrice()))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        //set adapter to display user's purchases
        if (this.getArguments() != null)
            setAdapter(this.getArguments().getString("userId"));
        else
            Toast.makeText(getContext(), R.string.error_account, Toast.LENGTH_LONG).show();

        return view;
    }

    private void setAdapter(String id) {
        final String URL = "http://onthego.myddns.me:8000/get_compras_cliente?id=" + id;
        final ArrayList<ItemPurchase> arrayList = new ArrayList<>();
        final JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); ++i)
                                arrayList.add(new ItemPurchase(response.getJSONObject(i)));
                            //set recycler & adapter
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
        Volley.newRequestQueue(Objects.requireNonNull(getContext())).add(arrayRequest);
    }
}