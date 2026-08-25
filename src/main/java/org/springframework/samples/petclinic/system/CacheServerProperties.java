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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Connection settings of the shared cache server backing the application caches. The
 * values are supplied by the deployment configuration.
 */
@ConfigurationProperties("petclinic.cache")
public class CacheServerProperties {

	/**
	 * Host name of the cache server.
	 */
	private String host = "localhost";

	/**
	 * Port the cache server listens on.
	 */
	private int port = 6379;

	/**
	 * Index of the logical database to use on the cache server.
	 */
	private int database = 0;

	/**
	 * Whether the connection to the cache server is encrypted.
	 */
	private boolean ssl = false;

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getDatabase() {
		return this.database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public boolean isSsl() {
		return this.ssl;
	}

	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

	/**
	 * The cache server endpoint, in {@code scheme://host:port/database} form.
	 */
	public String getUri() {
		return (this.ssl ? "rediss://" : "redis://") + this.host + ":" + this.port + "/" + this.database;
	}

}
