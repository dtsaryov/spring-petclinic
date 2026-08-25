/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security arrangement for the clinic. The Thymeleaf front end is browsed anonymously and
 * signs in through a login form, while the REST API under {@code /api} is consumed by
 * integration partners presenting a bearer token.
 *
 * @author Spring PetClinic team
 */
@Configuration(proxyBeanMethods = false)
public class SecurityConfiguration {

	/**
	 * Rules for the REST API consumed by integration partners.
	 * @param http the security builder
	 * @param apiToken the token partners are expected to present
	 * @return the filter chain serving {@code /api}
	 * @throws Exception if the chain cannot be built
	 */
	@Bean
	@Order(1)
	SecurityFilterChain apiSecurityFilterChain(HttpSecurity http, @Value("${petclinic.api.token:}") String apiToken)
			throws Exception {
		http.securityMatcher("/api/**")
			.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated())
			.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling(
					(exceptions) -> exceptions.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint()))
			.addFilterBefore(new ApiTokenAuthenticationFilter(apiToken), UsernamePasswordAuthenticationFilter.class)
			.csrf((csrf) -> csrf.disable());
		return http.build();
	}

	/**
	 * Rules for the browser facing part of the clinic. This chain matches everything the
	 * API chain has not already claimed.
	 * @param http the security builder
	 * @return the filter chain serving the Thymeleaf front end
	 * @throws Exception if the chain cannot be built
	 */
	@Bean
	@Order(2)
	SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
		http.securityMatcher("/**")
			.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll())
			.formLogin(Customizer.withDefaults())
			.logout(Customizer.withDefaults())
			.csrf((csrf) -> csrf.disable());
		return http.build();
	}

}
