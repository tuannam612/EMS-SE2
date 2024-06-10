package SE2.Project.Backend.service;


import SE2.Project.Backend.model.User;
import SE2.Project.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.logging.Logger;
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger logger = Logger.getLogger(CustomUserDetailsService.class.getName());
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user details for username: " + username);
        User user = userRepo.findByUsername(username);
        if (user == null) {
            logger.info("Loading user details for username: " + username);
            throw new UsernameNotFoundException("User not found");
        }
        logger.info("User details loaded successfully for username: " + username);
        return new CustomUserDetails(user);
    }

}