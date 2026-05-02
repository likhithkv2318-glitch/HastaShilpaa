package com.hastashilpa.app.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Batch implements Serializable {
    private long id;
    private String batchName;
    private String productType;
    private int bambooPoles;
    private double caneMeters;
    private double hoursWorked;
    private int unitsProduced;
    private double materialCostPerUnit;
    private String date;
    private String notes;

    public Batch() {
        this.id = System.currentTimeMillis();
        this.date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
    }

    public Batch(String batchName, String productType, int bambooPoles,
                 double caneMeters, double hoursWorked, int unitsProduced,
                 double materialCostPerUnit, String notes) {
        this.id = System.currentTimeMillis();
        this.batchName = batchName;
        this.productType = productType;
        this.bambooPoles = bambooPoles;
        this.caneMeters = caneMeters;
        this.hoursWorked = hoursWorked;
        this.unitsProduced = unitsProduced;
        this.materialCostPerUnit = materialCostPerUnit;
        this.notes = notes;
        this.date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getBatchName() { return batchName; }
    public void setBatchName(String batchName) { this.batchName = batchName; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public int getBambooPoles() { return bambooPoles; }
    public void setBambooPoles(int bambooPoles) { this.bambooPoles = bambooPoles; }

    public double getCaneMeters() { return caneMeters; }
    public void setCaneMeters(double caneMeters) { this.caneMeters = caneMeters; }

    public double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(double hoursWorked) { this.hoursWorked = hoursWorked; }

    public int getUnitsProduced() { return unitsProduced; }
    public void setUnitsProduced(int unitsProduced) { this.unitsProduced = unitsProduced; }

    public double getMaterialCostPerUnit() { return materialCostPerUnit; }
    public void setMaterialCostPerUnit(double materialCostPerUnit) { this.materialCostPerUnit = materialCostPerUnit; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public double getEfficiencyRatio() {
        if (unitsProduced == 0) return 0;
        return (double) bambooPoles / unitsProduced;
    }

    public double getTotalMaterialCost() {
        return materialCostPerUnit * unitsProduced;
    }
}
