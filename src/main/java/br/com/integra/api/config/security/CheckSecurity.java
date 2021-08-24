package br.com.integra.api.config.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public @interface CheckSecurity {

	public @interface DadosSumarizados {

		@PreAuthorize("isAuthenticated()")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeAcessar {
		}

	}
}