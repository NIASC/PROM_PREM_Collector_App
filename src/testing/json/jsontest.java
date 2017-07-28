package testing.json;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class jsontest
{
	public static void main(String [] args)
	{
		testread(testwrite());
	}
	
	@SuppressWarnings("unchecked")
	public static String testwrite()
	{
		JSONObject obj = new JSONObject();
		Map<String, String> moo = (Map<String, String>) obj;
		
		moo.put("command", "get_user");
		moo.put("id", "1");
		moo.put("type", "SingleOptions");
		moo.put("optional", "0");
		moo.put("question", "What is your name");
		moo.put("description", "Your name");
		moo.put("option0", "foo");
		moo.put("option1", "bar");
		moo.put("option2", null);
		moo.put("min_val", null);
		moo.put("max_val", null);
		
		return obj.toString();
	}
	
	public static void testread(String message)
	{
		JSONParser parser = new JSONParser();
		try{
			JSONObject obj = (JSONObject) parser.parse(message);
			System.out.printf("%s: '%s' should be '%s'\n",
					"command", obj.get("command"), "get_user");
			
			System.out.printf("%s: '%s' should be '%s'\n",
					"id", obj.get("id"), "1");
			
			System.out.printf("%s: '%s' should be '%s'\n",
					"type", obj.get("type"), "SingleOptions");
			
			System.out.printf("%s: '%s' should be '%s'\n",
					"optional", obj.get("optional"), "0");
			
			System.out.printf("%s: '%s' should be '%s'\n",
					"question", obj.get("question"), "What is your name");
			
			System.out.printf("%s: '%s' should be '%s'\n",
					"description", obj.get("description"), "Your name");
			
			System.out.printf("%s: '%s' should be '%s'\n",
					"option0", obj.get("option0"), "foo");
			
			System.out.printf("%s: '%s' should be '%s'\n",
					"option1", obj.get("option1"), "bar");
			
			System.out.printf("%s: '%s' should be '%s'\n",
					"option2", obj.get("option2"), "null");
			
			System.out.printf("%s: '%s' should be '%s'\n",
					"min_val", obj.get("min_val"), "null");
			
			System.out.printf("%s: '%s' should be '%s'\n",
					"max_val", obj.get("max_val"), "null");
		} catch (org.json.simple.parser.ParseException pe) {
			System.out.println("position: " + pe.getPosition());
			System.out.println(pe);
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}
	}

}
