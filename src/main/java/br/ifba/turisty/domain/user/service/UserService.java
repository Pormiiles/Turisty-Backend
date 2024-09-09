package br.ifba.turisty.domain.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.ifba.turisty.domain.user.model.User;
import br.ifba.turisty.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;  // Injeção de dependência para o repositório de usuários

    // Método para buscar todos os usuários do banco de dados
    public List<User> findAll() {
        List<User> allUsers = userRepository.findAll();

        // Se não houver usuários, lança uma exceção personalizada
        if(allUsers.isEmpty())
            throw new RuntimeException("There are no users to list.");

        return allUsers;
    }

    // Método para buscar um usuário por ID
    public User findById(Long id) {
        // Busca o usuário e, se não encontrado, lança uma exceção personalizada
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with id: " + id + " was not found!"));
    }

    // Método para salvar um novo usuário no banco de dados
    @Transactional
    public User save(User user) {
        // Verifica se o email já está registrado
        if(userRepository.existsByEmail(user.getEmail()))
            throw new RuntimeException("Email already exists.");

        // Cria um novo usuário com a senha criptografada
        User newUser = User.fromDTOWithEncryptedPassword(user);

        // Salva o novo usuário no banco de dados
        return userRepository.save(newUser);
    }

    // Método para atualizar os dados de um usuário existente
    @Transactional
    public User update(Long id, User user) {
        // Busca o usuário pelo ID ou lança uma exceção se não for encontrado
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found."));

        // Verifica se o novo email já está registrado por outro usuário
        if(userRepository.existsByEmail(user.getEmail()) && !existingUser.getEmail().equals(user.getEmail()))
            throw new RuntimeException("Email already exists.");
        // Verifica se o novo nome já está registrado por outro usuário
        else if(userRepository.existsByName(user.getName()) && !existingUser.getName().equals(user.getName()))
            throw new IllegalArgumentException("Username already exists.");

        // Atualiza o nome e o email do usuário
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());

        // Salva as alterações no banco de dados
        return userRepository.save(existingUser);
    }

    // Método para deletar um usuário pelo ID
    @Transactional
    public void delete(Long id) {
        // Verifica se o usuário existe no banco de dados
        if(!userRepository.existsById(id))
            throw new RuntimeException("User not found.");

        // Deleta o usuário do banco de dados
        userRepository.deleteById(id);
    }
}
