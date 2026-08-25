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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Second generation scheduling engine. It offers twenty minute slots, opens on Saturday
 * mornings and reserves a lunch break for the vet.
 */
public class V2VetScheduleService implements VetScheduleService {

	private static final LocalTime OPENING = LocalTime.of(8, 0);

	private static final LocalTime CLOSING = LocalTime.of(18, 0);

	private static final LocalTime SATURDAY_CLOSING = LocalTime.of(13, 0);

	private static final LocalTime LUNCH_START = LocalTime.of(12, 0);

	private static final LocalTime LUNCH_END = LocalTime.of(13, 0);

	@Override
	public String engine() {
		return "v2";
	}

	@Override
	public List<LocalTime> availableSlots(LocalDate day) {
		if (day.getDayOfWeek() == DayOfWeek.SUNDAY) {
			return List.of();
		}
		LocalTime closing = (day.getDayOfWeek() == DayOfWeek.SATURDAY) ? SATURDAY_CLOSING : CLOSING;
		List<LocalTime> slots = new ArrayList<>();
		for (LocalTime slot = OPENING; slot.isBefore(closing); slot = slot.plusMinutes(20)) {
			if (!slot.isBefore(LUNCH_START) && slot.isBefore(LUNCH_END)) {
				continue;
			}
			slots.add(slot);
		}
		return List.copyOf(slots);
	}

}
