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

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Applies the configured {@link VisitRule rules} to a visit. Spring injects the rules
 * sorted by their declared order, and the first rule that supports the request decides
 * the outcome.
 */
@Component
public class VisitRuleChain {

	private final List<VisitRule> rules;

	public VisitRuleChain(List<VisitRule> rules) {
		this.rules = List.copyOf(rules);
	}

	public VisitTriageDecision triage(VisitTriageRequest request) {
		for (VisitRule rule : this.rules) {
			if (rule.supports(request)) {
				return rule.triage(request);
			}
		}
		throw new IllegalStateException("No visit rule was able to triage a " + request.type() + " visit");
	}

	public List<VisitRule> getRules() {
		return this.rules;
	}

}
