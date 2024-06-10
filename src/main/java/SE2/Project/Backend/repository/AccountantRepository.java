package SE2.Project.Backend.repository;

import SE2.Project.Backend.model.Accountant;
import SE2.Project.Backend.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountantRepository extends JpaRepository<Accountant, Long> {
    Accountant findByAccountantCode(Integer accountantCode);
}