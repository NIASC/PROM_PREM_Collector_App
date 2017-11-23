package se.nordicehealth.ppc_app.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import se.nordicehealth.ppc_app.core.containers.Patient;
import se.nordicehealth.ppc_app.core.containers.QuestionContainer;
import se.nordicehealth.ppc_app.core.containers.form.FieldContainer;
import se.nordicehealth.ppc_app.core.containers.form.FormContainer;
import se.nordicehealth.ppc_app.core.interfaces.Server;
import se.nordicehealth.ppc_app.core.interfaces.FormUtils;
import se.nordicehealth.ppc_app.core.interfaces.Implementations;
import se.nordicehealth.ppc_app.core.interfaces.Messages;
import se.nordicehealth.ppc_app.core.interfaces.Questions;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface;

public class Questionnaire
{
	void start()
	{
		preg.createPatientRegistration();
	}

	Questionnaire(UserInterface ui, UserHandle uh)
	{
		this.ui = ui;
		this.uh = uh;
		questions = Questions.getQuestions().getContainer();
		preg = new PatientRegistration();
		pquest = new PatientQuestionnaire();
	}

	private UserHandle uh;
	private UserInterface ui;
	private Patient patient;
	private QuestionContainer questions;
	private PatientRegistration preg;
	private PatientQuestionnaire pquest;
	
	private class PatientQuestionnaire implements FormUtils
	{
		@Override
		public RetFunContainer validateUserInput(List<FormContainer> form) {
			RetFunContainer rfc = new RetFunContainer();

			Server db = Implementations.Server();
			if (!db.addQuestionnaireAnswers(uh.getUID(), patient, Collections.unmodifiableList(form))) {
				rfc.message = Server.DATABASE_ERROR;
				return rfc;
			}
			patient = null;
			rfc.valid = true;
			return rfc;
		}

		@Override
		public void callNext() { }
		
		PatientQuestionnaire() { }

		void createQuestionnaire()
		{
            List<FormContainer> form = new LinkedList<>();
			for (int i = 0; i < questions.getSize(); ++i)
			    form.add(questions.getContainer(i));
			
			ui.presentForm(form, this, false);
		}
	}
	
	private class PatientRegistration implements FormUtils
	{
		@Override
		public RetFunContainer validateUserInput(List<FormContainer> form)
		{
			RetFunContainer rfc = new RetFunContainer();
			List<String> answers = new ArrayList<>();
            for (FormContainer fc : form)
                answers.add((String) fc.getEntry());
			String forename = answers.get(0);
			String surname = answers.get(1);
			String pnr = answers.get(2);

            Server db = Implementations.Server();
            Messages msg = Implementations.Messages();
			if (!db.validatePatientID(uh.getUID(), pnr)) {
				rfc.message = msg.error(Messages.ERROR.QP_INVALID_PID);
				return rfc;
			}

            patient = new Patient(forename, surname, pnr);
			rfc.valid = true;
			return rfc;
		}

		@Override
		public void callNext()
		{
			pquest.createQuestionnaire();
		}
		
		PatientRegistration() { }

		void createPatientRegistration()
		{
            List<FormContainer> form = new LinkedList<>();
			Messages msg = Implementations.Messages();
			FieldContainer forename = new FieldContainer(false, false,
					msg.info(Messages.INFO.Q_PATIENT_FORENAME), null);
			form.add(forename);
			FieldContainer lastname = new FieldContainer(false, false,
					msg.info(Messages.INFO.Q_PATIENT_SURNAME), null);
			form.add(lastname);
			FieldContainer pnr = new FieldContainer(false, false,
					msg.info(Messages.INFO.Q_PATIENT_PNR), null);
			if (patient != null) {
				forename.setEntry(patient.getForename());
				lastname.setEntry(patient.getSurname());
				pnr.setEntry(patient.getPersonalNumber());
			}
			form.add(pnr);

			ui.presentForm(form, this, true);
		}
	}
}
