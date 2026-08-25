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

package org.springframework.samples.petclinic.visit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the visit basket endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
class VisitBasketControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void addedItemIsListedForTheSameSession() throws Exception {
		MockHttpSession session = new MockHttpSession();

		this.mockMvc.perform(post("/visit-basket/clear").session(session)).andExpect(status().isOk());
		this.mockMvc.perform(post("/visit-basket/items").session(session).param("description", "Dental cleaning"))
			.andExpect(status().isOk());

		this.mockMvc.perform(get("/visit-basket/items").session(session))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.items", contains("Dental cleaning")));
	}

}
