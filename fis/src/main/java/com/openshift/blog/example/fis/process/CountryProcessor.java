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
package com.openshift.blog.example.fis.process;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openshift.blog.example.common.model.CountryModel;

import shaded.org.apache.commons.io.FileUtils;

/**
 * Processor to populate the message body with a random country name.
 * 
 * @author benjaminholmes
 *
 */
@Singleton
@Named("countryProcessor")
public class CountryProcessor implements Processor {

	private static final Logger LOG = LoggerFactory.getLogger(CountryProcessor.class);

	private static final String COMMA = ",";

	private ArrayList<String> countries;

	@Inject
	@ConfigProperty(name = "DATA_DIRECTORY", defaultValue = "src/main/resources/data")
	private String dataDirectoryPath;

	@Override
	public void process(Exchange exchange) throws Exception {

		Message in = exchange.getIn();

		// Only need to do this once.
		if (this.countries == null) {
			this.countries = populateList();
		}

		int selector = getBoundedRandomNumber(countries.size());

		CountryModel model = new CountryModel();
		model.setName(countries.get(selector).trim());

		in.setBody(model);

		exchange.setIn(in);

	}

	/**
	 * Get a random int between 0 and an upper bound.
	 * 
	 * @param bound
	 * @return random int
	 */
	private int getBoundedRandomNumber(int bound) {
		Random random = new Random();
		return random.nextInt(bound);
	}

	/**
	 * Create a filename filter that limits file responses to csv types.
	 * 
	 * @return filenamefilter
	 */
	private FilenameFilter getFilenameFilter() {
		FilenameFilter csvFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				if (lowercaseName.endsWith(".csv")) {
					return true;
				} else {
					return false;
				}
			}
		};

		return csvFilter;
	}

	/**
	 * Populate the list of names.
	 * 
	 * @throws IOException
	 */
	private ArrayList<String> populateList() throws IOException {
		File dataDirectory = new File(dataDirectoryPath);

		File[] files = dataDirectory.listFiles(getFilenameFilter());

		ArrayList<String> list = new ArrayList<>();
		for (File dataFile : files) {

			// Sorry, couldn't be bothered to make a StringBuilder for this.
			LOG.info("Parsing data from " + dataFile.getAbsolutePath());

			String names = FileUtils.readFileToString(dataFile);
			list.addAll(Arrays.asList(names.split(COMMA)));
		}

		return list;
	}

}
