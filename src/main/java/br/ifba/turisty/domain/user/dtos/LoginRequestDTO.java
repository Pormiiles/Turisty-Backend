package br.ifba.turisty.domain.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    @Email(message = "Email inválido!")
    @NotNull(message = "O campo 'email' não pode ser nulo!")
    @NotBlank(message = "O campo 'email' não pode estar vazio!")
    private String email;

    @NotNull(message = "O campo 'senha' não pode ser nulo!")
    @NotBlank(message = "O campo 'nome' não pode estar vazio!")
    private String password;
}
