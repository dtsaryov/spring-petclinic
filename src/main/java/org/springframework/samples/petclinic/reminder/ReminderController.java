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

package org.springframework.samples.petclinic.reminder;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lets an operator trigger a reminder delivery to verify the clinic's mail setup.
 */
@RestController
class ReminderController {

	private final VisitReminderService visitReminderService;

	ReminderController(VisitReminderService visitReminderService) {
		this.visitReminderService = visitReminderService;
	}

	@PostMapping("/reminders/test")
	ResponseEntity<Void> sendTestReminder() {
		this.visitReminderService.sendTestReminder();
		return ResponseEntity.accepted().build();
	}

}
