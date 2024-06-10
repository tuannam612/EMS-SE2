package SE2.Project.Backend.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "student")
public class Student {
    // Backgound Information

    @Id
    @Column(name = "student_code")
    private Integer studentCode;

    @Column(name = "class_belong", nullable = false)
    private String classBelong;

    @Column(name = "training_system")
    private String trainingSystem; // Bachelor, Master, Part-Time

    @Valid
    @OneToOne
    @JoinColumn(name = "userID", referencedColumnName = "userID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "major_id", referencedColumnName = "major_id")
    private Major major;

    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(Set<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    @OneToMany(mappedBy = "student")
    private Set<Enrollment> enrollments = new HashSet<>();

    public Integer getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(Integer studentCode) {
        this.studentCode = studentCode;
    }

    public String getClassBelong() {
        return classBelong;
    }

    public void setClassBelong(String classBelong) {
        this.classBelong = classBelong;
    }

    public String getTrainingSystem() {
        return trainingSystem;
    }

    public void setTrainingSystem(String trainingSystem) {
        this.trainingSystem = trainingSystem;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }


}