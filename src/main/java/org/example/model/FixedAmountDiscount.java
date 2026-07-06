package org.example.model;

public class FixedAmountDiscount extends Discount{
    private double amount;

    public FixedAmountDiscount(String code, double amount) {
        super(code);
        this.amount = amount;
    }

    @Override
    public double apply(double originalAmount) {
        return Math.max(0, originalAmount - amount);
    }
}
