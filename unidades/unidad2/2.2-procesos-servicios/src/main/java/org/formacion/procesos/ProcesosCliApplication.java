package org.formacion.procesos;

import org.formacion.procesos.controllers.CliController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProcesosCliApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProcesosCliApplication.class, args);
    }

    @Bean
    CommandLineRunner demo(CliController procesos) {
        return args -> {
            System.out.println("Iniciando proceso al arrancar la aplicación...");

            procesos.menuConsola();

            System.out.println("Proceso finalizado.");
        };
    }
}