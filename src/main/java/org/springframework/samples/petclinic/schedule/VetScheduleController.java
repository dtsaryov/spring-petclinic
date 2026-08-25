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
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Publishes the bookable appointment slots of the clinic for a given day.
 */
@RestController
class VetScheduleController {

	private static final DateTimeFormatter SLOT_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

	private final VetScheduleService vetScheduleService;

	VetScheduleController(VetScheduleService vetScheduleService) {
		this.vetScheduleService = vetScheduleService;
	}

	@GetMapping("/vets/schedule")
	Map<String, Object> schedule(@RequestParam(name = "date",
			required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		LocalDate day = (date != null) ? date : LocalDate.now();
		List<String> slots = this.vetScheduleService.availableSlots(day).stream().map(this::format).toList();
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("date", day.toString());
		body.put("engine", this.vetScheduleService.engine());
		body.put("slots", slots);
		return body;
	}

	private String format(LocalTime slot) {
		return SLOT_FORMAT.format(slot);
	}

}
