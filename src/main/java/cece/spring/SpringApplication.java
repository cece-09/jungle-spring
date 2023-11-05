package cece.spring;

import cece.spring.repository.BaseRepositoryImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "cece.spring.repository", repositoryBaseClass = BaseRepositoryImpl.class)
public class SpringApplication {

    public static void main(String[] args) {
        /**/
        org.springframework.boot.SpringApplication.run(SpringApplication.class, args);
    }

}
