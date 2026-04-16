package com.example.demo_mini_10.DTO;

import java.io.Serializable;

public class LabRoom implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String roomName;
    private String description;
    private String imageUrl;
    private Integer capacity;
    private Boolean available;
    
    // Constructor
    public LabRoom() {
    }
    
    public LabRoom(Long id, String roomName, String description, String imageUrl, 
                   Integer capacity, Boolean available) {
        this.id = id;
        this.roomName = roomName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.capacity = capacity;
        this.available = available;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getRoomName() {
        return roomName;
    }
    
    public void setRoomName(String roomName) {
        this.roomName = roomName;
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
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    
    public Boolean getAvailable() {
        return available;
    }
    
    public void setAvailable(Boolean available) {
        this.available = available;
    }
    
    @Override
    public String toString() {
        return "LabRoom{" +
                "id=" + id +
                ", roomName='" + roomName + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", capacity=" + capacity +
                ", available=" + available +
                '}';
    }
}
