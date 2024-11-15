package com.aluracursos.screenmatch;

import com.aluracursos.screenmatch.principal.Principal;
import com.aluracursos.screenmatch.repository.LibroRepository;
import com.aluracursos.screenmatch.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	//@Autowired
	//private LibroRepository repository;
	@Autowired
	private AutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(autorRepository);
		principal.muestraElMenu();




	}
}
