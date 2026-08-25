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

import org.springframework.stereotype.Service;

/**
 * Composes the visit reminder messages sent to pet owners and passes them to the
 * configured {@link ReminderSender}.
 */
@Service("visitReminderService")
public class VisitReminderService {

	private static final String TEST_RECIPIENT = "reminders@petclinic.example.com";

	private static final String TEST_SUBJECT = "PetClinic visit reminder (test)";

	private static final String TEST_BODY = "This is a test of the PetClinic visit reminder delivery.";

	private final ReminderSender reminderSender;

	VisitReminderService(ReminderSender reminderSender) {
		this.reminderSender = reminderSender;
	}

	/**
	 * Send a reminder for an upcoming visit.
	 * @param recipient the owner's email address
	 * @param petName the name of the pet the visit is for
	 * @param visitDate the date of the visit, formatted for display
	 */
	public void sendVisitReminder(String recipient, String petName, String visitDate) {
		this.reminderSender.send(recipient, "PetClinic visit reminder",
				"Reminder: %s has an appointment on %s.".formatted(petName, visitDate));
	}

	/**
	 * Send a reminder to the clinic's own mailbox so that delivery can be checked.
	 */
	public void sendTestReminder() {
		this.reminderSender.send(TEST_RECIPIENT, TEST_SUBJECT, TEST_BODY);
	}

}
