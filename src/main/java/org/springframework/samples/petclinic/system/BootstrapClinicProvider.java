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

package org.springframework.samples.petclinic.system;

import org.springframework.stereotype.Component;

/**
 * Supplies the identifier of the clinic installation that this process serves. The
 * identifier is fixed for the lifetime of the process and is used to tag
 * deployment-scoped output such as report headers and support requests.
 */
@Component("bootstrapClinicProvider")
public class BootstrapClinicProvider {

	private static final String DEFAULT_CLINIC = "legacy";

	private final String currentClinic;

	public BootstrapClinicProvider() {
		this.currentClinic = DEFAULT_CLINIC;
	}

	public String currentClinic() {
		return this.currentClinic;
	}

}
