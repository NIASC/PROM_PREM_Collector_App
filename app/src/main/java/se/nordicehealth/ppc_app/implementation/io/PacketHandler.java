package se.nordicehealth.ppc_app.implementation.io;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import se.nordicehealth.ppc_app.core.containers.Patient;
import se.nordicehealth.ppc_app.core.containers.Question;
import se.nordicehealth.ppc_app.core.containers.QuestionContainer;
import se.nordicehealth.ppc_app.core.containers.StatisticsContainer;
import se.nordicehealth.ppc_app.core.containers.form.AreaContainer;
import se.nordicehealth.ppc_app.core.containers.form.FormContainer;
import se.nordicehealth.ppc_app.core.containers.form.MultipleOptionContainer;
import se.nordicehealth.ppc_app.core.containers.form.SingleOptionContainer;
import se.nordicehealth.ppc_app.core.containers.form.SliderContainer;
import se.nordicehealth.ppc_app.core.containers.statistics.Area;
import se.nordicehealth.ppc_app.core.containers.statistics.MultipleOption;
import se.nordicehealth.ppc_app.core.containers.statistics.SingleOption;
import se.nordicehealth.ppc_app.core.containers.statistics.Slider;
import se.nordicehealth.ppc_app.core.containers.statistics.Statistics;
import se.nordicehealth.ppc_app.core.interfaces.Server;
import se.nordicehealth.ppc_app.implementation.security.Encryption;
import se.nordicehealth.ppc_app.core.interfaces.Questions;
import se.nordicehealth.ppc_app.common.implementation.Constants;
import static se.nordicehealth.ppc_app.common.implementation.Constants.Packet.TYPE;
import static se.nordicehealth.ppc_app.common.implementation.Constants.Packet.DATA;
import se.nordicehealth.ppc_app.common.implementation.Constants.Packet.Types;
import se.nordicehealth.ppc_app.common.implementation.Constants.Packet.Data;
import se.nordicehealth.ppc_app.common.implementation.Constants.QuestionTypes;

public class PacketHandler implements Server
{
	public static synchronized PacketHandler getPacketHandler()
	{
		if (pktHandler == null)
		    throw new NullPointerException("PacketHandler have not been initialized.");
		return pktHandler;
	}

	public static void initialize(Encryption crypto)
    {
        pktHandler = new PacketHandler(crypto);
    }

	@Override
	public final Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}

	@Override
	public boolean ping(long uid)
	{
		MapData out = new MapData(null);
		out.put(TYPE, Types.PING);
        MapData dataOut = new MapData(null);

		MapData details = new MapData(null);
		details.put(Data.Ping.Details.UID, Long.toString(uid));
        dataOut.put(Data.Ping.DETAILS, crypto.encrypt(details.toString()));

        out.put(DATA, dataOut.toString());
        MapData in = sendMessage(out);
        MapData inData = jsonData.getMapData(in.get(DATA));

        Data.Ping.Response insert = Data.Ping.Response.FAIL;
        try {
            insert = Constants.getEnum(Data.Ping.Response.values(), inData.get(Data.Ping.RESPONSE));
        } catch (NumberFormatException ignored) { }
        return Constants.equal(Data.Ping.Response.SUCCESS, insert);
	}

	@Override
    public boolean validatePatientID(long uid, String patientID)
    {
        MapData out = new MapData(null);
        out.put(TYPE, Types.VALIDATE_PID);
        MapData dataOut = new MapData(null);

        MapData details = new MapData(null);
        details.put(Data.ValidatePatientID.Details.UID, Long.toString(uid));

        MapData pobj = new MapData(null);
        pobj.put(Data.ValidatePatientID.Patient.PERSONAL_ID, patientID);

        dataOut.put(Data.ValidatePatientID.DETAIL, crypto.encrypt(details.toString()));
        dataOut.put(Data.ValidatePatientID.PATIENT, crypto.encrypt(pobj.toString()));

        out.put(DATA, dataOut.toString());
        MapData in = sendMessage(out);
        MapData inData = jsonData.getMapData(in.get(DATA));

        Data.ValidatePatientID.Response insert = Data.ValidatePatientID.Response.FAIL;
        try {
            insert = Constants.getEnum(Data.ValidatePatientID.Response.values(), inData.get(Data.ValidatePatientID.RESPONSE));
        } catch (NumberFormatException ignored) { }
        return Constants.equal(Data.ValidatePatientID.Response.SUCCESS, insert);
    }

	@Override
	public boolean addQuestionnaireAnswers(long uid, Patient patient, List<FormContainer> answers)
	{
        MapData out = new MapData(null);
        out.put(TYPE, Types.ADD_QANS);
        MapData dataOut = new MapData(null);

        QuestionContainer qc = Questions.getContainer();
        if (qc == null || qc.getSize() != answers.size())
			return false;

		ListData questions = new ListData(null);
        for (FormContainer fc : answers)
            questions.add(qdbfmt.getDBFormat(fc));

		MapData pobj = new MapData(null);
        pobj.put(Data.AddQuestionnaireAnswers.Patient.FORENAME, patient.getForename());
        pobj.put(Data.AddQuestionnaireAnswers.Patient.SURNAME, patient.getSurname());
        pobj.put(Data.AddQuestionnaireAnswers.Patient.PERSONAL_ID, patient.getPersonalNumber());

		MapData details = new MapData(null);
        details.put(Data.AddQuestionnaireAnswers.Details.UID, Long.toString(uid));

        dataOut.put(Data.AddQuestionnaireAnswers.DETAILS, crypto.encrypt(details.toString()));
        dataOut.put(Data.AddQuestionnaireAnswers.PATIENT, crypto.encrypt(pobj.toString()));
        dataOut.put(Data.AddQuestionnaireAnswers.QUESTIONS, questions.toString());

        out.put(DATA, dataOut.toString());
        MapData in = sendMessage(out);
        MapData inData = jsonData.getMapData(in.get(DATA));

        Data.AddQuestionnaireAnswers.Response insert = Data.AddQuestionnaireAnswers.Response.FAIL;
        try {
            insert = Constants.getEnum(Data.AddQuestionnaireAnswers.Response.values(), inData.get(Data.AddQuestionnaireAnswers.RESPONSE));
        } catch (NumberFormatException ignored) { }
        return Constants.equal(Data.AddQuestionnaireAnswers.Response.SUCCESS, insert);
	}

	@Override
	public Data.SetPassword.Response setPassword(long uid, String oldPass, String newPass1, String newPass2) throws NumberFormatException
    {
		MapData out = new MapData(null);
		out.put(TYPE, Types.SET_PASSWORD);
        MapData dataOut = new MapData(null);

		MapData details = new MapData(null);
        details.put(Data.SetPassword.Details.UID, Long.toString(uid));
        details.put(Data.SetPassword.Details.OLD_PASSWORD, oldPass);
        details.put(Data.SetPassword.Details.NEW_PASSWORD1, newPass1);
        details.put(Data.SetPassword.Details.NEW_PASSWORD2, newPass2);
        dataOut.put(Data.SetPassword.DETAILS, crypto.encrypt(details.toString()));

        out.put(DATA, dataOut.toString());
        MapData in = sendMessage(out);
        MapData inData = jsonData.getMapData(in.get(DATA));

        Data.SetPassword.Response insert = Data.SetPassword.Response.ERROR;
        try {
            insert = Constants.getEnum(Data.SetPassword.Response.values(), inData.get(Data.SetPassword.RESPONSE));
        } catch (NumberFormatException ignored) { }
        return insert;
	}
	
	@Override
	public QuestionContainer loadQuestions()
	{
		MapData out = new MapData(null);
		out.put(TYPE, Types.LOAD_Q);
        MapData dataOut = new MapData(null);

        out.put(DATA, dataOut.toString());
        MapData in = sendMessage(out);
        MapData inData = jsonData.getMapData(in.get(DATA));

        QuestionContainer qc = new QuestionContainer();
		MapData qmap = jsonData.getMapData(inData.get(Data.LoadQuestions.QUESTIONS));
        for (Entry<String, String> e : qmap.iterable()) {
			MapData qtnmap = jsonData.getMapData(e.getValue());
			List<String> options = new ArrayList<>();
			for (String entry : jsonData.getListData(qtnmap.get(Data.LoadQuestions.Question.OPTIONS)).iterable()) {
				options.add(entry);
			}

            Data.LoadQuestions.Question.Optional optional = Constants.getEnum(Data.LoadQuestions.Question.Optional.values(),
                    qtnmap.get(Data.LoadQuestions.Question.OPTIONAL));
            Question q = new Question(
                    Integer.parseInt(qtnmap.get(Data.LoadQuestions.Question.ID)),
                    qtnmap.get(Data.LoadQuestions.Question.TYPE),
                    qtnmap.get(Data.LoadQuestions.Question.QUESTION),
                    qtnmap.get(Data.LoadQuestions.Question.DESCRIPTION),
                    options,
                    Constants.equal(Data.LoadQuestions.Question.Optional.YES, optional),
                    Integer.parseInt(qtnmap.get(Data.LoadQuestions.Question.MAX_VAL)),
                    Integer.parseInt(qtnmap.get(Data.LoadQuestions.Question.MIN_VAL)));
			qc.addQuestion(q);
		}
		return qc;
	}

	@Override
	public List<Calendar> loadQuestionnaireResultDates(long uid)
	{
		MapData out = new MapData(null);
		out.put(TYPE, Types.LOAD_QR_DATE);
        MapData dataOut = new MapData(null);

		MapData details = new MapData(null);
        details.put(Data.LoadQResultDates.Details.UID, Long.toString(uid));
        dataOut.put(Data.LoadQResultDates.DETAILS, crypto.encrypt(details.toString()));

        out.put(DATA, dataOut.toString());
        MapData in = sendMessage(out);
        MapData inData = jsonData.getMapData(in.get(DATA));

        List<Calendar> dates = new ArrayList<>();
        ListData dlist = jsonData.getListData(inData.get(Data.LoadQResultDates.DATES));
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            for (String str : dlist.iterable()) {
				Calendar cal = new GregorianCalendar();
				cal.setTime(sdf.parse(str));
                dates.add(cal);
			}
		} catch (ParseException _e) {
			return null;
		}
		return dates;
	}
	
	@Override
	public StatisticsContainer loadQuestionnaireResults(
	        long uid, Calendar begin, Calendar end, List<Integer> questionIDs)
	{
		MapData out = new MapData(null);
		out.put(TYPE, Types.LOAD_QR);
        MapData dataOut = new MapData(null);

		ListData questions = new ListData(null);
		for (Integer i : questionIDs) {
            questions.add(Integer.toString(i));
        }
        dataOut.put(Data.LoadQResults.QUESTIONS, questions.toString());

		MapData details = new MapData(null);
        details.put(Data.LoadQResults.Details.UID, Long.toString(uid));
        dataOut.put(Data.LoadQResults.DETAILS, crypto.encrypt(details.toString()));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dataOut.put(Data.LoadQResults.BEGIN, sdf.format(begin.getTime()));
        dataOut.put(Data.LoadQResults.END, sdf.format(end.getTime()));

        out.put(DATA, dataOut.toString());
        MapData in = sendMessage(out);
        MapData inData = jsonData.getMapData(in.get(DATA));

        StatisticsContainer container = new StatisticsContainer();
		ListData rlist = jsonData.getListData(inData.get(Data.LoadQResults.RESULTS));
        for (String str : rlist.iterable()) {
			MapData ansmap = jsonData.getMapData(str);
            QuestionContainer qc = Questions.getContainer();
            for (Entry<String, String> e : ansmap.iterable()) {
                int qid = Integer.parseInt(e.getKey());
                container.addResult(qdbfmt.getQFormat(qc.getQuestion(qid), jsonData.getMapData(e.getValue())));
            }
        }
		return container;
	}
	
	@Override
	public boolean requestRegistration(String name, String email, String clinic)
	{
		MapData out = new MapData(null);
		out.put(TYPE, Types.REQ_REGISTR);
        MapData dataOut = new MapData(null);

		MapData details = new MapData(null);
        details.put(Data.RequestRegistration.Details.NAME, name);
        details.put(Data.RequestRegistration.Details.EMAIL, email);
        details.put(Data.RequestRegistration.Details.CLINIC, clinic);
        dataOut.put(Data.RequestRegistration.DETAILS, crypto.encrypt(details.toString()));

        out.put(DATA, dataOut.toString());
        MapData in = sendMessage(out);
        MapData inData = jsonData.getMapData(in.get(DATA));

        Data.RequestRegistration.Response insert = Data.RequestRegistration.Response.FAIL;
        try {
            insert = Constants.getEnum(Data.RequestRegistration.Response.values(), inData.get(Data.RequestRegistration.RESPONSE));
        } catch (NumberFormatException ignored) { }
        return Constants.equal(Data.RequestRegistration.Response.SUCCESS, insert);
	}

	@Override
	public Session requestLogin(String username, String password)
	{
		MapData out = new MapData(null);
		out.put(TYPE, Types.REQ_LOGIN);
        MapData dataOut = new MapData(null);

		MapData details = new MapData(null);
        details.put(Data.RequestLogin.Details.USERNAME, username);
        details.put(Data.RequestLogin.Details.PASSWORD, password);
        dataOut.put(Data.RequestLogin.DETAILS, crypto.encrypt(details.toString()));

        out.put(DATA, dataOut.toString());
        MapData in = sendMessage(out);
        MapData inData = jsonData.getMapData(in.get(DATA));

        Data.RequestLogin.Response response = Data.RequestLogin.Response.ERROR;
        Data.RequestLogin.UpdatePassword update_password = Data.RequestLogin.UpdatePassword.NO;
        try {
            response = Constants.getEnum(Data.RequestLogin.Response.values(), inData.get(Data.RequestLogin.RESPONSE));
            update_password = Constants.getEnum(Data.RequestLogin.UpdatePassword.values(), inData.get(Data.RequestLogin.UPDATE_PASSWORD));
        } catch (NumberFormatException ignored) { }

        String uid = inData.get(Data.RequestLogin.UID);
        return new Session(uid != null ? Long.parseLong(uid) : 0L, response,
                Constants.equal(Data.RequestLogin.UpdatePassword.YES, update_password));
	}

	@Override
	public boolean requestLogout(long uid)
	{
		MapData out = new MapData(null);
		out.put(TYPE, Types.REQ_LOGOUT);
        MapData dataOut = new MapData(null);

		MapData details = new MapData(null);
        details.put(Data.RequestLogout.Details.UID, Long.toString(uid));
        dataOut.put(Data.RequestLogout.DETAILS, crypto.encrypt(details.toString()));

        out.put(DATA, dataOut.toString());
		MapData in = sendMessage(out);
        MapData inData = jsonData.getMapData(in.get(DATA));

        Data.RequestLogout.Response insert = Data.RequestLogout.Response.ERROR;
        try {
            insert = Constants.getEnum(Data.RequestLogout.Response.values(), inData.get(Data.RequestLogout.RESPONSE));
        } catch (NumberFormatException ignored) { }
        return Constants.equal(Data.RequestLogout.Response.SUCCESS, insert);
	}

	private static PacketHandler pktHandler;
	private Encryption crypto;
	
	private PacketData jsonData;
    private ServletConnection scom;
    private QDBFormat qdbfmt;

	private PacketHandler(Encryption crypto)
	{
        this.crypto = crypto;
        scom = new ServletConnection(Constants.SERVER_URL);
		jsonData = new PacketData();
        qdbfmt = new QDBFormat();
	}

	private MapData sendMessage(MapData obj)
	{
        return jsonData.getMapData(scom.sendMessage(obj.toString()));
	}

	private class QDBFormat
	{
		String getDBFormat(FormContainer fc)
		{
            MapData fmt = new MapData(null);
			if (fc.getEntry() == null)
				return fmt.toString();

			if (fc instanceof SingleOptionContainer) {
                if (fc instanceof MultipleOptionContainer) {
                    MultipleOptionContainer moc = (MultipleOptionContainer) fc;
                    ListData options = new ListData(null);
                    for (Integer i : moc.getEntry())
                        options.add(String.format(Locale.US, "%d", i));
                    fmt.put(QuestionTypes.MULTIPLE_OPTION, options.toString());
                } else {
                    SingleOptionContainer soc = (SingleOptionContainer) fc;
                    fmt.put(QuestionTypes.SINGLE_OPTION, String.format(Locale.US, "%d", soc.getEntry().get(0)));
                }
			} else if (fc instanceof SliderContainer) {
				SliderContainer sc = (SliderContainer) fc;
                fmt.put(QuestionTypes.SLIDER, String.format(Locale.US, "%d", sc.getEntry()));
			} else if (fc instanceof AreaContainer) {
				AreaContainer ac = (AreaContainer) fc;
                fmt.put(QuestionTypes.AREA, ac.getEntry());
			}
            return fmt.toString();
		}
		
		/**
		 * Converts the answer {@code dbEntry} from its pktHandler
		 * representation to its java representation. The return type
		 * is {@code Object} to keep the formats general. The returned
		 * objects are in the format they need to be in order to
		 * represent the answer in its java format.
		 * 
		 * @param dbEntry The pktHandler entry that is to be converted
		 * 		to a java entry.
		 * 
 		 * @return The {@code Object} representation of the answer.
		 */
		Statistics getQFormat(Question q, MapData dbEntry)
		{
            String val;
			if ((val = dbEntry.get(QuestionTypes.SINGLE_OPTION)) != null) {
				return new SingleOption(q, Integer.valueOf(val));
			} else if ((val = dbEntry.get(QuestionTypes.SLIDER)) != null) {
				return new Slider(q, Integer.valueOf(val));
			} else if ((val = dbEntry.get(QuestionTypes.MULTIPLE_OPTION)) != null) {
                ListData options = jsonData.getListData(val);
                List<Integer> lint = new ArrayList<>();
                for (String str : options.iterable())
                    lint.add(Integer.valueOf(str));
                return new MultipleOption(q, lint);
			} else if ((val = dbEntry.get(QuestionTypes.AREA)) != null) {
				return new Area(q, val);
			}
			return null;
		}
	}
}