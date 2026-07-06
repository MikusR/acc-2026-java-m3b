package org.example.payment;

import org.example.config.AppConfig;
import org.example.model.PaymentResult;

public class ChickenPayment extends PaymentMethod {
    private final AppConfig config = AppConfig.getInstance();
    private final int chickenCount;

    public ChickenPayment(int count) {
        super("Chicken");
        this.chickenCount = count;
    }

    @Override
    protected PaymentResult processPayment(double amount) {
        if (chickenCount <= 0) {
            return new PaymentResult(false, "Chicken payment failed");
        }
        if (chickenCount * config.getChickenValue() >= amount) {
            return new PaymentResult(true, "Paid " + amount + " with " + chickenCount + " chickens");
        } else {
            return new PaymentResult(false, "Chicken payment failed");
        }
    }
}
