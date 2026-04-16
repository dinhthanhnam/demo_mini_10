package com.example.demo_mini_10.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo_mini_10.service.BorrowRequestService;
import com.example.demo_mini_10.service.EquipmentService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private BorrowRequestService borrowRequestService;
    
    @Autowired
    private EquipmentService equipmentService;
    
    /**
     * REQ-A02: Display list of borrow requests for admin to review
     */
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("pendingRequests", borrowRequestService.getPendingBorrowRequests());
        model.addAttribute("approvedRequests", borrowRequestService.getApprovedBorrowRequests());
        model.addAttribute("allRequests", borrowRequestService.getAllBorrowRequests());
        return "admin-dashboard";
    }
    
    /**
     * Display pending borrow requests
     */
    @GetMapping("/pending-requests")
    public String viewPendingRequests(Model model) {
        model.addAttribute("borrowRequests", borrowRequestService.getPendingBorrowRequests());
        return "pending-requests";
    }
    
    /**
     * REQ-A01: Equipment management page
     */
    @GetMapping("/equipment-management")
    public String manageEquipment(Model model) {
        model.addAttribute("equipmentList", equipmentService.getAllEquipment());
        return "equipment-management";
    }
    
    /**
     * Approve a borrow request
     */
    @PostMapping("/approve/{requestId}")
    public String approveBorrowRequest(@PathVariable("requestId") Long requestId) {
        borrowRequestService.approveBorrowRequest(requestId);
        return "redirect:/admin/dashboard";
    }
    
    /**
     * Reject a borrow request
     */
    @PostMapping("/reject/{requestId}")
    public String rejectBorrowRequest(@PathVariable("requestId") Long requestId) {
        borrowRequestService.rejectBorrowRequest(requestId);
        return "redirect:/admin/dashboard";
    }
    
    /**
     * View details of a borrow request
     */
    @GetMapping("/request/{requestId}")
    public String viewRequestDetail(@PathVariable("requestId") Long requestId, Model model) {
        var borrowRequest = borrowRequestService.getBorrowRequestById(requestId);
        
        if (borrowRequest.isEmpty()) {
            return "redirect:/admin/dashboard";
        }
        
        model.addAttribute("borrowRequest", borrowRequest.get());
        
        // Add equipment/lab room details
        var equipment = equipmentService.getEquipmentById(borrowRequest.get().getEquipmentId());
        if (equipment.isPresent()) {
            model.addAttribute("equipment", equipment.get());
            model.addAttribute("itemType", "equipment");
        } else {
            var labRoom = equipmentService.getLabRoomById(borrowRequest.get().getEquipmentId());
            if (labRoom.isPresent()) {
                model.addAttribute("labRoom", labRoom.get());
                model.addAttribute("itemType", "labRoom");
            }
        }
        
        return "request-detail";
    }
}
