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
package org.springframework.samples.petclinic.owner;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Contact details of an {@link Owner} that may be maintained through the API.
 *
 * @param address the street address of the owner
 * @param city the city the owner lives in
 * @param telephone the ten digit telephone number of the owner
 */
public record OwnerContactDetails(@NotBlank String address, @NotBlank String city,
		@NotBlank @Pattern(regexp = "\\d{10}", message = "{telephone.invalid}") String telephone) {
}
