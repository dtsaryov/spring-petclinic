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

import java.util.Collections;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

/**
 * Straightforward {@link PetSupplyClient} implementation on top of a {@link RestClient},
 * keeping track of the catalogue location it talks to.
 */
public class PetSupplyClientImpl implements PetSupplyClient {

	private final RestClient restClient;

	private final String catalogueUrl;

	public PetSupplyClientImpl(RestClient restClient, String catalogueUrl) {
		this.restClient = restClient;
		this.catalogueUrl = catalogueUrl;
	}

	@Override
	public List<PetSupply> findAvailableSupplies() {
		List<PetSupply> supplies = this.restClient.get()
			.uri("/api/supplies")
			.retrieve()
			.body(new ParameterizedTypeReference<List<PetSupply>>() {
			});
		return (supplies != null) ? supplies : Collections.emptyList();
	}

	/**
	 * The catalogue location this client reads from, shown next to the supply list so
	 * that the clinic staff can tell which supplier answered.
	 * @return the catalogue base URL
	 */
	public String getCatalogueUrl() {
		return this.catalogueUrl;
	}

}
