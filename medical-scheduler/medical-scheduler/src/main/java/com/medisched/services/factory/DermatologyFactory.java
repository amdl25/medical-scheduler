package com.medisched.services.factory;

import com.medisched.model.entities.Appointment;
import com.medisched.model.protocols.DermatologyProtocol;
import com.medisched.model.protocols.MedicalProtocol;
import org.springframework.stereotype.Component;

@Component
public class DermatologyFactory implements AppointmentFactory {
    @Override
    public Appointment createAppointment() {
        Appointment app = new Appointment();
        app.setType("Dermatologie");
        return app;
    }

    @Override
    public MedicalProtocol createProtocol() {
        return new DermatologyProtocol();
    }
}
