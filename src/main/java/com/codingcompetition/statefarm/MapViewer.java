package com.codingcompetition.statefarm;

import com.codingcompetition.statefarm.model.PointOfInterest;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.OsmTileLoader;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MapViewer extends JFrame implements JMapViewerEventListener {
    private static final long serialVersionUID = 1L;

    private JMapViewer treeMap;

    private JLabel creditsLabel;
    private StreetMapDataInterpreter smallInterpreter, largeInterpreter, currentInterpreter;
    private java.util.List<PointOfInterest> smallInitPoints, largeInitPoints;

    public MapViewer() {
        super("Map");
        treeMap = new JMapViewer();
        init();
        map().addJMVListener(this);

        // Set some options, e.g. tile source and that markers are visible
        map().setTileSource(new OsmTileSource.Mapnik());
        map().setTileLoader(new OsmTileLoader(map()));
        map().setMapMarkerVisible(true);
        map().setZoomContolsVisible(true);

        add(treeMap, BorderLayout.CENTER);
    }

    // ... further methods like setupJFrame() or setupPanels()

    private JMapViewer map() {
        return treeMap;
    }


    /**
     * @param args Main program arguments
     */
    public static void main(String[] args) {
        new MapViewer().setVisible(true);
    }


    @Override
    public void processCommand(JMVCommandEvent command) {
    }

    public void init() {
        setSize(400, 400);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelTop = new JPanel();
        JPanel panelBottom = new JPanel();
        JPanel helpPanel = new JPanel();

        creditsLabel = new JLabel("Data provided by Open Street Map.\nViewer courtesy of JMapViewer.");

        add(panel, BorderLayout.NORTH);
        add(helpPanel, BorderLayout.SOUTH);
        panel.add(panelTop, BorderLayout.NORTH);
        panel.add(panelBottom, BorderLayout.SOUTH);
        JLabel helpLabel = new JLabel("Use right mouse button to move,\n "
                + "left double click or mouse wheel to zoom.");
        helpPanel.add(helpLabel);

        panelTop.add(creditsLabel);
        smallInterpreter = new StreetMapDataInterpreter("/small-metro.xml");
        largeInterpreter = new StreetMapDataInterpreter("/large-metro.xml");
        currentInterpreter = smallInterpreter;
        smallInitPoints = smallInterpreter.interpret();
        largeInitPoints = largeInterpreter.interpret();
        for (PointOfInterest po : smallInitPoints) {
            map().addMapMarker(new MapMarkerDot(Double.parseDouble(po.getLatitude()), Double.parseDouble(po.getLongitude())));
        }
        JButton button = new JButton("Zoom in to the points.");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                map().setDisplayToFitMapMarkers();
            }
        });
        String[] maps = {"Small Map", "Large Map"};
        JComboBox mapSelector = new JComboBox(maps);
        mapSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JComboBox cb = (JComboBox) actionEvent.getSource();
                int index = cb.getSelectedIndex();
                if (index == 0) {
                    currentInterpreter = smallInterpreter;
                    map().removeAllMapMarkers();
                    for (PointOfInterest po : smallInitPoints) {
                        map().addMapMarker(new MapMarkerDot(Double.parseDouble(po.getLatitude()), Double.parseDouble(po.getLongitude())));
                    }
                    map().setDisplayToFitMapMarkers();
                } else {
                    currentInterpreter = largeInterpreter;
                    map().removeAllMapMarkers();
                    for (PointOfInterest po : largeInitPoints) {
                        map().addMapMarker(new MapMarkerDot(Double.parseDouble(po.getLatitude()), Double.parseDouble(po.getLongitude())));
                    }
                    map().setDisplayToFitMapMarkers();
                }
            }
        });
        panelTop.add(mapSelector);
        /*JLabel search = new JLabel("Search:");
        panelBottom.add(search);
        String[] categories = {"LEISURE", "NAME", "AMENITY", "CUISINE", "SHOP", "WHEELCHAIR", "HIGHWAY", "PLACE", "POPULATION", "POWER", "BUILDING", "BEAUTY"};
        JComboBox catSelector = new JComboBox(categories);
        panelBottom.add(catSelector);
        JTextField searchField = new JTextField(20);
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String text = ((JTextField) actionEvent.getSource()).getText();
                switch (categories[catSelector.getSelectedIndex()]) {
                    case "LEISURE":
                        search(Category.LEISURE,text);
                        break;
                    case "NAME":
                        search(Category.NAME,text);
                        break;
                    case "AMENITY":
                        search(Category.AMENITY,text);
                        break;
                    case "CUISINE":
                        search(Category.CUISINE,text);
                        break;
                    case "SHOP":
                        search(Category.SHOP,text);
                        break;
                    case "WHEELCHAIR":
                        search(Category.WHEELCHAIR,text);
                        break;
                    case "HIGHWAY":
                        search(Category.HIGHWAY,text);
                        break;
                    case "PLACE":
                        search(Category.PLACE,text);
                        break;
                    case "POPULATION":
                        search(Category.POPULATION,text);
                        break;
                    case "POWER":
                        search(Category.POWER,text);
                        break;
                    case "BUILDING":
                        search(Category.BUILDING,text);
                        break;
                    case "BEAUTY":
                        search(Category.BEAUTY,text);
                        break;
                }
            }
        });
        panelBottom.add(searchField);*/
        map().setDisplayToFitMapMarkers();
        panel.add(button);
    }
    private void search(Category category,String text){
        map().removeAllMapMarkers();
        for (PointOfInterest po : currentInterpreter.interpret(new SearchCriteria(category, text))) {
            map().addMapMarker(new MapMarkerDot(Double.parseDouble(po.getLatitude()), Double.parseDouble(po.getLongitude())));

        }
        map().setDisplayToFitMapMarkers();
    }
}
