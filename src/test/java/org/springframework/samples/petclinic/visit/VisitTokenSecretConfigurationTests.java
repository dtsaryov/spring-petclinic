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

import java.io.IOException;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Checks how the shipped configuration sources the visit confirmation signing secret. The
 * secret has to survive restarts and be identical on every instance, so the shipped
 * configuration must delegate to the deployment environment instead of generating or
 * embedding a value of its own.
 */
class VisitTokenSecretConfigurationTests {

	private static final String PROPERTY = "petclinic.visit-token-secret";

	@Test
	void theSecretIsSourcedFromTheDeploymentEnvironment() throws IOException {
		Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"));
		String declared = properties.getProperty(PROPERTY);
		assertThat(declared).as("%s must be declared in application.properties", PROPERTY).isNotNull();
		assertThat(declared).as("%s must not be generated per start", PROPERTY).doesNotContain("${random.");
		assertThat(declared).as("%s must be resolved from an external variable with no baked-in fallback", PROPERTY)
			.isEqualTo("${PETCLINIC_VISIT_TOKEN_SECRET:}");
	}

}
