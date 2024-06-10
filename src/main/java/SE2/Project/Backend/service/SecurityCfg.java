package SE2.Project.Backend.service;

//import SE2.Project.Backend.model.User;

import SE2.Project.Backend.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.logging.Logger;

@Configuration
@EnableWebSecurity
public class SecurityCfg {

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());
        http.csrf(AbstractHttpConfigurer::disable)
                //.userDetailsService(customUserDetailsService)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()
                                .requestMatchers("/admin/**").hasAuthority("Admin")
                                .requestMatchers("/teacher/**").hasAuthority("Teacher")
                                .requestMatchers("/student/**").hasAuthority("Student")
                                .requestMatchers("/login*").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {
                            HttpSession session = request.getSession();
                            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                            session.setAttribute("userDetails", userDetails);
                            //potential risk
                            boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(grantedAuthority -> "Admin".equalsIgnoreCase(grantedAuthority.getAuthority()));
                            boolean isStudent = authentication.getAuthorities().stream()
                                    .anyMatch(grantedAuthority -> "Student".equalsIgnoreCase(grantedAuthority.getAuthority()));
                            boolean isTeacher = authentication.getAuthorities().stream()
                                    .anyMatch(grantedAuthority -> "Teacher".equalsIgnoreCase(grantedAuthority.getAuthority()));
                            if (isAdmin) {
                                response.sendRedirect("/admin/details");
                            } else {
                                if (isTeacher) {
                                    response.sendRedirect("/teacher/details");//modify this
                                } else if (isStudent) {
                                    response.sendRedirect("/student/details");//modify this
                                }

                            }
                        })

                        .failureUrl("/login?error=true")
                )
                .logout(logout -> logout.logoutSuccessUrl("/").permitAll()
                );
        return http.build();
    }

    private LogoutSuccessHandler logoutSuccessHandler() {
        SimpleUrlLogoutSuccessHandler logoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();
        logoutSuccessHandler.setDefaultTargetUrl("/login");
        return logoutSuccessHandler;
    }


}