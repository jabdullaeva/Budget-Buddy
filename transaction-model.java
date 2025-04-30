package com.budgetbuddy.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;
    
    @Column(name = "transaction_category")
    private String category;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;
    
    @Column
    private String notes;
    
    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
    
    public enum TransactionType {
        EXPENSE, INCOME, TRANSFER, BILL_PAYMENT
    }
    
    // Constructors
    public Transaction() {
        this.transactionDate = LocalDateTime.now();
    }
    
    public Transaction(String description, BigDecimal amount, TransactionType type, User user) {
        this();
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.user = user;
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
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    
    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Budget getBudget() {
        return budget;
    }
    
    public void setBudget(Budget budget) {
        this.budget = budget;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public void setType(TransactionType type) {
        this.type = type;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Payment getPayment() {
        return payment;
    }
    
    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    
    // Helper methods
    public boolean isExpense() {
        return this.type == TransactionType.EXPENSE || this.type == TransactionType.BILL_PAYMENT;
    }
    
    public boolean isIncome() {
        return this.type == TransactionType.INCOME;
    }
}
