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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import se.nordicehealth.ppc_app.core.containers.MessageContainer;
import se.nordicehealth.ppc_app.core.containers.Patient;
import se.nordicehealth.ppc_app.core.containers.QuestionContainer;
import se.nordicehealth.ppc_app.core.containers.StatisticsContainer;
import se.nordicehealth.ppc_app.core.containers.QuestionContainer.Question;
import se.nordicehealth.ppc_app.core.containers.form.AreaContainer;
import se.nordicehealth.ppc_app.core.containers.form.FieldContainer;
import se.nordicehealth.ppc_app.core.containers.form.FormContainer;
import se.nordicehealth.ppc_app.core.containers.form.MultipleOptionContainer;
import se.nordicehealth.ppc_app.core.containers.form.SingleOptionContainer;
import se.nordicehealth.ppc_app.core.containers.form.SliderContainer;
import se.nordicehealth.ppc_app.core.containers.form.TimePeriodContainer;
import se.nordicehealth.ppc_app.core.interfaces.Database;
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
public class PacketHandler implements Database
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
	public boolean addQuestionnaireAnswers(long uid, Patient patient, List<FormContainer> answers)
	{
        MapData ret = new MapData(null);
        ret.put("command", Constants.CMD_ADD_QANS);

        QuestionContainer qc = Questions.getQuestions().getContainer();
        if (qc == null || qc.getSize() != answers.size()) {
			return false;
		}

		MapData questions = new MapData(null);
		int i = 0;
        for (FormContainer fc : answers) {
            questions.put(String.format(Locale.US, "`question%d`", i++),
                    QDBFormat.getDBFormat(fc));
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
        return Integer.parseInt(amap.get(Constants.SETPASS_REPONSE));
	}

	@Override
    @Deprecated
	public boolean getErrorMessages(MessageContainer mc) {
        return mc != null && getMessages(Constants.CMD_GET_ERR_MSG, mc);
    }

	@Override
    @Deprecated
	public boolean getInfoMessages(MessageContainer mc) {
        return mc != null && getMessages(Constants.CMD_GET_INFO_MSG, mc);
    }
	
	@Override
	public boolean loadQuestions(QuestionContainer qc)
	{
		MapData ret = new MapData(null);
		ret.put("command", Constants.CMD_LOAD_Q);

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
			Class<? extends FormContainer> c;
			if ((c = getContainerClass(qtnmap.get("type"))) == null) {
                continue;
            }
			qc.addQuestion(Integer.parseInt(qtnmap.get("id")), c,
                    qtnmap.get("question"), qtnmap.get("description"),
					options, Integer.parseInt(qtnmap.get("optional")) != 0,
					Integer.parseInt(qtnmap.get("max_val")),
					Integer.parseInt(qtnmap.get("min_val")));
		}
		return true;
	}

	@Override
	public boolean loadQResultDates(long uid, TimePeriodContainer tpc)
	{
		MapData ret = new MapData(null);
		ret.put("command", Constants.CMD_LOAD_QR_DATE);

		MapData details = new MapData(null);
        details.put("uid", Long.toString(uid));
        ret.put("details", crypto.encrypt(details.toString()));

		MapData amap = sendMessage(ret);
        ListData dlist = jsonData.getListData(amap.get("dates"));
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            for (String str : dlist.iterable()) {
				Calendar cal = new GregorianCalendar();
				cal.setTime(sdf.parse(str));
				tpc.addDate(cal);
			}
		} catch (ParseException _e) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean loadQResults(long uid, Calendar begin, Calendar end,
			List<Integer> questionIDs, StatisticsContainer container)
	{
		MapData ret = new MapData(null);
		ret.put("command", Constants.CMD_LOAD_QR);

		ListData questions = new ListData(null);
		for (Integer i : questionIDs)
			questions.add(String.format(Locale.US, "question%d", i));
        ret.put("questions", questions.toString());

		MapData details = new MapData(null);
        details.put("uid", Long.toString(uid));
        ret.put("details", crypto.encrypt(details.toString()));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        ret.put("begin", sdf.format(begin.getTime()));
        ret.put("end", sdf.format(end.getTime()));


		MapData amap = sendMessage(ret);
		ListData rlist = jsonData.getListData(amap.get("results"));
        for (String str : rlist.iterable()) {
			MapData ansmap = jsonData.getMapData(str);
            QuestionContainer qc = Questions.getQuestions().getContainer();
            assert qc != null;
            for (Entry<String, String> e : ansmap.iterable()) {
                int qid = Integer.parseInt(e.getKey().substring("question".length()));
                Question q1 = qc.getQuestion(qid);
                container.addResult(q1, QDBFormat.getQFormat(e.getValue()));
            }
        }
		return true;
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
		return Integer.parseInt(ans.get(Constants.LOGOUT_REPONSE)) == Constants.SUCCESS;
	}
	
	/* Protected */
	
	/* Private */

	private static PacketHandler pktHandler;
	private Encryption crypto;
	
	private PacketData jsonData;
    private volatile MapData JSONOut, JSONIn;
    private ServletConnection scom;
	
	/**
	 * Initializes variables and loads the pktHandler configuration.
	 * This class is a singleton and should only be instantiated once.
	 */
	private PacketHandler(Encryption crypto)
	{
        this.crypto = crypto;
        scom = new ServletConnection(Constants.SERVER_URL);
		jsonData = new PacketData();
	}
	
	/**
	 * Sends a JSONObject to the servlet.
	 * 
	 * @param obj The JSONObject to send.
	 * 
	 * @return The JSONObject returned from the servlet.
	 */
	private MapData sendMessage(MapData obj)
	{
        return jsonData.getMapData(scom.sendMessage(obj.toString()));
	}

	/**
	 * Retrieves messages from the pktHandler and places them in the
	 * {@code MessageContainer}.
	 * 
	 * @param commandName The name of the (message) table to retrieve
	 * 		messages from.
	 * @param mc The {@code MessageContainer} to put the messages in.
	 * 
	 * @return true if the messages was put in {@code mc}.
	 */
	@Deprecated
	private boolean getMessages(String commandName, MessageContainer mc)
	{
		MapData ret = new MapData(null);
		ret.put("command", commandName);

		MapData ans = sendMessage(ret);
		MapData messages = jsonData.getMapData(ans.get("messages"));
		try {
            for (Entry<String, String> e : messages.iterable()) {
				MapData messagedata = jsonData.getMapData(e.getValue());
				MapData message = jsonData.getMapData(messagedata.get("message"));
				mc.addMessage(Integer.parseInt(messagedata.get("code")),
                        messagedata.get("name"), message.map());
			}
		}
		catch (NullPointerException _e) {
			return false;
		}
		return true;
	}
	
	/**
	 * This method converts a container type from the String
	 * representation in the pktHandler to the appropriate class
	 * representation in java.
	 * 
	 * @param type The type of container as it appears in the pktHandler
	 * 		(SingleOption, Slider, Field etc.).
	 * 
	 * @return The class representation of the supplied {@code type}.
	 * 		The classes can be acquired using isAssignableFrom.<Br>
	 * 		Example:<br>
	 * 		<code>
	 * 		if (SliderContainer.class.isAssignableFrom(getContainerClass("Slider")))
	 * 		new SliderContainer( ... );</code>
	 * 
	 * @see Class#isAssignableFrom
	 */
	private Class<? extends FormContainer> getContainerClass(String type)
	{
		if (type.equalsIgnoreCase("SingleOption")) {
            return SingleOptionContainer.class;
        } else if (type.equalsIgnoreCase("MultipleOption")) {
            return MultipleOptionContainer.class;
        } else if (type.equalsIgnoreCase("Field")) {
            return FieldContainer.class;
        } else if (type.equalsIgnoreCase("Slider")) {
            return SliderContainer.class;
        } else if (type.equalsIgnoreCase("Area")) {
            return AreaContainer.class;
        } else {
            return null;
        }
	}
	
	/**
	 * This class handles converting question answer formats between its
	 * pktHandler representation and its java representation.
	 * 
	 * @author Marcus Malmquist
	 *
	 */
	private static class QDBFormat
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
		static String getDBFormat(FormContainer fc)
		{
			if (fc.getEntry() == null)
				return "''";
			
			if (fc instanceof SingleOptionContainer) {
				SingleOptionContainer soc = (SingleOptionContainer) fc;
				return String.format(Locale.US, "'option%d'", soc.getEntry());
			} else if (fc instanceof MultipleOptionContainer) {
				MultipleOptionContainer moc = (MultipleOptionContainer) fc;
				List<String> lstr = new ArrayList<>();
				List<Integer> lint = new ArrayList<>(moc.getEntry());
				Collections.sort(lint);
				for (Integer i : lint)
					lstr.add(String.format(Locale.US, "option%d", i));

                StringBuilder sb = new StringBuilder();
                for (Iterator<String> itr = lstr.iterator(); itr.hasNext();) {
                    sb.append(itr.next());
                    if (itr.hasNext())
                        sb.append(",");
                }
				return String.format("[%s]", sb.toString());
			} else if (fc instanceof SliderContainer) {
				SliderContainer sc = (SliderContainer) fc;
				return String.format(Locale.US, "'slider%d'", sc.getEntry());
			} else if (fc instanceof AreaContainer) {
				AreaContainer ac = (AreaContainer) fc;
				return String.format("'%s'", ac.getEntry());
			} else
				return "''";
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
		static Object getQFormat(String dbEntry)
		{
			if (dbEntry == null || dbEntry.trim().isEmpty())
				return null;
			
			if (dbEntry.startsWith("option")) {
                /* single option */
				return Integer.valueOf(dbEntry.substring("option".length()));
			} else if (dbEntry.startsWith("slider")) {
                /* slider */
				return Integer.valueOf(dbEntry.substring("slider".length()));
			} else if (dbEntry.startsWith("[") && dbEntry.endsWith("]")) {
                /* multiple answers */
				List<String> entries = Arrays.asList(dbEntry.split(","));
				if (entries.get(0).startsWith("option")) {
                    /* multiple option */
					List<Integer> lint = new ArrayList<>();
					for (String str : entries)
						lint.add(Integer.valueOf(str.substring("option".length())));
					return lint;
				}
			} else {
                /* must be plain text entry */
				return dbEntry;
			}
			return null;
		}
	}
}