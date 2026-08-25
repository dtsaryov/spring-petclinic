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

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service giving read and write access to {@link Owner} records.
 * <p>
 * Owner details are read far more often than they are changed, so lookups by identifier
 * are served from the {@code owners} cache.
 */
@Service
public class OwnerService {

	private final OwnerRepository owners;

	public OwnerService(OwnerRepository owners) {
		this.owners = owners;
	}

	/**
	 * Find a single owner by its identifier.
	 * @param ownerId the identifier of the owner
	 * @return the matching owner, or {@code null} when no owner has that identifier
	 */
	@Cacheable("owners")
	public Owner findById(Integer ownerId) {
		return this.owners.findById(ownerId).orElse(null);
	}

	/**
	 * Update the contact details of an existing owner.
	 * @param ownerId the identifier of the owner to update
	 * @param details the new contact details
	 * @return the stored owner
	 * @throws IllegalArgumentException if no owner has the given identifier
	 */
	@CacheEvict(cacheNames = "owners", key = "#ownerId")
	@Transactional
	public Owner updateOwner(Integer ownerId, OwnerContactDetails details) {
		Owner owner = this.owners.findById(ownerId)
			.orElseThrow(() -> new IllegalArgumentException("Owner not found: " + ownerId));
		owner.setAddress(details.address());
		owner.setCity(details.city());
		owner.setTelephone(details.telephone());
		return this.owners.save(owner);
	}

}
