package com.medisched.services.factory;

import com.medisched.model.entities.Appointment;
import com.medisched.model.protocols.MedicalProtocol;
import com.medisched.model.protocols.OrthopedicsProtocol;
import org.springframework.stereotype.Component;

@Component
public class OrthopedicsFactory implements AppointmentFactory {
    @Override
    public Appointment createAppointment() {
        Appointment app = new Appointment();
        app.setType("Ortopedie");
        return app;
    }

    @Override
    public MedicalProtocol createProtocol() {
        return new OrthopedicsProtocol();
    }
}
