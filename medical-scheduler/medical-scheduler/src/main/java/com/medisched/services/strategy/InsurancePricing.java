package com.medisched.services.strategy;

import org.springframework.stereotype.Component;

@Component("insurance")
public class InsurancePricing implements PricingStrategy {
    @Override
    public double calculatePrice(double basePrice) {
        return 0.0;
    }

    @Override
    public String getName() {
        return "Asigurat";
    }
}
