package com.foreignex.modules.models;

import java.io.Serializable;
import java.util.List;

public class CurrencyModel implements Serializable {
    private List<Currency> currencies;

    public List<Currency> getCurrencies() {
        return currencies;
    }
}
