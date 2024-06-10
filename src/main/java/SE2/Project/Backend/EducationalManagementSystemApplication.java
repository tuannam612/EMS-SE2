package SE2.Project.Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EducationalManagementSystemApplication {

	public static void main(String[] args) {

		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String yourPassword = "123456";//dadsafds
			String rawPassword = yourPassword;
		String encodedPassword = passwordEncoder.encode(rawPassword);
		System.out.println("Encoded password: " + encodedPassword);
		SpringApplication.run(EducationalManagementSystemApplication.class, args);
	}
}
