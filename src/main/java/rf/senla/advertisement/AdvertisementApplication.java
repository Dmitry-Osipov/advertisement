package rf.senla.advertisement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс Spring Boot приложения для управления рекламными объявлениями.
 * Этот класс является точкой входа в приложение и содержит метод main, который запускает приложение с помощью
 * SpringApplication.run().
 */
@SpringBootApplication
public class AdvertisementApplication {
	/**
	 * Главный метод, запускающий Spring Boot приложение.
	 * @param args аргументы командной строки.
	 */
	public static void main(String[] args) {
		SpringApplication.run(AdvertisementApplication.class, args);
	}
}
