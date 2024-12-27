package com.tp.student;

import com.tp.student.model.Student;
import com.tp.student.service.StudentService;

public class Main {
    public static void main(String[] args) {
        StudentService service = new StudentService();

        service.addStudent(new Student(1, "Khalil", 22, "Computer Science"));
        service.addStudent(new Student(2, "Reda", 23, "Information Management"));
        service.addStudent(new Student(3, "Amine", 24, "Electrical Engineering"));
        service.addStudent(new Student(4, "Sara", 21, "Data Science"));

        service.updateStudent(1, "Khalil Abbaoui", 23, "Software Engineering");

        System.out.println("All Students:");
        service.getAllStudents().forEach(s -> System.out.println(s.getName()));
        service.deleteStudent(2);

        System.out.println("All Students after deletion:");
        service.getAllStudents().forEach(s -> System.out.println(s.getName()));
    }
}
