package com.huuphucdang.fashionshop;

import com.huuphucdang.fashionshop.model.payload.request.RegisterRequest;
import com.huuphucdang.fashionshop.repository.UserRepository;
import com.huuphucdang.fashionshop.service.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Bean;

import static com.huuphucdang.fashionshop.model.entity.Role.ADMIN;
import static com.huuphucdang.fashionshop.model.entity.Role.MANAGER;

@SpringBootApplication
public class FashionShopApplication {

    public FashionShopApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(FashionShopApplication.class, args);
	}
	private final UserRepository userRepository;
	@Bean
	public CommandLineRunner commandLineRunner(AuthenticationService service){


		return args -> {
			var admin = RegisterRequest.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email("admin@gmail.com")
					.password("password")
					.role(ADMIN)
					.build();
			var adminAccount = userRepository.findByEmail(admin.getEmail());
			if(adminAccount.isEmpty()){
				System.out.println("Admin token: " + service.register(admin).getAccessToken());
			}

			var manager = RegisterRequest.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email("manager@gmail.com")
					.password("password")
					.role(MANAGER)
					.build();
			var managerAccount = userRepository.findByEmail(manager.getEmail());
			if(managerAccount.isEmpty()){
				System.out.println("Manager token: " + service.register(manager).getAccessToken());
			}
		};


	}
}
