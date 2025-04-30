package com.budgetbuddy.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bills")
public class Bill {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private BigDecimal totalAmount;
    
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
    
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    
    @ManyToOne
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillStatus status;
    
    @ManyToMany
    @JoinTable(
        name = "bill_participants",
        joinColumns = @JoinColumn(name = "bill_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> participants = new HashSet<>();
    
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Payment> payments = new HashSet<>();
    
    public enum BillStatus {
        PENDING, PARTIALLY_PAID, PAID, OVERDUE, CANCELLED
    }
    
    // Constructors
    public Bill() {
        this.createdDate = LocalDateTime.now();
        this.status = BillStatus.PENDING;
    }
    
    public Bill(String description, BigDecimal totalAmount, User createdBy) {
        this();
        this.description = description;
        this.totalAmount = totalAmount;
        this.createdBy = createdBy;
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public LocalDateTime getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
    
    public User getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
    
    public BillStatus getStatus() {
        return status;
    }
    
    public void setStatus(BillStatus status) {
        this.status = status;
    }
    
    public Set<User> getParticipants() {
        return participants;
    }
    
    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }
    
    public Set<Payment> getPayments() {
        return payments;
    }
    
    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }
    
    // Helper methods
    public void addParticipant(User user) {
        participants.add(user);
        user.getBills().add(this);
    }
    
    public void removeParticipant(User user) {
        participants.remove(user);
        user.getBills().remove(this);
    }
    
    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setBill(this);
        updateStatus();
    }
    
    public BigDecimal getTotalPaid() {
        return payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public BigDecimal getRemainingAmount() {
        return totalAmount.subtract(getTotalPaid());
    }
    
    public void updateStatus() {
        BigDecimal totalPaid = getTotalPaid();
        
        if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {
            this.status = BillStatus.PENDING;
        } else if (totalPaid.compareTo(totalAmount) < 0) {
            this.status = BillStatus.PARTIALLY_PAID;
        } else {
            this.status = BillStatus.PAID;
        }
        
        if (dueDate != null && LocalDateTime.now().isAfter(dueDate) && !this.status.equals(BillStatus.PAID)) {
            this.status = BillStatus.OVERDUE;
        }
    }
    
    public BigDecimal getAmountPerPerson() {
        if (participants.isEmpty()) {
            return totalAmount;
        }
        return totalAmount.divide(BigDecimal.valueOf(participants.size()), 2, BigDecimal.ROUND_HALF_UP);
    }
}
