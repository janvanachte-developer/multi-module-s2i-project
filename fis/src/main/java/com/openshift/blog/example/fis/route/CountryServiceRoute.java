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
package com.openshift.blog.example.fis.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.model.rest.RestBindingMode;

import com.openshift.blog.example.common.model.CountryModel;

/**
 * Route definition for CountryService
 * 
 * @author benjaminholmes
 *
 */
@ContextName("countryContext")
public class CountryServiceRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		restConfiguration()
			.component("jetty")
			.bindingMode(RestBindingMode.json)
			.dataFormatProperty("prettyPrint", "true")
			.host("0.0.0.0")
			.port(8080);
		
		 rest("/").id("countryServiceRoute")
		 .description("Country Rest Service")
		 .produces("application/json")
		
		 	.get("/country")
		 		.description("Retrieve a random country name")
		 		.outType(CountryModel.class)
		 		.to("direct:country");
		 
		 from("direct:country").id("countryProcessorRoute")
		 .processRef("countryProcessor")
		 .log("Response sent: ${body}");
	}

}
