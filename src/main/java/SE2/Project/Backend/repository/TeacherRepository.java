package SE2.Project.Backend.repository;

import SE2.Project.Backend.model.Course;
import SE2.Project.Backend.model.Student;
import SE2.Project.Backend.model.Teacher;
import SE2.Project.Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Teacher findByTeacherCode(Integer teacherCode);
    Teacher findByUser(User user);

    @Query("SELECT s FROM Teacher s WHERE s.user.userID = :userID")
    Teacher findByUserId(@Param("userID") Long userID);
}
