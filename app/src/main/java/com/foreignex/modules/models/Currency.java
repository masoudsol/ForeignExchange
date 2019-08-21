package com.foreignex.modules.models;

import java.io.Serializable;

public class Currency implements Serializable {
    private String iso_code;
    private String name;
    private String price;

    public String getIso_code() {
        return iso_code;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
