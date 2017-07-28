package servlet.implementation;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import servlet.core.interfaces.Database;
import servlet.core.interfaces.Implementations;
import servlet.core.interfaces.Database.DatabaseFunction;

public class JSONRead
{
	public static String handleRequest(String message)
	{
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try{
			obj = (JSONObject) parser.parse(message);
		} catch (org.json.simple.parser.ParseException pe) {
			return null;
		}
		if (obj == null)
			return null;
		
		DatabaseFunction dbf = getDBMethod((String) obj.get("command"));
		if (dbf == null)
			return null;
		return dbf.dbfunc(obj);
	}
	
	private static DatabaseFunction getDBMethod(String command)
	{
		if (command == null)
			return null;
		
		Database db = Implementations.Database();
		if (command.equalsIgnoreCase("add_user"))
			return db::addUser;
		else if (command.equalsIgnoreCase("add_questionnaire_answers"))
			return db::addQuestionnaireAnswers;
		else if (command.equalsIgnoreCase("add_clinic"))
			return db::addClinic;
		else if (command.equalsIgnoreCase("get_clinics"))
			return db::getClinics;
		else if (command.equalsIgnoreCase("get_user"))
			return db::getUser;
		else if (command.equalsIgnoreCase("set_password"))
			return db::setPassword;
		else if (command.equalsIgnoreCase("get_error_messages"))
			return db::getErrorMessages;
		else if (command.equalsIgnoreCase("get_info_messages"))
			return db::getInfoMessages;
		else if (command.equalsIgnoreCase("load_questions"))
			return db::loadQuestions;
		else if (command.equalsIgnoreCase("load_q_result_dates"))
			return db::loadQResultDates;
		else if (command.equalsIgnoreCase("load_q_results"))
			return db::loadQResults;
		else
			return null;
	}
}
