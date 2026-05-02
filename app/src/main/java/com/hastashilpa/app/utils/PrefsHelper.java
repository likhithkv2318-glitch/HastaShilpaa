package com.hastashilpa.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hastashilpa.app.models.Batch;
import com.hastashilpa.app.models.ProductListing;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PrefsHelper {

    private static final String PREFS_NAME = "hasta_shilpa_prefs";
    private static final String KEY_BATCHES = "batches";
    private static final String KEY_LISTINGS = "listings";
    private static final String KEY_ARTISAN_NAME = "artisan_name";
    private static final String KEY_HOURLY_RATE = "hourly_rate";
    private static final String KEY_BAMBOO_PRICE = "bamboo_price_per_pole";
    private static final String KEY_CANE_PRICE = "cane_price_per_meter";

    private final SharedPreferences prefs;
    private final Gson gson;

    public PrefsHelper(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    // Batches
    public void saveBatch(Batch batch) {
        List<Batch> batches = getAllBatches();
        batches.add(0, batch);
        prefs.edit().putString(KEY_BATCHES, gson.toJson(batches)).apply();
    }

    public void deleteBatch(long batchId) {
        List<Batch> batches = getAllBatches();
        batches.removeIf(b -> b.getId() == batchId);
        prefs.edit().putString(KEY_BATCHES, gson.toJson(batches)).apply();
    }

    public List<Batch> getAllBatches() {
        String json = prefs.getString(KEY_BATCHES, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<List<Batch>>() {}.getType();
        List<Batch> result = gson.fromJson(json, type);
        return result != null ? result : new ArrayList<>();
    }

    // Product Listings
    public void saveListing(ProductListing listing) {
        List<ProductListing> listings = getAllListings();
        listings.add(0, listing);
        prefs.edit().putString(KEY_LISTINGS, gson.toJson(listings)).apply();
    }

    public void updateListingStatus(long listingId, String status) {
        List<ProductListing> listings = getAllListings();
        for (ProductListing p : listings) {
            if (p.getId() == listingId) {
                p.setStatus(status);
                break;
            }
        }
        prefs.edit().putString(KEY_LISTINGS, gson.toJson(listings)).apply();
    }

    public void deleteListing(long listingId) {
        List<ProductListing> listings = getAllListings();
        listings.removeIf(p -> p.getId() == listingId);
        prefs.edit().putString(KEY_LISTINGS, gson.toJson(listings)).apply();
    }

    public List<ProductListing> getAllListings() {
        String json = prefs.getString(KEY_LISTINGS, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<List<ProductListing>>() {}.getType();
        List<ProductListing> result = gson.fromJson(json, type);
        return result != null ? result : new ArrayList<>();
    }

    // User Settings
    public String getArtisanName() { return prefs.getString(KEY_ARTISAN_NAME, "Artisan"); }
    public void setArtisanName(String name) { prefs.edit().putString(KEY_ARTISAN_NAME, name).apply(); }

    public float getHourlyRate() { return prefs.getFloat(KEY_HOURLY_RATE, 80f); }
    public void setHourlyRate(float rate) { prefs.edit().putFloat(KEY_HOURLY_RATE, rate).apply(); }

    public float getBambooPrice() { return prefs.getFloat(KEY_BAMBOO_PRICE, 25f); }
    public void setBambooPrice(float price) { prefs.edit().putFloat(KEY_BAMBOO_PRICE, price).apply(); }

    public float getCanePrice() { return prefs.getFloat(KEY_CANE_PRICE, 40f); }
    public void setCanePrice(float price) { prefs.edit().putFloat(KEY_CANE_PRICE, price).apply(); }
}
