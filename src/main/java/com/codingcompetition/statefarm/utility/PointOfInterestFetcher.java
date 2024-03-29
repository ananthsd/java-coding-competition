package com.codingcompetition.statefarm.utility;

import java.io.*;
import java.net.*;

public class PointOfInterestFetcher {

    private String pointOfInterestQuery =  "https://api.openstreetmap.org/api/0.6/map?bbox=%f,%f,%f,%f";

    public void fetchXML(double latitude, double longitude, double boxSize) throws Exception {

        String outFilePathName = String.format("cache/poi/%f-%f-%f.xml", latitude, longitude, boxSize);
        File file = new File(outFilePathName);

        if (!file.exists() || file.isDirectory()) {
            double edgeDistance = boxSize / 2;
            URL url = new URL(String.format(pointOfInterestQuery,
                    longitude - edgeDistance,
                    latitude - edgeDistance,
                    longitude + edgeDistance,
                    latitude + edgeDistance));

            URLConnection urlConnection = url.openConnection();

            FileWriter writer = new FileWriter(file, false);
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

    public static void main(String[] args) {
        PointOfInterestFetcher request = new PointOfInterestFetcher();
        try {
            request.fetchXML(33.7488889, -84.3880556, 0.01);

            PointOfInterestParser parse = new PointOfInterestParser();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
