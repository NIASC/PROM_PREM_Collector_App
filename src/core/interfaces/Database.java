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
package core.interfaces;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import core.containers.MessageContainer;
import core.containers.Patient;
import core.containers.QuestionContainer;
import core.containers.StatisticsContainer;
import core.containers.User;
import core.containers.form.FormContainer;
import core.containers.form.TimePeriodContainer;

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
	
	public static final int ERROR = -1;
	public static final int QUERY_SUCCESS = 1;
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
	 * @return {@code QUERY_SUCCESS} on successful update,
	 *		{@code ERROR} on failure.
	 */
	public int addUser(String username,
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
	 * @return {@code QUERY_SUCCESS} on successful update,
	 * 		{@code ERROR} on failure.
	 */
	public int addQuestionnaireAnswers(Patient patient, List<FormContainer> answers);

	/**
	 * Adds a new clinic to the database.
	 * 
	 * @param clinicName The name of the clinic.
	 * 
	 * @return {@code QUERY_SUCCESS} on successful update,
	 * 		{@code ERROR} on failure.
	 */
	public int addClinic(String clinicName);
	
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
	 * Collects the information about the patient from the database.
	 * 
	 * @param pnr The personal number of the patient to look for.
	 * 
	 * @return If the patient was found the instance of the patient is
	 * 		returned else {@code null}.
	 */
	public boolean patientInDatabase(String pnr);

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
	 * @return {@code QUERY_SUCCESS} if the error messages were
	 * 		successfully loaded. {@code ERROR} if there was an error
	 * 		with the database.
	 */
	public int getErrorMessages(MessageContainer mc);
	
	/**
	 * Loads info messages from the database and puts them in a
	 * MessageContainer.
	 * 
	 * @param mc The (empty) message container to put info messages in.
	 * 
	 * @return {@code QUERY_SUCCESS} if the info messages were
	 * 		successfully loaded. {@code ERROR} if there was an error
	 * 		with the database.
	 */
	public int getInfoMessages(MessageContainer mc);
	
	/**
	 * Loads questions from the database and stores them in a list
	 * of questions.
	 * 
	 * @param qc The {@code QuestionContainer} to put the questions
	 * 		in.
	 * 
	 * @return {@code QUERY_SUCCESS} if the questions were successfully
	 * 		loaded. {@code ERROR} if there was an error with the
	 * 		database.
	 * 
	 * @see QuestionContainer
	 */
	public int loadQuestions(QuestionContainer qc);
	
	/**
	 * Loads the dates at which questionnaires have been filled in for
	 * {@code user}'s clinic.
	 * 
	 * @param user The user that requests statistics.
	 * @param tpc The container for the dates.
	 * 
	 * @return {@code QUERY_SUCCESS} if the questions were successfully
	 * 		loaded. {@code ERROR} if there was an error with the
	 * 		database.
	 * 
	 * @see TimePeriodContainer
	 */
	public int loadQResultDates(User user, TimePeriodContainer tpc);

	public int loadQResults(User user, Calendar begin, Calendar end,
			List<Integer> questionIDs, StatisticsContainer container);
	
	/* Protected */
	
	/* Private */
}
