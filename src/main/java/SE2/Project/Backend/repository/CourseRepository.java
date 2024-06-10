package SE2.Project.Backend.repository;

import SE2.Project.Backend.model.Course;
import SE2.Project.Backend.model.Teacher;
import SE2.Project.Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByCourseId(Long courseId);

    boolean existsByCourseCodeAndCourseName(String courseCode, String courseName);
    List<Course> findBySemesterSemesterId(Long semesterId);
    List<Course> findAllByCapacityGreaterThan(int i);
    List<Course> findByTeacher(Teacher teacher);
}
