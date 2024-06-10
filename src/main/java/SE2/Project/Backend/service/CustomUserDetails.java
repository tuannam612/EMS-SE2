package SE2.Project.Backend.service;

import SE2.Project.Backend.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetails.class);
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override// potential risk
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = user.getRole();
        // Log the role of the user
        if ("Admin".equals(role)) {
            // Log a message only if the user has the "Admin" role
            logger.info("User has Admin role");
        } else if ("Student".equals(role)) {
            logger.info("User has Student role");
        } else if ("Teacher".equals(role)) {
            logger.info("User has Teacher role");
        } else {
            logger.info("User has no role");
        }
        // Create a GrantedAuthority object based on the user's role
        GrantedAuthority authority = new SimpleGrantedAuthority(role);

        // Return a collection containing the single GrantedAuthority object
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        // Return the password of the user
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        // Return the username of the user
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Or implement your logic here
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Or implement your logic here
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Or implement your logic here
    }

    @Override
    public boolean isEnabled() {
        return true; // Or implement your logic here
    }
}