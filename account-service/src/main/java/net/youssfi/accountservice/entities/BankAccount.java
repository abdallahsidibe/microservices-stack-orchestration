package net.youssfi.accountservice.entities;

import jakarta.persistence.*;
import lombok.*;
import net.youssfi.accountservice.enums.AccountType;
import net.youssfi.accountservice.model.Customer;

import java.time.LocalDate;
@Entity
@Getter @Setter @ToString @Builder
public class BankAccount {
    @Id
    private String accountId;
    private double balance;
    private LocalDate createAt;
    private String currency;
    @Enumerated(EnumType.STRING)
    private AccountType type;
    @Transient
    private Customer customer;
    private Long customerId;

    public BankAccount() {
    }

    public BankAccount(String accountId, double balance, LocalDate createAt, String currency, AccountType type, Customer customer, Long customerId) {
        this.accountId = accountId;
        this.balance = balance;
        this.createAt = createAt;
        this.currency = currency;
        this.type = type;
        this.customer = customer;
        this.customerId = customerId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
