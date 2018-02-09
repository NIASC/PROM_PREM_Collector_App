package se.nordicehealth.ppc_app.core.interfaces;

import java.util.Calendar;
import java.util.List;

import se.nordicehealth.ppc_app.common.implementation.Packet.Data;
import se.nordicehealth.ppc_app.core.containers.Patient;
import se.nordicehealth.ppc_app.core.containers.QuestionContainer;
import se.nordicehealth.ppc_app.core.containers.StatisticsContainer;
import se.nordicehealth.ppc_app.core.containers.form.FormContainer;

public interface Server
{
	String DATABASE_ERROR = "Server error.";

	boolean ping(long uid);
    boolean validatePatientID(long uid, String patientID);
	boolean addQuestionnaireAnswers(long uid, Patient patient, List<FormContainer> answers);
    Data.SetPassword.Response setPassword(long uid, String oldPass, String newPass1, String newPass2);
	QuestionContainer loadQuestions();
	List<Calendar> loadQuestionnaireResultDates(long uid);
	StatisticsContainer loadQuestionnaireResults(long uid, Calendar begin, Calendar end, List<Integer> questionIDs);
	boolean requestRegistration(String name, String email, String clinic);
    Session requestLogin(String username, String password);
	boolean requestLogout(long uid);

	class Session
	{
		public final long uid;
		public final Data.RequestLogin.Response response;
		public final boolean update_password;

		public Session(long uid, Data.RequestLogin.Response response, boolean update_password)
		{
			this.uid = uid;
			this.response = response;
			this.update_password = update_password;
		}
	}
}
