package com.example.demo_mini_10.DTO;

import java.io.Serializable;

public class Equipment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Integer quantityAvailable;
    
    // Constructor
    public Equipment() {
    }
    
    public Equipment(Long id, String name, String description, String imageUrl, Integer quantityAvailable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.quantityAvailable = quantityAvailable;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public Integer getQuantityAvailable() {
        return quantityAvailable;
    }
    
    public void setQuantityAvailable(Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }
    
    @Override
    public String toString() {
        return "Equipment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", quantityAvailable=" + quantityAvailable +
                '}';
    }
}
