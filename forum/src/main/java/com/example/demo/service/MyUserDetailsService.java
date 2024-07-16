package com.example.demo.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * A classe MyUserDetailsService implementa a interface UserDetailsService do Spring Security.
 * Esta interface é usada pelo Spring Security para carregar detalhes específicos do usuário durante
 * o processo de autenticação.
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        /**
         * Este bloco simula que o email que veio, é igual ao email encontrado no banco de dados,
         * depois disso é pego o usuário e senha e criptogrado em memória para servir de validação
         * em outras partes do processo de segurança
         */
        if ("test@example.com".equals(email)) {
            // Simulando que este usuário veio do banco de dados e foi então criado os detalhes deste usuário,
            // mas no exemplo, foi criado aqui em memória direto
            return new User("test@example.com", new BCryptPasswordEncoder().encode("123"), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
    }
}
