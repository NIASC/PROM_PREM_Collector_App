/**
 * Copyright 2017 Marcus Malmquist
 * 
 * This file is part of PROM_PREM_Collector.
 * 
 * PROM_PREM_Collector is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * PROM_PREM_Collector is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with PROM_PREM_Collector.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package servlet.core;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import servlet.implementation.JSONRead;

public class Main extends HttpServlet
{
	private static final long serialVersionUID = -2340346250534805168L;
	private String message;
	

	public void init() throws ServletException
	{
		// Do required initialization
		message = "PROM/PREM Collector";
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Set response content type
		response.setContentType("text/html");

		// Actual logic goes here.
		PrintWriter out = response.getWriter();
		out.println("<h1>" + message + "</h1>");
		
		out.println("<APPLET codebase='classes' code='applet/implementation/GUI_UserInterface.class' width=800 height=650 archive='activation-1.1.1.jar,gnumail-providers-1.1.2.jar,json-simple-1.1.1.jar,gnumail-1.1.2.jar,inetlib-1.1.2.jar'></APPLET>");
		out.println("<a href='./WEB-INF/classes'>Link to this folder</a>");

		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "load_questions");

		URL url;
		HttpURLConnection connection;
		try {
			url = new URL("http://localhost:8080/PROM_PREM_Collector/main");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			//Send request
			OutputStream os = connection.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
			out.println(ret.toString());
			osw.write(ret.toString());
			osw.flush();
			osw.close();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				out.println("Ok response");
			} else {
				out.println("Bad response");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try{
			StringBuilder sb = new StringBuilder();
			BufferedReader br = request.getReader();
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			out.println(str);
			String response_str = JSONRead.handleRequest(sb.toString());
			out.println(response_str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void destroy() {
		// do nothing.
	}
}
