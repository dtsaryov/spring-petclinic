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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that the veterinarian report template is compiled while the application
 * starts, so that no request has to wait for the compilation to finish.
 */
@SpringBootTest(properties = "petclinic.vet.report.template-compile-duration=0s")
class VetReportTemplateCompilerIntegrationTests {

	@Autowired
	private ConfigurableApplicationContext context;

	@Test
	void templateCompilerIsInitializedDuringStartup() {
		ConfigurableListableBeanFactory beanFactory = this.context.getBeanFactory();

		assertThat(beanFactory.getBeanDefinition("vetReportTemplateCompiler").isLazyInit())
			.as("the report template compiler must not be initialized on first use")
			.isFalse();
		assertThat(beanFactory.containsSingleton("vetReportTemplateCompiler"))
			.as("the report template compiler must already be initialized once the context is up")
			.isTrue();
	}

}
