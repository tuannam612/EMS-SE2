package SE2.Project.Backend.repository;

import SE2.Project.Backend.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Classroom findByClassroomId(Long classroomId);
    boolean existsByRoomNumber(String roomNumber);
}
