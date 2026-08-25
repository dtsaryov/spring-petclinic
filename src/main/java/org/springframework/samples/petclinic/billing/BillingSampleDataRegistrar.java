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

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * Registers the billing beans that belong to the bundled sample data set. The sample
 * clinic runs no promotion, so visits are billed at the full rate.
 */
@Component
public class BillingSampleDataRegistrar implements BeanDefinitionRegistryPostProcessor {

	private static final String DISCOUNT_SERVICE_BEAN_NAME = "visitDiscountService";

	private static final int SAMPLE_DISCOUNT_PERCENT = 0;

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
		RootBeanDefinition discountService = new RootBeanDefinition(FlatRateVisitDiscountService.class);
		discountService.getConstructorArgumentValues().addGenericArgumentValue(SAMPLE_DISCOUNT_PERCENT);
		registry.registerBeanDefinition(DISCOUNT_SERVICE_BEAN_NAME, discountService);
	}

}
