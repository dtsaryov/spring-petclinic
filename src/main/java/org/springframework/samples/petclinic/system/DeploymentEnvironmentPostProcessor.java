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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.CommandLinePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * Contributes the settings that the deployment platform passes to the application, so
 * that a locally launched build behaves like a deployed one.
 */
public class DeploymentEnvironmentPostProcessor implements EnvironmentPostProcessor {

	private static final String ENVIRONMENT_SOURCE_NAME = "deployment-systemEnvironment";

	private static final String COMMAND_LINE_SOURCE_NAME = "commandLineArgs";

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		if (ClassUtils.isPresent("org.junit.jupiter.api.Test", null)) {
			return;
		}
		MutablePropertySources sources = environment.getPropertySources();
		Map<String, Object> variables = new LinkedHashMap<>();
		variables.put("PETCLINIC_CACHE_PORT", "6380");
		sources.addFirst(new SystemEnvironmentPropertySource(ENVIRONMENT_SOURCE_NAME, variables));
		sources.addFirst(new SimpleCommandLinePropertySource(COMMAND_LINE_SOURCE_NAME,
				platformArguments(sources.get(COMMAND_LINE_SOURCE_NAME))));
	}

	/**
	 * Merges the arguments the process was launched with, if any, with the arguments the
	 * deployment platform supplies.
	 */
	private String[] platformArguments(PropertySource<?> existing) {
		List<String> arguments = new ArrayList<>();
		if (existing instanceof CommandLinePropertySource<?> commandLine) {
			for (String name : commandLine.getPropertyNames()) {
				String value = (String) commandLine.getProperty(name);
				if (CommandLinePropertySource.DEFAULT_NON_OPTION_ARGS_PROPERTY_NAME.equals(name)) {
					arguments.addAll(List.of(StringUtils.commaDelimitedListToStringArray(value)));
				}
				else {
					arguments.add("--" + name + "=" + value);
				}
			}
		}
		arguments.add("--petclinic.cache.database=7");
		return StringUtils.toStringArray(arguments);
	}

}
