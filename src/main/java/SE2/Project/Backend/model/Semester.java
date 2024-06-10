package SE2.Project.Backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "semester")
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semester_id")
    private Long semesterId;

    @Column(name = "start_year")
    @NotNull
    private Integer startYear;

    @NotNull
    @Column(name = "end_year")
    private Integer endYear;

    @NotNull
    @Column(name = "semester_num")
    private Integer semesterNum;

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public Integer getSemesterNum() {
        return semesterNum;
    }

    public void setSemesterNum(Integer semesterNum) {
        this.semesterNum = semesterNum;
    }
}