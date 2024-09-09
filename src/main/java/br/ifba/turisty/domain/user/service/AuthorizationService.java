package br.ifba.turisty.domain.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.ifba.turisty.domain.user.repository.UserRepository;


@Service
public class AuthorizationService implements UserDetailsService {
    // Injeção de dependência do repositório de usuários usando a anotação @Autowired
    @Autowired
    UserRepository repository;

    // Método sobrescrito de UserDetailsService que carrega os detalhes do usuário com base no email (username)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca o usuário pelo email usando o método do repositório e retorna o UserDetails correspondente
        return repository.getByEmail(username);
    }
}
