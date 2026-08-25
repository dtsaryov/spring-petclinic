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

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Tests for {@link OwnerService}.
 *
 * @author Wick Dynex
 */
@SpringBootTest
@Transactional
class OwnerServiceTests {

	@Autowired
	private OwnerService ownerService;

	@Autowired
	private OwnerRepository owners;

	private static Owner owner(String firstName, String lastName, String telephone) {
		Owner owner = new Owner();
		owner.setFirstName(firstName);
		owner.setLastName(lastName);
		owner.setAddress("1 Fair Way");
		owner.setCity("Madison");
		owner.setTelephone(telephone);
		return owner;
	}

	@Test
	void registersEveryOwnerOfACleanBatch() {
		long before = this.owners.count();

		List<Owner> registered = this.ownerService
			.registerBatch(List.of(owner("Ada", "Lovelace", "6085550101"), owner("Grace", "Hopper", "6085550102")));

		assertThat(registered).hasSize(2).allSatisfy((owner) -> assertThat(owner.getId()).isNotNull());
		assertThat(this.owners.count()).isEqualTo(before + 2);
	}

	@Test
	void rejectsAnOwnerThatIsAlreadyOnFile() {
		assertThatExceptionOfType(DuplicateOwnerException.class)
			.isThrownBy(() -> this.ownerService.saveOne(owner("George", "Franklin", "6085551023")));
	}

}
