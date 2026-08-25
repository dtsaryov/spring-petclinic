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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests for the individual {@link VisitRule} implementations.
 */
class VisitRuleTests {

	private final EmergencyVisitRule emergencyRule = new EmergencyVisitRule();

	private final FallbackVisitRule fallbackRule = new FallbackVisitRule();

	@Test
	void emergencyRuleSupportsUrgentVisits() {
		assertThat(this.emergencyRule.supports(new VisitTriageRequest("emergency", "hit by a car"))).isTrue();
		assertThat(this.emergencyRule.supports(new VisitTriageRequest("routine", "Urgent: bleeding paw"))).isTrue();
	}

	@Test
	void emergencyRuleIgnoresRoutineVisits() {
		assertThat(this.emergencyRule.supports(new VisitTriageRequest("routine", "annual check-up"))).isFalse();
	}

	@Test
	void emergencyRuleSendsVisitsToTheEmergencyQueue() {
		VisitTriageDecision decision = this.emergencyRule.triage(new VisitTriageRequest("emergency", "hit by a car"));
		assertThat(decision.priority()).isEqualTo("IMMEDIATE");
		assertThat(decision.queue()).isEqualTo("emergency");
		assertThat(decision.handledBy()).isEqualTo("emergency");
	}

	@Test
	void fallbackRuleSupportsEveryVisit() {
		assertThat(this.fallbackRule.supports(new VisitTriageRequest("routine", "annual check-up"))).isTrue();
		assertThat(this.fallbackRule.supports(new VisitTriageRequest(null, null))).isTrue();
	}

	@Test
	void fallbackRuleSendsVisitsToTheStandardQueue() {
		VisitTriageDecision decision = this.fallbackRule.triage(new VisitTriageRequest("routine", "annual check-up"));
		assertThat(decision.priority()).isEqualTo("ROUTINE");
		assertThat(decision.queue()).isEqualTo("standard-consultation");
		assertThat(decision.handledBy()).isEqualTo("fallback");
	}

	@Test
	void chainUsesTheFirstMatchingRule() {
		VisitRuleChain chain = new VisitRuleChain(List.of(this.emergencyRule, this.fallbackRule));
		assertThat(chain.triage(new VisitTriageRequest("routine", "annual check-up")).handledBy())
			.isEqualTo("fallback");
	}

}
