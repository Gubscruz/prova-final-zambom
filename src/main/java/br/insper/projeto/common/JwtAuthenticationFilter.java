package br.insper.projeto.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Component
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

					// Validação de permissões para rotas específicas
					if (!isAuthorized(role, request)) {
						response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado: permissão insuficiente");
						return;
					}

				} else {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
					return;
				}
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Erro ao validar o token");
				return;
			}
		} else {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token não fornecido");
			return;
		}

		// Se o usuário tiver a permissão, continua o filtro
		filterChain.doFilter(request, response);
	}

	private boolean isAuthorized(String role, HttpServletRequest request) {
		String path = request.getServletPath();
		String method = request.getMethod();

		// Definir as permissões de acesso com base na role e rota
		if (method.equals("POST") && path.equals("/tarefa")) {
			return role.equals("ADMIN");
		} else if (method.equals("DELETE") && path.startsWith("/tarefa")) {
			return role.equals("ADMIN");
		} else if (method.equals("GET") && path.startsWith("/tarefa")) {
			return role.equals("ADMIN") || role.equals("DEVELOPER");
		}

		// Permitir acesso a rotas adicionais, se necessário
		return false; // Bloquear qualquer outra rota por padrão
	}
}
