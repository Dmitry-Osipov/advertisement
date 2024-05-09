package rf.senla.advertisement.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import rf.senla.advertisement.security.filter.JwtAuthenticationFilter;
import rf.senla.advertisement.security.service.UserService;

import java.util.List;

/**
 * Конфигурационный класс для настройки безопасности приложения. Этот класс определяет настройки безопасности, такие
 * как фильтры, правила доступа к конечным точкам, провайдер аутентификации и другие. Он также содержит бины для
 * создания аутентификационного провайдера, кодировщика паролей и менеджера аутентификации.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

    /**
     * Настройка цепочки фильтров безопасности.
     * @param http объект HttpSecurity
     * @return цепочка фильтров безопасности
     * @throws Exception если произошла ошибка во время конфигурации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                // Своего рода отключение CORS (разрешение запросов со всех доменов)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                // Настройка доступа к конечным точкам
                .authorizeHttpRequests(request -> request
                        // TODO: в конце убрать разрешение на любой реквест и убрать многострочный комментарий ниже
                        .anyRequest().permitAll())
                        /*
                        // Можно указать конкретный путь, * - 1 уровень вложенности, ** - любое количество уровней
                        // вложенности
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/endpoint", "/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                         */
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Создает и возвращает объект PasswordEncoder для кодирования паролей.
     * @return объект PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Создает и возвращает провайдер аутентификации для сервиса пользователей.
     * @return провайдер аутентификации
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Создает и возвращает менеджер аутентификации.
     * @param config конфигурация аутентификации
     * @return менеджер аутентификации
     * @throws Exception если произошла ошибка во время создания менеджера аутентификации
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
