package com.medisched.model.protocols;

public class PediatricsProtocol implements MedicalProtocol {
    @Override
    public String getInstructions() {
        return "Aduceți orice document medical relevant al copilului.";
    }

}
