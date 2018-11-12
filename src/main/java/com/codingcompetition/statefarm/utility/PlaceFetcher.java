package com.codingcompetition.statefarm.utility;

import com.sothawo.mapjfx.Coordinate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class PlaceFetcher {
    public void fetch(String city, String state) throws Exception {
        city = city.toLowerCase().replace(' ', '-');
        state = state.toLowerCase();
        File file = new File(String.format("cache/places/%s-%s.xml", city, state));
        if (!file.exists() || file.isDirectory()) {

            String f = String.format("https://nominatim.openstreetmap.org/search?format=xml&country=USA&state=%s&city=%s", state, city);
            URL url = new URL(f);
            URLConnection urlConnection = url.openConnection();

            FileWriter writer = new FileWriter(file);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            urlConnection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                writer.append(inputLine);
            }
            in.close();
            writer.close();
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            PlaceFetcher fetcher = new PlaceFetcher();
            PlaceParser parser = new PlaceParser();

            fetcher.fetch("san jose", "california");
            Coordinate sj = parser.parse("san jose", "california");
            System.out.println("Latitude " + sj.getLatitude());
            System.out.println("Longitude " + sj.getLongitude());
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
