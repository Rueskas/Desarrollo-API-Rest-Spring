package com.example.demo;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner lombokTest() {
		return args->{
			Persona p1 = new Persona();
			p1.setNombre("Sergio");
			p1.setApellidos("Rueskas");
			p1.setEdad(20);
			p1.setFechaNacimiento(LocalDate.of(2000, 02, 12));
			Persona p2 = new Persona("Sergio", "Rueskas", 20, LocalDate.of(2000, 02, 12));
			
			System.out.println(p1);
			System.out.println(p2);
		};
	}
}
