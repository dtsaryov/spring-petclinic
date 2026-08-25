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
import java.util.List;

/**
 * Computes the appointment slots a veterinarian can be booked into on a given day.
 */
public interface VetScheduleService {

	/**
	 * Identifier of the scheduling engine backing this service.
	 * @return the engine identifier
	 */
	String engine();

	/**
	 * Appointment slots offered for the given day.
	 * @param day the day to build the schedule for
	 * @return the start time of every bookable slot, in chronological order
	 */
	List<LocalTime> availableSlots(LocalDate day);

}
