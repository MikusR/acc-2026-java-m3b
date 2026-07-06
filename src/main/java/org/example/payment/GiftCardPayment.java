package org.example.payment;

import org.example.model.PaymentResult;

public class GiftCardPayment extends PaymentMethod{
    private final String code;
    private double balance;

    public GiftCardPayment(String code, double balance) {
        super("Gift Card");
        this.code = code;
        this.balance = balance;
    }

    @Override
    public PaymentResult processPayment(double amount){
        if (amount > balance) {
            return new PaymentResult(false, "Insufficient balance");
        }
        if (code == null || code.isEmpty()) {
            return new PaymentResult(false, "Invalid gift card code");
        }
        balance -= amount;
        return new PaymentResult(true, "Paid " + amount + " using gift card");
    }
}
