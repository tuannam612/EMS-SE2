package SE2.Project.Backend.repository;

import SE2.Project.Backend.model.Classroom;
import SE2.Project.Backend.model.Course;
import SE2.Project.Backend.model.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    Timetable findByTimetableId(Long timetableId);
    Timetable findByCourseCourseId(Long courseId);
    boolean existsByCourseCourseId(Long courseId);

    Timetable findByCourse(Course course);

}