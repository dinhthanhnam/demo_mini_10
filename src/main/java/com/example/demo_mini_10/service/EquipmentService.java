package com.example.demo_mini_10.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo_mini_10.DTO.Equipment;
import com.example.demo_mini_10.DTO.LabRoom;

@Service
public class EquipmentService {
    
    // Mock data - in real application, this would be from database
    private Map<Long, Equipment> equipmentDatabase = new HashMap<>();
    private Map<Long, LabRoom> labRoomDatabase = new HashMap<>();
    private Long equipmentIdCounter = 3L;
    private Long labRoomIdCounter = 2L;
    
    public EquipmentService() {
        initializeMockData();
    }
    
    private void initializeMockData() {
        // Initialize equipment
        equipmentDatabase.put(1L, new Equipment(1L, "Màn hình rời 24 inch", 
            "Màn hình rời độ phân giải Full HD", "https://placehold.co/200x150?text=Monitor", 5));
        equipmentDatabase.put(2L, new Equipment(2L, "Cáp kết nối HDMI", 
            "Cáp HDMI 2.0 dài 2 mét", "https://placehold.co/200x150?text=HDMI+Cable", 10));
        
        // Initialize lab rooms
        labRoomDatabase.put(1L, new LabRoom(1L, "Phòng Lab A", 
            "Phòng thí nghiệm chính, sức chứa 20 người", "https://placehold.co/200x150?text=Lab+A", 20, true));
    }
    
    // Equipment methods
    public List<Equipment> getAllEquipment() {
        return new ArrayList<>(equipmentDatabase.values());
    }
    
    public Optional<Equipment> getEquipmentById(Long id) {
        return Optional.ofNullable(equipmentDatabase.get(id));
    }
    
    public Equipment createEquipment(Equipment equipment) {
        equipment.setId(equipmentIdCounter++);
        equipmentDatabase.put(equipment.getId(), equipment);
        return equipment;
    }
    
    public Equipment updateEquipment(Long id, Equipment equipment) {
        equipment.setId(id);
        equipmentDatabase.put(id, equipment);
        return equipment;
    }
    
    public void deleteEquipment(Long id) {
        equipmentDatabase.remove(id);
    }
    
    public boolean reduceEquipmentQuantity(Long equipmentId, Integer quantity) {
        Optional<Equipment> equipment = getEquipmentById(equipmentId);
        if (equipment.isPresent()) {
            Equipment eq = equipment.get();
            if (eq.getQuantityAvailable() >= quantity) {
                eq.setQuantityAvailable(eq.getQuantityAvailable() - quantity);
                return true;
            }
        }
        return false;
    }
    
    public void restoreEquipmentQuantity(Long equipmentId, Integer quantity) {
        Optional<Equipment> equipment = getEquipmentById(equipmentId);
        if (equipment.isPresent()) {
            Equipment eq = equipment.get();
            eq.setQuantityAvailable(eq.getQuantityAvailable() + quantity);
        }
    }
    
    // Lab Room methods
    public List<LabRoom> getAllLabRooms() {
        return new ArrayList<>(labRoomDatabase.values());
    }
    
    public Optional<LabRoom> getLabRoomById(Long id) {
        return Optional.ofNullable(labRoomDatabase.get(id));
    }
    
    public LabRoom createLabRoom(LabRoom labRoom) {
        labRoom.setId(labRoomIdCounter++);
        labRoomDatabase.put(labRoom.getId(), labRoom);
        return labRoom;
    }
    
    public LabRoom updateLabRoom(Long id, LabRoom labRoom) {
        labRoom.setId(id);
        labRoomDatabase.put(id, labRoom);
        return labRoom;
    }
    
    public void deleteLabRoom(Long id) {
        labRoomDatabase.remove(id);
    }
}
