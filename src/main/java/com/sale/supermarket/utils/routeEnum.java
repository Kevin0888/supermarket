package com.sale.supermarket.utils;

public enum routeEnum {
    login("/supermarket/login", 1),
    error("/supermarket/error", 2),
    cashier("/supermarket/cashier", 3),
    ;


    private String uri;
    private int index;

    private routeEnum(String uri, int index) {
        this.uri = uri;
        this.index = index;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
