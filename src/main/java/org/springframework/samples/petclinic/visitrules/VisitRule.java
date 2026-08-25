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

/**
 * A single visit-triage rule. Rules are consulted in the order the container hands them
 * to the {@link VisitRuleChain} and the first rule that supports the request decides the
 * outcome.
 */
public interface VisitRule {

	/**
	 * Whether this rule is able to triage the given request.
	 * @param request the visit to triage
	 * @return {@code true} if this rule can decide the outcome
	 */
	boolean supports(VisitTriageRequest request);

	/**
	 * Produce the triage decision for the given request.
	 * @param request the visit to triage
	 * @return the decision
	 */
	VisitTriageDecision triage(VisitTriageRequest request);

	/**
	 * The name reported back to the caller as the rule that handled the visit.
	 * @return the rule name
	 */
	String name();

}
