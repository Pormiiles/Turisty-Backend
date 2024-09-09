package br.ifba.turisty.infrastructure.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.ifba.turisty.domain.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService; // Serviço responsável pela geração e validação de tokens

    @Autowired
    UserRepository userRepository; // Repositório para acessar dados de usuários

    // Método que executa o filtro para cada requisição
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Recupera o token JWT da requisição
        var token = this.recoverToken(request);
        
        // Se um token estiver presente
        if(token != null) {
            var email = tokenService.validateToken(token);
            
            UserDetails user = userRepository.getByEmail(email);

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para recuperar o token do cabeçalho da requisição
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        
        if(authHeader == null) 
            return null;
        
        // Remove o prefixo "Bearer " do cabeçalho e retorna o token
        return authHeader.replace("Bearer ", "");
    }
}
