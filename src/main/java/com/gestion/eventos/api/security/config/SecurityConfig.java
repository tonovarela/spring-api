package com.gestion.eventos.api.security.config;

import com.gestion.eventos.api.exception.CustomAuthenticationEntryPoint;
import com.gestion.eventos.api.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

   private  final UserDetailsService userDetailsService;
   private  final CustomAuthenticationEntryPoint authenticationEntryPoint;
   private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http
              .csrf(AbstractHttpConfigurer::disable)
              .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))

              .sessionManagement(s-> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
              .httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPoint))
              .authorizeHttpRequests(
                      auth->
                              auth
                                      .requestMatchers("/api/v1/auth/**").permitAll()
                                      .requestMatchers("/h2-console/**").permitAll()
                                      .requestMatchers(HttpMethod.GET,"/api/v1/events").authenticated()
                                      .anyRequest().authenticated()
              ).headers(AbstractHttpConfigurer::disable)
      ;
   http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
      return http.build();
  }
  @Bean
    public PasswordEncoder passwordEncoder(){
      return new BCryptPasswordEncoder(); 
  }

  @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder){
      DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
      provider.setPasswordEncoder(passwordEncoder);
      return new ProviderManager(provider);
  }

}
