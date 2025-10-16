package com.MyInventory.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {


    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {// Disable CSRF for development/testing (enable in production)
        http.
                 csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/css/**", "/js/**", "/logout", "/Images/**", "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/register", "/RegisterUser", "/user/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/register", "/RegisterUser", "/user/**").permitAll()
                        .requestMatchers("/admin/**","/register").hasRole("ADMIN")
                        .requestMatchers("/product/**").hasAnyRole("ADMIN","STAFF")
                        .requestMatchers("/staff/**").hasRole("STAFF")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/Login") // GET endpoint that serves the login page
                        .loginProcessingUrl("/login") // POST action that processes credentials
                        .usernameParameter("email") // expects "email" field in form
                        .passwordParameter("password")
                        .defaultSuccessUrl("/default", true) // redirect after login
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // default logout URL (accepts POST)
                        .logoutSuccessUrl("/") // redirect after logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Needed for AuthenticationManagerBuilder replacement
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }




}
