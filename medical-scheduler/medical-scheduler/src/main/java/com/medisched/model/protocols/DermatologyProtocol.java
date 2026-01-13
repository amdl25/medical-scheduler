package com.medisched.model.protocols;

public class DermatologyProtocol implements MedicalProtocol {
    @Override
    public String getInstructions() {
        return "Evitați aplicarea cremelor / machiajului pe zona evaluată în ziua consultației.";
    }


}
