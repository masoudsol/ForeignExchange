package com.foreignex.modules.repository;

import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.foreignex.R;
import com.foreignex.modules.models.Currency;
import com.foreignex.modules.models.CurrencyModel;

/**
 * Singleton pattern
 */
public class Repository {

    private static Repository instance;
    private CurrencyModel currencyModel;
    private List<Currency> currencies = new ArrayList<>();
    private HashMap<String,Currency> currencyHashMap = new HashMap<>();
    public static Repository getInstance(){
        if(instance == null){
            instance = new Repository();
        }
        return instance;
    }

    public synchronized void setCurrencyModels(CurrencyModel currencyModel) {
        List<Currency> currencyList = currencyModel.currencies;
        Collections.sort(currencyList, new Comparator<Currency>() {
            @Override
            public int compare(Currency currency, Currency t1) {
                Log.d("dssda", "compare: "+currency.iso_code+" "+t1.iso_code);
                if (currency != null && t1 != null) {
                    return currency.iso_code.compareToIgnoreCase(t1.iso_code);
                } else {
                    return 0;
                }
            }
        });
        if (currencies.isEmpty()) {
            this.currencyModel = currencyModel;
            currencies = currencyList;
            for (Currency currency:currencyList) {
                currencyHashMap.put(currency.iso_code,currency);
            }
        } else {
            for (int i = 0; i < currencyList.size(); i++) {
                Currency currency = currencyList.get(i);
                Currency existingCurrency = currencies.get(i);
                Currency currencyModelCurrency = this.currencyModel.currencies.get(i);

                String isoCode = currency.iso_code;
                if (currency.name == null) {
                    existingCurrency.price = currency.price;
                    currencyModelCurrency.price = currency.price;
                } else {
                    existingCurrency.name = currency.name;
                    currencyModelCurrency.name = currency.name;
                }
                currencies.set(i, existingCurrency);
                currencyHashMap.put(isoCode,existingCurrency);
                this.currencyModel.currencies.set(i,currencyModelCurrency);
            }

        }


    }

    public List<String> getCurrencyNames() {
        List<String> currencyNames = new ArrayList<>();
        for (Currency currency: currencies) {
            currencyNames.add(currency.name);
        }
        return currencyNames;
    }

    public String getConversionRate(int index){
        return currencies.get(index).price;
    }

    public String getISOCode(int index){
        return currencies.get(index).iso_code;
    }

    public String getConversionRate(String isoCode){
        return currencyHashMap.get(isoCode).price;
    }

    public CurrencyModel getCurrencyModel() {
        return currencyModel;
    }
}











