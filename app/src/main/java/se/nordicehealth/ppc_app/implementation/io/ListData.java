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

import org.json.simple.JSONArray;

import java.util.Collections;
import java.util.List;

class ListData
{
    private JSONArray jarr;
    private List<String> jlist;

    @SuppressWarnings("unchecked")
    ListData(JSONArray jarr)
    {
        this.jarr = jarr != null ? jarr : new JSONArray();
        this.jlist = (List<String>) this.jarr;
    }

    void add(String value)
    {
        jlist.add(value);
    }

    Iterable<String> iterable()
    {
        return Collections.unmodifiableList(jlist);
    }

    public String toString()
    {
        return jarr.toString();
    }
}