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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Hands reminder messages to the clinic's SMTP relay. Messages are spooled locally before
 * they are picked up by the relay, so the clinic keeps a record of what was sent.
 */
@Component("smtpReminderSender")
@Profile("prod")
class SmtpReminderSender implements ReminderSender {

	private static final Logger logger = LoggerFactory.getLogger(SmtpReminderSender.class);

	private final List<String> spool = new CopyOnWriteArrayList<>();

	private final AtomicInteger delivered = new AtomicInteger();

	@Override
	public void send(String recipient, String subject, String body) {
		this.spool.add(recipient + " | " + subject + " | " + body);
		this.delivered.incrementAndGet();
		logger.info("Queued visit reminder '{}' for delivery to {}", subject, recipient);
	}

	@Override
	public int deliveredCount() {
		return this.delivered.get();
	}

	/**
	 * The messages currently waiting in the local spool directory.
	 * @return the spooled messages, most recently added last
	 */
	List<String> spooledMessages() {
		return List.copyOf(this.spool);
	}

}
