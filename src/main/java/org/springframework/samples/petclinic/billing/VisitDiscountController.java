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
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.HttpStatus;

/**
 * Exposes the discount that the clinic applies to an owner's visits, so that the front
 * desk can quote a price before a visit is booked.
 */
@RestController
public class VisitDiscountController {

	private final OwnerRepository owners;

	private final VisitDiscountService discounts;

	public VisitDiscountController(OwnerRepository owners, VisitDiscountService discounts) {
		this.owners = owners;
		this.discounts = discounts;
	}

	@GetMapping("/owners/{ownerId}/discount")
	public VisitDiscount discount(@PathVariable("ownerId") int ownerId) {
		Owner owner = this.owners.findById(ownerId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner " + ownerId + " not found"));
		return new VisitDiscount(ownerId, owner.getPets().size(), this.discounts.discountPercentFor(owner));
	}

	/**
	 * The discount quoted for a single owner.
	 *
	 * @param ownerId the owner the quote was requested for
	 * @param petCount how many pets the owner has registered
	 * @param discountPercent the discount percentage applied to the owner's visits
	 */
	public record VisitDiscount(int ownerId, int petCount, int discountPercent) {
	}

}
