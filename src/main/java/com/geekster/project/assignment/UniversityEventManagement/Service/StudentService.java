package com.geekster.project.assignment.UniversityEventManagement.Service;

import com.geekster.project.assignment.UniversityEventManagement.Model.Department;
import com.geekster.project.assignment.UniversityEventManagement.Model.Student;
import com.geekster.project.assignment.UniversityEventManagement.Repository.IStudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    IStudentRepo studentRepo;

    public String addAStudent(Student s){
        studentRepo.save(s);
        return "A student is added !!!";
    }

    public String addStudents(List<Student> s){
        studentRepo.saveAll(s);
        return "List of students are added !!!";
    }

    public Iterable<Student> getAllStudents(){
        return studentRepo.findAll();
    }

    public Optional<Student> getStudentById(Integer universityId){
        return studentRepo.findById(universityId);
    }

    public  String updateStudentDepartment(Integer universityId, Department department){
        Optional<Student> studentOpt = studentRepo.findById(universityId);
        if(studentOpt.isPresent()){
            Student s = studentOpt.get();
            s.setStudentDepartment(department);
            studentRepo.save(s);
            return "Department updated";
        }
        return "Student not found";
    }

    public String removeStudentById(Integer universityId){
        if(studentRepo.existsById(universityId)){
            studentRepo.deleteById(universityId);
            return "Student removed";
        }
        return "Student not found";
    }

    public List<Student> searchStudents(String query) {
        try {
            Integer id = Integer.parseInt(query);
            Optional<Student> student = studentRepo.findById(id);
            return student.map(List::of).orElse(List.of());
        } catch (NumberFormatException e) {
            return studentRepo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query);
        }
    }
    public List<Student> filterByDepartment(Department department) {
        return studentRepo.findByStudentDepartment(department);
    }
    public List<Student> searchAndFilter(Department department, String query) {
        return studentRepo.findByStudentDepartmentAndFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(department, query, query);
    }
    public List<Student> getAllStudentsSorted(String sortBy, boolean asc) {
        Sort sort = asc ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return studentRepo.findAll(sort);
    }
}
