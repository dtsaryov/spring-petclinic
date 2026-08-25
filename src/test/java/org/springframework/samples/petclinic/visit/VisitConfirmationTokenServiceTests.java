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
package org.springframework.samples.petclinic.visit;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * Tests for {@link VisitConfirmationTokenService}.
 */
class VisitConfirmationTokenServiceTests {

	private static final LocalDate VISIT_DATE = LocalDate.of(2025, 3, 14);

	private final VisitConfirmationTokenService service = new VisitConfirmationTokenService(
			"test-visit-token-secret-0123456789");

	@Test
	void issuedTokenVerifies() {
		String token = this.service.issueToken(7, VISIT_DATE);
		assertThat(this.service.verifyToken(7, VISIT_DATE, token)).isTrue();
	}

	@Test
	void tokenIsRejectedForAnotherVisit() {
		String token = this.service.issueToken(7, VISIT_DATE);
		assertThat(this.service.verifyToken(8, VISIT_DATE, token)).isFalse();
	}

	@Test
	void tokenSignedWithAnotherSecretIsRejected() {
		String token = new VisitConfirmationTokenService("another-visit-token-secret-9876543210").issueToken(7,
				VISIT_DATE);
		assertThat(this.service.verifyToken(7, VISIT_DATE, token)).isFalse();
	}

	@Test
	void aMissingSecretIsRejectedAtStartup() {
		assertThatIllegalStateException().isThrownBy(() -> new VisitConfirmationTokenService(""))
			.withMessageContaining("PETCLINIC_VISIT_TOKEN_SECRET");
	}

	@Test
	void malformedTokenIsRejected() {
		assertThat(this.service.verifyToken(7, VISIT_DATE, "not-a-token")).isFalse();
		assertThat(this.service.verifyToken(7, VISIT_DATE, null)).isFalse();
	}

}
