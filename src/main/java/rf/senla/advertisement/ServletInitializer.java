package rf.senla.advertisement;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Инициализатор сервлета для Spring Boot приложения.
 * Этот класс используется для конфигурации приложения при запуске его как традиционного веб-приложения, развернутого
 * на сервере приложений, например, Tomcat или Jetty.
 */
public class ServletInitializer extends SpringBootServletInitializer  {
    /**
     * Настраивает приложение.
     * Этот метод вызывается при инициализации сервлета и используется для настройки источника Spring Boot приложения.
     * @param application объект SpringApplicationBuilder, который используется для конфигурации приложения.
     * @return настроенный объект SpringApplicationBuilder.
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AdvertisementApplication.class);
    }
}
