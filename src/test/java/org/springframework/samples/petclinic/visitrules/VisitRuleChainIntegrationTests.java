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

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * Tests that the rules are applied in the precedence the container resolves for them.
 */
class VisitRuleChainIntegrationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withUserConfiguration(VisitRuleChain.class, FallbackVisitRule.class, EmergencyVisitRule.class);

	@Test
	void emergencyVisitIsHandledByTheEmergencyRule() {
		this.contextRunner.run((context) -> {
			VisitTriageDecision decision = context.getBean(VisitRuleChain.class)
				.triage(new VisitTriageRequest("emergency", "hit by a car"));
			assertThat(decision.handledBy()).isEqualTo("emergency");
			assertThat(decision.queue()).isEqualTo("emergency");
			assertThat(decision.priority()).isEqualTo("IMMEDIATE");
		});
	}

	@Test
	void routineVisitIsHandledByTheFallbackRule() {
		this.contextRunner.run((context) -> {
			VisitTriageDecision decision = context.getBean(VisitRuleChain.class)
				.triage(new VisitTriageRequest("routine", "annual check-up"));
			assertThat(decision.handledBy()).isEqualTo("fallback");
		});
	}

	@Test
	void theFallbackRuleIsConsultedLast() {
		this.contextRunner.run((context) -> {
			VisitRuleChain chain = context.getBean(VisitRuleChain.class);
			assertThat(chain.getRules()).last().isInstanceOf(FallbackVisitRule.class);
		});
	}

}
