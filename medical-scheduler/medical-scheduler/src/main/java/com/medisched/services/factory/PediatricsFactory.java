package com.medisched.services.factory;

import com.medisched.model.entities.Appointment;
import com.medisched.model.protocols.MedicalProtocol;
import com.medisched.model.protocols.PediatricsProtocol;
import org.springframework.stereotype.Component;

@Component
public class PediatricsFactory implements AppointmentFactory {
    @Override
    public Appointment createAppointment() {
        Appointment app = new Appointment();
        app.setType("Pediatrie");
        return app;
    }

    @Override
    public MedicalProtocol createProtocol() {
        return new PediatricsProtocol();
    }
}
