package com.example.whitefox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import jakarta.persistence.EntityManagerFactory;

@SpringBootApplication
@EnableCaching
public class WhitefoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhitefoxApplication.class, args);
	}

	@Bean
	@Primary
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	@Bean
	public CommandLineRunner dropConstraint(JdbcTemplate jdbcTemplate) {
		return args -> {
			try {
				jdbcTemplate.execute("ALTER TABLE garments DROP CONSTRAINT IF EXISTS garments_status_check");
				jdbcTemplate.execute("ALTER TABLE garment_tracking_history DROP CONSTRAINT IF EXISTS garment_tracking_history_status_check");
				System.out.println("Dropped constraints successfully.");
			} catch (Exception e) {
				System.out.println("Constraints might not exist or could not be dropped: " + e.getMessage());
			}
		};
	}
}