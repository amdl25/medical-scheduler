package com.medisched.model.protocols;

public class NeurologyProtocol implements MedicalProtocol {
    @Override
    public String getInstructions() {
        return "Evitați consumul de alcool cu 24h înainte. Notați episoadele / simptomele recente.";
    }

}
