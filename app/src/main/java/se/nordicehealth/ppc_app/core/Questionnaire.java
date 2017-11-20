/*! Questionnaire.java
 * 
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
package se.nordicehealth.ppc_app.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.nordicehealth.ppc_app.core.containers.Form;
import se.nordicehealth.ppc_app.core.containers.Patient;
import se.nordicehealth.ppc_app.core.containers.QuestionContainer;
import se.nordicehealth.ppc_app.core.containers.form.FieldContainer;
import se.nordicehealth.ppc_app.core.containers.form.FormContainer;
import se.nordicehealth.ppc_app.core.interfaces.Database;
import se.nordicehealth.ppc_app.core.interfaces.FormUtils;
import se.nordicehealth.ppc_app.core.interfaces.Implementations;
import se.nordicehealth.ppc_app.core.interfaces.Messages;
import se.nordicehealth.ppc_app.core.interfaces.Questions;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface;

/**
 * This class is the central point for the questionnaire part of the
 * program. It handles creating and validating the patient
 * registration as well as the questionnaire.
 * 
 * @author Marcus Malmquist
 *
 */
public class Questionnaire
{
	/* Public */
	
	/**
	 * Starts the questionnaire. The questionnaire is preceded by
	 * a patient registration form.
	 */
	void start()
	{
		if (!uh.isLoggedIn())
			ui.displayError(Implementations.Messages().getError(
					Messages.ERROR_NOT_LOGGED_IN), false);
		else
			preg.createPatientRegistration();
	}
	
	/* Protected */
	
	/**
	 * Initialize variables.
	 * 
	 * @param ui The active instance of the {@code UserInterface}.
	 * @param uh The active instance of the {@code UserHandle}.
	 */
	Questionnaire(UserInterface ui, UserHandle uh)
	{
		this.ui = ui;
		this.uh = uh;
		questions = Questions.getQuestions().getContainer();
		preg = new PatientRegistration();
		pquest = new PatientQuestionnaire();
	}
	
	/* Private */

	private UserHandle uh;
	private UserInterface ui;
	private Patient patient;
	private QuestionContainer questions;
	private PatientRegistration preg;
	private PatientQuestionnaire pquest;
	
	private class PatientQuestionnaire implements FormUtils
	{
		@Override
		public RetFunContainer ValidateUserInput(Form form) {
			RetFunContainer rfc = new RetFunContainer();
			List<FormContainer> answers = new ArrayList<>();
			form.jumpTo(Form.AT_BEGIN);
			do
				answers.add(form.currentEntry());
			while (form.nextEntry() != null);
			
			if (!Implementations.Database().addQuestionnaireAnswers(uh.getUID(), patient,
					Collections.unmodifiableList(answers))) {
				rfc.message = Database.DATABASE_ERROR;
				return rfc;
			}
			patient = null;
			rfc.valid = true;
			return rfc;
		}

		@Override
		public void callNext()
		{
			
		}
		
		private PatientQuestionnaire()
		{
			
		}
		
		/**
		 * Creates the questionnaire form and sends it to the
		 * {@code UserInterface}. It is required to fill in the patient
		 * form before the questionnaire is able to start.
		 */
		private void createQuestionnaire()
		{
			if (patient == null)
				return;
			Form form = new Form();
			for (int i = 0; i < questions.getSize(); ++i)
				form.insert(questions.getContainer(i), Form.AT_END);
			form.jumpTo(Form.AT_BEGIN);
			
			ui.presentForm(form, this, false);
		}
		
	}
	
	private class PatientRegistration implements FormUtils
	{

		/* Public */
		
		/* Protected */
		
		/* Private */
		
		@Override
		public RetFunContainer ValidateUserInput(Form form)
		{
			RetFunContainer rfc = new RetFunContainer();
			List<String> answers = new ArrayList<>();
			form.jumpTo(Form.AT_BEGIN);
			do {
				answers.add((String) form.currentEntry().getEntry());
			} while (form.nextEntry() != null);
			String forename = answers.get(0);
			String lastname = answers.get(1);
			String pnr = answers.get(2);
			
			String pID = Implementations.Locale().formatPersonalID(pnr);
			if (pID == null)
			{
				rfc.message = Implementations.Messages().getError(
						Messages.ERROR_QP_INVALID_PID);
				return rfc;
			}
			
			try
			{
				patient = new Patient(forename, lastname, pID);
			}
			catch (NullPointerException npe)
			{
				rfc.message = Implementations.Messages().getError(
						Messages.ERROR_NOT_LOGGED_IN);
				return rfc;
			}
			rfc.valid = true;
			return rfc;
		}
		
		public void callNext()
		{
			pquest.createQuestionnaire();
		}
		
		private PatientRegistration()
		{
			
		}
		
		/**
		 * Displays a patient registration form. This registration form is
		 * used to link the patient to the questionnaire.
		 */
		private void createPatientRegistration()
		{
			Form form = new Form();
			Messages msg = Implementations.Messages();
			FieldContainer forename = new FieldContainer(false, false,
					msg.getInfo(Messages.INFO_Q_PATIENT_FORENAME), null);
			form.insert(forename, Form.AT_END);
			FieldContainer lastname = new FieldContainer(false, false,
					msg.getInfo(Messages.INFO_Q_PATIENT_SURNAME), null);
			form.insert(lastname, Form.AT_END);
			FieldContainer pnr = new FieldContainer(false, false,
					msg.getInfo(Messages.INFO_Q_PATIENT_PNR), null);
			if (patient != null) {
				forename.setEntry(patient.getForename());
				lastname.setEntry(patient.getSurname());
				pnr.setEntry(patient.getPersonalNumber());
			}
			form.insert(pnr, Form.AT_END);

			form.jumpTo(Form.AT_BEGIN);

			ui.presentForm(form, this, true);
		}
	}
}
