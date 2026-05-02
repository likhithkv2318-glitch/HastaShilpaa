package com.hastashilpa.app.models;

import java.io.Serializable;
import java.util.List;

public class Design implements Serializable {
    private int id;
    private String title;
    private String category;
    private String description;
    private String imageUrl;
    private String difficulty;
    private int estimatedHours;
    private double marketValue;
    private String tag; // "TRENDING", "NEW", "FEATURED"
    private List<String> materials;
    private List<Dimension> dimensions;
    private List<String> assemblySteps;
    private String bambooPoles;
    private String caneMeters;

    public Design(int id, String title, String category, String description,
                  String imageUrl, String difficulty, int estimatedHours,
                  double marketValue, String tag, List<String> materials,
                  List<Dimension> dimensions, List<String> assemblySteps,
                  String bambooPoles, String caneMeters) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;
        this.difficulty = difficulty;
        this.estimatedHours = estimatedHours;
        this.marketValue = marketValue;
        this.tag = tag;
        this.materials = materials;
        this.dimensions = dimensions;
        this.assemblySteps = assemblySteps;
        this.bambooPoles = bambooPoles;
        this.caneMeters = caneMeters;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getDifficulty() { return difficulty; }
    public int getEstimatedHours() { return estimatedHours; }
    public double getMarketValue() { return marketValue; }
    public String getTag() { return tag; }
    public List<String> getMaterials() { return materials; }
    public List<Dimension> getDimensions() { return dimensions; }
    public List<String> getAssemblySteps() { return assemblySteps; }
    public String getBambooPoles() { return bambooPoles; }
    public String getCaneMeters() { return caneMeters; }

    public static class Dimension implements Serializable {
        private String label;
        private String value;

        public Dimension(String label, String value) {
            this.label = label;
            this.value = value;
        }

        public String getLabel() { return label; }
        public String getValue() { return value; }
    }
}
