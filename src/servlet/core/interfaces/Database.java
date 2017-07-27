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
package servlet.core.interfaces;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;


/**
 * This interface contains the methods required by the core part of
 * this program to function. The purpose of this interface is to give
 * the freedom of choosing your own database along with the core part
 * of this program.
 * 
 * @author Marcus Malmquist
 *
 */
public interface Database
{
	/* Public */
	
	/**
	 * If the messages was not retrieved from the database this
	 * message (in English) should be used to notify the caller that
	 * there was a database error.
	 */
	public static final String DATABASE_ERROR = "Database error.";
	
	public static final String INSERT_RESULT = "insert_result";
	public static final String INSERT_SUCCESS = "1";
	public static final String INSERT_FAIL = "0";
	
	public String addUser					(JSONObject obj);
	public String addQuestionnaireAnswers	(JSONObject obj);
	public String addClinic					(JSONObject obj);
	public String getClinics				(JSONObject obj);
	public String getUser					(JSONObject obj);
	public String setPassword				(JSONObject obj);
	public String getErrorMessages			(JSONObject obj);
	public String getInfoMessages			(JSONObject obj);
	public String loadQuestions				(JSONObject obj);
	public String loadQResultDates			(JSONObject obj);
	public String loadQResults				(JSONObject obj);

	@FunctionalInterface
	public interface DatabaseFunction
	{
		public String dbfunc(JSONObject obj);
	}
	
	/* Protected */
	
	/* Private */
}
