package br.insper.projeto.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String VALIDATE_URL = "http://184.72.80.215/usuario/validate";

	@Override
	protected void doFilterInternal(HttpServletRequest request,
											  HttpServletResponse response,
											  FilterChain filterChain) throws ServletException, IOException {

		String token = request.getHeader("Authorization");

		if (token != null && !token.isEmpty()) {
			try {
				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", token);
				HttpEntity<String> entity = new HttpEntity<>(headers);

				ResponseEntity<Map> resp = restTemplate.exchange(
							 VALIDATE_URL,
							 HttpMethod.GET,
							 entity,
							 Map.class
				);

				if (resp.getStatusCode() == HttpStatus.OK) {
					Map<String, Object> userDetails = resp.getBody();
					String role = (String) userDetails.get("papel");

					List<GrantedAuthority> authorities = Collections.singletonList(
								 new SimpleGrantedAuthority("ROLE_" + role)
					);

					Authentication authentication = new UsernamePasswordAuthenticationToken(
								 userDetails.get("email"),
								 null,
								 authorities
					);

					SecurityContextHolder.getContext().setAuthentication(authentication);
				} else {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
					return;
				}
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
				return;
			}
		} else {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token não fornecido");
			return;
		}

		filterChain.doFilter(request, response);
	}
}