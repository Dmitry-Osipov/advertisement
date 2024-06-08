package rf.senla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс для веб-приложения Advertisement.
 * Этот класс служит точкой входа для Spring Boot приложения. Он содержит метод {@link #main(String[])},
 * который запускает приложение.
 */
@SpringBootApplication
public class WebApplication {
    /**
     * Главный метод, запускающий приложение.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
