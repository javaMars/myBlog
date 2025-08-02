package ru.practicum.sprint4.spring.boot.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.practicum.sprint4.spring.boot.blog.config.UploadProperties;

@SpringBootApplication()
@EnableConfigurationProperties(UploadProperties.class)
public class SpringBootSprint4Application {

	public static void main(String[] args) {
			SpringApplication.run(SpringBootSprint4Application.class, args);
	}

}

