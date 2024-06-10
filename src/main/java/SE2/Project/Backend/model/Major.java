package SE2.Project.Backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "major")
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "major_id")
    private Long majorId;

    @Column(name = "major_name", nullable = false)
    @NotEmpty(message = "Major Name cannot be empty")
    private String majorName;

    @NotNull
    @Column(name = "tuition_based_course", nullable = false)
    private Double tuitionBasedCourse;

    @NotNull
    @Column(name = "tuition_specialized_course", nullable = false)
    private Double tuitionSpecializedCourse;

    public Long getMajorId() {
        return majorId;
    }

    public void setMajorId(Long majorId) {
        this.majorId = majorId;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public Double getTuitionBasedCourse() {
        return tuitionBasedCourse;
    }

    public void setTuitionBasedCourse(Double tuitionBasedCourse) {
        this.tuitionBasedCourse = tuitionBasedCourse;
    }

    public Double getTuitionSpecializedCourse() {
        return tuitionSpecializedCourse;
    }


    public void setTuitionSpecializedCourse(Double tuitionSpecializedCourse) {
        this.tuitionSpecializedCourse = tuitionSpecializedCourse;
    }
}