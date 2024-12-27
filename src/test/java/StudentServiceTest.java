import com.tp.student.model.Student;
import com.tp.student.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentServiceTest {
    private StudentService service;

    @BeforeEach
    public void setUp() {
        service = new StudentService();
    }

    @Test
    public void testAddStudent() {
        Student student = new Student(1, "Alice", 20, "Mathematics");
        service.addStudent(student);
        List<Student> students = service.getAllStudents();
        assertEquals(1, students.size());
        assertEquals("Alice", students.get(0).getName());
    }

    @Test
    public void testFindStudentById() {
        Student student = new Student(2, "Bob", 22, "Physics");
        service.addStudent(student);
        Student result = service.findStudentById(2);
        assertNotNull(result);
        assertEquals("Bob", result.getName());
    }

    @Test
    public void testUpdateStudent() {
        service.addStudent(new Student(3, "Charlie", 21, "Computer Science"));
        service.updateStudent(3, "Charlie Updated", 22, "Data Science");
        Student updatedStudent = service.findStudentById(3);
        assertNotNull(updatedStudent);
        assertEquals("Charlie Updated", updatedStudent.getName());
        assertEquals(22, updatedStudent.getAge());
        assertEquals("Data Science", updatedStudent.getCourse());
    }

    @Test
    public void testDeleteStudent() {
        service.addStudent(new Student(4, "David", 23, "Engineering"));
        service.deleteStudent(4);
        assertNull(service.findStudentById(4));
    }
}
