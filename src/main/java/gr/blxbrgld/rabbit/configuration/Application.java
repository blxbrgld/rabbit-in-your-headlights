package gr.blxbrgld.rabbit.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main Configuration Class
 * @author blxbrgld
 */
@SpringBootApplication
@ComponentScan("gr.blxbrgld.rabbit")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
