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
package se.nordicehealth.ppc_app.implementation;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import se.nordicehealth.ppc_app.common.implementation.Constants;

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
class _ServletCommunication implements Runnable
{
	/* Public */

    @Override
    public void run()
    {
        while (true) {
            // wait here
            synchronized (this) {
                try {
                    sendPacket(connection.getOutputStream(), msgOut);
                    msgIn = receivePacket(connection.getInputStream());
                } catch (IOException pe) {
                    msgIn = null;
                }
                Log.i("MSGIN", msgIn);
            }
            // notify here
        }
    }

	/* Protected */

	/* Package */

    _ServletCommunication(URL servletURL) throws IOException
    {
        connection = (HttpURLConnection) servletURL.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
    }

    String sendMessage(String message)
    {
        // set message
        // notify here
        // wait here
        // return message

        Thread t = new Thread(this);
        msgOut = message;
        t.start();
        try { t.join(); } catch (InterruptedException ignored) { }
        return msgIn;
    }

	/* Private */

    private volatile String msgOut, msgIn;
	private volatile HttpURLConnection connection;

	private void sendPacket(OutputStream os, String pktOut) throws IOException
    {
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        Log.i("MSGOUT", pktOut);
        synchronized (this) {
            osw.write(pktOut);
        }
        osw.flush();
    }

    private String receivePacket(InputStream is) throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();

        for (String inputLine; (inputLine = in.readLine()) != null; sb.append(inputLine));

        return sb.toString();
    }
}