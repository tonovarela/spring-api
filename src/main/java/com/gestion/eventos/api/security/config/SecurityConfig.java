package com.gestion.eventos.api.security.config;

import com.gestion.eventos.api.exception.CustomAuthenticationEntryPoint;
import com.gestion.eventos.api.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

   private  final UserDetailsService userDetailsService;
   private  final CustomAuthenticationEntryPoint authenticationEntryPoint;
   private final JwtAuthenticationFilter jwtAuthenticationFilter;
   private final Environment  environment;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http

              .cors(c->c.configurationSource(corsConfigurationSource()))
              .csrf(AbstractHttpConfigurer::disable)
              .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))

              .sessionManagement(s-> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
              .httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPoint))
              .authorizeHttpRequests(
                      auth->{
                          List<String> pathsSwagger = List.of(
                                  "/swagger-ui.html",
                                  "/swagger-ui/**",
                                  "/v3/api-docs",
                                  "/v3/api-docs/**"
                          );
                          auth.requestMatchers("/api/v1/auth/**","/error").permitAll();

                                  //.requestMatchers("/h2-console/**").permitAll()

                                 if (environment.acceptsProfiles(Profiles.of("dev"))) {
                                     auth.requestMatchers(pathsSwagger.toArray(new String[0])).permitAll();

                                 }
                                  auth.anyRequest().authenticated();
                      }

              )
              //.headers(AbstractHttpConfigurer::disable)
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

  @Bean
    public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOriginPatterns(List.of("*"));
      configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));
      configuration.setAllowedHeaders(List.of("*"));

      configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));
      configuration.setAllowCredentials(true);
      configuration.setMaxAge(3600L);
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);
      return source;



  }

}
