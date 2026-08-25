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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

/**
 * Jackson setup for the JSON resources exposed by the application. Dates are rendered in
 * the day-first format the clinic staff use on their paper records rather than in the
 * Jackson defaults.
 */
@Configuration(proxyBeanMethods = false)
public class ApiJacksonConfiguration {

	/**
	 * The date format used by the JSON resources of the application.
	 */
	public static final String DATE_PATTERN = "dd/MM/yyyy";

	@Bean
	public ObjectMapper apiObjectMapper() {
		SimpleModule dates = new SimpleModule("petclinic-api-dates");
		dates.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_PATTERN)));
		return JsonMapper.builder().addModule(dates).build();
	}

}
