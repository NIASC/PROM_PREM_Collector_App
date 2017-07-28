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
package applet.implementation;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import applet.core.containers.MessageContainer;
import applet.core.containers.Patient;
import applet.core.containers.QuestionContainer;
import applet.core.containers.StatisticsContainer;
import applet.core.containers.User;
import applet.core.containers.QuestionContainer.Question;
import applet.core.containers.form.AreaContainer;
import applet.core.containers.form.FieldContainer;
import applet.core.containers.form.FormContainer;
import applet.core.containers.form.MultipleOptionContainer;
import applet.core.containers.form.SingleOptionContainer;
import applet.core.containers.form.SliderContainer;
import applet.core.containers.form.TimePeriodContainer;
import applet.core.interfaces.Database;
import applet.core.interfaces.Encryption;
import applet.core.interfaces.Implementations;
import applet.core.interfaces.Questions;

/**
 * This class is an example of an implementation of
 * Database_Interface. This is done using a MySQL database and a
 * MySQL Connector/J to provide a MySQL interface to Java.
 * 
 * This class is designed to be thread safe and a singleton.
 * 
 * @author Marcus Malmquist
 *
 */
@SuppressWarnings("unchecked")
public class MySQL_Database implements Database
{
	/* Public */
	
	/**
	 * 
	 * @return The active instance of this class.
	 */
	public static synchronized MySQL_Database getDatabase()
	{
		if (database == null)
			database = new MySQL_Database();
		return database;
	}

	@Override
	public final Object clone()
			throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
	/* 
	 * Public methods required by the interface.
	 */

	@Override
	public boolean addUser(String username, String password,
			String salt, int clinic, String email)
	{
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "add_user");
		rmap.put("clinic_id", Integer.toString(clinic));
		rmap.put("name", username);
		rmap.put("password", password);
		rmap.put("email", email);
		rmap.put("salt", salt);
		
		JSONObject ans = sendMessage(ret.toString());
		if (ans == null)
			return false;
		String insert = (String) ans.get("insert_result");
		return (insert != null && insert.equals("insert_success"));
	}

	@Override
	public boolean addQuestionnaireAnswers(Patient patient, List<FormContainer> answers)
	{
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "add_questionnaire_answers");
		
		int nQuestions = Questions.getQuestions().getContainer().getSize();
		if (answers.size() != nQuestions)
			return false;

		JSONObject questions = new JSONObject();
		Map<String, String> qmap = (Map<String, String>) questions;
		int i = 0;
		for (Iterator<FormContainer> itr = answers.iterator(); itr.hasNext();)
		{
			qmap.put(String.format("`question%d`", i++),
					QDBFormat.getDBFormat(itr.next()));
		}
		
		String identifier = crypto.encryptMessage(
				patient.getForename(), patient.getPersonalNumber(),
				patient.getSurname());
		
		rmap.put("clinic_id", Integer.toString(patient.getClinicID()));
		rmap.put("identifier", identifier);
		rmap.put("questions", questions.toString());

		JSONObject ans = sendMessage(ret.toString());
		if (ans == null)
			return false;
		String insert = (String) ans.get("insert_result");
		return (insert != null && insert.equals("insert_success"));
	}

	@Override
	public boolean addClinic(String clinicName)
	{
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "add_clinic");
		rmap.put("name", clinicName);
		
		JSONObject ans = sendMessage(ret.toString());
		if (ans == null)
			return false;
		Map<String, String> amap = (Map<String, String>) ans;
		String insert = amap.get("insert_result");
		return (insert != null && insert.equals("insert_success"));
	}
	
	@Override
	public Map<Integer, String> getClinics()
	{
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "get_clinics");
		
		JSONObject ans = sendMessage(ret.toString());
		JSONObject clinics = getJSONObject((String) ans.get("clinics"));
		Map<String, String> cmap = (Map<String, String>) clinics;
		
		Map<Integer, String> clinic = new TreeMap<Integer, String>();
		for (Entry<String, String> e : cmap.entrySet())
		{
			clinic.put(Integer.parseInt(e.getKey()),
					e.getValue());
		}
		return clinic;
	}

	@Override
	public User getUser(String username)
	{
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "get_user");
		rmap.put("name", username);

		JSONObject ans = sendMessage(ret.toString());
		JSONObject user = getJSONObject((String) ans.get("user"));
		Map<String, String> umap = (Map<String, String>) user;
		User usr = null;
		try {
			usr = new User(Integer.parseInt(umap.get("clinic_id")),
					umap.get("name"),
					umap.get("password"),
					umap.get("email"),
					umap.get("salt"),
					Integer.parseInt(umap.get("update_password")) != 0);
		} catch (NullPointerException _e) {}
		return usr;
	}

	@Override
	public User setPassword(User currentUser, String oldPass, String newPass,
			String newSalt)
	{
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "set_password");
		rmap.put("name", currentUser.getUsername());
		rmap.put("old_password", currentUser.hashWithSalt(oldPass));
		rmap.put("new_password", crypto.hashString(newPass, newSalt));
		rmap.put("new_salt", newSalt);

		JSONObject ans = sendMessage(ret.toString());
		JSONObject user = getJSONObject((String) ans.get("user"));
		Map<String, String> umap = (Map<String, String>) user;
		User usr = null;
		try {
			usr = new User(Integer.parseInt(umap.get("clinic_id")),
					umap.get("name"),
					umap.get("password"),
					umap.get("email"),
					umap.get("salt"),
					Integer.parseInt(umap.get("update_password")) != 0);
		} catch (NullPointerException _e) {}
		return usr;
	}

	@Override
	public boolean getErrorMessages(MessageContainer mc)
	{
		if (mc == null)
			return false;
		return getMessages("get_error_messages", mc);
	}

	@Override
	public boolean getInfoMessages(MessageContainer mc)
	{
		if (mc == null)
			return false;
		return getMessages("get_info_messages", mc);
	}
	
	@Override
	public boolean loadQuestions(QuestionContainer qc)
	{
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "load_questions");
		
		Map<String, String> amap = (Map<String, String>) sendMessage(ret.toString());
		Map<String, String> qmap = (Map<String, String>) getJSONObject(amap.get("questions"));
		for (Entry<String, String> e : qmap.entrySet())
		{
			Map<String, String> qtnmap = (Map<String, String>) getJSONObject(e.getValue());
			List<String> options = new ArrayList<String>();
			for (int i = 0; ; ++i)
			{
				String entry = qtnmap.get(String.format("option%d", i));
				if (entry == null)
					break;
				options.add(entry);
			}
			Class<? extends FormContainer> c;
			if ((c = getContainerClass(qtnmap.get("type"))) == null)
				continue;
			qc.addQuestion(Integer.parseInt(qtnmap.get("id")), c,
					qtnmap.get("question"), qtnmap.get("description"),
					options, Integer.parseInt(qtnmap.get("optional")) != 0,
					Integer.parseInt(qtnmap.get("max_val")),
					Integer.parseInt(qtnmap.get("min_val")));
		}
		return true;
	}

	@Override
	public boolean loadQResultDates(User user, TimePeriodContainer tpc)
	{
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "load_q_result_dates");
		rmap.put("name", user.getUsername());

		Map<String, String> amap = (Map<String, String>) sendMessage(ret.toString());
		List<String> dlist = (List<String>) getJSONArray(amap.get("dates"));
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for (Iterator<String> itr = dlist.iterator(); itr.hasNext();)
			{
				Calendar cal = new GregorianCalendar();
				cal.setTime(sdf.parse(itr.next()));
				tpc.addDate(cal);
			}
		}
		catch (ParseException _e)
		{
			return false;
		}
		return true;
	}
	
	@Override
	public boolean loadQResults(User user, Calendar begin, Calendar end,
			List<Integer> questionIDs, StatisticsContainer container)
	{
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "load_q_results");

		JSONArray questions = new JSONArray();
		List<String> qlist = (List<String>) questions;
		for (Iterator<Integer> itr = questionIDs.iterator(); itr.hasNext();)
			qlist.add(String.format("`question%d`", itr.next()));
		rmap.put("questions", questions.toString());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		rmap.put("begin", sdf.format(begin.getTime()));
		rmap.put("end", sdf.format(end.getTime()));
		
		
		Map<String, String> amap = (Map<String, String>) sendMessage(ret.toString());
		List<String> rlist = (List<String>) getJSONArray(amap.get("results"));
		for (Iterator<String> itr = rlist.iterator(); itr.hasNext();)
		{
			Map<String, String> ansmap = (Map<String, String>) getJSONObject(itr.next());
			QuestionContainer qc = Questions.getQuestions().getContainer();
			for (Entry<String, String> e : ansmap.entrySet())
			{
				int qid = Integer.parseInt(e.getKey().substring("question".length()));
				Question q1 = qc.getQuestion(qid);
				container.addResult(q1, QDBFormat.getQFormat(e.getValue()));
			}
		}
		return true;
	}
	
	/* Protected */
	
	/* Private */

	private static MySQL_Database database;
	private Encryption crypto;
	
	/**
	 * Initializes variables and loads the database configuration.
	 * This class is a singleton and should only be instantiated once.
	 */
	private MySQL_Database()
	{
		crypto = Implementations.Encryption();
	}
	
	private JSONObject sendMessage(String obj)
	{
		/* send message */
		URL url;
		HttpURLConnection connection;
		try {
			url = new URL("http://localhost:8080/PROM_PREM_Collector/main");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			//Send request
			OutputStream os = connection.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
			System.out.println(obj);
			osw.write(obj);
			osw.flush();
			osw.close();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				System.out.println("Ok response");
			} else {
				System.out.println("Bad response");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		/* receive message */
		String ans = null; // receive msg from servlet
		return getJSONObject(ans);
	}
	
	private JSONObject getJSONObject(String str)
	{
		JSONParser parser = new JSONParser();
		JSONObject userobj = null;
		try{
			userobj = (JSONObject) parser.parse(str);
		} catch (org.json.simple.parser.ParseException pe) {
			return null;
		}
		return userobj;
	}
	
	private JSONArray getJSONArray(String str)
	{
		JSONParser parser = new JSONParser();
		JSONArray userarr = null;
		try{
			userarr = (JSONArray) parser.parse(str);
		} catch (org.json.simple.parser.ParseException pe) {
			return null;
		}
		return userarr;
	}

	/**
	 * Retrieves messages from the database and places them in a
	 * MessageContainer.
	 * 
	 * @param commandName The name of the (message) table to retrieve
	 * 		messages from.
	 * @param mc
	 * @return
	 */
	private boolean getMessages(String commandName, MessageContainer mc)
	{
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", commandName);
		
		Map<String, String> amap = (Map<String, String>) sendMessage(ret.toString());
		Map<String, String> mmap = (Map<String, String>) getJSONObject(amap.get(commandName));
		try {
			for (Entry<String, String> e : mmap.entrySet())
			{
				Map<String, String> messagemap = (Map<String, String>) getJSONObject(e.getValue());
				Map<String, String> msgmap = (Map<String, String>) getJSONObject(messagemap.get("message"));
				mc.addMessage(Integer.parseInt(messagemap.get("code")),
						messagemap.get("name"), msgmap);
			}
		}
		catch (NullPointerException _e) {
			return false;
		}
		return true;
	}
	
	/**
	 * This method converts a container type from the String
	 * representation in the database to the appropriate class
	 * representation in java.
	 * 
	 * @param type The type of container as it appears in the database
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
		if (type.equalsIgnoreCase("SingleOption"))
			return SingleOptionContainer.class;
		else if (type.equalsIgnoreCase("MultipleOption"))
			return MultipleOptionContainer.class;
		else if (type.equalsIgnoreCase("Field"))
			return FieldContainer.class;
		else if (type.equalsIgnoreCase("Slider"))
			return SliderContainer.class;
		else if (type.equalsIgnoreCase("Area"))
			return AreaContainer.class;
		else
			return null;
	}
	
	/**
	 * This class handles converting question answer formats
	 * between its database representation and its java
	 * representation.
	 * 
	 * @author Marcus Malmquist
	 *
	 */
	private static class QDBFormat
	{
		/**
		 * Converts the answer stored in {@code fc} to the
		 * format used in the database.
		 * 
		 * @param fc The container for the question which have
		 * 		been answered and should have the answer stored
		 * 		in the database.
		 * 
		 * @return The database representation for the answer
		 * 		in {@code fc}.
		 */
		static String getDBFormat(FormContainer fc)
		{
			if (fc.getEntry() == null)
				return "''";
			
			if (fc instanceof SingleOptionContainer)
			{
				SingleOptionContainer soc = (SingleOptionContainer) fc;
				return String.format("'option%d'", soc.getEntry());
			}
			else if (fc instanceof MultipleOptionContainer)
			{
				MultipleOptionContainer moc = (MultipleOptionContainer) fc;
				List<String> lstr = new ArrayList<String>();
				List<Integer> lint = new ArrayList<Integer>(moc.getEntry());
				Collections.sort(lint);
				for (Iterator<Integer> itr = lint.iterator(); itr.hasNext();)
					lstr.add(String.format("option%d", itr.next()));
				return String.format("[%s]", String.join(",", lstr));
			}
			else if (fc instanceof SliderContainer)
			{
				SliderContainer sc = (SliderContainer) fc;
				return String.format("'slider%d'", sc.getEntry());
			}
			else if (fc instanceof AreaContainer)
			{
				AreaContainer ac = (AreaContainer) fc;
				return String.format("'%s'", ac.getEntry());
			}
			else
				return "''";
		}
		
		/**
		 * Converts the answer {@code dbEntry} from its database
		 * representation to its java representation. The return
		 * type is {@code Object} to keep the formats general. The
		 * returned objects are in the format they need to be in
		 * order to represent the answer in its java format.
		 * 
		 * @param dbEntry The database entry that is to be converted
		 * 		to a java entry.
		 * 
 		 * @return The {@code Object} representation of the answer.
		 */
		static Object getQFormat(String dbEntry)
		{
			if (dbEntry == null || dbEntry.trim().isEmpty())
				return null;
			
			if (dbEntry.startsWith("option"))
			{ /* single option */
				return new Integer(dbEntry.substring("option".length()));
			}
			else if (dbEntry.startsWith("slider"))
			{ /* slider */
				return new Integer(dbEntry.substring("slider".length()));
			}
			else if (dbEntry.startsWith("[") && dbEntry.endsWith("]"))
			{ /* multiple answers */
				List<String> entries = Arrays.asList(dbEntry.split(","));
				if (entries.get(0).startsWith("option"))
				{ /* multiple option */
					List<Integer> lint = new ArrayList<Integer>();
					for (Iterator<String> itr = entries.iterator(); itr.hasNext();)
						lint.add(new Integer(itr.next().substring("option".length())));
					return lint;
				}
			}
			else
			{ /* must be plain text entry */
				return dbEntry;
			}
			return null;
		}
	}
}