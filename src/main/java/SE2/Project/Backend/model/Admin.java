package SE2.Project.Backend.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "admin")
public class Admin {
    // Backgound Information
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminCode;

    @NotNull
    @OneToOne
    @JoinColumn(name = "userID", referencedColumnName = "userID")
    private User user;

    public Integer getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(Integer adminCode) {
        this.adminCode = adminCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}