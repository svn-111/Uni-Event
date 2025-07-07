package com.geekster.project.assignment.UniversityEventManagement.Controller;

import com.geekster.project.assignment.UniversityEventManagement.Model.Department;
import com.geekster.project.assignment.UniversityEventManagement.Model.Event;
import com.geekster.project.assignment.UniversityEventManagement.Model.Student;
import com.geekster.project.assignment.UniversityEventManagement.Service.EventService;
import com.geekster.project.assignment.UniversityEventManagement.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class WebController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private EventService eventService;

    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }

    @GetMapping("/students")
    public String studentsPage(Model model) {
        Iterable<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        model.addAttribute("departments", Department.values());
        model.addAttribute("newStudent", new Student());
        return "students";
    }

    @GetMapping("/events")
    public String eventsPage(Model model) {
        Iterable<Event> events = eventService.getAllEvents();
        model.addAttribute("events", events);
        model.addAttribute("newEvent", new Event());
        return "events";
    }

    @PostMapping("/students/add")
    public String addStudent(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        try {
            String result = studentService.addAStudent(student);
            redirectAttributes.addFlashAttribute("successMessage", result);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding student: " + e.getMessage());
        }
        return "redirect:/students";
    }

    @PostMapping("/events/add")
    public String addEvent(@ModelAttribute Event event, RedirectAttributes redirectAttributes) {
        try {
            String result = eventService.addAEvent(event);
            redirectAttributes.addFlashAttribute("successMessage", result);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding event: " + e.getMessage());
        }
        return "redirect:/events";
    }

    @PostMapping("/students/update-department")
    public String updateStudentDepartment(@RequestParam Integer studentId, 
                                        @RequestParam Department department, 
                                        RedirectAttributes redirectAttributes) {
        try {
            String result = studentService.updateStudentDepartment(studentId, department);
            redirectAttributes.addFlashAttribute("successMessage", result);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating student: " + e.getMessage());
        }
        return "redirect:/students";
    }

    @PostMapping("/events/update-location")
    public String updateEventLocation(@RequestParam Integer eventId, 
                                    @RequestParam String location, 
                                    RedirectAttributes redirectAttributes) {
        try {
            String result = eventService.updateEventLocationById(eventId, location);
            redirectAttributes.addFlashAttribute("successMessage", result);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating event: " + e.getMessage());
        }
        return "redirect:/events";
    }

    @PostMapping("/students/delete")
    public String deleteStudent(@RequestParam Integer studentId, RedirectAttributes redirectAttributes) {
        try {
            String result = studentService.removeStudentById(studentId);
            redirectAttributes.addFlashAttribute("successMessage", result);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting student: " + e.getMessage());
        }
        return "redirect:/students";
    }

    @PostMapping("/events/delete")
    public String deleteEvent(@RequestParam Integer eventId, RedirectAttributes redirectAttributes) {
        try {
            String result = eventService.removeEventById(eventId);
            redirectAttributes.addFlashAttribute("successMessage", result);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting event: " + e.getMessage());
        }
        return "redirect:/events";
    }

    @PostMapping("/students/update")
    public String updateStudent(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        try {
            // Use the service to update all fields
            Optional<Student> existingOpt = studentService.getStudentById(student.getStudentId());
            if (existingOpt.isPresent()) {
                Student existing = existingOpt.get();
                existing.setFirstName(student.getFirstName());
                existing.setLastName(student.getLastName());
                existing.setAge(student.getAge());
                existing.setStudentDepartment(student.getStudentDepartment());
                studentService.addAStudent(existing); // save updated student
                redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Student not found.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating student: " + e.getMessage());
        }
        return "redirect:/students";
    }

    @PostMapping("/events/update")
    public String updateEvent(@ModelAttribute Event event, RedirectAttributes redirectAttributes) {
        try {
            Optional<Event> existingOpt = eventService.getEventById(event.getEventId());
            if (existingOpt.isPresent()) {
                Event existing = existingOpt.get();
                existing.setEventName(event.getEventName());
                existing.setLocationOfEvent(event.getLocationOfEvent());
                existing.setEventDate(event.getEventDate());
                existing.setStartTime(event.getStartTime());
                existing.setEndTime(event.getEndTime());
                eventService.addAEvent(existing); // save updated event
                redirectAttributes.addFlashAttribute("successMessage", "Event updated successfully.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Event not found.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating event: " + e.getMessage());
        }
        return "redirect:/events";
    }

    @GetMapping("/events/by-date")
    public String getEventsByDate(@RequestParam LocalDate date, Model model) {
        try {
            Iterable<Event> events = eventService.getEventsOnSameDate(date);
            model.addAttribute("events", events);
            model.addAttribute("selectedDate", date);
            model.addAttribute("newEvent", new Event());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error fetching events: " + e.getMessage());
        }
        return "events";
    }
} 