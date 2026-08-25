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
 * The outcome of settling a visit, including the channel that took the money.
 *
 * @param visitId the identifier of the visit that was paid for
 * @param amount the settled amount
 * @param channel the payment channel that settled the visit
 * @param reference the reference printed on the receipt
 */
public record BillingReceipt(int visitId, BigDecimal amount, String channel, String reference) {
}
