package com.foreignex.modules.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.List;

import com.foreignex.R;
import com.foreignex.modules.models.CurrencyModel;
import com.foreignex.modules.repository.Repository;
import com.google.gson.Gson;
import com.services.APIServices;

public class ExchangeViewModel extends AndroidViewModel {
    private MutableLiveData<List<String>> currencyModelMutableLiveData;
    private Repository repo = Repository.getInstance();
    private APIServices apiServices;
    private SharedPreferences sharedPref;
    private final String sharedPrefKey = "Main";
    private final String sharedPrefCurrencyKey = "Currency";

    public ExchangeViewModel(@NonNull Application application) {
        super(application);

        currencyModelMutableLiveData = new MutableLiveData<>();
        apiServices = new APIServices(application);
        sharedPref = application.getSharedPreferences(
                sharedPrefKey, Context.MODE_PRIVATE);
        loadCurrency();

    }

    public void getExcahngeRate() {
        if (repo.getCurrencyModel() != null) {
            currencyModelMutableLiveData.postValue(repo.getCurrencyNames());
        } else {
            apiServices.getExchangeRate(new APIServices.CompletionListener() {
                @Override
                public void onCompletion(Boolean success, Exception error) {
                    if (success) {
                        saveCurrency();
                        currencyModelMutableLiveData.postValue(repo.getCurrencyNames());
                    }
                }
            });
        }
    }

    public MutableLiveData<List<String>> getCurrencyModelMutableLiveData() {
        return currencyModelMutableLiveData;
    }

    public String conversion(int index, String amount) {
        Double rate = Double.valueOf(repo.getConversionRate(index));
        Double candianRate = Double.valueOf(repo.getConversionRate("CAD"));
        return String.valueOf((Double.valueOf(amount)/ candianRate)*rate);
    }

    public String getISOCode(int index) {
        return repo.getISOCode(index);
    }

    private void saveCurrency(){
        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(repo.getCurrencyModel());
        prefsEditor.putString(sharedPrefKey, json);
        prefsEditor.apply();
    }

    private void loadCurrency(){
        Gson gson = new Gson();
        String json = sharedPref.getString(sharedPrefKey, "");
        CurrencyModel obj = gson.fromJson(json, CurrencyModel.class);
        if (obj != null) {
            repo.setCurrencyModels(obj);
        }
    }
}
