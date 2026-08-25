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

package org.springframework.samples.petclinic.visitrules;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Places a visit in the standard consultation queue, so that no visit is ever left
 * without a triage decision.
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
class FallbackVisitRule implements VisitRule {

	@Override
	public boolean supports(VisitTriageRequest request) {
		return true;
	}

	@Override
	public VisitTriageDecision triage(VisitTriageRequest request) {
		return new VisitTriageDecision("ROUTINE", "standard-consultation", name());
	}

	@Override
	public String name() {
		return "fallback";
	}

}
