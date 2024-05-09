package rf.senla.advertisement.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "advertisement", name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "boosted")
    private Boolean boosted = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    /**
     * Получение роли пользователя в виде коллекции GrantedAuthority.
     * @return Роли пользователя.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Проверка, истек ли срок действия учетной записи пользователя.
     * @return Всегда возвращает {@code true}, так как срок действия учетной записи не учитывается.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверка, заблокирована ли учетная запись пользователя.
     * @return Всегда возвращает {@code true}, так как учетная запись пользователя не блокируется.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверка, истек ли срок действия учетных данных пользователя.
     * @return Всегда возвращает {@code true}, так как срок действия учетных данных не учитывается.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Проверка, активирована ли учетная запись пользователя.
     * @return Всегда возвращает {@code true}, так как учетная запись пользователя всегда активна.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
