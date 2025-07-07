package com.geekster.project.assignment.UniversityEventManagement.Repository;

import com.geekster.project.assignment.UniversityEventManagement.Model.Student;
import com.geekster.project.assignment.UniversityEventManagement.Model.Department;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface IStudentRepo extends CrudRepository<Student, Integer> {
    List<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    List<Student> findByStudentDepartment(Department department);
    List<Student> findByStudentDepartmentAndFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(Department department, String firstName, String lastName);
    List<Student> findAll(Sort sort);
}
