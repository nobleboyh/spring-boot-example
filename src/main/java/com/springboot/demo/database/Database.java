package com.springboot.demo.database;

import com.springboot.demo.models.Product;
import com.springboot.demo.repositories.ProductRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Database {
    @Bean
    CommandLineRunner initDB(ProductRepo productRepo){
        return  new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                Product product1 = new Product("Hoang", 1200, 2400, "");
//                Product product2 =  new Product( "123", 1234, 2500, "123");
//                System.out.println("Insert data" + productRepo.save(product1));
//                System.out.println("Insert data" + productRepo.save(product2));
            }
        };
    }
}
