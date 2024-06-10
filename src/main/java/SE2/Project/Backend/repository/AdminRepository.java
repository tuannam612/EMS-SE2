package SE2.Project.Backend.repository;

import SE2.Project.Backend.model.Admin;
import SE2.Project.Backend.model.Teacher;
import SE2.Project.Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByAdminCode(Integer adminCode);
    Admin findByUser (User user);
}