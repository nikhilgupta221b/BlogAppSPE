package com.example.blogs;

//import com.example.blogs.Repositories.RoleRepository;
import com.example.blogs.config.AppConstants;
import com.example.blogs.entities.Role;
import com.example.blogs.entities.User;
import com.example.blogs.payloads.UserDto;
import com.example.blogs.services.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;



@SpringBootApplication
public class BlogsApplication /*implements CommandLineRunner*/ {
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//
//	@Autowired
//	private RoleRepository roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(BlogsApplication.class, args);
	}
//	@Bean
//	public ModelMapper modelMapper(){
//		return new ModelMapper();
//	}

//	@Override
//	public void run(String... args) throws Exception {
//
//		System.out.println(this.passwordEncoder.encode("xyz"));
//
//		try {
//
//			Role role = new Role();
//			role.setId(AppConstants.ADMIN_USER);
//			role.setName("ROLE_ADMIN");
//
//			Role role1 = new Role();
//			role1.setId(AppConstants.NORMAL_USER);
//			role1.setName("ROLE_NORMAL");
//
//			List<Role> roles = List.of(role, role1);
//
//			List<Role> result = this.roleRepo.saveAll(roles);
//
//			result.forEach(r -> {
//				System.out.println(r.getName());
//			});
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//
//	}
//


	@Component
	class AdminIntiailizer implements CommandLineRunner {
		@Autowired
		private UserService userService;

		@Transactional
		public void run(String... args) throws Exception {

			if(!userService.getAdminList().isEmpty()) return;

			User user = new User();
			user.setEmail("admin@gmail.com");
			user.setPassword("admin");
			user.setRole(Role.ADMIN);
			user.setName("Admin");
			user.setAbout("I am ADMIN");

			userService.addUser(user);
		}
	}
}
