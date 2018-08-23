package se.nordicehealth.ppc_app.impl.io;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import se.nordicehealth.ppc_app.common.impl.Packet;
import se.nordicehealth.ppc_app.core.containers.Patient;
import se.nordicehealth.ppc_app.core.containers.Question;
import se.nordicehealth.ppc_app.core.containers.QuestionContainer;
import se.nordicehealth.ppc_app.core.containers.StatisticsContainer;
import se.nordicehealth.ppc_app.core.containers.StatisticsData;
import se.nordicehealth.ppc_app.core.containers.form.AreaContainer;
import se.nordicehealth.ppc_app.core.containers.form.FormContainer;
import se.nordicehealth.ppc_app.core.containers.form.MultipleOptionContainer;
import se.nordicehealth.ppc_app.core.containers.form.SingleOptionContainer;
import se.nordicehealth.ppc_app.core.containers.form.SliderContainer;
import se.nordicehealth.ppc_app.core.interfaces.Server;
import se.nordicehealth.ppc_app.impl.res.Resource;
import se.nordicehealth.ppc_app.impl.security.Encryption;
import se.nordicehealth.ppc_app.core.interfaces.Questions;
import se.nordicehealth.ppc_app.common.impl.Constants;

public enum PacketHandler implements Server {
    instance;

	public static void initialize(Encryption crypto) {
        instance.crypto = crypto;
    }

	@Override
	public boolean ping(long uid)
	{
		MapData out = jsonData.getMapData();
		out.put(Packet.TYPE, Packet.PING);
        MapData dataOut = jsonData.getMapData();

		MapData details = jsonData.getMapData();
		details.put(Packet.UID, Long.toString(uid));
        dataOut.put(Packet.DETAILS, crypto.encrypt(details.toString()));

        out.put(Packet.DATA, dataOut.toString());
        MapData in = sendMessage(out);
        MapData inData = jsonData.getMapData(in.get(Packet.DATA));

        String insert = inData.get(Packet.RESPONSE);
        if (insert == null) {
            insert = Packet.FAIL;
        }
        return insert.equals(Packet.SUCCESS);
	}

	@Override
    public boolean validatePatientID(long uid, String patientID)
    {
        MapData out = jsonData.getMapData();
        out.put(Packet.TYPE, Packet.VALIDATE_PID);
        MapData dataOut = jsonData.getMapData();

        MapData details = jsonData.getMapData();
        details.put(Packet.UID, Long.toString(uid));

        MapData pobj = jsonData.getMapData();
        pobj.put(Packet.PERSONAL_ID, patientID);

        dataOut.put(Packet.DETAIL, crypto.encrypt(details.toString()));
        dataOut.put(Packet.PATIENT, crypto.encrypt(pobj.toString()));

        out.put(Packet.DATA, dataOut.toString());
        MapData in = sendMessage(out);
        MapData inData = jsonData.getMapData(in.get(Packet.DATA));

        String insert = inData.get(Packet.RESPONSE);
        if (insert == null) {
            insert = Packet.FAIL;
        }
        return insert.equals(Packet.SUCCESS);
    }

	@Override
	public boolean addQuestionnaireAnswers(long uid, Patient patient, List<FormContainer> answers)
	{
        MapData out = jsonData.getMapData();
        out.put(Packet.TYPE, Packet.ADD_QANS);
        MapData dataOut = jsonData.getMapData();

        QuestionContainer qc = Questions.getContainer();
        if (qc == null || qc.getSize() != answers.size())
			return false;

		ListData questions = jsonData.getListData();
        for (FormContainer fc : answers)
            questions.add(qdbfmt.getDBFormat(fc));

		MapData pobj = jsonData.getMapData();
        pobj.put(Packet.FORENAME, patient.getForename());
        pobj.put(Packet.SURNAME, patient.getSurname());
        pobj.put(Packet.PERSONAL_ID, patient.getPersonalNumber());

		MapData details = jsonData.getMapData();
        details.put(Packet.UID, Long.toString(uid));

        dataOut.put(Packet.DETAILS, crypto.encrypt(details.toString()));
        dataOut.put(Packet.PATIENT, crypto.encrypt(pobj.toString()));
        dataOut.put(Packet.QUESTIONS, questions.toString());

        out.put(Packet.DATA, dataOut.toString());
        MapData in = sendMessage(out);
        MapData inData = jsonData.getMapData(in.get(Packet.DATA));

        String insert = inData.get(Packet.RESPONSE);
        if (insert == null) {
            insert = Packet.FAIL;
        }
        return insert.equals(Packet.SUCCESS);
	}

	@Override
	public String setPassword(long uid, String oldPass, String newPass1, String newPass2) throws NumberFormatException
    {
		MapData out = jsonData.getMapData();
		out.put(Packet.TYPE, Packet.SET_PASSWORD);
        MapData dataOut = jsonData.getMapData();

		MapData details = jsonData.getMapData();
        details.put(Packet.UID, Long.toString(uid));
        details.put(Packet.OLD_PASSWORD, oldPass);
        details.put(Packet.NEW_PASSWORD1, newPass1);
        details.put(Packet.NEW_PASSWORD2, newPass2);
        dataOut.put(Packet.DETAILS, crypto.encrypt(details.toString()));

        out.put(Packet.DATA, dataOut.toString());
        MapData in = sendMessage(out);
        MapData inData = jsonData.getMapData(in.get(Packet.DATA));

        String insert = inData.get(Packet.RESPONSE);
        if (insert == null) {
            insert = Packet.ERROR;
        }
        return insert;
	}
	
	@Override
	public QuestionContainer loadQuestions()
	{
		MapData out = jsonData.getMapData();
		out.put(Packet.TYPE, Packet.LOAD_Q);
        MapData dataOut = jsonData.getMapData();

        out.put(Packet.DATA, dataOut.toString());
        MapData in = sendMessage(out);
        MapData inData = jsonData.getMapData(in.get(Packet.DATA));

        QuestionContainer qc = new QuestionContainer();
		MapData qmap = jsonData.getMapData(inData.get(Packet.QUESTIONS));
        for (Entry<String, String> e : qmap.iterable()) {
			MapData qtnmap = jsonData.getMapData(e.getValue());
			List<String> options = new ArrayList<>();
			for (String entry : jsonData.getListData(qtnmap.get(Packet.OPTIONS)).iterable()) {
				options.add(entry);
			}

            String optional = qtnmap.get(Packet.OPTIONAL);
            Question q = new Question(
                    Integer.parseInt(qtnmap.get(Packet.ID)),
                    qtnmap.get(Packet.TYPE),
                    qtnmap.get(Packet.QUESTION),
                    qtnmap.get(Packet.DESCRIPTION),
                    options,
                    optional.equals(Packet.YES),
                    Integer.parseInt(qtnmap.get(Packet.MAX_VAL)),
                    Integer.parseInt(qtnmap.get(Packet.MIN_VAL)));
			qc.addQuestion(q);
		}
		return qc;
	}

	@Override
	public List<Calendar> loadQuestionnaireResultDates(long uid)
	{
		MapData out = jsonData.getMapData();
		out.put(Packet.TYPE, Packet.LOAD_QR_DATE);
        MapData dataOut = jsonData.getMapData();

		MapData details = jsonData.getMapData();
        details.put(Packet.UID, Long.toString(uid));
        dataOut.put(Packet.DETAILS, crypto.encrypt(details.toString()));

        out.put(Packet.DATA, dataOut.toString());
        MapData in = sendMessage(out);
        MapData inData = jsonData.getMapData(in.get(Packet.DATA));

        List<Calendar> dates = new ArrayList<>();
        ListData dlist = jsonData.getListData(inData.get(Packet.DATES));
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
		MapData out = jsonData.getMapData();
		out.put(Packet.TYPE, Packet.LOAD_QR);
        MapData dataOut = jsonData.getMapData();

		ListData questions = jsonData.getListData();
		for (Integer i : questionIDs) {
            questions.add(Integer.toString(i));
        }
        dataOut.put(Packet.QUESTIONS, questions.toString());

		MapData details = jsonData.getMapData();
        details.put(Packet.UID, Long.toString(uid));
        dataOut.put(Packet.DETAILS, crypto.encrypt(details.toString()));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dataOut.put(Packet.BEGIN, sdf.format(begin.getTime()));
        dataOut.put(Packet.END, sdf.format(end.getTime()));

        out.put(Packet.DATA, dataOut.toString());
        MapData in = sendMessage(out);
        MapData inData = jsonData.getMapData(in.get(Packet.DATA));

        StatisticsContainer container = new StatisticsContainer();
        MapData rlist = jsonData.getMapData(inData.get(Packet.RESULTS));
        for (Entry<String, String> str : rlist.iterable()) {
            Question q = Questions.getContainer().getQuestion(Integer.parseInt(str.getKey()));
            MapData ansmap = jsonData.getMapData(str.getValue());

            Map<String, Integer> identifierAndCount = new TreeMap<>();
            for (Entry<String, String> e : ansmap.iterable()) {
                identifierAndCount.put(e.getKey(), Integer.parseInt(e.getValue()));
            }
            container.addResult(qdbfmt.getQFormat(q, identifierAndCount));
        }

		return container;
	}
	
	@Override
	public boolean requestRegistration(String name, String email, String clinic)
	{
		MapData out = jsonData.getMapData();
		out.put(Packet.TYPE, Packet.REQ_REGISTR);
        MapData dataOut = jsonData.getMapData();

		MapData details = jsonData.getMapData();
        details.put(Packet.NAME, name);
        details.put(Packet.EMAIL, email);
        details.put(Packet.CLINIC, clinic);
        dataOut.put(Packet.DETAILS, crypto.encrypt(details.toString()));

        out.put(Packet.DATA, dataOut.toString());
        MapData in = sendMessage(out);
        MapData inData = jsonData.getMapData(in.get(Packet.DATA));

        String insert = inData.get(Packet.RESPONSE);
        if (insert == null) {
            insert = Packet.FAIL;
        }
        return insert.equals(Packet.SUCCESS);
	}

	@Override
	public Session requestLogin(String username, String password)
	{
		MapData out = jsonData.getMapData();
		out.put(Packet.TYPE, Packet.REQ_LOGIN);
        MapData dataOut = jsonData.getMapData();

		MapData details = jsonData.getMapData();
        details.put(Packet.USERNAME, username);
        details.put(Packet.PASSWORD, password);
        dataOut.put(Packet.DETAILS, crypto.encrypt(details.toString()));

        out.put(Packet.DATA, dataOut.toString());
        MapData in = sendMessage(out);
        MapData inData = jsonData.getMapData(in.get(Packet.DATA));

        String response = inData.get(Packet.RESPONSE);
        if (response == null) {
            response = Packet.ERROR;
        }
        String update_password = inData.get(Packet.UPDATE_PASSWORD);
        if (update_password == null) {
            update_password = Packet.NO;
        }
        String uid = inData.get(Packet.UID);
        return new Session(uid != null ? Long.parseLong(uid) : 0L, response,
                update_password.equals(Packet.YES));
	}

	@Override
	public boolean requestLogout(long uid)
	{
		MapData out = jsonData.getMapData();
		out.put(Packet.TYPE, Packet.REQ_LOGOUT);
        MapData dataOut = jsonData.getMapData();

		MapData details = jsonData.getMapData();
        details.put(Packet.UID, Long.toString(uid));
        dataOut.put(Packet.DETAILS, crypto.encrypt(details.toString()));

        out.put(Packet.DATA, dataOut.toString());
		MapData in = sendMessage(out);
        MapData inData = jsonData.getMapData(in.get(Packet.DATA));

        String insert = inData.get(Packet.RESPONSE);
        if (insert == null) {
            insert = Packet.ERROR;
        }
        return insert.equals(Packet.SUCCESS);
	}

	private Encryption crypto;
	
	private PacketData jsonData;
    private ServletConnection scom;
    private QDBFormat qdbfmt;

    PacketHandler() {
        scom = new ServletConnection(Resource.data().getServerURL());
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
            MapData fmt = jsonData.getMapData();
			if (fc.getEntry() == null)
				return fmt.toString();

			if (fc instanceof SingleOptionContainer) {
                if (fc instanceof MultipleOptionContainer) {
                    MultipleOptionContainer moc = (MultipleOptionContainer) fc;
                    ListData options = jsonData.getListData();
                    for (Integer i : moc.getEntry())
                        options.add(String.format(Locale.US, "%d", i));
                    fmt.put(Constants.MULTIPLE_OPTION, options.toString());
                } else {
                    SingleOptionContainer soc = (SingleOptionContainer) fc;
                    fmt.put(Constants.SINGLE_OPTION, String.format(Locale.US, "%d", soc.getEntry().get(0)));
                }
			} else if (fc instanceof SliderContainer) {
				SliderContainer sc = (SliderContainer) fc;
                fmt.put(Constants.SLIDER, String.format(Locale.US, "%d", sc.getEntry()));
			} else if (fc instanceof AreaContainer) {
				AreaContainer ac = (AreaContainer) fc;
                fmt.put(Constants.AREA, ac.getEntry());
			}
            return fmt.toString();
		}

        StatisticsData getQFormat(Question q, Map<String, Integer> identifierAndCount)
		{
            FormContainer container = q.getContainer();
			if (container instanceof SingleOptionContainer) {
                Map<String, Integer> data = new TreeMap<>();
                for (Entry<String, Integer> e : identifierAndCount.entrySet()) {
                    data.put(q.getOption(Integer.parseInt(e.getKey())), e.getValue());
                }
				return new StatisticsData(q, data);
			} else if (container instanceof SliderContainer) {
                Map<String, Integer> data = new TreeMap<>();
                for (Entry<String, Integer> e : identifierAndCount.entrySet()) {
                    data.put(e.getKey(), e.getValue());
                }
                return new StatisticsData(q, data);
            } else if (container instanceof AreaContainer) {
                Map<String, Integer> data = new TreeMap<>();
                for (Entry<String, Integer> e : identifierAndCount.entrySet()) {
                    if (!e.getKey().trim().isEmpty()) {
                        data.put(e.getKey(), e.getValue());
                    }
                }
                return new StatisticsData(q, data);
			}
			return null;
		}
	}
}