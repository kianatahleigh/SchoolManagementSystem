package services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sba.sms.models.Student;
import sba.sms.services.StudentService;
import sba.sms.utils.CommandLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


    @FieldDefaults(level = AccessLevel.PRIVATE)
    public class StudentServiceTest {

        static StudentService studentService;

        @BeforeAll
        public static void setUp() {
            studentService = new StudentService();
        }

        @Test
        public void testCreateStudent() {
            Student student = new Student();
            student.setEmail("test@example.com");
            student.setName("Test Student");
            student.setPassword("password");

            studentService.createStudent(student);

            Student retrievedStudent = studentService.getStudentByEmail("test@example.com");
            assertThat(retrievedStudent).isNotNull();
            assertThat(retrievedStudent.getName()).isEqualTo("Test Student");
        }
    }

