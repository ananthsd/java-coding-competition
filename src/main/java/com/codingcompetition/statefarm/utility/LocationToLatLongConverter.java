package com.codingcompetition.statefarm.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class LocationToLatLongConverter {
    public void convert(String location) throws Exception {
        URL url = new URL("https://nominatim.openstreetmap.org/search?format=xml&country=USA&state=georgia&city=" + location);
        URLConnection urlConnection = url.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        urlConnection.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }
        in.close();
    }

    public static void main(String[] args) throws Exception {
        try {
            LocationToLatLongConverter converter = new LocationToLatLongConverter();
            converter.convert("atlanta");
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
