package se.nordicehealth.ppc_app.implementation.io;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

class PacketData
{
    PacketData()
    {
        parser = new JSONParser();
    }

    MapData getMapData(String str)
	{
		JSONObject obj;
		try {
			obj = (JSONObject) parser.parse(str);
		} catch (org.json.simple.parser.ParseException | NullPointerException e) {
			obj = null;
		}
		return new MapData(obj);
	}

    ListData getListData(String str)
	{
		JSONArray arr;
		try {
			arr = (JSONArray) parser.parse(str);
		} catch (org.json.simple.parser.ParseException | NullPointerException e) {
			arr = null;
        }
        return new ListData(arr);
	}

    private JSONParser parser;
}