package com.medisched.model.protocols;

public class OrthopedicsProtocol implements MedicalProtocol {
    @Override
    public String getInstructions() {
        return "Purtați haine comode; aduceți investigațiile imagistice anterioare (radiografii/RMN).";
    }

}
