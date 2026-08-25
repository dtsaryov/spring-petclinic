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

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service for owner registration.
 *
 * @author Wick Dynex
 */
@Service
public class OwnerService {

	private final OwnerRepository owners;

	public OwnerService(OwnerRepository owners) {
		this.owners = owners;
	}

	/**
	 * Register every owner of an imported batch.
	 * @param batch the owners to register, in submission order
	 * @return the registered owners, with their generated identifiers
	 * @throws DuplicateOwnerException if one of the owners is already on file
	 */
	public List<Owner> registerBatch(List<Owner> batch) {
		List<Owner> registered = new ArrayList<>();
		for (Owner owner : batch) {
			registered.add(saveOne(owner));
		}
		return registered;
	}

	/**
	 * Register a single owner, rejecting an owner the clinic already knows about.
	 * @param owner the owner to register
	 * @return the registered owner, with its generated identifier
	 * @throws DuplicateOwnerException if the owner is already on file
	 */
	@Transactional
	public Owner saveOne(Owner owner) {
		if (this.owners.existsByLastNameIgnoreCaseAndTelephone(owner.getLastName(), owner.getTelephone())) {
			throw new DuplicateOwnerException(owner.getLastName(), owner.getTelephone());
		}
		return this.owners.save(owner);
	}

}
