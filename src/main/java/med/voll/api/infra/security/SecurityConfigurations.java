package med.voll.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe responsável pela maneira que ocorrerá o "login" do usuário
 * No caso, iremos mudar de Stateful para Stateless
 */
@Configuration // Para dizer que é uma classe de configuração
@EnableWebSecurity // Para dizer que iremos personalizar as configurações de segurança
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean // Para expor o retorno desse método, um objeto que pode ser injetado por alguma classe
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // .csrf().disable(): Para desativar a proteção contra ataques csrf, o token jwt ja faz isso
        // .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS): Para dizer que iremos autenticar via Stateless
        // .and().build(): Para gerar o objeto SecurityFilterChain

        // .and().authorizeRequests()
        // .antMatchers(HttpMethod.POST, "/login").permitAll()
        // Diz para o Spring que: Se vier uma requisição post para url "/login" libere, não é para checar se o usuário está logado
        // .anyRequest().authenticated(): Diz para o Spring que qualquer outra url deve ser blockeada
        // .and().addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class): Diz para o spring que o meu filtro deve ser executado antes do dele

        return http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest().authenticated()
                .and().addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    // Serve para exportar uma classe para o Spring, fazendo com que ele consiga carregá-la e realze a injeção de dependência em outras classes
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
