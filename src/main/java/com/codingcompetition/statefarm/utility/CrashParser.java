package com.codingcompetition.statefarm.utility;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.Marker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

public class CrashParser {
    private final int maxMarkers = 10000;
    public List<Marker> parse() {
        List<Marker> markers = new LinkedList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/Crash_Data_Location.csv"));
            int numberOfMarkers = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (numberOfMarkers >= maxMarkers) {
                    break;
                }
                try {
                    if(!line.substring(0,1).equals("X")){
                        int comma1 = line.indexOf(",");
                        double longitude = Double.parseDouble(line.substring(0, comma1));
                        int comma2 = line.indexOf(",", comma1 + 1);
                        double latitude = Double.parseDouble(line.substring(comma1 + 1, comma2));
                        Marker marker = Marker.createProvided(Marker.Provided.RED)
                                .setPosition(new Coordinate(latitude, longitude))
                                .setVisible(true);
                        markers.add(marker);

                        numberOfMarkers++;
                    }


                } catch (Exception e) {

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("markers " + markers.size());
        return markers;
    }

    public static void main(String[] args) {
       CrashParser crashParser = new CrashParser();
       List<Marker> m = crashParser.parse();
        System.out.println(m.size());
    }
}
