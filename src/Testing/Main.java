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


import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import core.interfaces.Database;
import core.interfaces.Messages;
import core.interfaces.Questions;
import implementation.GUI_UserInterface;

public class Main extends HttpServlet
{
	private static final long serialVersionUID = -2340346250534805168L;
	private String message;

	public void init() throws ServletException {
		// Do required initialization
		message = "Hello World";
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Set response content type
		response.setContentType("text/html");

		// Actual logic goes here.
		PrintWriter out = response.getWriter();
		out.println("<h1>" + message + "</h1>");
	}

	public void destroy() {
		// do nothing.
	}

	public static void main(String[] args)
	{
		Servlet s = (Servlet) new Main();
		if (!Messages.getMessages().loadMessages()
				|| !Questions.getQuestions().loadQuestionnaire())
		{
			System.out.printf("%s\n", Database.DATABASE_ERROR);
			System.exit(1);
		}
		GUI_UserInterface qf1 = new GUI_UserInterface(false);
		qf1.start();
	}
}
