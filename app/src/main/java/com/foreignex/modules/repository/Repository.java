package com.foreignex.modules.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

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
        List<Currency> currencyList = currencyModel.getCurrencies();
        Collections.sort(currencyList, new Comparator<Currency>() {
            @Override
            public int compare(Currency currency, Currency t1) {
                if (currency != null && t1 != null) {
                    return currency.getIso_code().compareToIgnoreCase(t1.getIso_code());
                } else {
                    return 0;
                }
            }
        });

        if (currencies.isEmpty()) {
            this.currencyModel = currencyModel;
            currencies = currencyList;
            for (Currency currency:currencyList) {
                currencyHashMap.put(currency.getIso_code(),currency);
            }
        } else {
            for (int i = 0; i < currencyList.size(); i++) {
                Currency currency = currencyList.get(i);
                Currency existingCurrency = currencies.get(i);
                Currency currencyModelCurrency = this.currencyModel.getCurrencies().get(i);

                String isoCode = currency.getIso_code();
                if (currency.getName() == null) {
                    existingCurrency.setPrice(currency.getPrice());
                    currencyModelCurrency.setPrice(currency.getPrice());
                } else {
                    existingCurrency.setName(currency.getName());
                    currencyModelCurrency.setName(currency.getName());
                }
                currencies.set(i, existingCurrency);
                currencyHashMap.put(isoCode,existingCurrency);
                this.currencyModel.getCurrencies().set(i,currencyModelCurrency);
            }
        }
    }

    public List<String> getCurrencyNames() {
        List<String> currencyNames = new ArrayList<>();
        for (Currency currency: currencies) {
            currencyNames.add(currency.getName());
        }
        return currencyNames;
    }

    public String getConversionRate(int index){
        return currencies.get(index).getPrice();
    }

    public String getISOCode(int index){
        return currencies.get(index).getIso_code();
    }

    public String getConversionRate(String isoCode){
        return currencyHashMap.get(isoCode).getPrice();
    }

    public CurrencyModel getCurrencyModel() {
        return currencyModel;
    }
}











