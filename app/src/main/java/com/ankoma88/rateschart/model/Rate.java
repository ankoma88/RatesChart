package com.ankoma88.rateschart.model;

import android.util.Log;

import java.security.Timestamp;

/**
 * Created by ankoma88 on 19.06.16.
 */
public class Rate {
    private String asset;
    private long time;
    private float price;

    public Rate(String data) {
        String[] split = data.split(",");
        this.asset = split[0];
        this.time = Long.parseLong(split[1]);
        this.price = Float.parseFloat(split[2] + split[3]);
    }

    public String getAsset() {
        return asset;
    }

    public long getTime() {
        return time;
    }

    public float getPrice() {
        return price;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rate rate = (Rate) o;

        if (time != rate.time) return false;
        if (Float.compare(rate.price, price) != 0) return false;
        return asset.equals(rate.asset);

    }

    @Override
    public int hashCode() {
        int result = asset.hashCode();
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "asset='" + asset + '\'' +
                ", time=" + time +
                ", price=" + price +
                '}';
    }
}
