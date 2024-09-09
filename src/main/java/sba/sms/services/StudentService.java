package sba.sms.services;

import lombok.extern.java.Log;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;


@Log
public class StudentService implements StudentI {

    @Override
    public void createStudent(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(student);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            log.severe("Failed to create student: " + e.getMessage());
        }
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createQuery("FROM Student", Student.class);
            students = query.getResultList();
        } catch (HibernateException e) {
            log.severe("Failed to retrieve students: " + e.getMessage());
        }
        return students;
    }

    @Override
    public Student getStudentByEmail(String email) {
        Student student = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            student = session.get(Student.class, email);
        } catch (HibernateException e) {
            log.severe("Failed to retrieve student: " + e.getMessage());
        }
        return student;
    }

    @Override
    public boolean validateStudent(String email, String password) {
        boolean isValid = false;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createQuery("FROM Student WHERE email = :email AND password = :password", Student.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
            Student student = query.uniqueResult();
            isValid = (student != null);
        } catch (HibernateException e) {
            log.severe("Failed to validate student: " + e.getMessage());
        }
        return isValid;
    }

    @Override
    public void registerStudentToCourse(String email, int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, email);
            Course course = session.get(Course.class, courseId);
            if (student != null && course != null) {
                student.getCourses().add(course);
                session.merge(student);
            }
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            log.severe("Failed to register student to course: " + e.getMessage());
        }
    }

    @Override
    public List<Course> getStudentCourses(String email) {
        List<Course> courses = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NativeQuery<Course> query = session.createNativeQuery(
                    "SELECT c.* FROM Course c JOIN student_courses sc ON c.id = sc.courses_id WHERE sc.student_email = :email",
                    Course.class
            );
            query.setParameter("email", email);
            courses = query.getResultList();
        } catch (HibernateException e) {
            log.severe("Failed to retrieve student courses: " + e.getMessage());
        }
        return courses;
    }
}
