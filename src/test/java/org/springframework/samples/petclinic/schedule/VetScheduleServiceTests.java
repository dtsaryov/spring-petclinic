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

package org.springframework.samples.petclinic.schedule;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the two scheduling engines.
 */
class VetScheduleServiceTests {

	private static final LocalDate WEDNESDAY = LocalDate.of(2025, 6, 4);

	private static final LocalDate SATURDAY = LocalDate.of(2025, 6, 7);

	private final VetScheduleService legacy = new LegacyVetScheduleService();

	private final VetScheduleService v2 = new V2VetScheduleService();

	@Test
	void legacyEngineOffersHourlyWeekdaySlots() {
		assertThat(this.legacy.engine()).isEqualTo("legacy");
		assertThat(this.legacy.availableSlots(WEDNESDAY)).hasSize(8)
			.startsWith(LocalTime.of(9, 0))
			.endsWith(LocalTime.of(16, 0));
		assertThat(this.legacy.availableSlots(SATURDAY)).isEmpty();
	}

	@Test
	void v2EngineSkipsLunchAndOpensOnSaturdayMorning() {
		assertThat(this.v2.engine()).isEqualTo("v2");
		assertThat(this.v2.availableSlots(WEDNESDAY)).doesNotContain(LocalTime.of(12, 0), LocalTime.of(12, 20))
			.startsWith(LocalTime.of(8, 0))
			.endsWith(LocalTime.of(17, 40));
		assertThat(this.v2.availableSlots(SATURDAY)).endsWith(LocalTime.of(11, 40));
	}

}
