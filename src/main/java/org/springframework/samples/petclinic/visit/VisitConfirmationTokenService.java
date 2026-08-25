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

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Issues and verifies the signed tokens that back the visit confirmation links mailed to
 * owners. A token carries the visit identifier and the visit date together with a keyed
 * signature, so a link cannot be tampered with before it is followed.
 *
 * @author Spring PetClinic
 */
@Service
public class VisitConfirmationTokenService {

	private static final String ALGORITHM = "HmacSHA256";

	private final SecretKeySpec signingKey;

	public VisitConfirmationTokenService(@Value("${petclinic.visit-token-secret:}") String secret) {
		if (!StringUtils.hasText(secret)) {
			throw new IllegalStateException("No visit confirmation token secret is configured. "
					+ "Set 'petclinic.visit-token-secret' (environment variable PETCLINIC_VISIT_TOKEN_SECRET) "
					+ "to a stable secret shared by every instance of this deployment; "
					+ "a per-instance or per-restart value invalidates confirmation links that were already sent.");
		}
		this.signingKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
	}

	/**
	 * Issue a confirmation token for the given visit.
	 * @param visitId the identifier of the visit being confirmed
	 * @param visitDate the date of the visit
	 * @return the token to embed in the confirmation link
	 */
	public String issueToken(int visitId, LocalDate visitDate) {
		String payload = payload(visitId, visitDate);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8)) + "."
				+ sign(payload);
	}

	/**
	 * Verify a token previously handed out for the given visit.
	 * @param visitId the identifier of the visit being confirmed
	 * @param visitDate the date of the visit
	 * @param token the token taken from the confirmation link
	 * @return whether the token carries a valid signature for this visit
	 */
	public boolean verifyToken(int visitId, LocalDate visitDate, String token) {
		if (token == null) {
			return false;
		}
		int separator = token.lastIndexOf('.');
		if (separator < 0) {
			return false;
		}
		String expected = sign(payload(visitId, visitDate));
		String presented = token.substring(separator + 1);
		return MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8),
				presented.getBytes(StandardCharsets.UTF_8));
	}

	private String payload(int visitId, LocalDate visitDate) {
		return visitId + "|" + visitDate;
	}

	private String sign(String payload) {
		try {
			Mac mac = Mac.getInstance(ALGORITHM);
			mac.init(this.signingKey);
			return Base64.getUrlEncoder()
				.withoutPadding()
				.encodeToString(mac.doFinal(payload.getBytes(StandardCharsets.UTF_8)));
		}
		catch (GeneralSecurityException ex) {
			throw new IllegalStateException("Unable to sign visit confirmation token", ex);
		}
	}

}
