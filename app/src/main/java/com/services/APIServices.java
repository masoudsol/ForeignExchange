package com.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.foreignex.modules.models.CurrencyModel;
import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;

import com.foreignex.BuildConfig;
import com.foreignex.modules.repository.Repository;

import org.json.JSONException;
import org.json.JSONObject;

public class APIServices {
    private static final String TAG = "APIServices";
    private boolean retrivedRates,retrievedNames;

    public interface NetworkListener {
        void onEvent(String url, Object response, Exception error);   //method, which can have parameters
    }

    public interface CompletionListener {
        void onCompletion(Boolean success, Exception error);
    }

    private Repository dataProvider;
    private RequestQueue requestQueue;

    public APIServices(Context context) {
        dataProvider = Repository.getInstance();
        retrivedRates = false;
        retrievedNames = false;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getExchangeRate(final CompletionListener completionListener) {
        NetworkListener networkListener = new NetworkListener() {
            @Override
            public void onEvent(String url, final Object response, final Exception error) {
                if (error == null) {
                    CurrencyModel currencyModel = new Gson().fromJson((String) response, CurrencyModel.class);
                    dataProvider.setCurrencyModels(currencyModel);
                    if (url.equals(BuildConfig.url_name)) {
                        retrievedNames = true;
                    } else if (url.equals(BuildConfig.url_price)){
                        retrivedRates = true;
                    }
                    if (retrievedNames && retrivedRates) {
                        completionListener.onCompletion(true, error);
                    }

                }else {
                    completionListener.onCompletion( false, error);
                }

            }
        };

        requestRestaurants(BuildConfig.url_name, networkListener);
        requestRestaurants(BuildConfig.url_price, networkListener);

    }

    private void requestRestaurants(final String url, final NetworkListener networkListener){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            networkListener.onEvent(url, response, null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                Log.d(TAG, "onErrorResponse: "+error);
                error.printStackTrace();

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }

            }
        });
        requestQueue.add(stringRequest);
    }
}
