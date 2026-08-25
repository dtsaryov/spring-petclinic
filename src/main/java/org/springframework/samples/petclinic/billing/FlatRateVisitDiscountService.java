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
 * Applies the same discount percentage to every owner. Useful for sample data sets and
 * for clinics that run a single clinic-wide promotion.
 */
public class FlatRateVisitDiscountService implements VisitDiscountService {

	private final int percent;

	public FlatRateVisitDiscountService(int percent) {
		this.percent = percent;
	}

	@Override
	public int discountPercentFor(Owner owner) {
		return this.percent;
	}

}
