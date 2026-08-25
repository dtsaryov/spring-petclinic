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

package org.springframework.samples.petclinic.schedule;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Selects the scheduling engine the clinic runs on. The second generation engine is
 * opt-in through {@code petclinic.schedule.v2.enabled}; the original engine stays in
 * place for every deployment that has not switched over yet.
 */
@Configuration(proxyBeanMethods = false)
class VetScheduleConfiguration {

	@Bean
	@ConditionalOnProperty(name = "petclinic.schedule.v2.enabled", havingValue = "true")
	VetScheduleService v2VetScheduleService() {
		return new V2VetScheduleService();
	}

	@Bean
	@ConditionalOnMissingBean(VetScheduleService.class)
	VetScheduleService legacyVetScheduleService() {
		return new LegacyVetScheduleService();
	}

}
