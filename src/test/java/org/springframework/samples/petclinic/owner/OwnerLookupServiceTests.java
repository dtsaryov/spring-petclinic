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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.springframework.boot.test.context.SpringBootTest;

/**
 * Tests for {@link OwnerLookupService} in isolation from the persistence layer.
 */
@SpringBootTest(classes = OwnerLookupService.class)
class OwnerLookupServiceTests extends OwnerLookupTestSupport {

	@Test
	void findAllReturnsWhatTheRepositoryProvides() {
		given(this.ownerRepository.findAll())
			.willReturn(List.of(sampleOwner("George", "Franklin", "Madison"), sampleOwner("Betty", "Davis", "Monona")));

		List<Owner> found = this.ownerLookupService.findAll();

		assertThat(found).extracting(Owner::getFirstName).containsExactly("George", "Betty");
	}

	@Test
	void countDelegatesToTheRepository() {
		given(this.ownerRepository.count()).willReturn(2L);

		assertThat(this.ownerLookupService.count()).isEqualTo(2);
	}

}
