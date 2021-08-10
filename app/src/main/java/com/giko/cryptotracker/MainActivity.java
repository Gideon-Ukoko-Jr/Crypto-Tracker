package com.giko.cryptotracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText edtSearch;
    private RecyclerView rvCurrencies;
    private ProgressBar pbLoading;
    private ArrayList<CurrencyModel> currencyModelArrayList;
    private CurrencyAdapter currencyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtSearch = findViewById(R.id.edtSearch);
        rvCurrencies = findViewById(R.id.rvCurrencies);
        pbLoading = findViewById(R.id.pbLoading);

        currencyModelArrayList = new ArrayList<>();
        currencyAdapter = new CurrencyAdapter(currencyModelArrayList, this);

        rvCurrencies.setLayoutManager(new LinearLayoutManager(this));
        rvCurrencies.setAdapter(currencyAdapter);

        getCurrencyData();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterCurrencies(s.toString());
            }
        });
    }

    private void filterCurrencies(String currency){
        ArrayList<CurrencyModel> filteredList = new ArrayList<>();
        for (CurrencyModel item : currencyModelArrayList){
            if (item.getName().toLowerCase().contains(currency.toLowerCase())){
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No Currency Found For Searched Query", Toast.LENGTH_SHORT).show();
        }else {
            currencyAdapter.filterList(filteredList);
        }
    }

    private void getCurrencyData(){
        pbLoading.setVisibility(View.VISIBLE);
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pbLoading.setVisibility(View.GONE);

                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String symbol = jsonObject.getString("symbol");
                        JSONObject quote = jsonObject.getJSONObject("quote");
                        JSONObject USD = quote.getJSONObject("USD");
                        double price = USD.getDouble("price");
                        currencyModelArrayList.add(new CurrencyModel(name, symbol, price));
                    }
                    currencyAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbLoading.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed To Get The Data...", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("X-CMC_PRO_API_KEY", "4021b7d5-30c5-4a38-b3d4-50ad58157c1c");
                return  headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}