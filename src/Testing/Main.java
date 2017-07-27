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
package Testing;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import core.interfaces.Database;
import core.interfaces.Implementations;
import core.interfaces.Messages;
import core.interfaces.Questions;
import implementation.GUI_UserInterface;
import implementation.MySQL_Database;
import implementation.exceptions.DBReadException;

public class Main extends HttpServlet
{
	private static final long serialVersionUID = -2340346250534805168L;
	private String message;
	
	private DataSource dataSource = null;

	public void init() throws ServletException
	{
		// Do required initialization
		message = "PROM/PREM Collector";

		if (!Messages.getMessages().loadMessages()
				|| !Questions.getQuestions().loadQuestionnaire())
		{
			System.out.printf("%s\n", Database.DATABASE_ERROR);
			System.exit(1);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Set response content type
		response.setContentType("text/html");

		// Actual logic goes here.
		PrintWriter out = response.getWriter();
		out.println("<h1>" + message + "</h1>");
		
		out.println(String.format("If this message does not display null, "
				+ "then database was successfully loaded:<br>%s",
				Messages.getMessages().getInfo(Messages.INFO_REG_BODY_DESCRIPTION)));
	}

	public void destroy() {
		// do nothing.
	}
}
