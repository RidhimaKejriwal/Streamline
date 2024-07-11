package com.crm.streamline.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.crm.streamline.services.impl.SecurityCustomUserDetailsService;

@Configuration
public class SecurityConfig {
    
    @Autowired
    private SecurityCustomUserDetailsService userDetailService;

    // CONFIGURATION OF AUTHENTICATION PROVIDER FOR SPRING SECURITY
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        System.out.println("This is dao authentication provider ...........");

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // user detail servicde ka object
        System.out.println(userDetailService);
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        System.out.println("details got");
        // password encoder ka object
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(authorize -> {
            // authorize.requestMatchers("/home", "/signup", "/services").permitAll();
            authorize.requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/employee/**").hasRole("EMPLOYEE")
            .requestMatchers("/customer/**").hasRole("CUSTOMER")
            .anyRequest().permitAll();
        });

        httpSecurity.formLogin(formLogin -> {
            formLogin.loginPage("/login")
            .loginProcessingUrl("/authenticate")
            .successForwardUrl("/default");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");
            formLogin.failureForwardUrl("/login?error=true").permitAll();
        }
        );
       
        
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutForm -> {
            logoutForm.logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout=true");
        });
        
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
