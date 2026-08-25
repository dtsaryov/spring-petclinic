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

/**
 * Settles the amount charged for a visit through one of the clinic's payment channels.
 */
public interface BillingProcessor {

	/**
	 * The payment channel this processor settles through.
	 * @return the channel name, as printed on the receipt
	 */
	String getChannel();

	/**
	 * Settles the given amount for the given visit.
	 * @param visitId the identifier of the visit being paid for
	 * @param amount the amount to settle
	 * @return the resulting receipt
	 */
	BillingReceipt settle(int visitId, BigDecimal amount);

}
