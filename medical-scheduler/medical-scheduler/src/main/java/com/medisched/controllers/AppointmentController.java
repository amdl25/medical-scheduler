package com.medisched.controllers;

import com.medisched.model.entities.Appointment;
import com.medisched.model.entities.Doctor;
import com.medisched.model.entities.Patient;
import com.medisched.repositories.AppointmentRepository;
import com.medisched.repositories.DoctorRepository;
import com.medisched.repositories.PatientRepository;
import com.medisched.services.factory.AppointmentFactory;
import com.medisched.services.factory.CardiologyFactory;
import com.medisched.services.factory.GeneralFactory;
import com.medisched.services.factory.DermatologyFactory;
import com.medisched.services.factory.PediatricsFactory;
import com.medisched.services.factory.NeurologyFactory;
import com.medisched.services.factory.OrthopedicsFactory;
import com.medisched.services.impl.AppointmentManagerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import java.time.LocalDateTime;
import com.medisched.util.ValidationUtils;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentManagerServiceImpl appointmentManagerService;

    @Autowired
    private CardiologyFactory cardiologyFactory;

    @Autowired
    private GeneralFactory generalFactory;

    @Autowired
    private DermatologyFactory dermatologyFactory;

    @Autowired
    private PediatricsFactory pediatricsFactory;

    @Autowired
    private NeurologyFactory neurologyFactory;

    @Autowired
    private OrthopedicsFactory orthopedicsFactory;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/")
    public String home() {
        return "index";
    }


    private AppointmentFactory getFactoryForSpecialization(String specialization) {
        if ("Cardiologie".equals(specialization)) {
            return cardiologyFactory;
        } else if ("Medicina generala".equals(specialization)) {
            return generalFactory;
        } else if ("Dermatologie".equals(specialization)) {
            return dermatologyFactory;
        } else if ("Pediatrie".equals(specialization)) {
            return pediatricsFactory;
        } else if ("Neurologie".equals(specialization)) {
            return neurologyFactory;
        } else if ("Ortopedie".equals(specialization)) {
            return orthopedicsFactory;
        }
        return generalFactory; // fallback
    }

    @GetMapping(value = "/protocol/instructions", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getProtocolInstructions(@RequestParam("specialization") String specialization) {
        AppointmentFactory factory = getFactoryForSpecialization(specialization);
        return factory.createProtocol().getInstructions();
    }

    @GetMapping("/programare/cardiologie")
    public String createCardio(Model model) {
        Doctor doc = doctorRepository.findAll().stream().findFirst().orElse(null);

        if (doc != null) {
            appointmentManagerService.createMedicalAppointmentWithFactory(cardiologyFactory, doc, "standard");
            model.addAttribute("status", "Programare la Cardiologie creata cu succes!");
        } else {
            model.addAttribute("status", "Eroare: Nu exista medici in baza de date!");
        }
        return "index";
    }

    @GetMapping("/pacients")
    public String showForm(Model model) {
        model.addAttribute("allDoctors", doctorRepository.findAll());
        return "appointment_form";
    }

    @PostMapping("/appointment/save")
    public String saveAppointment(@RequestParam Long doctorId,
                                  @RequestParam String specialization,
                                  @RequestParam String date,
                                  @RequestParam String time,
                                  @RequestParam String firstName,
                                  @RequestParam String lastName,
                                  @RequestParam Integer age,
                                  @RequestParam String patientStatus,
                                  Model model) {

        String fn = firstName != null ? firstName.trim() : null;
        String ln = lastName != null ? lastName.trim() : null;
        if (!ValidationUtils.isValidName(fn)) {
            model.addAttribute("formError", "Prenumele poate contine doar litere, spatiu, cratima sau apostrof (2–50 caractere).");
            model.addAttribute("allDoctors", doctorRepository.findAll());
            return "appointment_form";
        }
        if (!ValidationUtils.isValidName(ln)) {
            model.addAttribute("formError", "Numele poate contine doar litere, spatiu, cratima sau apostrof (2–50 caractere).");
            model.addAttribute("allDoctors", doctorRepository.findAll());
            return "appointment_form";
        }
        if (!ValidationUtils.isValidAge(age)) {
            model.addAttribute("formError", "Varsta trebuie sa fie între 0 si 100 de ani.");
            model.addAttribute("allDoctors", doctorRepository.findAll());
            return "appointment_form";
        }

        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);

        if (doctor != null) {
            Patient patient = patientRepository.findByFirstNameAndLastName(fn, ln).orElse(null);

            if (patient == null) {
                patient = new Patient();
                patient.setFirstName(fn);
                patient.setLastName(ln);
                patient.setEmail(fn.toLowerCase() + "." + ln.toLowerCase() + "@example.com");
            }

            patient.setAge(age);
            patient.setFirstName(fn);
            patient.setLastName(ln);

            try {
                patientRepository.save(patient);
            } catch (Exception e) {
                model.addAttribute("status", "Eroare critica la salvarea pacientului: " + e.getMessage());
                return "index";
            }

            AppointmentFactory factory = getFactoryForSpecialization(specialization);

            Appointment appointment = factory.createAppointment();
            appointment.setDoctor(doctor);
            appointment.setPatient(patient);

            LocalDateTime appointmentDateTime = LocalDateTime.parse(date + "T" + time + ":00");
            appointment.setAppointmentDate(appointmentDateTime);


            appointmentManagerService.processAndSaveAppointment(appointment, patientStatus);

            model.addAttribute("status", "Programarea a fost salvata cu succes pentru pacientul " +
                    firstName + " " + lastName + " (Pret calculat: " + appointment.getPrice() + " RON)!");


            String doctorFullName = (doctor.getFirstName() != null && doctor.getLastName() != null)
                    ? (doctor.getFirstName() + " " + doctor.getLastName())
                    : "medicul selectat";
            model.addAttribute("appointmentSuccess", true);
            model.addAttribute("doctorName", doctorFullName);
        } else {
            model.addAttribute("status", "Eroare: Medicul selectat nu a fost gasit!");
        }

        return "index";
    }

    @GetMapping("/medics")
    public String showAgenda(Model model) {
        model.addAttribute("listaProgramari", appointmentRepository.findAll());
        return "appointments";
    }

}
