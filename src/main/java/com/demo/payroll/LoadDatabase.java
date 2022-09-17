package com.demo.payroll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository,
                                   OrderRepository orderRepository) {

        return args -> {
            employeeRepository.save(new Employee("Arnout", "Cator", "Consultant"));
            employeeRepository.save(new Employee("Patricia Fatima","Torres Aldunate", "Analyst Consultant"));
            employeeRepository.save(new Employee("Lila Isabel", "Cator Torres", "Analyst"));

            employeeRepository.findAll().forEach(employee -> log.info("Preloaded " + employee));

            orderRepository.save(new Order("MacBook Pro", Status.COMPLETED));
            orderRepository.save(new Order("iPhone", Status.IN_PROGRESS));

            orderRepository.findAll().forEach(order -> {
                log.info("Preloaded " + order);
            });
        };


    }
}
