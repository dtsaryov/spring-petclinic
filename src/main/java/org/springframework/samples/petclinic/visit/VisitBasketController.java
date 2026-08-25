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

package org.springframework.samples.petclinic.visit;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

/**
 * Lets an owner collect the clinic services they want before a visit is booked.
 */
@RestController
@RequestMapping("/visit-basket")
class VisitBasketController {

	private static final String BOOKING_REFERENCE = "bookingReference";

	private final VisitBasket visitBasket;

	VisitBasketController(VisitBasket visitBasket) {
		this.visitBasket = visitBasket;
	}

	@PostMapping("/items")
	Map<String, Object> addItem(@RequestParam("description") String description, HttpSession session) {
		this.visitBasket.addItem(description);
		return basketView(session);
	}

	@GetMapping("/items")
	Map<String, Object> listItems(HttpSession session) {
		return basketView(session);
	}

	@PostMapping("/clear")
	Map<String, Object> clear(HttpSession session) {
		this.visitBasket.clear();
		return basketView(session);
	}

	/**
	 * Renders the basket together with the booking reference that identifies the owner's
	 * current booking, so the same reference can be quoted on every follow-up request.
	 */
	private Map<String, Object> basketView(HttpSession session) {
		Object reference = session.getAttribute(BOOKING_REFERENCE);
		if (reference == null) {
			reference = UUID.randomUUID().toString().substring(0, 8);
			session.setAttribute(BOOKING_REFERENCE, reference);
		}
		Map<String, Object> view = new LinkedHashMap<>();
		view.put(BOOKING_REFERENCE, reference);
		view.put("items", this.visitBasket.getItems());
		return view;
	}

}
