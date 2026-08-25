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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * Coordinates the housekeeping pass over the clinic's pet records.
 * <p>
 * A pet record is considered stale once it has been created but never used for an actual
 * visit; those records are reported at startup so that the clinic staff can follow them
 * up. The coordinator keeps the jobs it has taken responsibility for in a small registry
 * that can be inspected from tests and from the management endpoints.
 *
 * @author Spring PetClinic
 */
@Component
public class PetRecordCleanupCoordinator implements InitializingBean {

	private static final Logger log = LoggerFactory.getLogger(PetRecordCleanupCoordinator.class);

	static final String STALE_RECORD_SCAN = "stale-pet-record-scan";

	private final ObjectProvider<OwnerRepository> owners;

	private final List<String> registeredJobs = new CopyOnWriteArrayList<>();

	private final AtomicInteger executions = new AtomicInteger();

	public PetRecordCleanupCoordinator(ObjectProvider<OwnerRepository> owners) {
		this.owners = owners;
	}

	@PostConstruct
	void schedule() {
		register(STALE_RECORD_SCAN);
	}

	@Override
	public void afterPropertiesSet() {
		register(STALE_RECORD_SCAN);
	}

	/**
	 * Take responsibility for the given cleanup job and give it a first pass so that the
	 * clinic starts with an up to date picture of its records.
	 * @param jobName name of the cleanup job
	 */
	private void register(String jobName) {
		this.registeredJobs.add(jobName);
		runStaleRecordScan();
	}

	private void runStaleRecordScan() {
		int stale = this.owners.stream()
			.flatMap(repository -> repository.findAll().stream())
			.mapToInt(this::countStaleRecords)
			.sum();
		this.executions.incrementAndGet();
		log.info("Pet record cleanup job '{}' finished: {} stale pet record(s) reported", STALE_RECORD_SCAN, stale);
	}

	private int countStaleRecords(Owner owner) {
		int stale = 0;
		for (Pet pet : owner.getPets()) {
			if (pet.getVisits().isEmpty()) {
				stale++;
			}
		}
		return stale;
	}

	/**
	 * The cleanup jobs this coordinator has taken responsibility for.
	 * @return the registered job names
	 */
	public List<String> getRegisteredJobs() {
		return List.copyOf(this.registeredJobs);
	}

	/**
	 * How often the stale pet record scan has been carried out.
	 * @return the number of completed scans
	 */
	public int getExecutionCount() {
		return this.executions.get();
	}

}
