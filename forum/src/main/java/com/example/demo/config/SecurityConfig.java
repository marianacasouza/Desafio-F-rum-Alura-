package com.example.demo.config;

import com.example.demo.filter.JwtRequestFilter;
import com.example.demo.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * A classe SecurityConfig é responsável por configurar as regras de segurança da aplicação usando
 * Spring Security. Ela define como a aplicação deve lidar com a autenticação e autorização,
 * além de configurar outros aspectos de segurança, como a proteção CSRF, o gerenciamento de sessões,
 * e os filtros de segurança.
 *
 * Configurar Autenticação:
 *
 * Define como a aplicação deve autenticar os usuários, por exemplo, usando um banco de dados,
 * um serviço de autenticação externo,
 * ou um provedor de autenticação personalizado.
 *
 * Configurar Autorização:
 *
 * Define quais endpoints ou recursos são acessíveis publicamente e quais exigem autenticação.
 * Também pode especificar as permissões necessárias para acessar certos recursos.
 *
 * Configurar Filtros de Segurança:
 *
 * Adiciona filtros personalizados, como um filtro JWT para autenticação baseada em tokens JWT.
 *
 * Gerenciar Sessões:
 *
 * Define políticas de gerenciamento de sessões, como a criação de sessões sem estado (stateless) para APIs RESTful.
 *
 * Configurar Proteções Adicionais:
 *
 * Configura proteções adicionais, como a proteção CSRF, cabeçalhos de segurança, etc.
 *
 * CSRF (Cross-Site Request Forgery), ou "falsificação de solicitação entre sites",
 * é um tipo de ataque que ocorre quando um atacante engana um usuário autenticado para que
 * ele envie uma solicitação não desejada a um aplicativo web em que está autenticado. Em outras palavras,
 * o atacante força o navegador do usuário a executar ações no site onde ele está autenticado,
 * sem o seu consentimento.
 *
 * No contexto de APIs RESTful, especialmente aquelas que seguem uma arquitetura sem estado (stateless),
 * a proteção CSRF é frequentemente desabilitada. o Servidor não vai enviar o token, porque a aplicação
 * não está gerenciando o front-end, pois esta aplicação é apenas uma api rest.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/auth").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
