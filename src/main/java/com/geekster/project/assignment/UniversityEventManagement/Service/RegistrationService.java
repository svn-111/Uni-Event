package com.geekster.project.assignment.UniversityEventManagement.Service;

import com.geekster.project.assignment.UniversityEventManagement.Model.Event;
import com.geekster.project.assignment.UniversityEventManagement.Model.Registration;
import com.geekster.project.assignment.UniversityEventManagement.Model.Student;
import com.geekster.project.assignment.UniversityEventManagement.Repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationService {
    @Autowired
    private RegistrationRepository registrationRepository;

    public boolean registerStudentForEvent(Student student, Event event) {
        if (registrationRepository.existsByStudentAndEvent(student, event)) {
            return false; // already registered
        }
        long count = registrationRepository.countByEvent(event);
        if (event.getCapacity() != null && count >= event.getCapacity()) {
            return false; // event full
        }
        Registration reg = new Registration();
        reg.setStudent(student);
        reg.setEvent(event);
        reg.setRegistrationTime(LocalDateTime.now());
        registrationRepository.save(reg);
        return true;
    }

    public List<Registration> getRegistrationsForEvent(Event event) {
        return registrationRepository.findByEvent(event);
    }

    public List<Registration> getRegistrationsForStudent(Student student) {
        return registrationRepository.findByStudent(student);
    }

    public long getRegistrationCountForEvent(Event event) {
        return registrationRepository.countByEvent(event);
    }
} 