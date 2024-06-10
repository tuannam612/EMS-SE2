package SE2.Project.Backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "enrollment")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Long enrollmentId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "student_code", referencedColumnName = "student_code")
    private Student student;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "semester_id", referencedColumnName = "semester_id")
    private Semester semester;


    @NotNull
    @Column(name = "attendance")
    private double attendance;

    @NotNull
    @Column(name = "midterm_grade")
    private double midtermGrade;

    @NotNull
    @Column(name = "final_grade")
    private double finalGrade;

    @NotNull
    @Column(name = "total_grade")
    private double totalGrade;


    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public double getAttendance() {
        return attendance;
    }

    public void setAttendance(double attendance) {
        this.attendance = attendance;
    }

    public double getMidtermGrade() {
        return midtermGrade;
    }

    public void setMidtermGrade(double midtermGrade) {
        this.midtermGrade = midtermGrade;
    }

    public double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(double finalGrade) {
        this.finalGrade = finalGrade;
    }

    public double getTotalGrade(){
        totalGrade = attendance*0.1 + midtermGrade*0.3 + finalGrade*0.6;
        return totalGrade;
    }


}