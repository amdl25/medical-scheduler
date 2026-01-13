package com.medisched.services.strategy;

import org.springframework.stereotype.Component;

@Component("emergency")
public class EmergencyPricing implements PricingStrategy {
    @Override
    public double calculatePrice(double basePrice) {
        return basePrice * 1.2;
    }

    @Override
    public String getName() {
        return "Urgență";
    }
}
