package sba.sms.dao;

import sba.sms.models.Student;
import sba.sms.models.Course;

import java.util.List;

public interface StudentI {


    void createStudent(Student student);

   List<Student> getAllStudents();

   Student getStudentByEmail(String email);

   boolean validateStudent(String email, String password);


   void registerStudentToCourse(String email, int courseId);


   List<Course> getStudentCourses(String email);




}
