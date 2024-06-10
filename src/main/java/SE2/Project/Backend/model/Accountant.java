package SE2.Project.Backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.boot.model.naming.Identifier;

import java.util.List;

@Entity
@Table(name = "accountant")
public class Accountant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountantCode;

    @NotNull
    @OneToOne
    @JoinColumn(name = "userID", referencedColumnName = "userID")
    private User user;

    public Integer getAccountantCode() {
        return accountantCode;
    }

    public void setAccountantCode(Integer accountantCode) {
        this.accountantCode = accountantCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
