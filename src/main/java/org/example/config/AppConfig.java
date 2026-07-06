package org.example.config;

// Singleton configuration class
public class AppConfig {
    private static AppConfig instance;

    private final String applicationName;
    private final String currency;
    private final double taxRate;
    private final int chickenValue;

    private AppConfig() {
        this.applicationName = "Bootcamp Payment App";
        this.currency = "EUR";
        this.taxRate = 0.21;
        this.chickenValue = 10;
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public String getApplicationName() {
        return applicationName;
    }
    public String getCurrency() {
        return currency;
    }
    public int getChickenValue() {return chickenValue; }
    public double getTaxRate() {
        return taxRate;
    }
}
