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

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * A simple servlet taking advantage of features added in 3.0.
 * </p>
 *
 * <p>
 * The servlet is registered and mapped to /Country using the
 * {@linkplain WebServlet
 * @HttpServlet}. The {@link CountryServiceClient} is injected by CDI.
 *                </p>
 *
 * @author benjaminholmes
 *
 */
@SuppressWarnings("serial")
@WebServlet("/Country")
public class CountryServlet extends HttpServlet {

	static String PAGE_HEADER = "<html><head><title>Country GeneratorĀ§</title></head><body>";

	static String PAGE_FOOTER = "</body></html>";

	@Inject
	private CountryServiceClient countryService;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		writer.println(PAGE_HEADER);
		writer.println("<h1> Hello from " + countryService.getCountry() + "! </h1>");
		writer.println(PAGE_FOOTER);
		writer.close();
	}

}
