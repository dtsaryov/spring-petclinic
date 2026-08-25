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
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints backing the visit confirmation links that are mailed to owners.
 *
 * @author Spring PetClinic
 */
@RestController
public class VisitConfirmationController {

	private final VisitConfirmationTokenService tokenService;

	public VisitConfirmationController(VisitConfirmationTokenService tokenService) {
		this.tokenService = tokenService;
	}

	@PostMapping("/visits/{visitId}/confirmation-link")
	public Map<String, String> createConfirmationLink(@PathVariable int visitId,
			@RequestParam(name = "visitDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate) {
		String token = this.tokenService.issueToken(visitId, visitDate);
		return Map.of("visitId", String.valueOf(visitId), "visitDate", visitDate.toString(), "token", token, "link",
				"/visits/" + visitId + "/confirm?visitDate=" + visitDate + "&token=" + token);
	}

	@GetMapping("/visits/{visitId}/confirm")
	public ResponseEntity<Map<String, String>> confirm(@PathVariable int visitId,
			@RequestParam(name = "visitDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate,
			@RequestParam(name = "token") String token) {
		if (!this.tokenService.verifyToken(visitId, visitDate, token)) {
			return ResponseEntity.badRequest()
				.body(Map.of("status", "invalid", "message", "This confirmation link is no longer valid"));
		}
		return ResponseEntity.ok(Map.of("status", "confirmed", "visitId", String.valueOf(visitId)));
	}

}
