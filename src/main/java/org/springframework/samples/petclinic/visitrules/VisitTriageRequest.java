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
 * The details the front desk submits when it asks the clinic to triage a visit.
 *
 * @param type the kind of visit as recorded by the front desk, for example
 * {@code routine} or {@code emergency}
 * @param description the free-text description of the visit
 */
public record VisitTriageRequest(String type, String description) {

	public boolean hasType(String candidate) {
		return this.type != null && this.type.equalsIgnoreCase(candidate);
	}

	public boolean descriptionContains(String keyword) {
		return this.description != null && this.description.toLowerCase().contains(keyword);
	}

}
