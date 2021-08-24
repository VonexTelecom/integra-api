package br.com.integra.api.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IntegraSecurity {
	
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public Long getClienteId() {
		Jwt jwt = (Jwt) getAuthentication().getPrincipal();
		
		return jwt.getClaim("cliente_id");
	}
	
	public boolean verificarPerfil(String perfil) {
		Jwt jwt = (Jwt) getAuthentication().getPrincipal();
		
		List<String> grupos = jwt.getClaim("grupos");

		return grupos.contains(perfil);
	}
}