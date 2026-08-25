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

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Application service for the {@link Owner} queries shared by the web layer and the
 * clinic directory API.
 *
 * @author Wick Dynex
 */
@Service
public class OwnerService {

	private static final Sort BY_NAME = Sort.by("lastName", "firstName");

	private final OwnerRepository ownerRepository;

	public OwnerService(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	/**
	 * List the owners registered in the given city, ordered by name.
	 * @param city the city to list the owners of
	 * @return the owners of that city
	 */
	@Transactional(readOnly = true)
	public List<Owner> listForCity(String city) {
		Assert.hasText(city, "City must not be empty");
		return this.ownerRepository.findByCity(city, BY_NAME);
	}

	/**
	 * Count the owners registered in the given city.
	 * @param city the city to count the owners of
	 * @return the number of owners of that city
	 */
	@Transactional(readOnly = true)
	public long countForCity(String city) {
		Assert.hasText(city, "City must not be empty");
		return this.ownerRepository.countByCity(city);
	}

}
