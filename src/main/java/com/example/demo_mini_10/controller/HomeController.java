package com.example.demo_mini_10.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo_mini_10.service.EquipmentService;

@Controller
public class HomeController {
    
    @Autowired
    private EquipmentService equipmentService;
    
    /**
     * REQ-S01: Display list of equipment and lab rooms
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("equipmentList", equipmentService.getAllEquipment());
        model.addAttribute("labRoomList", equipmentService.getAllLabRooms());
        return "index";
    }
    
    @GetMapping("/equipment")
    public String listEquipment(Model model) {
        model.addAttribute("equipmentList", equipmentService.getAllEquipment());
        return "equipment-list";
    }
    
    @GetMapping("/lab-rooms")
    public String listLabRooms(Model model) {
        model.addAttribute("labRoomList", equipmentService.getAllLabRooms());
        return "lab-rooms-list";
    }
}
