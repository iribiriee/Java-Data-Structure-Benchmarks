package org;

import org.tuc.Element;

public class MyElement implements Element {
    private int key;
    private String data;

    protected MyElement(int key, String data) { // Private constructor
        this.key = key;
        this.data = data;
    }

    @Override
    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
