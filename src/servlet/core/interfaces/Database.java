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
