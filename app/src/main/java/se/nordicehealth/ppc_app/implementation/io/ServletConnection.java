package se.nordicehealth.ppc_app.implementation.io;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class ServletConnection
{
    ServletConnection(URL servletURL)
    {
        this.servletURL = servletURL;
        msgOut = new Packet();
        msgIn = new Packet();
    }

    String sendMessage(String message)
    {
        Thread t = new Thread(new IOHandler());
        msgOut.message = message;
        t.start();
        try { t.join(); } catch (InterruptedException ignored) { }
        return msgIn.message;
    }

    private volatile Packet msgOut, msgIn;
    private final URL servletURL;

	private void sendPacket(OutputStream os, String pktOut) throws IOException
    {
        OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
        osw.write(pktOut);
        osw.flush();
    }

    private String receivePacket(InputStream is) throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();

        for (String inputLine; (inputLine = in.readLine()) != null;)
            sb.append(inputLine);

        return sb.toString();
    }

    private HttpURLConnection openConnection(URL url) throws IOException
    {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        return connection;
    }

    private class IOHandler implements Runnable
    {
        @Override
        public void run() {
            HttpURLConnection connection;
            try {
                connection = openConnection(servletURL);
            } catch (IOException e) {
                Log.e("ECONN", e.getMessage());
                return;
            }
            synchronized (msgOut) {
                Log.i("MSGOUT", msgOut.message != null ? msgOut.message : "null");
                try (OutputStream os = connection.getOutputStream()) {
                    sendPacket(os, msgOut.message);
                } catch (IOException pe) {
                    Log.e("EOUT", pe.getMessage());
                    return;
                }
            }
            synchronized (msgIn) {
                msgIn.message = null;
                try (InputStream is = connection.getInputStream()) {
                    msgIn.message = receivePacket(is);
                } catch (IOException pe) {
                    Log.e("EIN", pe.getMessage());
                }
                Log.i("MSGIN", msgIn.message != null ? msgIn.message : "null");
            }
        }
    }

    private class Packet
    {
        String message;
    }
}