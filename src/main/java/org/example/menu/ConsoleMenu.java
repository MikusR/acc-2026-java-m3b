package org.example.menu;

import org.example.config.AppConfig;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.PaymentResult;
import org.example.model.PercentageDiscount;
import org.example.model.FixedAmountDiscount;
import org.example.payment.PaymentMethod;
import org.example.payment.PaymentMethodFactory;
import org.example.payment.PaymentProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final PaymentProcessor paymentProcessor = new PaymentProcessor();
    private final List<Order> completedOrders = new ArrayList<>();
    private Order currentOrder;

    public void start() {
        AppConfig config = AppConfig.getInstance();
        System.out.println("Welcome to " + config.getApplicationName());

        boolean running = true;
        while (running) {
            printMenu();

            int option = readInt();

            switch (option) {
                case 1 -> createOrder();
                case 2 -> addItem();
                case 3 -> viewOrder();
                case 4 -> payOrder();
                case 5 -> viewCompletedOrders();
                case 6 -> applyDiscount();
                case 0 -> running = false;
                default -> running = true;
            }
        }
    }

    private void viewCompletedOrders() {
        System.out.println("Completed Orders:");
        for (Order order : completedOrders) {
            System.out.println(order);
        }
    }

    private void applyDiscount() {
        if (currentOrder == null) {
            System.out.println("Please create an order first.");
            return;
        }

        if (currentOrder.isPaid()) {
            System.out.println("Cannot apply discount to an order that has already been paid.");
            return;
        }

        System.out.println("""
                Select discount type:
                1. Percentage Discount
                2. Fixed Amount Discount
                """);
        int option = readInt();

        switch (option) {
            case 1 -> {
                System.out.println("Discount code:");
                String code = readString("Discount code");
                System.out.println("Percentage:");
                double percentage = readDouble();
                if (percentage < 0 || percentage > 100) {
                    System.out.println("Invalid percentage. Must be between 0 and 100.");
                    return;
                }
                currentOrder.applyDiscount(new PercentageDiscount(code, percentage));
                System.out.println("Percentage discount applied successfully.");
            }
            case 2 -> {
                System.out.println("Discount code:");
                String code = readString("Discount code");
                System.out.println("Amount:");
                double amount = readDouble();
                if (amount < 0) {
                    System.out.println("Invalid amount. Must be positive.");
                    return;
                }
                currentOrder.applyDiscount(new FixedAmountDiscount(code, amount));
                System.out.println("Fixed amount discount applied successfully.");
            }
            default -> System.out.println("Invalid discount type selection.");
        }
    }

    private void createOrder() {
        System.out.println("Customer name:");
        String customerName = readString("Customer name");

        currentOrder = Order.builder().customerName(customerName).build();
        System.out.println("Order created for " + customerName);
    }

    private String readString(String string) {
        while (true) {
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("Please input " + string);
        }
    }

    private void addItem() {
        if (currentOrder == null) {
            System.out.println("Please create an order first.");
            return;
        }

        if (currentOrder.isPaid()) {
            System.out.println("Cannot add items to an order that has already been paid.");
            return;
        }

        System.out.println("Item name:");
        String itemName = readString("Item name");

        System.out.println("Price:");
        double price = readDouble();

        System.out.println("Quantity:");
        int quantity = readInt();

        currentOrder.addItem(new OrderItem(itemName, price, quantity));
        System.out.println("Item added to order");
    }


    private void viewOrder() {
        if (currentOrder == null) {
            System.out.println("Please create an order first.");
            return;
        }

        System.out.println("Customer: " + currentOrder.getCustomerName());
        System.out.println("Status: " + currentOrder.getStatus());
        System.out.println("Items:");

        for (OrderItem item : currentOrder.getItems()) {
            System.out.println("- " + item);
        }
        System.out.println("Subtotal: " + currentOrder.calculateSubtotal());
        double discount = currentOrder.calculateSubtotal() - currentOrder.calculateDiscountedSubtotal();
        System.out.println("Discount: " + discount);
        System.out.println("Tax: " + currentOrder.calculateTax());
        System.out.println("Total: " + currentOrder.calculateTotal());
    }

    private void payOrder() {
        if (currentOrder == null) {
            System.out.println("Please create an order first.");
            return;
        }

        if (currentOrder.isPaid()) {
            System.out.println("Order is already paid.");
            return;
        }

        if (currentOrder.getItems().isEmpty()) {
            System.out.println("Cannot pay for an empty order. Please add items first.");
            return;
        }

        System.out.println("""
                Select payment method:
                1. Credit Card
                2. PayPal
                3. Gift Card
                4. Chicken Payment
                """);
        int option = readInt();

        PaymentMethod paymentMethod = switch (option) {
            case 1 -> createCreditCardPayment();
            case 2 -> createPayPalPayment();
            case 3 -> createGiftCardPayment();
            case 4 -> createChickenPayment();
            default -> {
                System.out.println("Invalid payment method");
                yield null;
            }
        };

        if (paymentMethod == null) {
            return;
        }

        PaymentResult result = paymentProcessor.process(currentOrder, paymentMethod);
        if (result.isSuccessful()) {
            completedOrders.add(currentOrder);
        }
        System.out.println(result.getMessage());
    }

    private PaymentMethod createCreditCardPayment() {
        System.out.println("Card number:");
        String cardNumber = scanner.nextLine();

        System.out.println("Card holder name:");
        String cardHolderName = scanner.nextLine();

        return PaymentMethodFactory.createCreditCardPayment(cardNumber, cardHolderName);
    }

    private PaymentMethod createPayPalPayment() {
        System.out.println("PayPal email:");
        String payPalEmail = scanner.nextLine();

        return PaymentMethodFactory.createPayPalPayment(payPalEmail);
    }

    private PaymentMethod createGiftCardPayment() {
        System.out.println("Gift card number:");
        String giftCardNumber = readString("Gift card number");
        System.out.println("Gift card balance:");
        double balance = readDouble();

        return PaymentMethodFactory.createGiftCardPayment(giftCardNumber, balance);
    }

    private PaymentMethod createChickenPayment() {
        System.out.println("Chicken count:");
        int count = readInt();

        return PaymentMethodFactory.createChickenPayment(count);
    }

    private int readInt() {
        while (true) {
            try {
                int number = Integer.parseInt(scanner.nextLine());
                if (number < 0) {
                    System.out.print("Invalid input. Number cannot be negative. Try again: ");
                    continue;
                }
                return number;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
    }

    private double readDouble() {
        while (true) {
            try {Double number = Double.parseDouble(scanner.nextLine());
                if (number < 0) {
                    System.out.print("Invalid input. Number cannot be negative. Try again: ");
                    continue;
                }
                return number;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
    }

    private void printMenu() {
        System.out.println("""
                1. Create order
                2. Add item to order
                3. View order
                4. Pay order
                5. View completed orders
                6. Apply discount
                0. Exit
                """);
    }
}
