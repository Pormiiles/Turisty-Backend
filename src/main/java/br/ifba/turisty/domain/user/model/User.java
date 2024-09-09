package br.ifba.turisty.domain.user.model;

import java.util.Collection;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    private UserRoleEnum role;

    public User(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole() != null ? user.getRole() : UserRoleEnum.USER;
    }

    public static User fromDTOWithEncryptedPassword(User user) {
        user = new User(user);

        // BCrypt é uma classe usada para criptografar a senha do usuário.
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        return user;
    }

    // Implementação do método getAuthorities() da interface UserDetails.
    // Retorna as autoridades (permissões) do usuário baseado em seu papel.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRoleEnum.ADMIN) 
            return List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN"), 
                new SimpleGrantedAuthority("ROLE_USER")
            );
        else 
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // Implementação do método getUsername() da interface UserDetails.
    // Retorna o nome do usuário.
    @Override
    public String getUsername() {
        return name;
    }

    // Os métodos abaixo são parte da interface UserDetails.
    // Eles controlam se a conta do usuário está expirada, bloqueada,
    // se as credenciais (senha) estão expiradas e se a conta está habilitada.

    @Override
    public boolean isAccountNonExpired() {
        return true; // Retorna true, indicando que a conta não está expirada.
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Retorna true, indicando que a conta não está bloqueada.
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Retorna true, indicando que as credenciais não estão expiradas.
    }

    @Override
    public boolean isEnabled() {
        return true; // Retorna true, indicando que a conta está habilitada.
    }
}
