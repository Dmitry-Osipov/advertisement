package rf.senla.advertisement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс Spring Boot приложения для управления рекламными объявлениями.
 * Этот класс является точкой входа в приложение и содержит метод main, который запускает приложение с помощью
 * SpringApplication.run().
 */
@SpringBootApplication
@Slf4j
public class AdvertisementApplication {
	/**
	 * Главный метод, запускающий Spring Boot приложение.
	 * @param args аргументы командной строки.
	 */
	public static void main(String[] args) {
		log.info("Before Starting application");
		SpringApplication.run(AdvertisementApplication.class, args);
		log.debug("Starting application in debug with {} args", args.length);
		log.info("Starting application with {} args", args.length);
	}
}
