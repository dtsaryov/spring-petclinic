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
package org.springframework.samples.petclinic.owner;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint used by the front desk to import several owners at once.
 *
 * @author Wick Dynex
 */
@RestController
class OwnerBatchController {

	private final OwnerService ownerService;

	OwnerBatchController(OwnerService ownerService) {
		this.ownerService = ownerService;
	}

	@PostMapping("/owners/batch")
	@ResponseStatus(HttpStatus.CREATED)
	BatchRegistrationResult registerBatch(@RequestBody List<Owner> batch) {
		List<Owner> registered = this.ownerService.registerBatch(batch);
		return new BatchRegistrationResult(registered.size(), registered.stream().map(Owner::getId).toList());
	}

	record BatchRegistrationResult(int registered, List<Integer> ownerIds) {
	}

}
