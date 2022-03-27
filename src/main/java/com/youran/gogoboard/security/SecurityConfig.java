package com.youran.gogoboard.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private JwtTokenFilter jwtTokenFilter;
	
	public SecurityConfig(JwtTokenFilter jwtTokenFilter) {
		log.debug("SecurityConfig");   
		this.jwtTokenFilter = jwtTokenFilter;
	}
	
	@Bean 
	public JwtAuthenticationProvider customAuthenticationProvider() { 
		return new JwtAuthenticationProvider(); 
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		log.debug("SecurityConfig.configure");
		
		http.cors().and()
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
			.antMatchers("/").permitAll()
			.antMatchers("/users").permitAll()
			.antMatchers("/users/auth").permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Bean
	public CorsFilter corsFilter() {
		
		log.debug("CorsFilter");
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedHeader("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

}
