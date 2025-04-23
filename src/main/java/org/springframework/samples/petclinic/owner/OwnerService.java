package org.springframework.samples.petclinic.owner;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

	private final OwnerRepository ownerRepository;

	public OwnerService(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	@Transactional
	public void foo() {
		var foo = 42;
	}
}
