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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * Shared test configuration for the tests around {@link OwnerLookupService}. Holds the
 * collaborators every one of those tests needs, together with a small factory for sample
 * owners, so the individual test classes stay focused on their assertions.
 */
public abstract class OwnerLookupTestSupport {

	@MockitoBean
	protected OwnerRepository ownerRepository;

	@Autowired
	protected OwnerLookupService ownerLookupService;

	/**
	 * Build a detached {@link Owner} suitable for assertions.
	 * @param firstName the owner's first name
	 * @param lastName the owner's last name
	 * @param city the owner's city
	 * @return a populated owner
	 */
	protected Owner sampleOwner(String firstName, String lastName, String city) {
		Owner owner = new Owner();
		owner.setFirstName(firstName);
		owner.setLastName(lastName);
		owner.setAddress("110 W. Liberty St.");
		owner.setCity(city);
		owner.setTelephone("6085551023");
		return owner;
	}

}
