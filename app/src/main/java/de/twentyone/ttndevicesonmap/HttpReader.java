package de.twentyone.ttndevicesonmap;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpReader extends Thread {

    String credentials;
    String uri = "twentyone.de";
    public String result = "";

    public HttpReader(String uri, String credentials ){this.uri = uri;this.credentials = credentials;}

    public void run() {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("User-Agent", "de.twentyone");
            urlConnection.setRequestProperty("Referer", "http://twentyone.de");
            if (credentials.length()>0) urlConnection.setRequestProperty("Authorization", credentials);
            int responseCode = urlConnection.getResponseCode();
            if (responseCode==HttpURLConnection.HTTP_OK) {
                urlConnection.connect();
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) sb.append(line + "\n");
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = sb.toString();
    }
}

