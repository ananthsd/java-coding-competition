package com.codingcompetition.statefarm;

import com.codingcompetition.statefarm.utility.PlaceFetcher;
import com.codingcompetition.statefarm.utility.PlaceParser;
import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapView;

public class ViewController {
    public static void performBasicSearch(String city, String state, MapView mapView) {
        PlaceFetcher fetcher = new PlaceFetcher();
        PlaceParser parser = new PlaceParser();

        try {
            double[] ll = parser.parse(fetcher.fetch(city, state));
            mapView.setCenter(new Coordinate(ll[0], ll[1]));
        } catch (Exception e) {
            System.out.println("Invalid city / state");
        }
    }

    public static void performAdvancedSearch(double latitude, double longitude, MapView mapView) {
        try {
            mapView.setCenter(new Coordinate(latitude, longitude));
        } catch (Exception e) {
            System.out.println("Invalid latitude/longitude");
        }
    }

}
