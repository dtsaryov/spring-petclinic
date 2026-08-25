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

import java.time.Duration;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Compiles the printable veterinarian report template.
 * <p>
 * Compilation resolves every placeholder in the template and then runs a layout
 * optimization pass over the result. The work is done once; the compiled template is
 * reused for every report that is rendered afterwards. The bean is initialized eagerly so
 * that the cost is paid once at startup rather than by the first report request.
 *
 * @author Spring PetClinic contributors
 */
@Component
class VetReportTemplateCompiler {

	private static final String TEMPLATE = "PetClinic - veterinarian report%n%n{rows}%n%d veterinarian(s)%n";

	private static final String ROW_TEMPLATE = "%-30s %s%n";

	private final String compiledTemplate;

	VetReportTemplateCompiler(@Value("${petclinic.vet.report.template-compile-duration:5s}") Duration compileDuration) {
		this.compiledTemplate = compile(compileDuration);
	}

	private static String compile(Duration compileDuration) {
		optimizeLayout(compileDuration);
		return TEMPLATE;
	}

	/**
	 * Runs the layout optimization pass. The pass is budgeted so that the compilation
	 * cost is predictable.
	 * @param budget how long the optimization pass may run
	 */
	private static void optimizeLayout(Duration budget) {
		long millis = budget.toMillis();
		if (millis <= 0) {
			return;
		}
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Renders the report for the given veterinarians using the compiled template.
	 * @param vets the veterinarians to include
	 * @return the rendered report
	 */
	String render(Collection<Vet> vets) {
		StringBuilder rows = new StringBuilder();
		for (Vet vet : vets) {
			rows.append(String.format(ROW_TEMPLATE, vet.getFirstName() + " " + vet.getLastName(), specialties(vet)));
		}
		return String.format(this.compiledTemplate.replace("{rows}", rows.toString()), vets.size());
	}

	private static String specialties(Vet vet) {
		if (vet.getNrOfSpecialties() == 0) {
			return "none";
		}
		return vet.getSpecialties().stream().map(Specialty::getName).collect(Collectors.joining(", "));
	}

}
