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

package org.springframework.samples.petclinic.billing;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.billing.VisitDiscountController.VisitDiscount;
import org.springframework.web.client.RestTemplate;

/**
 * Checks that the running application quotes the clinic's real multi-pet discount.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class VisitDiscountIntegrationTests {

	/**
	 * Owner 3 of the sample data set has two pets and therefore qualifies for the
	 * multi-pet discount.
	 */
	private static final int MULTI_PET_OWNER_ID = 3;

	@LocalServerPort
	int port;

	@Autowired
	private RestTemplateBuilder builder;

	@Autowired
	private VisitDiscountService discounts;

	@Test
	void quotesTheMultiPetDiscount() {
		RestTemplate template = this.builder.baseUri("http://localhost:" + this.port).build();
		ResponseEntity<VisitDiscount> result = template
			.exchange(RequestEntity.get("/owners/" + MULTI_PET_OWNER_ID + "/discount").build(), VisitDiscount.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().petCount()).isEqualTo(2);
		assertThat(result.getBody().discountPercent()).isEqualTo(10);
	}

	@Test
	void usesTheStandardDiscountService() {
		assertThat(this.discounts).isInstanceOf(StandardVisitDiscountService.class);
	}

}
