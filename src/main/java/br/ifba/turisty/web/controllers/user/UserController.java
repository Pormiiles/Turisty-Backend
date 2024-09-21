package br.ifba.turisty.web.controllers.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifba.turisty.domain.user.dtos.LoginRequestDTO;
import br.ifba.turisty.domain.user.dtos.LoginResponseDTO;
import br.ifba.turisty.domain.user.dtos.UserRequestDTO;
import br.ifba.turisty.domain.user.dtos.UserResponseDTO;
import br.ifba.turisty.domain.user.dtos.UserUpdateDataDTO;
import br.ifba.turisty.domain.user.model.User;
import br.ifba.turisty.domain.user.service.UserService;
import br.ifba.turisty.infrastructure.config.TokenService;
import br.ifba.turisty.infrastructure.util.ObjectMapperUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final ObjectMapperUtil modelMapper;
    private final TokenService tokenService;

    @GetMapping // Mapeia requisições HTTP GET para o caminho "/user"
    public ResponseEntity<?> findAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(modelMapper.mapAll(this.userService.findAll(), UserResponseDTO.class));
    }

    @GetMapping("/{id}") // Mapeia requisições HTTP GET para o caminho "/user/{id}"
    public ResponseEntity<String> findById(@PathVariable Long id) {
        try {
            User user = userService.findById(id);
            UserResponseDTO userResponseDTO = modelMapper.map(user, UserResponseDTO.class);

            return ResponseEntity.status(HttpStatus.OK).body("User found: " + userResponseDTO.getName());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found by id: " + id);
        }
    }

    @PostMapping("/auth/login") // Mapeia requisições HTTP POST para "/user/auth/login"
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword());
        
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/auth") // Mapeia requisições HTTP POST para "/user/auth"
    public ResponseEntity<UserResponseDTO> save(@RequestBody @Valid UserRequestDTO data) {
        User createUser = modelMapper.map(data, User.class);
        User user = userService.save(createUser);
        UserResponseDTO responseDTO = modelMapper.map(user, UserResponseDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}") // Mapeia requisições HTTP PUT para "/user/{id}"
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody @Valid UserUpdateDataDTO data) {
        User updatedUser = modelMapper.map(data, User.class);
        User user = userService.update(id, updatedUser);
        UserResponseDTO responseDTO = modelMapper.map(user, UserResponseDTO.class);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}") // Mapeia requisições HTTP DELETE para "/user/{id}"
    public ResponseEntity<String> delete(@PathVariable Long id) {
        userService.delete(id);

        return ResponseEntity.status(HttpStatus.OK).body("User with id: " + id + " was successfully deleted!");
    }
}
