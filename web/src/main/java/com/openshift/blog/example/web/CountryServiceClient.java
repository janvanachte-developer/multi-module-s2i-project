/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.openshift.blog.example.web;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openshift.blog.example.common.Constants;
import com.openshift.blog.example.common.model.CountryModel;

/**
 * A simple CDI service to query a RESTful endpoint and get a random country
 * name back
 *
 * @author benjaminholmes
 *
 */
@SessionScoped
public class CountryServiceClient implements Serializable {

	private static final Logger LOG = LoggerFactory.getLogger(CountryServiceClient.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 17182478623782L;

	private ResteasyClient client;

	/**
	 * Returns a random Country name from a web service
	 * 
	 * @return a Country name, or null if failure occurs
	 */
	public String getCountry() {
		LOG.debug("Entering getCountry()");

		String url = this.getServiceURL();

		String result = null;

		if (client != null && (url != null && !url.isEmpty())) {
			LOG.info("Calling web service...");

			ResteasyWebTarget target = client.target(url);
			Response response = target.request().get();
			CountryModel model = response.readEntity(CountryModel.class);
			result = model.getName();

			LOG.info("...web service responded");
		}

		LOG.debug("Leaving getCountry()");

		return result;

	}

	@PostConstruct
	protected void postConstruct() {
		LOG.debug("Entering postConstruct()");

		client = new ResteasyClientBuilder().build();

		LOG.debug("Leaving postConstruct()");
	}

	private String getServiceURL() {
		StringBuilder sb = new StringBuilder();

		String host = System.getenv(Constants.COUNTRY_SERVICE_HOST);
		String port = System.getenv(Constants.COUNTRY_SERVICE_PORT);

		sb.append("http://");
		sb.append(host);
		sb.append(":");
		sb.append(port);
		sb.append("/");
		sb.append("country");

		return sb.toString();
	}
}
