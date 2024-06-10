package SE2.Project.Backend.repository;

import SE2.Project.Backend.model.Course;
import SE2.Project.Backend.model.Enrollment;
import SE2.Project.Backend.model.Student;
import SE2.Project.Backend.model.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    Optional<Enrollment> findByStudentAndCourse(Student student, Course course);
    List<Enrollment> findByStudentAndCourseCourseName(Student student, String courseName);
    Enrollment findByEnrollmentId(Long enrollmentId);
    boolean existsByStudentAndCourseCourseName(Student student, String courseName);

    List<Enrollment> findByCourseCourseName(String courseName);
    boolean existsByCourseCourseIdAndStudentStudentCode(Long courseId, Integer studentCode);
    List<Enrollment> findAllByStudent(Student student);
    List<Enrollment> findByCourseCourseId(Long courseId);
}