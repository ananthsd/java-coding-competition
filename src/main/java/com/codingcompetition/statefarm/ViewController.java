package com.codingcompetition.statefarm;

import com.codingcompetition.statefarm.model.PointOfInterest;
import com.codingcompetition.statefarm.utility.*;
import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.offline.OfflineCache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class ViewController {

    public static final double ZOOM = 12D;
    public static final double BOX_SIZE = 0.006;

    public static List<Marker> markers;
    // public static List<Marker.Provided> colors;
    public static List<PointOfInterest> points;
    // public static List<PointOfInterest> highways;

    public static void cacheMapOffline(MapView mapView) {
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
            fetchPointsOfInterest(place);
            plotHighways(mapView);
            CrashParser crashParser = new CrashParser();
            System.out.println("first " + markers.size());
            for (Marker m: crashParser.parse()) {
                markers.add(m);
                mapView.addMarker(m);
                //System.out.println("Addeeee ");
            }
            System.out.println("Before " + markers.size());
            System.out.println("after " + markers.size());
        } catch (Exception e) {
            System.out.println("Invalid city / state");
        }
    }

    public static void performAdvancedSearch(double latitude, double longitude, MapView mapView) {
        try {
            Coordinate place = new Coordinate(latitude, longitude);
            mapView.setCenter(place);
            mapView.setZoom(ZOOM);
            fetchPointsOfInterest(place);
            plotHighways(mapView);
            CrashParser crashParser = new CrashParser();
            System.out.println("first " + markers.size());
            for (Marker m: crashParser.parse()) {
                markers.add(m);
                mapView.addMarker(m);
                //System.out.println("Addeeee ");
            }
        } catch (Exception e) {
            System.out.println("Invalid latitude/longitude");
        }
    }

    public static void fetchPointsOfInterest(Coordinate location) {
        markers = new LinkedList<>();
        // colors = new LinkedList<>();
        PointOfInterestFetcher fetcher = new PointOfInterestFetcher();
        PointOfInterestParser parser = new PointOfInterestParser();

        try {

            fetcher.fetchXML(location.getLatitude(), location.getLongitude(), BOX_SIZE);
            points = parser.parseFile(String.format("cache/poi/%f-%f-%f.xml", location.getLatitude(),
                    location.getLongitude(), BOX_SIZE));


        } catch (Exception e) {
            System.out.println(e);
        }
    }

//    public static void plotPoints(MapView mapView, List<PointOfInterest> pointsToPlot) {
//        markers = new LinkedList<>();
//        for (int i = 0; i < pointsToPlot.size(); i++) {
//            double pLat = Double.parseDouble(pointsToPlot.get(i).getLatitude());
//            double pLong = Double.parseDouble(pointsToPlot.get(i).getLongitude());
//            Marker marker = Marker.createProvided(colors.get(i))
//                    .setPosition(new Coordinate(pLat, pLong))
//                    .setVisible(true);
//            markers.add(marker);
//            mapView.addMarker(marker);
//        }
//    }

    public static void plotPoint(MapView mapView, PointOfInterest point, Marker.Provided color) {
            double pLat = Double.parseDouble(point.getLatitude());
            double pLong = Double.parseDouble(point.getLongitude());
            Marker marker = Marker.createProvided(color)
                    .setPosition(new Coordinate(pLat, pLong))
                    .setVisible(true);
            markers.add(marker);
            mapView.addMarker(marker);
    }

//    public static void plotAllPointsOfInterest(MapView mapView) {
//        plotPoints(mapView, points);
//    }

    public static void plotHighways(MapView mapView) {
        // markers = new LinkedList<>();
        // highways = new LinkedList<>();
        // colors = new LinkedList<>();

        for (PointOfInterest point: points) {
            if (point.getDescriptors().getOrDefault("name", "").contains("US")) {
                // highways.add(point);
                plotPoint(mapView, point, Marker.Provided.ORANGE);
            } else if (point.getDescriptors().get("highway") != null) {
                //highways.add(point);
                //colors.add(Marker.Provided.BLUE);
                plotPoint(mapView, point, Marker.Provided.BLUE);
            }

        }


        // plotPoints(mapView, highways);
    }

    public static void refreshMarkers(MapView mapView) {
        for (Marker marker: markers) {
            mapView.removeMarker(marker);
            mapView.addMarker(marker);
        }

        System.out.println(points.size());
        System.out.println("Refresh " + markers.size());
    }


}
