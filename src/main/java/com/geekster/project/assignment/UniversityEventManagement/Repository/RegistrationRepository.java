package com.geekster.project.assignment.UniversityEventManagement.Repository;

import com.geekster.project.assignment.UniversityEventManagement.Model.Registration;
import com.geekster.project.assignment.UniversityEventManagement.Model.Event;
import com.geekster.project.assignment.UniversityEventManagement.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByEvent(Event event);
    List<Registration> findByStudent(Student student);
    boolean existsByStudentAndEvent(Student student, Event event);
    long countByEvent(Event event);
} 