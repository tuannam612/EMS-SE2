package SE2.Project.Backend.repository;

import SE2.Project.Backend.model.Major;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorRepository extends JpaRepository<Major, Long> {
    Major findByMajorId(Long majorId);
    boolean existsByMajorName(String majorName);
}
