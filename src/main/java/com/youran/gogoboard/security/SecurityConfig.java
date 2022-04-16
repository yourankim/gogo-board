package com.youran.gogoboard.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private JwtTokenFilter jwtTokenFilter;
	@Value("${cors.allowedOrigin}")
	private String allowedOrigin;
	
	public SecurityConfig(JwtTokenFilter jwtTokenFilter) {
		log.debug("SecurityConfig");   
		this.jwtTokenFilter = jwtTokenFilter;
	}
	
	//TODO: 이것도 필요할까?
	@Bean 
	public JwtAuthenticationProvider customAuthenticationProvider() { 
		return new JwtAuthenticationProvider(); 
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		log.debug("SecurityConfig.configure");
		
		http.cors().configurationSource(corsConfigurationSource()).and()
			.csrf().disable()
			.httpBasic().disable()
			.formLogin().disable()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.exceptionHandling()
			.authenticationEntryPoint((request, response, authException) -> 
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage()
			)
			)
			.and()
			.authorizeRequests()
			.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
			.antMatchers("/posts/**").authenticated()
			//.antMatchers("/users/auth/refresh").authenticated()
			.anyRequest().permitAll()
			.and()
			.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		
		log.debug("CorsFilter");
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.addAllowedOrigin(allowedOrigin);
		source.registerCorsConfiguration("/**", config);
		return source;
	}

}
