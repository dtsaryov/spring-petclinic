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
package org.springframework.samples.petclinic.billing;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests that visits are settled through the clinic's default payment channel.
 */
class VisitBillingServiceTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withUserConfiguration(BillingConfiguration.class);

	@Test
	void settlesVisitsThroughTheCardTerminal() {
		this.contextRunner.run((context) -> {
			VisitBillingService service = context.getBean(VisitBillingService.class);
			BillingReceipt receipt = service.settleVisit(1, new BigDecimal("42.50"));
			assertThat(receipt.channel()).isEqualTo("card");
			assertThat(receipt.reference()).startsWith("CARD-");
			assertThat(service.findReceipt(1)).isEqualTo(receipt);
		});
	}

	@Configuration(proxyBeanMethods = false)
	@ComponentScan("org.springframework.samples.petclinic.billing")
	static class BillingConfiguration {

	}

}
