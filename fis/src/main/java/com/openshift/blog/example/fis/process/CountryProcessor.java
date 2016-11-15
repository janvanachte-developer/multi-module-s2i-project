package com.openshift.blog.example.fis.process;

import java.io.File;
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

import com.openshift.blog.example.common.model.CountryModel;

import shaded.org.apache.commons.io.FileUtils;

@Singleton
@Named("countryProcessor")
public class CountryProcessor implements Processor {

	private static final String COMMA = ",";

	@Inject
	@ConfigProperty(name = "DATA_FILE", defaultValue = "src/main/resources/data/country_data.csv")
	private String dataFile;

	@Override
	public void process(Exchange exchange) throws Exception {

		Message in = exchange.getIn();

		File data = new File(dataFile);

		String names = FileUtils.readFileToString(data);

		ArrayList<String> list = new ArrayList<String>(Arrays.asList(names.split(COMMA)));

		int selector = getBoundedRandomNumber(list.size());

		CountryModel model = new CountryModel();
		model.setName(list.get(selector));

		in.setBody(model);

		exchange.setIn(in);

	}

	private int getBoundedRandomNumber(int bound) {
		Random random = new Random();
		return random.nextInt(bound);
	}

}
