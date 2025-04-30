package com.budgetbuddy.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;
    
    @ManyToOne
    @JoinColumn(name = "payer_id", nullable = false)
    private User payer;
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;
    
    @Column
    private String notes;
    
    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, REFUNDED
    }
    
    // Constructors
    public Payment() {
        this.paymentDate = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
    }
    
    public Payment(Bill bill, User payer, BigDecimal amount) {
        this();
        this.bill = bill;
        this.payer = payer;
        this.amount = amount;
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Bill getBill() {
        return bill;
    }
    
    public void setBill(Bill bill) {
        this.bill = bill;
    }
    
    public User getPayer() {
        return payer;
    }
    
    public void setPayer(User payer) {
        this.payer = payer;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public PaymentStatus getStatus() {
        return status;
    }
    
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    // Helper methods
    public void markAsCompleted() {
        this.status = PaymentStatus.COMPLETED;
        if (this.bill != null) {
            this.bill.updateStatus();
        }
    }
    
    public void markAsFailed() {
        this.status = PaymentStatus.FAILED;
    }
    
    public void markAsRefunded() {
        this.status = PaymentStatus.REFUNDED;
        if (this.bill != null) {
            this.bill.updateStatus();
        }
    }
}
