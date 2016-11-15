package com.openshift.blog.example.fis.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.model.rest.RestBindingMode;

import com.openshift.blog.example.common.model.CountryModel;

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
		
		 rest("/").description("Country Rest Service").produces("application/json")
		
		 	.get("/country")
		 		.description("Retrieve a random country name")
		 		.outType(CountryModel.class).to("direct:country");
		 
		 from("direct:country")
		 .processRef("countryProcessor")
		 .log("Event received: ${body}");
	}

}
