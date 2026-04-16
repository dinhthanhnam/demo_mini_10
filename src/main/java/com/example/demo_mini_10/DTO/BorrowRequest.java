package com.example.demo_mini_10.DTO;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import com.example.demo_mini_10.validation.ValidStatus;
import com.example.demo_mini_10.validation.ValidStudentId;

public class BorrowRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "Họ và tên sinh viên không được để trống")
    private String studentName;

    @NotBlank(message = "Mã sinh viên không được để trống")
    @ValidStudentId(message = "Mã sinh viên phải bắt đầu bằng 2 chữ cái theo sau là các chữ số (ví dụ: SV123456)")
    private String studentId;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotNull(message = "Thiết bị không được để trống")
    private Long equipmentId;

    @NotNull(message = "Số lượng không được để trống")
    @Positive(message = "Số lượng phải là số dương")
    private Integer quantity;

    @NotNull(message = "Ngày dự kiến nhận không được để trống")
    private LocalDate receiveDate;

    @NotNull(message = "Ngày dự kiến trả không được để trống")
    private LocalDate returnDate;

    @NotBlank(message = "Lý do mượn không được để trống")
    private String borrowReason;

    @ValidStatus(message = "Trạng thái phải là PENDING, APPROVED hoặc REJECTED")
    private String status; // PENDING, APPROVED, REJECTED

    // Constructor
    public BorrowRequest() {
        this.status = "PENDING";
    }

    public BorrowRequest(Long id, String studentName, String studentId, String email,
            Long equipmentId, Integer quantity, LocalDate receiveDate,
            LocalDate returnDate, String borrowReason) {
        this.id = id;
        this.studentName = studentName;
        this.studentId = studentId;
        this.email = email;
        this.equipmentId = equipmentId;
        this.quantity = quantity;
        this.receiveDate = receiveDate;
        this.returnDate = returnDate;
        this.borrowReason = borrowReason;
        this.status = "PENDING";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(LocalDate receiveDate) {
        this.receiveDate = receiveDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getBorrowReason() {
        return borrowReason;
    }

    public void setBorrowReason(String borrowReason) {
        this.borrowReason = borrowReason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BorrowRequest{" +
                "id=" + id +
                ", studentName='" + studentName + '\'' +
                ", studentId='" + studentId + '\'' +
                ", email='" + email + '\'' +
                ", equipmentId=" + equipmentId +
                ", quantity=" + quantity +
                ", receiveDate=" + receiveDate +
                ", returnDate=" + returnDate +
                ", borrowReason='" + borrowReason + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
