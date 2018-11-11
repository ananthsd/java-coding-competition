package com.codingcompetition.statefarm.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class PlaceFetcher {
    public String fetch(String city, String state) throws Exception {
        city = city.toLowerCase().replace(' ', '-');
        state = state.toLowerCase();
        String f = String.format("https://nominatim.openstreetmap.org/search?format=xml&country=USA&state=%s&city=%s", state, city);
        URL url = new URL(f);
        URLConnection urlConnection = url.openConnection();
        StringBuilder dest = new StringBuilder();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        urlConnection.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            dest.append(inputLine);
        }
        in.close();

        return dest.toString();
    }

    public static void main(String[] args) throws Exception {
        try {
            PlaceFetcher fetcher = new PlaceFetcher();
            PlaceParser parser = new PlaceParser();

            String xml = fetcher.fetch("san jose", "california");
            System.out.println(xml);
            double[] ll = parser.parse(xml);
            System.out.println("Latitude " + ll[0]);
            System.out.println("Longitude " + ll[1]);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
