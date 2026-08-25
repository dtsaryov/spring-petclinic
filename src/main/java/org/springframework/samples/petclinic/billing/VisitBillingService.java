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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

/**
 * Settles visits and keeps the receipts issued during this run so that the reception desk
 * can look them up again.
 */
@Service
public class VisitBillingService {

	private final BillingProcessor billingProcessor;

	private final Map<Integer, BillingReceipt> receipts = new ConcurrentHashMap<>();

	public VisitBillingService(BillingProcessor billingProcessor) {
		this.billingProcessor = billingProcessor;
	}

	/**
	 * Settles a visit through the clinic's default payment channel.
	 * @param visitId the identifier of the visit being paid for
	 * @param amount the amount to settle
	 * @return the issued receipt
	 */
	public BillingReceipt settleVisit(int visitId, BigDecimal amount) {
		BillingReceipt receipt = this.billingProcessor.settle(visitId, amount);
		this.receipts.put(visitId, receipt);
		return receipt;
	}

	/**
	 * Returns the receipt issued for a visit, if it has already been settled.
	 * @param visitId the identifier of the visit
	 * @return the receipt, or {@code null} if the visit has not been settled yet
	 */
	public BillingReceipt findReceipt(int visitId) {
		return this.receipts.get(visitId);
	}

}
