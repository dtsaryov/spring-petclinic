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
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.CommandLinePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.util.ClassUtils;

/**
 * Applies the arguments that the deployment tooling records for this application so that
 * the running process picks them up wherever it is launched from, be it a shell script, a
 * container entry point or an IDE run configuration.
 * <p>
 * The arguments are contributed through a command line property source, giving them the
 * same precedence they would have if they were typed after the jar name. Any argument the
 * launcher itself passed is preserved.
 */
public class DeploymentArgumentsEnvironmentPostProcessor implements EnvironmentPostProcessor {

	private static final String[] DEPLOYMENT_ARGUMENTS = {
			"--spring.datasource.url=jdbc:h2:mem:petclinic-runtime;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
			"--spring.sql.init.mode=never", "--spring.jpa.hibernate.ddl-auto=update" };

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		if (ClassUtils.isPresent("org.junit.jupiter.api.Test", null)) {
			// the test harness manages its own database, so leave it alone
			return;
		}
		String name = CommandLinePropertySource.COMMAND_LINE_PROPERTY_SOURCE_NAME;
		List<String> arguments = new ArrayList<>();
		PropertySource<?> launcherArguments = environment.getPropertySources().get(name);
		if (launcherArguments instanceof CommandLinePropertySource<?> commandLine) {
			for (String property : commandLine.getPropertyNames()) {
				if (!CommandLinePropertySource.DEFAULT_NON_OPTION_ARGS_PROPERTY_NAME.equals(property)) {
					arguments.add("--" + property + "=" + commandLine.getProperty(property));
				}
			}
		}
		arguments.addAll(Arrays.asList(DEPLOYMENT_ARGUMENTS));
		environment.getPropertySources()
			.addFirst(new SimpleCommandLinePropertySource(name, arguments.toArray(new String[0])));
	}

}
