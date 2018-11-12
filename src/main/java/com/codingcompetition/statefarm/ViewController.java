package com.codingcompetition.statefarm;

import com.codingcompetition.statefarm.model.PointOfInterest;
import com.codingcompetition.statefarm.utility.PlaceFetcher;
import com.codingcompetition.statefarm.utility.PlaceParser;
import com.codingcompetition.statefarm.utility.PointOfInterestFetcher;
import com.codingcompetition.statefarm.utility.PointOfInterestParser;
import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;

import java.util.List;

public class ViewController {

    public static final double ZOOM = 12D;

    public static void performBasicSearch(String city, String state, MapView mapView) {
        PlaceFetcher fetcher = new PlaceFetcher();
        PlaceParser parser = new PlaceParser();

        try {
            double[] ll = parser.parse(fetcher.fetch(city, state));
            mapView.setCenter(new Coordinate(ll[0], ll[1]));
            mapView.setZoom(ZOOM);
            placeMarkersAtPointsOfInterest(ll[0], ll[1], mapView);
        } catch (Exception e) {
            System.out.println("Invalid city / state");
        }
    }

    public static void performAdvancedSearch(double latitude, double longitude, MapView mapView) {
        try {
            mapView.setCenter(new Coordinate(latitude, longitude));
            mapView.setZoom(ZOOM);
        } catch (Exception e) {
            System.out.println("Invalid latitude/longitude");
        }
    }

    public static void placeMarkersAtPointsOfInterest(double latitude, double longitude, MapView mapView) {
        PointOfInterestFetcher fetcher = new PointOfInterestFetcher();
        PointOfInterestParser parser = new PointOfInterestParser();

        try {
            fetcher.fetchXML(latitude, longitude, 0.01);
            List<PointOfInterest> points = parser.parseFile("/poi.xml");
            for (PointOfInterest point: points) {
                double pLat = Double.parseDouble(point.getLatitude());
                double pLong = Double.parseDouble(point.getLongitude());
                Marker m = Marker.createProvided(Marker.Provided.RED)
                        .setPosition(new Coordinate(pLat, pLong))
                        .setVisible(true);
                mapView.addMarker(m);

            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
