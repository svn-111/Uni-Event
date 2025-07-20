package com.geekster.project.assignment.UniversityEventManagement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.geekster.project.assignment.UniversityEventManagement.Model.Event;
import com.geekster.project.assignment.UniversityEventManagement.Model.Student;
import com.geekster.project.assignment.UniversityEventManagement.Service.EventService;
import com.geekster.project.assignment.UniversityEventManagement.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;

@Component
public class DataLoader {
    @Autowired
    private StudentService studentService;
    @Autowired
    private EventService eventService;

    @PostConstruct
    public void loadData() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            // Load students
            InputStream studentsStream = new ClassPathResource("students.json").getInputStream();
            List<Student> students = mapper.readValue(studentsStream, new TypeReference<List<Student>>(){});
            for (Student s : students) {
                try { studentService.addAStudent(s); } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            System.err.println("Failed to load students.json: " + e.getMessage());
        }
        try {
            // Load events
            InputStream eventsStream = new ClassPathResource("events.json").getInputStream();
            List<Event> events = mapper.readValue(eventsStream, new TypeReference<List<Event>>(){});
            for (Event e : events) {
                try { eventService.addAEvent(e); } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            System.err.println("Failed to load events.json: " + e.getMessage());
        }
    }
} 