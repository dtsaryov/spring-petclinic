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
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Tests the atomicity of {@link OwnerService#registerBatch(List)}.
 * <p>
 * Deliberately not annotated with {@code @Transactional}: the point of these tests is
 * what survives a real commit or rollback, which a surrounding test transaction would
 * hide.
 *
 * @author Wick Dynex
 */
@SpringBootTest
class OwnerBatchRegistrationTests {

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

	private boolean isOnFile(String lastName) {
		return !this.owners.findByLastNameStartingWith(lastName, PageRequest.of(0, 1)).isEmpty();
	}

	@Test
	void leavesNoPartialDataWhenAnOwnerOfTheBatchIsRejected() {
		long before = this.owners.count();

		assertThatExceptionOfType(DuplicateOwnerException.class).isThrownBy(() -> this.ownerService
			.registerBatch(List.of(owner("Zoe", "Aardvark", "6085550901"), owner("Yuri", "Babbage", "6085550902"),
					owner("George", "Franklin", "6085551023"), owner("Xena", "Curie", "6085550904"))));

		assertThat(this.owners.count()).isEqualTo(before);
		assertThat(isOnFile("Aardvark")).isFalse();
		assertThat(isOnFile("Babbage")).isFalse();
		assertThat(isOnFile("Curie")).isFalse();
	}

}
