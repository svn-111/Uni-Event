package com.geekster.project.assignment.UniversityEventManagement.Controller;

import com.geekster.project.assignment.UniversityEventManagement.Model.Department;
import com.geekster.project.assignment.UniversityEventManagement.Model.Event;
import com.geekster.project.assignment.UniversityEventManagement.Model.Student;
import com.geekster.project.assignment.UniversityEventManagement.Model.User;
import com.geekster.project.assignment.UniversityEventManagement.Service.EventService;
import com.geekster.project.assignment.UniversityEventManagement.Service.StudentService;
import com.geekster.project.assignment.UniversityEventManagement.Service.RegistrationService;
import com.geekster.project.assignment.UniversityEventManagement.Service.UserService;
import com.geekster.project.assignment.UniversityEventManagement.Model.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@Controller
public class WebController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private EventService eventService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute("user") User user, Model model) {
        boolean registered = userService.registerUser(user);
        if (registered) {
            return "redirect:/login?signup";
        } else {
            model.addAttribute("error", "Username or email already exists");
            return "signup";
        }
    }

    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return "redirect:/login";
        User user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("user") User formUser, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) return "redirect:/login";
        User user = userService.findByUsername(userDetails.getUsername());
        boolean updated = userService.updateProfile(user.getId(), formUser.getFirstName(), formUser.getLastName(), formUser.getEmail(), formUser.getPassword());
        if (updated) {
            model.addAttribute("success", "Profile updated successfully.");
        } else {
            model.addAttribute("error", "Failed to update profile.");
        }
        // Reload user data
        user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/students")
    public String studentsPage(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "department", required = false) Department department,
            @RequestParam(value = "sort", required = false, defaultValue = "universityId") String sort,
            @RequestParam(value = "dir", required = false, defaultValue = "asc") String dir,
            Model model) {
        List<Student> students;
        boolean asc = dir.equalsIgnoreCase("asc");
        if (query != null && !query.isEmpty() && department != null) {
            students = studentService.searchAndFilter(department, query);
        } else if (query != null && !query.isEmpty()) {
            students = studentService.searchStudents(query);
        } else if (department != null) {
            students = studentService.filterByDepartment(department);
        } else {
            students = studentService.getAllStudentsSorted(sort, asc);
        }
        model.addAttribute("students", students);
        model.addAttribute("departments", Department.values());
        model.addAttribute("newStudent", new Student());
        model.addAttribute("query", query);
        model.addAttribute("selectedDepartment", department);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        return "students";
    }

    @GetMapping("/events")
    public String eventsPage(Model model) {
        Iterable<Event> events = eventService.getAllEvents();
        Iterable<Student> students = studentService.getAllStudents();
        model.addAttribute("events", events);
        model.addAttribute("students", students);
        model.addAttribute("newEvent", new Event());
        // For each event, add attendee list and available seats
        Map<Integer, List<Student>> eventAttendees = new HashMap<>();
        Map<Integer, Long> eventRegistrationCounts = new HashMap<>();
        for (Event event : events) {
            List<Registration> regs = registrationService.getRegistrationsForEvent(event);
            List<Student> attendees = regs.stream().map(Registration::getStudent).toList();
            eventAttendees.put(event.getEventId(), attendees);
            eventRegistrationCounts.put(event.getEventId(), registrationService.getRegistrationCountForEvent(event));
        }
        model.addAttribute("eventAttendees", eventAttendees);
        model.addAttribute("eventRegistrationCounts", eventRegistrationCounts);
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
    public String updateStudentDepartment(@RequestParam Integer universityId, 
                                        @RequestParam Department department, 
                                        RedirectAttributes redirectAttributes) {
        try {
            String result = studentService.updateStudentDepartment(universityId, department);
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
    public String deleteStudent(@RequestParam Integer universityId, RedirectAttributes redirectAttributes) {
        try {
            String result = studentService.removeStudentById(universityId);
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
            Optional<Student> existingOpt = studentService.getStudentById(student.getUniversityId());
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

    @PostMapping("/events/register")
    public String registerStudentForEvent(@RequestParam Integer universityId, @RequestParam Integer eventId, RedirectAttributes redirectAttributes) {
        Optional<Student> studentOpt = studentService.getStudentById(universityId);
        Optional<Event> eventOpt = eventService.getEventById(eventId);
        if (studentOpt.isPresent() && eventOpt.isPresent()) {
            boolean success = registrationService.registerStudentForEvent(studentOpt.get(), eventOpt.get());
            if (success) {
                redirectAttributes.addFlashAttribute("successMessage", "Student registered for event successfully.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Registration failed: already registered or event is full.");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid student or event.");
        }
        return "redirect:/events";
    }

    @PostMapping("/events/unregister")
    public String unregisterStudentFromEvent(@RequestParam Integer universityId, @RequestParam Integer eventId, RedirectAttributes redirectAttributes) {
        Optional<Student> studentOpt = studentService.getStudentById(universityId);
        Optional<Event> eventOpt = eventService.getEventById(eventId);
        if (studentOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Student not found: " + universityId);
            return "redirect:/events";
        }
        if (eventOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Event not found: " + eventId);
            return "redirect:/events";
        }
        try {
            boolean deleted = registrationService.unregisterStudentFromEvent(studentOpt.get(), eventOpt.get());
            if (deleted) {
                redirectAttributes.addFlashAttribute("successMessage", "Attendee removed from event.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Attendee registration not found.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error removing attendee: " + e.getMessage());
        }
        return "redirect:/events";
    }

    @GetMapping("/events/by-date")
    public String getEventsByDate(@RequestParam(required = false) String date, Model model) {
        try {
            if (date == null || date.isBlank()) {
                model.addAttribute("errorMessage", "No date provided.");
                return "events";
            }
            java.time.LocalDate parsedDate = java.time.LocalDate.parse(date);
            Iterable<Event> events = eventService.getEventsOnSameDate(parsedDate);
            model.addAttribute("events", events);
            model.addAttribute("selectedDate", parsedDate);
            model.addAttribute("newEvent", new Event());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error fetching events: " + e.getMessage());
        }
        return "events";
    }
} 