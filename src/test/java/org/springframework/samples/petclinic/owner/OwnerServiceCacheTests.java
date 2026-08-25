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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests that the {@code owners} cache stays coherent with the database when owner contact
 * details are updated.
 */
@SpringBootTest
class OwnerServiceCacheTests {

	private static final Integer OWNER_ID = 1;

	@Autowired
	private OwnerService ownerService;

	@Autowired
	private CacheManager cacheManager;

	private OwnerContactDetails original;

	@BeforeEach
	void captureOriginalDetails() {
		Owner owner = this.ownerService.findById(OWNER_ID);
		this.original = new OwnerContactDetails(owner.getAddress(), owner.getCity(), owner.getTelephone());
	}

	@AfterEach
	void restoreOriginalDetails() {
		this.ownerService.updateOwner(OWNER_ID, this.original);
	}

	@Test
	void updatedOwnerIsVisibleToSubsequentReads() {
		// warm the cache
		assertThat(this.ownerService.findById(OWNER_ID).getTelephone()).isEqualTo(this.original.telephone());

		this.ownerService.updateOwner(OWNER_ID,
				new OwnerContactDetails(this.original.address(), "Springfield", "5555550100"));

		Owner reread = this.ownerService.findById(OWNER_ID);
		assertThat(reread.getTelephone()).isEqualTo("5555550100");
		assertThat(reread.getCity()).isEqualTo("Springfield");
	}

	@Test
	void cacheDoesNotHoldTheSupersededOwner() {
		this.ownerService.findById(OWNER_ID);

		this.ownerService.updateOwner(OWNER_ID,
				new OwnerContactDetails(this.original.address(), "Springfield", "5555550100"));

		Cache cache = this.cacheManager.getCache("owners");
		assertThat(cache).isNotNull();
		Cache.ValueWrapper cached = cache.get(OWNER_ID);
		if (cached != null) {
			assertThat(((Owner) cached.get()).getTelephone()).isEqualTo("5555550100");
		}
	}

}
