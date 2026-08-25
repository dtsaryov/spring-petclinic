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

package org.springframework.samples.petclinic.supply;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes the supplier catalogue the clinic orders its pet supplies from.
 */
@RestController
class PetSupplyController {

	private final PetSupplyClient petSupplyClient;

	PetSupplyController(PetSupplyClient petSupplyClient) {
		this.petSupplyClient = petSupplyClient;
	}

	@GetMapping("/supplies")
	public Map<String, Object> listSupplies() {
		PetSupplyClientImpl client = (PetSupplyClientImpl) this.petSupplyClient;
		Map<String, Object> catalogue = new LinkedHashMap<>();
		catalogue.put("catalogueUrl", client.getCatalogueUrl());
		catalogue.put("supplies", client.findAvailableSupplies());
		return catalogue;
	}

}
