package com.medisched.services.factory;

import com.medisched.model.entities.Appointment;
import com.medisched.model.protocols.CardiologyProtocol;
import com.medisched.model.protocols.MedicalProtocol;
import org.springframework.stereotype.Component;

@Component
public class CardiologyFactory implements AppointmentFactory{
    @Override
    public Appointment createAppointment() {
        Appointment app = new Appointment();
        app.setType("Cardiologie");
        return app;
    }

    @Override
    public MedicalProtocol createProtocol() {
        return new CardiologyProtocol();
    }
}
