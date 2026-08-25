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
import org.springframework.stereotype.Service;

/**
 * The clinic's standard multi-pet discount: owners who bring more than one pet to the
 * clinic pay less for every visit.
 */
@Service("visitDiscountService")
public class StandardVisitDiscountService implements VisitDiscountService {

	private static final int MULTI_PET_DISCOUNT_PERCENT = 10;

	private static final int LARGE_HOUSEHOLD_DISCOUNT_PERCENT = 15;

	private static final int LARGE_HOUSEHOLD_SIZE = 3;

	@Override
	public int discountPercentFor(Owner owner) {
		int pets = (owner != null) ? owner.getPets().size() : 0;
		if (pets >= LARGE_HOUSEHOLD_SIZE) {
			return LARGE_HOUSEHOLD_DISCOUNT_PERCENT;
		}
		if (pets > 1) {
			return MULTI_PET_DISCOUNT_PERCENT;
		}
		return 0;
	}

}
