package org.example.payment;

import org.example.model.PaymentResult;

public class CreditCardPayment extends PaymentMethod {
    private final String cardNumber;
    private final String cardHolderName;

    public CreditCardPayment(String cardNumber, String cardHolderName) {
        super("CreditCard");
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
    }

    @Override
    public PaymentResult processPayment(double amount) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return new PaymentResult(false,"Card number cannot be empty");
        }
        if (cardHolderName == null || cardHolderName.isEmpty()) {
            return new PaymentResult(false,"Card holder name cannot be empty");
        }
        if (cardNumber.length() < 12 || cardNumber.length() > 19) {
            return new PaymentResult(false, "Card number must be between 12 and 19 digits");
        }
        return new PaymentResult(true, "Paid " + amount + " using credit card ending with " + cardNumber.substring(cardNumber.length() - 4));
    }
}
