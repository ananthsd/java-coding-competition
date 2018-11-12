package com.codingcompetition.statefarm;

import com.codingcompetition.statefarm.model.PointOfInterest;
import com.codingcompetition.statefarm.utility.PlaceFetcher;
import com.codingcompetition.statefarm.utility.PlaceParser;
import com.codingcompetition.statefarm.utility.PointOfInterestFetcher;
import com.codingcompetition.statefarm.utility.PointOfInterestParser;
import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.offline.OfflineCache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ViewController {

    public static final double ZOOM = 12D;

    public static void cacheMapOffiline(MapView mapView) {
        final OfflineCache offlineCache = mapView.getOfflineCache();
        final String cacheDir = "cache/map";
        // logger.info("using dir for cache: " + cacheDir);
        try {
            Files.createDirectories(Paths.get(cacheDir));
            offlineCache.setCacheDirectory(cacheDir);
            offlineCache.setActive(true);
        } catch (IOException e) {
            System.out.println("Cache not created");
        }
    }

    public static void performBasicSearch(String city, String state, MapView mapView) {
        PlaceFetcher fetcher = new PlaceFetcher();
        PlaceParser parser = new PlaceParser();

        try {
            fetcher.fetch(city, state);
            Coordinate place = parser.parse(city, state);
            mapView.setCenter(place);
            mapView.setZoom(ZOOM);
            placeMarkersAtPointsOfInterest(place.getLatitude(), place.getLongitude(), mapView);
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
//            mapView.addMarker(Marker.createProvided(Marker.Provided.RED)
//                    .setPosition(new Coordinate(33.7488889D, -84.3880556D))
//                    .setVisible(true));
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
