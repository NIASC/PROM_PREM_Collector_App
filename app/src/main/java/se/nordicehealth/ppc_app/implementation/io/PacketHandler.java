/*! ServletCommunication.java
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
import se.nordicehealth.ppc_app.core.containers.form.FieldContainer;
import se.nordicehealth.ppc_app.core.containers.form.FormContainer;
import se.nordicehealth.ppc_app.core.containers.form.MultipleOptionContainer;
import se.nordicehealth.ppc_app.core.containers.form.SingleOptionContainer;
import se.nordicehealth.ppc_app.core.containers.form.SliderContainer;
import se.nordicehealth.ppc_app.core.containers.form.TimePeriodContainer;
import se.nordicehealth.ppc_app.core.containers.statistics.Area;
import se.nordicehealth.ppc_app.core.containers.statistics.MultipleOption;
import se.nordicehealth.ppc_app.core.containers.statistics.SingleOption;
import se.nordicehealth.ppc_app.core.containers.statistics.Slider;
import se.nordicehealth.ppc_app.core.containers.statistics.Statistics;
import se.nordicehealth.ppc_app.core.interfaces.Server;
import se.nordicehealth.ppc_app.implementation.security.Encryption;
import se.nordicehealth.ppc_app.core.interfaces.Questions;
import se.nordicehealth.ppc_app.common.implementation.Constants;

/**
 * This class is an example of an implementation of
 * Database_Interface. This is done using a MySQL pktHandler and a
 * MySQL Connector/J to provide a MySQL interface to Java.
 * 
 * This class is designed to be thread safe and a singleton.
 * 
 * @author Marcus Malmquist
 *
 */
public class PacketHandler implements Server
{
	/* Public */
	
	/**
	 * Retrieves the active instance of this class.
	 * 
	 * @return The active instance of this class.
	 */
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
	public final Object clone()
			throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}

	@Override
	public boolean ping(long uid)
	{
		MapData ret = new MapData(null);
		ret.put("command", Constants.CMD_PING);

		MapData details = new MapData(null);
		details.put("uid", Long.toString(uid));

		ret.put("details", crypto.encrypt(details.toString()));

		MapData ans = sendMessage(ret);
        String resp = ans.get(Constants.INSERT_RESULT);
        return (resp != null && resp.equals(Constants.INSERT_SUCCESS));
	}

	@Override
    public boolean validatePatientID(long uid, String patientID)
    {
        MapData ret = new MapData(null);
        ret.put("command", Constants.CMD_VALIDATE_PID);

        MapData details = new MapData(null);
        details.put("uid", Long.toString(uid));

        MapData pobj = new MapData(null);
        pobj.put("personal_id", patientID);

        ret.put("details", crypto.encrypt(details.toString()));
        ret.put("patient", crypto.encrypt(pobj.toString()));

        MapData ans = sendMessage(ret);
        String resp = ans.get(Constants.INSERT_RESULT);
        return (resp != null && resp.equals(Constants.INSERT_SUCCESS));
    }

	@Override
	public boolean addQuestionnaireAnswers(long uid, Patient patient, List<FormContainer> answers)
	{
        MapData ret = new MapData(null);
        ret.put("command", Constants.CMD_ADD_QANS);

        QuestionContainer qc = Questions.getContainer();
        if (qc == null || qc.getSize() != answers.size()) {
			return false;
		}

		ListData questions = new ListData(null);
        for (FormContainer fc : answers) {
            questions.add(qdbfmt.getDBFormat(fc));
        }

		MapData pobj = new MapData(null);
        pobj.put("forename", patient.getForename());
        pobj.put("surname", patient.getSurname());
        pobj.put("personal_id", patient.getPersonalNumber());

		MapData details = new MapData(null);
        details.put("uid", Long.toString(uid));

        ret.put("details", crypto.encrypt(details.toString()));
        ret.put("patient", crypto.encrypt(pobj.toString()));
        ret.put("questions", questions.toString());

		MapData ans = sendMessage(ret);
		String insert = ans.get(Constants.INSERT_RESULT);
		return (insert != null && insert.equals(Constants.INSERT_SUCCESS));
	}

	@Override
	public int setPassword(long uid, String oldPass, String newPass1, String newPass2) throws NumberFormatException
    {
		MapData ret = new MapData(null);
		ret.put("command", Constants.CMD_SET_PASSWORD);

		MapData details = new MapData(null);
        details.put("uid", Long.toString(uid));
        details.put("old_password", oldPass);
        details.put("new_password1", newPass1);
        details.put("new_password2", newPass2);
        ret.put("details", crypto.encrypt(details.toString()));

		MapData amap = sendMessage(ret);
        String response = amap.get(Constants.SETPASS_REPONSE);
        return response != null ? Integer.parseInt(response) : Constants.ERROR;
	}
	
	@Override
	public QuestionContainer loadQuestions()
	{
		MapData ret = new MapData(null);
		ret.put("command", Constants.CMD_LOAD_Q);

        QuestionContainer qc = new QuestionContainer();
		MapData amap = sendMessage(ret);
		MapData qmap = jsonData.getMapData(amap.get("questions"));
        for (Entry<String, String> e : qmap.iterable()) {
			MapData qtnmap = jsonData.getMapData(e.getValue());
			List<String> options = new ArrayList<>();
			for (int i = 0; ; ++i) {
                String entry = qtnmap.get(String.format(Locale.US, "option%d", i));
				if (entry == null)
					break;
				options.add(entry);
			}

            Question q = new Question(Integer.parseInt(qtnmap.get("id")), qtnmap.get("type"),
                    qtnmap.get("question"), qtnmap.get("description"),
                    options, Integer.parseInt(qtnmap.get("optional")) != 0,
                    Integer.parseInt(qtnmap.get("max_val")),
                    Integer.parseInt(qtnmap.get("min_val")));
			qc.addQuestion(q);
		}
		return qc;
	}

	@Override
	public List<Calendar> loadQuestionnaireResultDates(long uid)
	{
		MapData ret = new MapData(null);
		ret.put("command", Constants.CMD_LOAD_QR_DATE);

		MapData details = new MapData(null);
        details.put("uid", Long.toString(uid));
        ret.put("details", crypto.encrypt(details.toString()));

        List<Calendar> dates = new ArrayList<>();
		MapData amap = sendMessage(ret);
        ListData dlist = jsonData.getListData(amap.get("dates"));
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
		MapData ret = new MapData(null);
		ret.put("command", Constants.CMD_LOAD_QR);

		ListData questions = new ListData(null);
		for (Integer i : questionIDs)
			questions.add(String.format(Locale.US, "%d", i));
        ret.put("questions", questions.toString());

		MapData details = new MapData(null);
        details.put("uid", Long.toString(uid));
        ret.put("details", crypto.encrypt(details.toString()));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        ret.put("begin", sdf.format(begin.getTime()));
        ret.put("end", sdf.format(end.getTime()));

		StatisticsContainer container = new StatisticsContainer();
		MapData amap = sendMessage(ret);
		ListData rlist = jsonData.getListData(amap.get("results"));
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
	public boolean requestRegistration(
			String name, String email, String clinic)
	{
		MapData ret = new MapData(null);
		ret.put("command", Constants.CMD_REQ_REGISTR);

		MapData details = new MapData(null);
        details.put("name", name);
        details.put("email", email);
        details.put("clinic", clinic);
        ret.put("details", crypto.encrypt(details.toString()));

		MapData ans = sendMessage(ret);
		String insert = ans.get(Constants.INSERT_RESULT);
		return (insert != null && insert.equals(Constants.INSERT_SUCCESS));
	}

	@Override
	public Session requestLogin(String username, String password)
	{
		MapData ret = new MapData(null);
		ret.put("command", Constants.CMD_REQ_LOGIN);

		MapData details = new MapData(null);
        details.put("name", username);
        details.put("password", password);
        ret.put("details", crypto.encrypt(details.toString()));

		MapData ans = sendMessage(ret);
        String response = ans.get(Constants.LOGIN_REPONSE);
        String uid = ans.get(Constants.LOGIN_UID);
        String update_password = ans.get("update_password");
        return new Session(uid != null ? Long.parseLong(uid) : 0L,
                response != null ? Integer.parseInt(response) : Constants.ERROR,
                update_password != null && Integer.parseInt(update_password) > 0);
	}

	@Override
	public boolean requestLogout(long uid)
	{
		MapData ret = new MapData(null);
		ret.put("command", Constants.CMD_REQ_LOGOUT);

		MapData details = new MapData(null);
        details.put("uid", Long.toString(uid));
        ret.put("details", crypto.encrypt(details.toString()));

		MapData ans = sendMessage(ret);
		String response = ans.get(Constants.LOGOUT_REPONSE);
		return response != null && Integer.parseInt(response) == Constants.SUCCESS;
	}
	
	/* Protected */
	
	/* Private */

	private static PacketHandler pktHandler;
	private Encryption crypto;
	
	private PacketData jsonData;
    private ServletConnection scom;
    private QDBFormat qdbfmt;
	
	/**
	 * Initializes variables and loads the pktHandler configuration.
	 * This class is a singleton and should only be instantiated once.
	 */
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
		/**
		 * Converts the answer stored in {@code fc} to the format used
		 * in the pktHandler.
		 * 
		 * @param fc The container for the question which have been
		 * 		answered and should have the answer stored in the
		 * 		pktHandler.
		 * 
		 * @return The pktHandler representation for the answer in
		 * 		{@code fc}.
		 */
		String getDBFormat(FormContainer fc)
		{
            MapData fmt = new MapData(null);
			if (fc.getEntry() == null)
				return fmt.toString();

			if (fc instanceof SingleOptionContainer) {
				SingleOptionContainer soc = (SingleOptionContainer) fc;
				fmt.put("SingleOption", String.format(Locale.US, "%d", soc.getEntry()));
			} else if (fc instanceof MultipleOptionContainer) {
				MultipleOptionContainer moc = (MultipleOptionContainer) fc;
                ListData options = new ListData(null);
				for (Integer i : moc.getEntry())
                    options.add(String.format(Locale.US, "%d", i));
                fmt.put("MultipleOption", options.toString());
			} else if (fc instanceof SliderContainer) {
				SliderContainer sc = (SliderContainer) fc;
                fmt.put("Slider", String.format(Locale.US, "%d", sc.getEntry()));
			} else if (fc instanceof AreaContainer) {
				AreaContainer ac = (AreaContainer) fc;
                fmt.put("Area", ac.getEntry());
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
			if ((val = dbEntry.get("SingleOption")) != null) {
				return new SingleOption(q, Integer.valueOf(val));
			} else if ((val = dbEntry.get("Slider")) != null) {
				return new Slider(q, Integer.valueOf(val));
			} else if ((val = dbEntry.get("MultipleOption")) != null) {
                ListData options = jsonData.getListData(val);
                List<Integer> lint = new ArrayList<>();
                for (String str : options.iterable())
                    lint.add(Integer.valueOf(str));
                return new MultipleOption(q, lint);
			} else if ((val = dbEntry.get("Area")) != null) {
				return new Area(q, val);
			}
			return null;
		}
	}
}