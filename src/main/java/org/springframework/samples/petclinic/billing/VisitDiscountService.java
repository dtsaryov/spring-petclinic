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

import org.springframework.samples.petclinic.owner.Owner;

/**
 * Calculates the discount that applies to the visits of a given owner.
 */
public interface VisitDiscountService {

	/**
	 * Return the discount percentage, between 0 and 100, that applies to the visits
	 * booked by the given owner.
	 * @param owner the owner the visits are billed to
	 * @return the discount percentage to apply
	 */
	int discountPercentFor(Owner owner);

}
