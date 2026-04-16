package com.example.demo_mini_10.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo_mini_10.DTO.BorrowRequest;
import com.example.demo_mini_10.service.BorrowRequestService;
import com.example.demo_mini_10.service.EquipmentService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/borrow")
public class BorrowController {
    
    @Autowired
    private BorrowRequestService borrowRequestService;
    
    @Autowired
    private EquipmentService equipmentService;
    
    /**
     * REQ-S02: Display borrowing form for equipment (equipment ID = type 1)
     */
    @GetMapping("/equipment/{equipmentId}")
    public String showEquipmentBorrowForm(@PathVariable("equipmentId") Long equipmentId, Model model) {
        var equipment = equipmentService.getEquipmentById(equipmentId);
        
        if (equipment.isEmpty()) {
            return "redirect:/equipment";
        }
        
        model.addAttribute("equipment", equipment.get());
        model.addAttribute("borrowRequest", new BorrowRequest());
        model.addAttribute("itemType", "equipment");
        return "borrow-form";
    }
    
    /**
     * REQ-S02: Display borrowing form for lab room (lab room ID = type 2)
     */
    @GetMapping("/lab-room/{labRoomId}")
    public String showLabRoomBorrowForm(@PathVariable("labRoomId") Long labRoomId, Model model) {
        var labRoom = equipmentService.getLabRoomById(labRoomId);
        
        if (labRoom.isEmpty()) {
            return "redirect:/lab-rooms";
        }
        
        model.addAttribute("labRoom", labRoom.get());
        model.addAttribute("borrowRequest", new BorrowRequest());
        model.addAttribute("itemType", "labRoom");
        return "borrow-form";
    }
    
    /**
     * REQ-S02: Submit borrowing request with validation
     * VAL-01: All fields required
     * VAL-02: Dates validation
     * VAL-03: Quantity validation
     * VAL-04: Email and StudentId format validation
     */
    @PostMapping("/submit-equipment/{equipmentId}")
    public String submitEquipmentBorrowRequest(
            @PathVariable("equipmentId") Long equipmentId,
            @Valid BorrowRequest borrowRequest,
            BindingResult bindingResult,
            Model model) {
        
        var equipment = equipmentService.getEquipmentById(equipmentId);
        
        if (equipment.isEmpty()) {
            return "redirect:/equipment";
        }
        
        // VAL-02: Date validation
        if (borrowRequest.getReceiveDate() != null && 
            !borrowRequestService.isReceiveDateValid(borrowRequest.getReceiveDate())) {
            bindingResult.rejectValue("receiveDate", "error.receiveDate",
                "Ngày dự kiến nhận phải là ngày trong tương lai");
        }
        
        if (borrowRequest.getReceiveDate() != null && borrowRequest.getReturnDate() != null &&
            !borrowRequestService.isReturnDateValid(borrowRequest.getReceiveDate(), borrowRequest.getReturnDate())) {
            bindingResult.rejectValue("returnDate", "error.returnDate",
                "Ngày trả phải sau ngày nhận");
        }
        
        // VAL-03: Quantity validation + Stock availability check
        if (borrowRequest.getQuantity() != null) {
            // Kiểm tra số lượng đơn lẻ
            if (borrowRequest.getQuantity() > equipment.get().getQuantityAvailable()) {
                bindingResult.rejectValue("quantity", "error.quantity",
                    "Số lượng mượn không được vượt quá " + equipment.get().getQuantityAvailable() + " cái");
            }
            
            // Kiểm tra overlap: có yêu cầu khác mượn cùng khoảng thời gian không?
            if (borrowRequest.getReceiveDate() != null && borrowRequest.getReturnDate() != null) {
                Integer availableInPeriod = borrowRequestService.getAvailableQuantityInPeriod(
                    equipmentId,
                    borrowRequest.getReceiveDate(),
                    borrowRequest.getReturnDate(),
                    equipment.get().getQuantityAvailable()
                );
                
                if (availableInPeriod < borrowRequest.getQuantity()) {
                    bindingResult.rejectValue("quantity", "error.quantity.overlap",
                        "Chỉ còn " + availableInPeriod + " cái khả dụng trong khoảng thời gian này. " +
                        "Vui lòng chọn ngày khác hoặc giảm số lượng.");
                }
            }
        }
        
        // VAL-04: If there are errors, return to form with preserved data
        if (bindingResult.hasErrors()) {
            model.addAttribute("equipment", equipment.get());
            model.addAttribute("borrowRequest", borrowRequest);
            model.addAttribute("itemType", "equipment");
            return "borrow-form";
        }
        
        // Set equipment ID and create request
        borrowRequest.setEquipmentId(equipmentId);
        borrowRequestService.createBorrowRequest(borrowRequest);
        
        return "redirect:/borrow/success";
    }
    
    /**
     * REQ-S02: Submit borrowing request for lab room
     */
    @PostMapping("/submit-lab-room/{labRoomId}")
    public String submitLabRoomBorrowRequest(
            @PathVariable("labRoomId") Long labRoomId,
            @Valid BorrowRequest borrowRequest,
            BindingResult bindingResult,
            Model model) {
        
        var labRoom = equipmentService.getLabRoomById(labRoomId);
        
        if (labRoom.isEmpty()) {
            return "redirect:/lab-rooms";
        }
        
        // For lab room, quantity is always 1
        borrowRequest.setQuantity(1);
        
        // VAL-02: Date validation
        if (borrowRequest.getReceiveDate() != null && 
            !borrowRequestService.isReceiveDateValid(borrowRequest.getReceiveDate())) {
            bindingResult.rejectValue("receiveDate", "error.receiveDate",
                "Ngày dự kiến sử dụng phải là ngày trong tương lai");
        }
        
        if (borrowRequest.getReceiveDate() != null && borrowRequest.getReturnDate() != null &&
            !borrowRequestService.isReturnDateValid(borrowRequest.getReceiveDate(), borrowRequest.getReturnDate())) {
            bindingResult.rejectValue("returnDate", "error.returnDate",
                "Ngày kết thúc sử dụng phải sau ngày bắt đầu");
        }
        
        // VAL-03: Check if lab room is available in this time period (lab rooms can only be used by 1 group at a time)
        if (borrowRequest.getReceiveDate() != null && borrowRequest.getReturnDate() != null) {
            List<BorrowRequest> overlappingRequests = borrowRequestService.getOverlappingRequests(
                labRoomId,
                borrowRequest.getReceiveDate(),
                borrowRequest.getReturnDate()
            );
            
            if (!overlappingRequests.isEmpty()) {
                bindingResult.reject("error.labRoom.overlap",
                    "Phòng Lab này đã bị mượn trong khoảng thời gian này. Vui lòng chọn ngày khác. " +
                    "Các lịch mượn hiện tại: " + formatOverlappingDates(overlappingRequests));
            }
        }
        
        // VAL-04: If there are errors, return to form
        if (bindingResult.hasErrors()) {
            model.addAttribute("labRoom", labRoom.get());
            model.addAttribute("borrowRequest", borrowRequest);
            model.addAttribute("itemType", "labRoom");
            return "borrow-form";
        }
        
        // Set lab room ID and create request
        borrowRequest.setEquipmentId(labRoomId);
        borrowRequestService.createBorrowRequest(borrowRequest);
        
        return "redirect:/borrow/success";
    }
    
    @GetMapping("/success")
    public String borrowSuccess() {
        return "borrow-success";
    }
    
    /**
     * Helper method: Format overlapping dates for display
     */
    private String formatOverlappingDates(List<BorrowRequest> overlappingRequests) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return overlappingRequests.stream()
            .map(req -> req.getReceiveDate().format(formatter) + " - " + req.getReturnDate().format(formatter))
            .collect(Collectors.joining("; "));
    }
}
