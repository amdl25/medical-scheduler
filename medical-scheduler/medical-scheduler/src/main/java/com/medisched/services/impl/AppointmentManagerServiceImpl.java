package com.medisched.services.impl;

import com.medisched.config.ClinicLogger;
import com.medisched.model.entities.Appointment;
import com.medisched.model.entities.Doctor;
import com.medisched.model.entities.Patient;
import com.medisched.model.protocols.MedicalProtocol;
import com.medisched.repositories.AppointmentRepository;
import com.medisched.repositories.DoctorRepository;
import com.medisched.services.email.EmailService;
import com.medisched.services.factory.AppointmentFactory;
import com.medisched.services.observer.AppointmentSubject;
import com.medisched.services.strategy.PricingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class AppointmentManagerServiceImpl {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private Map<String, PricingStrategy> pricingStrategies;

    private static final double BASE_PRICE = 200.0;


    private String buildPatientDetails(Patient patient) {
        if (patient == null) {
            return "";
        }

        StringBuilder detailsBuilder = new StringBuilder();
        detailsBuilder.append("Detalii pacient:\n")
            .append("Nume: ").append(patient.getFirstName()).append(" ").append(patient.getLastName()).append("\n")
            .append("Email: ").append(patient.getEmail()).append("\n");

        if (patient.getAge() != null) {
            detailsBuilder.append("Vârsta: ").append(patient.getAge()).append("\n");
        }

        return detailsBuilder.toString();
    }

    public void processAndSaveAppointment(Appointment appointment, String strategyType) {

        PricingStrategy strategy = pricingStrategies.getOrDefault(strategyType, pricingStrategies.get("standard"));
        double finalPrice = strategy.calculatePrice(BASE_PRICE);
        appointment.setPrice(finalPrice);

        appointmentRepository.save(appointment);

        if (appointment.getDoctor() != null) {
            Doctor doctor = appointment.getDoctor();


            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append("Programare nouă (" + strategy.getName() + ") pe data de ")
                    .append(appointment.getAppointmentDate())
                    .append(". Preț: ")
                    .append(finalPrice)
                    .append(" RON\n\n");


            messageBuilder.append(buildPatientDetails(appointment.getPatient()));

            String message = messageBuilder.toString();

            AppointmentSubject appointmentSubject = new AppointmentSubject();
            appointmentSubject.addObserver(doctor);
            appointmentSubject.notifyObservers(message, emailService);
        }

        ClinicLogger.getInstance().addLog("Programare creată cu succes. Strategie: " + strategy.getName() + 
                ", Preț final: " + finalPrice);
    }

    public void createMedicalAppointmentWithFactory(AppointmentFactory factory, Doctor doctor, String strategyType) {

        Appointment appointment = factory.createAppointment();
        MedicalProtocol protocol = factory.createProtocol();

        appointment.setDoctor(doctor);

        if (appointment.getAppointmentDate() == null) {
            appointment.setAppointmentDate(java.time.LocalDateTime.now().plusDays(1));
        }

        processAndSaveAppointment(appointment, strategyType);

        StringBuilder protocolMessageBuilder = new StringBuilder();
        protocolMessageBuilder.append("Protocol medical generat: ").append(protocol.getInstructions()).append("\n\n");

        protocolMessageBuilder.append(buildPatientDetails(appointment.getPatient()));

        String protocolMessage = protocolMessageBuilder.toString();

        AppointmentSubject appointmentSubject = new AppointmentSubject();
        appointmentSubject.addObserver(doctor);
        appointmentSubject.notifyObservers(protocolMessage, emailService);
    }
}
