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
package org.springframework.samples.petclinic.vet;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Serves the printable veterinarian report.
 *
 * @author Spring PetClinic contributors
 */
@Controller
class VetReportController {

	private final VetRepository vetRepository;

	private final VetReportTemplateCompiler templateCompiler;

	VetReportController(VetRepository vetRepository, VetReportTemplateCompiler templateCompiler) {
		this.vetRepository = vetRepository;
		this.templateCompiler = templateCompiler;
	}

	@GetMapping(path = "/vets/report", produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String showVetReport() {
		return this.templateCompiler.render(this.vetRepository.findAll());
	}

}
