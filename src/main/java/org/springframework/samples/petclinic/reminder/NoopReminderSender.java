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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Discards reminder messages so that developer and test runs never contact real pet
 * owners.
 */
@Component("noopReminderSender")
@Profile("!prod")
class NoopReminderSender implements ReminderSender {

	private static final Logger logger = LoggerFactory.getLogger(NoopReminderSender.class);

	@Override
	public void send(String recipient, String subject, String body) {
		logger.debug("Discarding visit reminder '{}' addressed to {}", subject, recipient);
	}

	@Override
	public int deliveredCount() {
		return 0;
	}

}
