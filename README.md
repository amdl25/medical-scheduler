# MediSched – Medical Appointment Scheduler

Spring Boot web application for booking and managing medical appointments, designed around classic design patterns. Patients book appointments through a web form, and doctors have an agenda view. Each specialty comes with its own medical protocol, and doctors are notified by email when an appointment is created.

## Features
- Appointment booking, listing and editing through Thymeleaf pages
- Specialties: cardiology, dermatology, neurology, orthopedics, pediatrics and general medicine
- Specialty-specific medical protocols with patient instructions
- Price calculation with interchangeable pricing strategies: standard, student, insurance and emergency
- Email notification to the doctor when an appointment is created
- Doctor agenda and sample data created on first start
- Input validation and logging

## Design patterns

| Pattern | Where |
|---|---|
| Factory | `services/factory` creates the appointment and protocol for each specialty (`CardiologyFactory`, `DermatologyFactory`, ...) |
| Strategy | `services/strategy` provides the pricing rules (`StandardPricing`, `StudentPricing`, `InsurancePricing`, `EmergencyPricing`) |
| Observer | `services/observer`: doctors implement `AppointmentObserver` and are notified through `AppointmentSubject` |
| Singleton | `config/ClinicLogger` keeps a single shared logger for the clinic |

## Tech stack
Java 21, Spring Boot 3.2 (Web MVC, Data JPA, Mail), Thymeleaf, Hibernate, H2 database (file mode), Maven.

## Project structure

```
medical-scheduler/                  Maven project
  src/main/java/com/medisched/
    controllers/                    MVC controller
    model/entities/                 Appointment, Doctor, Patient, UserProfile
    model/protocols/                medical protocols per specialty
    repositories/                   Spring Data JPA repositories
    services/                       factory, strategy, observer, email, appointment manager
    config/                         sample data initializer and logging
    util/                           validation helpers
  src/main/resources/
    templates/                      Thymeleaf pages
    application.properties
```

## Getting started

### Prerequisites
- JDK 21

### Run

```bash
cd medical-scheduler
./mvnw spring-boot:run        # on Windows: mvnw.cmd spring-boot:run
```

Open `http://localhost:8080`. The H2 database is created in `./data/medicaldb` on first start, and the H2 console is available at `http://localhost:8080/admin/db`.

### Email notifications
Configure `spring.mail.username` and `spring.mail.password` in `src/main/resources/application.properties` with your SMTP account (Gmail requires an app password). Without valid credentials the emails cannot be sent.

## Notes
- The UI is in Romanian.
