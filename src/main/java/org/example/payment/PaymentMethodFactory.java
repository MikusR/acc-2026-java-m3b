package org.example.payment;

public class PaymentMethodFactory {
    public static PaymentMethod createCreditCardPayment(String cardNumber, String cardHolderName) {
        return new CreditCardPayment(cardNumber, cardHolderName);
    }

    public static PaymentMethod createGiftCardPayment(String code, double balance) {
        return new GiftCardPayment(code, balance);
    }

    public static PaymentMethod createPayPalPayment(String email) {
        return new PayPalPayment(email);
    }

    public static PaymentMethod createChickenPayment(int count) {
        return new ChickenPayment(count);
    }
}
