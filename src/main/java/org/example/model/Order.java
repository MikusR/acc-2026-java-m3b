package org.example.model;

import org.example.config.AppConfig;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final String customerName;
    private final List<OrderItem> items;
    private OrderStatus status;
    private Discount discount = new NoDiscount();
    AppConfig config = AppConfig.getInstance();

    public Order(Builder builder) {
        this.customerName = builder.customerName;
        this.items = builder.items;
        this.status = OrderStatus.NEW;
    }

    public void addItem(OrderItem item){
        if (this.status == OrderStatus.PAID) {
            throw new IllegalStateException("Cannot add items to an order that's been paid");
        }
        items.add(item);
    }
    public double calculateSubtotal(){
        double subtotal = 0;
        for (OrderItem item : items) {
            subtotal += item.calculateTotal();
        }
        return subtotal;
    }
    public double calculateDiscountedSubtotal(){
        double subtotal = calculateSubtotal();
        return discount.apply(subtotal);
    }
    public double calculateTotal(){
        return calculateDiscountedSubtotal() + calculateTax();
    }

    public double calculateTax() {
        double discountedSubtotal = calculateDiscountedSubtotal();
        double taxRate = config.getTaxRate();
        return discountedSubtotal * taxRate;
    }


    public void markAsPaid(){
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot mark an empty order as paid");
        }
        this.status = OrderStatus.PAID;
    }

    public void applyDiscount(Discount discount){
        this.discount = discount;
    }

    public boolean isPaid(){
        return this.status == OrderStatus.PAID;
    }

    public List<OrderItem> getItems() {
        return items;
    }
    public String getCustomerName() {
        return customerName;
    }
    public OrderStatus getStatus() {
        return status;
    }
    public static Builder builder(){
        return new Builder();
    }
    public static class Builder{
        private String customerName;
        private List<OrderItem> items = new ArrayList<>();
        public Builder customerName(String customerName){
            this.customerName = customerName;
            return this;
        }
        public Builder addItem(OrderItem item){
            this.items.add(item);
            return this;
        }
        public Order build(){
            if (customerName == null || customerName.trim().isEmpty()) {
                throw new IllegalArgumentException("Customer name cannot be empty");
            }
            return new Order(this);
        }
    }

    @Override
    public String toString() {
        return "Order | Customer: " + customerName +
                " | Items: " + items.size() +
                " | Total: $" + calculateTotal() +
                " | Status: " + status;
    }
}
