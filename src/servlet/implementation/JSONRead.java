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
package servlet.implementation;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import common.implementation.Constants;
import servlet.core.interfaces.Database;
import servlet.core.interfaces.Implementations;
import servlet.core.interfaces.Database.DatabaseFunction;

public class JSONRead
{
	private static JSONParser parser;
	
	static {
		parser = new JSONParser();
	}
	public static String handleRequest(String message)
	{
		try
		{
			JSONObject obj = (JSONObject) parser.parse(message);
			return getDBMethod((String) obj.get("command")).dbfunc(obj);
		}
		catch (ParseException pe) { /* unknown format */ }
		catch (NullPointerException _e) { /* unknown command */ }
		return null;
	}
	
	private static DatabaseFunction getDBMethod(String command)
			throws NullPointerException
	{
		Database db = Implementations.Database();
		if (command.equalsIgnoreCase(Constants.CMD_ADD_USER))
			return db::addUser;
		else if (command.equalsIgnoreCase(Constants.CMD_ADD_QANS))
			return db::addQuestionnaireAnswers;
		else if (command.equalsIgnoreCase(Constants.CMD_ADD_CLINIC))
			return db::addClinic;
		else if (command.equalsIgnoreCase(Constants.CMD_GET_CLINICS))
			return db::getClinics;
		else if (command.equalsIgnoreCase(Constants.CMD_GET_USER))
			return db::getUser;
		else if (command.equalsIgnoreCase(Constants.CMD_SET_PASSWORD))
			return db::setPassword;
		else if (command.equalsIgnoreCase(Constants.CMD_GET_ERR_MSG))
			return db::getErrorMessages;
		else if (command.equalsIgnoreCase(Constants.CMD_GET_INFO_MSG))
			return db::getInfoMessages;
		else if (command.equalsIgnoreCase(Constants.CMD_LOAD_Q))
			return db::loadQuestions;
		else if (command.equalsIgnoreCase(Constants.CMD_LOAD_QR_DATE))
			return db::loadQResultDates;
		else if (command.equalsIgnoreCase(Constants.CMD_LOAD_QR))
			return db::loadQResults;
		else if (command.equalsIgnoreCase(Constants.CMD_REQ_REGISTR))
			return db::requestRegistration;
		else if (command.equalsIgnoreCase(Constants.CMD_REQ_LOGIN))
			return db::requestLogin;
		else if (command.equalsIgnoreCase(Constants.CMD_REQ_LOGOUT))
			return db::requestLogout;
		
		throw new NullPointerException("unkown command");
	}
}
