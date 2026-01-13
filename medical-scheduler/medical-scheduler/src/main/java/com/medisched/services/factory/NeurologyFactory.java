package com.medisched.services.factory;

import com.medisched.model.entities.Appointment;
import com.medisched.model.protocols.MedicalProtocol;
import com.medisched.model.protocols.NeurologyProtocol;
import org.springframework.stereotype.Component;

@Component
public class NeurologyFactory implements AppointmentFactory {
    @Override
    public Appointment createAppointment() {
        Appointment app = new Appointment();
        app.setType("Neurologie");
        return app;
    }

    @Override
    public MedicalProtocol createProtocol() {
        return new NeurologyProtocol();
    }
}
