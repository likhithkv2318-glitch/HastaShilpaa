package com.hastashilpa.app.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProductListing implements Serializable {
    private long id;
    private String productName;
    private String description;
    private String category;
    private double askingPrice;
    private double suggestedPrice;
    private String status; // "AVAILABLE", "SOLD"
    private String date;
    private int quantity;
    private String artisanName;

    public ProductListing() {
        this.id = System.currentTimeMillis();
        this.status = "AVAILABLE";
        this.date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
    }

    public ProductListing(String productName, String description, String category,
                          double askingPrice, double suggestedPrice, int quantity, String artisanName) {
        this.id = System.currentTimeMillis();
        this.productName = productName;
        this.description = description;
        this.category = category;
        this.askingPrice = askingPrice;
        this.suggestedPrice = suggestedPrice;
        this.quantity = quantity;
        this.artisanName = artisanName;
        this.status = "AVAILABLE";
        this.date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getAskingPrice() { return askingPrice; }
    public void setAskingPrice(double askingPrice) { this.askingPrice = askingPrice; }

    public double getSuggestedPrice() { return suggestedPrice; }
    public void setSuggestedPrice(double suggestedPrice) { this.suggestedPrice = suggestedPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getArtisanName() { return artisanName; }
    public void setArtisanName(String artisanName) { this.artisanName = artisanName; }

    public boolean isAvailable() { return "AVAILABLE".equals(status); }
}
