package SE2.Project.Backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.validator.constraints.Length;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
public class User {

    // Field in database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long userID;

    @Length(min = 3, max = 20)
    @NotEmpty(message = "username cannot be empty")
    @Column(nullable = false)
    private String username;

    @Length(min = 3, max = 255)
    @NotEmpty(message = "password cannot be empty")
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = false)
    private String userFullname;

    private String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotEmpty(message = "email cannot be empty")
    private String email;

    @NotEmpty(message = "phone number cannot be empty")
    @Length(min = 0, max = 12)
    private String phoneNumber;

    @NotEmpty(message = "role cannot be empty")
    private String role;

    @Length(min = 3, max = 100)
    @NotEmpty(message = "address cannot be empty")
    private String address;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long id) {
        this.userID = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}