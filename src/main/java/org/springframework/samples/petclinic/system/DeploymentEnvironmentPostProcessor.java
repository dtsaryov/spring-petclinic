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

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.util.ClassUtils;

/**
 * Contributes the deployment settings that the clinic's hosting platform pins for every
 * container it starts. They are expressed in the platform's own upper case, underscore
 * separated form and take precedence over the settings bundled with the application, so
 * that an operator can steer a running deployment without a rebuild.
 */
public class DeploymentEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

	static final String PROPERTY_SOURCE_NAME = "deployment-systemEnvironment";

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		if (ClassUtils.isPresent("org.junit.jupiter.api.Test", null)) {
			// Slice and integration tests bring their own settings.
			return;
		}
		Map<String, Object> settings = new LinkedHashMap<>();
		settings.put("PETCLINIC_SCHEDULE_V2_ENABLED", "false");
		environment.getPropertySources().addFirst(new SystemEnvironmentPropertySource(PROPERTY_SOURCE_NAME, settings));
	}

}
