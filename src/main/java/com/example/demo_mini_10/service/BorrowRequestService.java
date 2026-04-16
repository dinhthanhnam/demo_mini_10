package com.example.demo_mini_10.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo_mini_10.DTO.BorrowRequest;

@Service
public class BorrowRequestService {
    
    private Map<Long, BorrowRequest> borrowRequestDatabase = new HashMap<>();
    private Long borrowRequestIdCounter = 1L;
    
    // Get all borrow requests
    public List<BorrowRequest> getAllBorrowRequests() {
        return new ArrayList<>(borrowRequestDatabase.values());
    }
    
    // Get borrow requests by status
    public List<BorrowRequest> getBorrowRequestsByStatus(String status) {
        return borrowRequestDatabase.values().stream()
            .filter(req -> status.equalsIgnoreCase(req.getStatus()))
            .collect(Collectors.toList());
    }
    
    // Get borrow request by ID
    public Optional<BorrowRequest> getBorrowRequestById(Long id) {
        return Optional.ofNullable(borrowRequestDatabase.get(id));
    }
    
    // Get borrow requests by student ID
    public List<BorrowRequest> getBorrowRequestsByStudentId(String studentId) {
        return borrowRequestDatabase.values().stream()
            .filter(req -> req.getStudentId().equals(studentId))
            .collect(Collectors.toList());
    }
    
    // Create new borrow request
    public BorrowRequest createBorrowRequest(BorrowRequest borrowRequest) {
        borrowRequest.setId(borrowRequestIdCounter++);
        borrowRequest.setStatus("PENDING");
        borrowRequestDatabase.put(borrowRequest.getId(), borrowRequest);
        return borrowRequest;
    }
    
    // Update borrow request
    public BorrowRequest updateBorrowRequest(Long id, BorrowRequest borrowRequest) {
        borrowRequest.setId(id);
        borrowRequestDatabase.put(id, borrowRequest);
        return borrowRequest;
    }
    
    // Approve borrow request
    public void approveBorrowRequest(Long id) {
        Optional<BorrowRequest> borrowRequest = getBorrowRequestById(id);
        borrowRequest.ifPresent(req -> req.setStatus("APPROVED"));
    }
    
    // Reject borrow request
    public void rejectBorrowRequest(Long id) {
        Optional<BorrowRequest> borrowRequest = getBorrowRequestById(id);
        borrowRequest.ifPresent(req -> req.setStatus("REJECTED"));
    }
    
    // Delete borrow request
    public void deleteBorrowRequest(Long id) {
        borrowRequestDatabase.remove(id);
    }
    
    // Get pending borrow requests (for admin dashboard)
    public List<BorrowRequest> getPendingBorrowRequests() {
        return getBorrowRequestsByStatus("PENDING");
    }
    
    // Get approved borrow requests
    public List<BorrowRequest> getApprovedBorrowRequests() {
        return getBorrowRequestsByStatus("APPROVED");
    }
    
    // Validation helper methods
    public boolean isValidStudentId(String studentId) {
        // Format: 2 letters followed by digits (e.g., SV123456)
        return studentId != null && studentId.matches("^[A-Za-z]{2}\\d+$");
    }
    
    public boolean isReceiveDateValid(LocalDate receiveDate) {
        // Receive date must be in the future
        return receiveDate != null && receiveDate.isAfter(LocalDate.now());
    }
    
    public boolean isReturnDateValid(LocalDate receiveDate, LocalDate returnDate) {
        // Return date must be after receive date
        return returnDate != null && receiveDate != null && returnDate.isAfter(receiveDate);
    }
    
    /**
     * Kiểm tra xem có yêu cầu mượn trùng lịch không (overlapping dates)
     * Chỉ kiểm tra các yêu cầu APPROVED
     * 
     * Hai khoảng thời gian trùng nếu:
     * receiveDate1 < returnDate2 AND returnDate1 > receiveDate2
     */
    public List<BorrowRequest> getOverlappingRequests(Long equipmentId, LocalDate receiveDate, LocalDate returnDate) {
        return borrowRequestDatabase.values().stream()
            .filter(req -> req.getEquipmentId().equals(equipmentId))
            .filter(req -> "APPROVED".equals(req.getStatus())) // Chỉ kiểm tra các yêu cầu đã được duyệt
            .filter(req -> {
                // Kiểm tra overlap: receiveDate < returnDate2 AND returnDate > receiveDate2
                return receiveDate.isBefore(req.getReturnDate()) && 
                       returnDate.isAfter(req.getReceiveDate());
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Kiểm tra xem có yêu cầu mượn trùng (chỉ check PENDING + APPROVED)
     */
    public List<BorrowRequest> getOverlappingRequestsIncludingPending(Long equipmentId, LocalDate receiveDate, LocalDate returnDate) {
        return borrowRequestDatabase.values().stream()
            .filter(req -> req.getEquipmentId().equals(equipmentId))
            .filter(req -> "APPROVED".equals(req.getStatus()) || "PENDING".equals(req.getStatus()))
            .filter(req -> {
                return receiveDate.isBefore(req.getReturnDate()) && 
                       returnDate.isAfter(req.getReceiveDate());
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Tính số lượng đã được mượn trong 1 khoảng thời gian
     * (từ các yêu cầu APPROVED)
     */
    public Integer getBorrowedQuantityInPeriod(Long equipmentId, LocalDate receiveDate, LocalDate returnDate) {
        return getOverlappingRequests(equipmentId, receiveDate, returnDate)
            .stream()
            .mapToInt(BorrowRequest::getQuantity)
            .sum();
    }
    
    /**
     * Kiểm tra xem có đủ hàng để mượn trong khoảng thời gian này không
     * @param equipmentId ID của thiết bị
     * @param receiveDate Ngày bắt đầu mượn
     * @param returnDate Ngày kết thúc mượn
     * @param requestedQuantity Số lượng muốn mượn
     * @param totalAvailable Số lượng sẵn có tổng cộng
     * @return true nếu đủ hàng, false nếu không đủ
     */
    public boolean isStockAvailable(Long equipmentId, LocalDate receiveDate, LocalDate returnDate, 
                                    Integer requestedQuantity, Integer totalAvailable) {
        Integer borrowedQuantity = getBorrowedQuantityInPeriod(equipmentId, receiveDate, returnDate);
        Integer remainingQuantity = totalAvailable - borrowedQuantity;
        return remainingQuantity >= requestedQuantity;
    }
    
    /**
     * Lấy số lượng có thể mượn trong 1 khoảng thời gian
     * @return Số lượng còn lại khả dụng (có thể là âm nếu overbooked)
     */
    public Integer getAvailableQuantityInPeriod(Long equipmentId, LocalDate receiveDate, LocalDate returnDate, 
                                                Integer totalAvailable) {
        Integer borrowedQuantity = getBorrowedQuantityInPeriod(equipmentId, receiveDate, returnDate);
        return totalAvailable - borrowedQuantity;
    }
}
