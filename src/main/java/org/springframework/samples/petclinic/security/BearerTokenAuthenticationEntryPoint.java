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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Answers unauthenticated API calls with a plain {@code 401} carrying a
 * {@code WWW-Authenticate} challenge, so that partner clients retry with a bearer token
 * instead of being sent to a login form.
 *
 * @author Spring PetClinic team
 */
class BearerTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final String CHALLENGE = "Bearer realm=\"petclinic-api\"";

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) {
		response.setHeader(HttpHeaders.WWW_AUTHENTICATE, CHALLENGE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}

}
