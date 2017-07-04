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
package core;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import core.containers.Form;
import core.containers.Patient;
import core.containers.QuestionContainer;
import core.containers.form.FieldContainer;
import core.interfaces.Implementations;
import core.interfaces.Messages;
import core.interfaces.UserInterface;
import core.interfaces.UserInterface.RetFunContainer;

/**
 * This class is the central point for the questionaire part of the
 * program. This is where the questionare loop is run from.
 * 
 * @author Marcus Malmquist
 *
 */
public class Questionnaire
{
	/* Public */
	
	/**
	 * Initialize variables.
	 * 
	 * @param ui The active instance of the user interface.
	 * @param uh The active instance of the user handle.
	 */
	protected Questionnaire(UserInterface ui, UserHandle uh)
	{
		userInterface = ui;
		userHandle = uh;
		questions = new QuestionContainer();
		Implementations.Database().loadQuestions(questions);
	}
	
	/**
	 * Starts the questionnaire.
	 */
	public void start()
	{
		if (!userHandle.isLoggedIn())
		{
			userInterface.displayError(Messages.getMessages().getError(
					Messages.ERROR_NOT_LOGGED_IN), false);
			return;
		}
		displayPatientRegistration();
	}
	
	/* Protected */
	
	/* Private */
	
	private void displayPatientRegistration()
	{
		Form form = new Form();
		FieldContainer forename = new FieldContainer(false, false, "Patient forename");
		form.insert(forename, Form.AT_END);
		FieldContainer lastname = new FieldContainer(false, false, "Patient lastname");
		form.insert(lastname, Form.AT_END);
		FieldContainer pnr = new FieldContainer(false, false, "Patient Personal number");
		if (patient != null)
		{
			forename.setEntry(patient.getForename());
			lastname.setEntry(patient.getLastname());
			pnr.setEntry(patient.getPersonalNumber());
		}
		form.insert(pnr, Form.AT_END);

		form.jumpTo(Form.AT_BEGIN);

		userInterface.presentForm(form, this::validatePatient, true);
	}
	
	private void displayQuestionnaire()
	{
		if (patient == null)
			return;
		Form form = new Form();
		for (int i = 0; i < questions.getSize(); ++i)
			form.insert(questions.getQuestion(i), Form.AT_END);
		form.jumpTo(Form.AT_BEGIN);
		
		userInterface.presentForm(form, this::saveQuestionaire, false);
	}
	
	private RetFunContainer validatePatient(Form form)
	{
		RetFunContainer rfc = new RetFunContainer(this::displayQuestionnaire);
		form.jumpTo(Form.AT_BEGIN);
		FieldContainer forename = (FieldContainer) form.currentEntry();
		form.jumpTo(Form.AT_NEXT);
		FieldContainer lastname = (FieldContainer) form.currentEntry();
		form.jumpTo(Form.AT_NEXT);
		FieldContainer pnr = (FieldContainer) form.currentEntry();
		form.jumpTo(Form.AT_NEXT);
		String personalNumber = validDate(pnr.getEntry());
		if (personalNumber == null)
		{
			rfc.message = String.format(
					"Valid personal numbers are: %s, %s, %s, %s",
					"yymmddxxxx", "yymmdd-xxxx", "yyyymmddxxxx", "yyyymmdd-xxxx");
			return rfc;
		}
		try {
			patient = new Patient(
					forename.getEntry(), lastname.getEntry(),
					personalNumber, userHandle.getUser());
		} catch (NullPointerException npe)
		{
			rfc.message = Messages.getMessages().getError(
					Messages.ERROR_NOT_LOGGED_IN);
			return rfc;
		}
		rfc.valid = true;
		return rfc;
	}
	
	private RetFunContainer saveQuestionaire(Form form)
	{
		RetFunContainer rfc = new RetFunContainer(null);
		List<Object> answers = new ArrayList<Object>();
		form.jumpTo(Form.AT_BEGIN);
		if (form.currentEntry() == null)
			return rfc;
		do
			answers.add(form.currentEntry().getEntry());
		while (form.nextEntry() != null);
		
		Implementations.Database().addQuestionnaireAnswers(
				patient, answers);
		
		patient = null;
		rfc.valid = true;
		return rfc;
	}
	
	private String validDate(String dateStr)
	{
		DateFormat dateFormat;
		Date date = null;
		dateStr = dateStr.trim();
		Integer lastFour = null;
		try
		{
			switch(dateStr.length())
			{
			case 10: // yymmddxxx
			case 11: // yymmdd-xxxx
				dateFormat = new SimpleDateFormat("yyMMdd");
				dateFormat.setLenient(false);
				date = dateFormat.parse(dateStr.substring(0, 6));
				lastFour = Integer.parseInt(dateStr.substring(dateStr.length()-4));
				break;
			case 12: // yyyymmddxxx
			case 13: // yyyymmdd-xxxx
				dateFormat = new SimpleDateFormat("yyyyMMdd");
				dateFormat.setLenient(false);
				date = dateFormat.parse(dateStr.substring(0, 8));
				lastFour = Integer.parseInt(dateStr.substring(dateStr.length()-4));
				break;
			default:
				throw new ParseException("Unknown format", 0);
			}
		} catch (ParseException pe)
		{
			return null;
		}
		System.out.println(String.format("%s-%04d",
				(new SimpleDateFormat("yyyyMMdd")).format(date), lastFour));
		return String.format("%s-%04d",
				(new SimpleDateFormat("yyyyMMdd")).format(date), lastFour);
	}

	private UserHandle userHandle;
	private UserInterface userInterface;
	private Patient patient;
	private QuestionContainer questions;
}
