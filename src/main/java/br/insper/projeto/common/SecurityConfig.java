package br.insper.projeto.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
					 .csrf(csrf -> csrf.disable())
					 .authorizeHttpRequests(authz -> authz
								  .requestMatchers(HttpMethod.POST, "/tarefa").hasRole("ADMIN")
								  .requestMatchers(HttpMethod.DELETE, "/tarefa/**").hasRole("ADMIN")
								  .requestMatchers(HttpMethod.GET, "/tarefa/**").hasAnyRole("ADMIN", "DEVELOPER")
								  .anyRequest().authenticated()
					 )
					 .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}