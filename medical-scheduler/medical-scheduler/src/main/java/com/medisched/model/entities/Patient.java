package com.medisched.model.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Patient extends UserProfile {

    private Integer age;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }
}