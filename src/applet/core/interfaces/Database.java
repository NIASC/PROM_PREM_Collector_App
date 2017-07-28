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
package applet.core.interfaces;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import applet.core.containers.MessageContainer;
import applet.core.containers.Patient;
import applet.core.containers.QuestionContainer;
import applet.core.containers.StatisticsContainer;
import applet.core.containers.User;
import applet.core.containers.form.FormContainer;
import applet.core.containers.form.TimePeriodContainer;

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
	
	/**
	 * Adds a new user to the database.
	 * 
	 * @param username The username of the new user.
	 * @param password The (hashed) password of the new user.
	 * @param salt The salt that was used to hash the password.
	 * @param clinic The clinic ID that the new user belongs to.
	 * @param email The email of the new user.
	 * 
	 * @return {@code true} on successful update,
	 *		{@code false} on failure.
	 */
	public boolean addUser(String username,
			String password, String salt, int clinic, String email);
	
	/**
	 * Adds a patient's answers to the database. If the patient does not
	 * already exist in the database it will be added as well.
	 * 
	 * @param patient The patient to add
	 * @param answers The questionnaire form containers.  The containers
	 * 		should be in the same order as the questions in the
	 * 		questionnaire.
	 * 
	 * @return {@code true} on successful update,
	 *		{@code false} on failure.
	 */
	public boolean addQuestionnaireAnswers(Patient patient, List<FormContainer> answers);

	/**
	 * Adds a new clinic to the database.
	 * 
	 * @param clinicName The name of the clinic.
	 * 
	 * @return {@code true} on successful update,
	 *		{@code false} on failure.
	 */
	public boolean addClinic(String clinicName);
	
	/**
	 * Collects the clinic names and id and places them in a Map.
	 * 
	 * @return A Map containing clinic id as keys and clinic names
	 * 		as values.
	 */
	public Map<Integer, String> getClinics();
	
	/**
	 * Collects the information about the user from the database.
	 * 
	 * @param username The name of the user to look for.
	 * 
	 * @return If the user was found the instance of the user is
	 * 		returned else {@code null}.
	 */
	public User getUser(String username);

	/**
	 * Updates the user's password and salt to newPass and newSalt if
	 * oldPass matches the user's current password.
	 * 
	 * @param user The user that is updating it's password.
	 * @param oldPass The old (current) (unhashed) password.
	 * @param newPass The new password hashed using the new salt.
	 * @param newSalt The new salt.
	 * 
	 * @return The instance of the user with updated password. If the
	 * 		old password does not match the user's password then the
	 * 		password is not updated and null is returned.
	 */
	public User setPassword(User user, String oldPass, String newPass,
			String newSalt);
	
	/**
	 * Loads error messages from the database and puts them in a
	 * MessageContainer.
	 * 
	 * @param mc The (empty) message container to put error messages in.
	 * 
	 * @return {@code true} if the error messages were
	 * 		successfully loaded. {@code false} if there was an error
	 * 		with the database.
	 */
	public boolean getErrorMessages(MessageContainer mc);
	
	/**
	 * Loads info messages from the database and puts them in a
	 * MessageContainer.
	 * 
	 * @param mc The (empty) message container to put info messages in.
	 * 
	 * @return {@code true} if the info messages were
	 * 		successfully loaded. {@code false} if there was an error
	 * 		with the database.
	 */
	public boolean getInfoMessages(MessageContainer mc);
	
	/**
	 * Loads questions from the database and stores them in a list
	 * of questions.
	 * 
	 * @param qc The {@code QuestionContainer} to put the questions
	 * 		in.
	 * 
	 * @return {@code true} if the questions were successfully
	 * 		loaded. {@code false} if there was an error with the
	 * 		database.
	 * 
	 * @see QuestionContainer
	 */
	public boolean loadQuestions(QuestionContainer qc);
	
	/**
	 * Loads the dates at which questionnaires have been filled in for
	 * {@code user}'s clinic.
	 * 
	 * @param user The user that requests statistics.
	 * @param tpc The container for the dates.
	 * 
	 * @return {@code true} if the questions were successfully
	 * 		loaded. {@code false} if there was an error with the
	 * 		database.
	 * 
	 * @see TimePeriodContainer
	 */
	public boolean loadQResultDates(User user, TimePeriodContainer tpc);

	/**
	 * Loads questionnaire results for the questions contained in
	 * {@code questionIDs} and during the time period specified by
	 * {@code begin} and {@code end}.
	 * Only the results associated with the same clinic as {@code user} will
	 * be loaded.
	 * 
	 * @param user The user that is requesting the data.
	 * @param begin The (inclusive) start date.
	 * @param end The (inclusive) end date.
	 * @param questionIDs a list of question IDs for the requested questions.
	 * @param container The container to put the results in.
	 * 
	 * @return {@code true} if the results were loaded. {@code false} if not.
	 */
	public boolean loadQResults(User user, Calendar begin, Calendar end,
			List<Integer> questionIDs, StatisticsContainer container);

	/**
	 * Sends a registration request to the servlet.
	 * 
	 * @param name The name of the person that wishes to register (not the username).
	 * @param email The email of the person that wishes to register.
	 * @param clinic The clinic that the person who wishes to register belongs to.
	 * 
	 * @return True if the registration was successful.
	 */
	public boolean requestRegistration(
			String name, String email, String clinic);

	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public int requestLogin(String username, String password);

	/**
	 * 
	 * @param username
	 * @return
	 */
	public boolean requestLogout(String username);
	
	/* Protected */
	
	/* Private */
}
