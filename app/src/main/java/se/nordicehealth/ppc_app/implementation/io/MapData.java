package se.nordicehealth.ppc_app.implementation.io;

import org.json.simple.JSONObject;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

public class MapData
{
    private JSONObject jobj;
    private Map<String, String> jmap;

    @SuppressWarnings("unchecked")
    public MapData(JSONObject jobj)
    {
        this.jobj = jobj != null ? jobj : new JSONObject();
        this.jmap = (Map<String, String>) this.jobj;
    }

    public void put(String key, String value)
    {
        jmap.put(key, value);
    }

    public String get(String key)
    {
        return jmap.get(key);
    }

    public Iterable<Entry<String, String>> iterable()
    {
        return jmap.entrySet();
    }

    public Map<String, String> map()
    {
        return Collections.unmodifiableMap(jmap);
    }

    public String toString()
    {
        return jobj.toString();
    }
}