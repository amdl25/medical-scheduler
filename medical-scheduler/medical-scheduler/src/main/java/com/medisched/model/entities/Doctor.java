package com.medisched.model.entities;

import com.medisched.services.email.EmailService;
import com.medisched.services.observer.AppointmentObserver;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;
import java.util.List;

@Entity
@Component
public class Doctor extends UserProfile implements AppointmentObserver {

    private String specialization;
    private String cabinetNumber;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @Override
    public void update(String message) {
        System.out.println("Trimitere email catre Dr. " + getLastName() + " la adresa " + getEmail() + ": " + message);
    }

    @Override
    public void update(String message, EmailService emailService) {
        String subject = "Notificare programare - MediSched";
        if (emailService != null) {
            emailService.sendEmail(getEmail(), subject, message);
        } else {
            System.out.println("Trimitere email catre Dr. " + getLastName() + " la adresa " + getEmail() + ": " + message);
        }
    }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getCabinetNumber() { return cabinetNumber; }
    public void setCabinetNumber(String cabinetNumber) { this.cabinetNumber = cabinetNumber; }
    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }
}
