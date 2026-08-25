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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

/**
 * Integration tests asserting that the REST API is guarded by the bearer token chain
 * rather than by the browser login form.
 *
 * @author Spring PetClinic team
 */
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = "petclinic.api.token=test-token")
@AutoConfigureTestRestTemplate
class ApiSecurityIntegrationTests {

	@Value("${local.server.port}")
	private int port;

	@Autowired
	private TestRestTemplate rest;

	@Test
	void anonymousApiCallIsChallengedWithBearerToken() {
		ResponseEntity<String> response = this.rest
			.exchange(RequestEntity.get("http://localhost:" + this.port + "/api/vets").build(), String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(response.getHeaders().getFirst(HttpHeaders.WWW_AUTHENTICATE)).startsWith("Bearer");
		assertThat(response.getHeaders().getLocation()).isNull();
	}

	@Test
	void apiCallWithTokenReturnsTheVetDirectory() {
		ResponseEntity<String> response = this.rest
			.exchange(RequestEntity.get("http://localhost:" + this.port + "/api/vets")
				.header(HttpHeaders.AUTHORIZATION, "Bearer test-token")
				.build(), String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("Leary");
	}

	@Test
	void browserPagesRemainPublic() {
		ResponseEntity<String> response = this.rest
			.exchange(RequestEntity.get("http://localhost:" + this.port + "/vets.html").build(), String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

}
