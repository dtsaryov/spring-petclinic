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

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Settles visits paid for in cash at the reception desk.
 */
@Component
@Primary
public class CashBillingProcessor implements BillingProcessor {

	private final AtomicLong sequence = new AtomicLong();

	@Override
	public String getChannel() {
		return "cash";
	}

	@Override
	public BillingReceipt settle(int visitId, BigDecimal amount) {
		String reference = "CASH-%d-%04d".formatted(visitId, this.sequence.incrementAndGet());
		return new BillingReceipt(visitId, amount, getChannel(), reference);
	}

}
