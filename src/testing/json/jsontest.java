package testing.json;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

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
		
		obj.put("command", "get_user");
		obj.put("id", "1");
		obj.put("type", "SingleOptions");
		obj.put("optional", "0");
		obj.put("question", "What is your name");
		obj.put("description", "Your name");
		obj.put("option0", "foo");
		obj.put("option1", "bar");
		obj.put("option2", null);
		obj.put("min_val", null);
		obj.put("max_val", null);
		
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
