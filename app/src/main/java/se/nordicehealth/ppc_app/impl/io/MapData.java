package se.nordicehealth.ppc_app.impl.io;

import org.json.simple.JSONObject;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

class MapData
{
    private JSONObject jobj;
    private Map<String, String> jmap;

    @SuppressWarnings("unchecked")
    MapData(JSONObject jobj)
    {
        this.jobj = jobj != null ? jobj : new JSONObject();
        this.jmap = (Map<String, String>) this.jobj;
    }

    public void put(String k, String v) { jmap.put(k, v); }
    public String get(String k) { return jmap.get(k); }

    Iterable<Entry<String, String>> iterable()
    {
        return jmap.entrySet();
    }

    Map<String, String> map()
    {
        return Collections.unmodifiableMap(jmap);
    }

    public String toString()
    {
        return jobj.toString();
    }
}