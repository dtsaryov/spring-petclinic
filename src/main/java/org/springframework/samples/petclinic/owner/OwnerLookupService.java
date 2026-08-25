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
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Read-only lookup operations for {@link Owner} records. Keeps the repository out of the
 * callers that only need a simple listing of the clinic's owners.
 */
@Service
public class OwnerLookupService {

	private final OwnerRepository owners;

	public OwnerLookupService(OwnerRepository owners) {
		this.owners = owners;
	}

	/**
	 * Return every owner known to the clinic.
	 * @return all owners, ordered as the store returns them
	 */
	@Transactional(readOnly = true)
	public List<Owner> findAll() {
		return this.owners.findAll();
	}

	/**
	 * Return a single owner by its identifier.
	 * @param id the owner identifier
	 * @return the owner, if one exists with that identifier
	 */
	@Transactional(readOnly = true)
	public Optional<Owner> findById(Integer id) {
		return this.owners.findById(id);
	}

	/**
	 * Return the number of owners known to the clinic.
	 * @return the owner count
	 */
	@Transactional(readOnly = true)
	public long count() {
		return this.owners.count();
	}

}
