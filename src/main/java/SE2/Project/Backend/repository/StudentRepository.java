package SE2.Project.Backend.repository;

import SE2.Project.Backend.model.Student;
import SE2.Project.Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.tags.form.SelectTag;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // We can customize @SQL statement to check the condition with database
    // This is JPA Query by Name Convention
    // boolean existsByUsername(String username);
    // boolean existsByEmail(String email);
    // boolean existsByStudentCode(String studentCode);

    // Define a custom query method to find a Student by userID
    @Query("SELECT s FROM Student s WHERE s.user.userID = :userID")
    Student findByUserId(@Param("userID") Long userID);

    @Query("SELECT s FROM Student s WHERE s.studentCode = :studentCode")
    Student findByStudentCode(@Param("studentCode") Integer studentCode);

    @Query("DELETE FROM Student s WHERE s.studentCode = :studentCode")
    void deleteByStudentCode(@Param("studentCode") Integer studentCode);

    Student findByUser(User user);
}