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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * Renders the veterinarian JSON resource. The payload is converted up front so that the
 * representation stays the same whichever view ends up writing the response.
 * <p>
 * The mapper is taken from the context when one is published there and a stand-alone
 * mapper is used otherwise, which keeps the component usable outside a fully configured
 * application.
 */
@Component
public class VetJsonCompatibility {

	private final ObjectMapper mapper;

	public VetJsonCompatibility(BeanFactory beanFactory) {
		this.mapper = resolveMapper(beanFactory);
	}

	private static ObjectMapper resolveMapper(BeanFactory beanFactory) {
		try {
			return beanFactory.getBean("objectMapper", ObjectMapper.class);
		}
		catch (BeansException ex) {
			return JsonMapper.builder().build();
		}
	}

	public JsonNode toJson(Object payload) {
		return this.mapper.valueToTree(payload);
	}

}
