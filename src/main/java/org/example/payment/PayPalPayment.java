package org.example.payment;

import org.example.model.PaymentResult;

public class PayPalPayment extends PaymentMethod {
    private final String email;

    public PayPalPayment(String email) {
        super("PayPal");
        this.email = email;
    }

    @Override
    public PaymentResult processPayment(double amount) {
        if (email == null || email.trim().isEmpty() || !email.matches(".+@.+\\..+")) {
            return new PaymentResult(false, "Invalid email");
        }
        return new PaymentResult(true, "Paid " + amount + " using PayPal: " + email);
    }
}
