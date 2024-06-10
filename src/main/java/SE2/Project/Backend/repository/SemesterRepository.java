package SE2.Project.Backend.repository;

import SE2.Project.Backend.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
    Semester findBySemesterId(Long semesterId);
    boolean existsByStartYearAndEndYearAndSemesterNum(Integer startYear, Integer endYear, Integer semesterNum);
}
