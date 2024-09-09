package sba.sms.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "courses")




public class Student {

    @Id
    @Column(name = "Email", length = 50, nullable = false)
    private String email;

    @Column(name = "Name", length = 50, nullable = false)
    private String name;

    @Column(name = "Password", length = 50, nullable = false)
    private String password;

  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST})
  @JoinTable(
          name = "student_courses",
          joinColumns = @JoinColumn(name = "student_email"),
          inverseJoinColumns = @JoinColumn(name = "courses_id")
  )

  private Set<Course> courses = new HashSet<>();

  public Student(String email, String name, String password) {
      this.email = email;
      this.name = name;
      this.password = password;
  }


    public void addCourse(Course course) {
        this.courses.add(course);
        course.getStudents().add(this);
    }

    public void removeCourse(Course course) {
        this.courses.remove(course);
        course.getStudents().remove(this);
    }

  @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Student student = (Student) o;
      return email.equals(student.email);
  }

  @Override
    public int hashCode() {
      return Objects.hash(email);
  }
}
