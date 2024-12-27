package com.tp.student.service;

import com.tp.student.model.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentService {

    private List<Student> students;

    public StudentService() {
        students = new ArrayList<>();
    }
    public void addStudent(Student student) {
        students.add(student);
    }
    public void updateStudent(int id, String newName, int newAge, String newCourse) {
        for (Student student : students) {
            if (student.getId() == id) {
                student.setName(newName);
                student.setAge(newAge);
                student.setCourse(newCourse);
                break;
            }
        }
    }
    public Student findStudentById(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }
    public void deleteStudent(int id) {
        students.removeIf(student -> student.getId() == id);
    }

    public List<Student> getAllStudents() {
        return students;
    }
}
