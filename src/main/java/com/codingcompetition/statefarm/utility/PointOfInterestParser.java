package com.codingcompetition.statefarm.utility;

import com.codingcompetition.statefarm.model.PointOfInterest;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PointOfInterestParser {


    // Stacks for storing the elements and objects.
    private Stack<String> elements = new Stack<String>();
    private Stack<PointOfInterest> objects = new Stack<PointOfInterest>();
    private String startLat, endLat, startLong, endLong;

    private SAXParserFactory saxParserFactory;

    private FileWriter parseLineDestinationFile;


    public PointOfInterestParser() {
        this.saxParserFactory = SAXParserFactory.newInstance();
    }

    /**
     * Parses XML file and returns list of PointOfInterest
     * @param fileName XML file
     * @return list of points of interest
     * @throws IOException file not found
     * @throws SAXException SAX error
     */
    public List<PointOfInterest> parseFile(String fileName) throws IOException, SAXException, ParserConfigurationException {
        SAXParser saxParser = this.saxParserFactory.newSAXParser();
        PointHandler pointHandler = new PointHandler();

        try {
            saxParser.parse(new File(fileName), pointHandler);
        } catch (IOException io) {
            throw new IOException(io);
        }

        return pointHandler.getPoints();
    }

    /**
     * Must be called before the user starts reading in XML line by line.
     * @throws IOException Destination file was unable to be created.
     */
    public void startParseLine() throws IOException {
        try {
            this.parseLineDestinationFile = new FileWriter(new File("src/main/resources/temp.xml"), false);
        } catch (IOException io) {
            throw new IOException(io);
        }
    }

    /**
     * Reads in a line of XML
     * @param line line of XML
     * @throws IOException Destination file could not be appended to.
     */
    public void parseLine(String line) throws IOException {
        this.parseLineDestinationFile.append(line);

    }

    /**
     * Must be called after the user finishes reading in XML line by line.
     * @return List of Points of Interest
     * @throws IOException Destination file could not be closed.
     * @throws SAXException Parser encounters errpr while parsing.
     * @throws ParserConfigurationException Parser could not be created.
     */
    public List<PointOfInterest> endParseLine() throws IOException, SAXException, ParserConfigurationException {
        this.parseLineDestinationFile.close();
        SAXParser saxParser = this.saxParserFactory.newSAXParser();
        PointHandler pointHandler = new PointHandler();

        try {
            saxParser.parse(new File("src/main/resources/temp.xml"), pointHandler);
        } catch (IOException io) {
            throw new SAXException(io);
        }

        return pointHandler.getPoints();
    }




    //Handler to parseFile data with SAX
    private class PointHandler extends DefaultHandler {
        private List<PointOfInterest> pointsOfInterest;
        private PointOfInterest currentPoint;

        /**
         * Starts at beginning of XML
         * @throws SAXException XML parsing exception
         */
        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            pointsOfInterest = new ArrayList<>();
        }

        /**
         * Beginning of XML element
         * @param uri url
         * @param localName name
         * @param qName qName
         * @param attributes attributes
         * @throws SAXException XML parsing exception
         */
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.toLowerCase().equals("node")) {
                currentPoint = new PointOfInterest();
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (attributes.getQName(i).equals("lat")) {
                        currentPoint.setLatitude(attributes.getValue(i));
                    } else if (attributes.getQName(i).equals("lon")) {
                        currentPoint.setLongitude(attributes.getValue(i));
                    } else {
                        currentPoint.addDescriptor(attributes.getQName(i), attributes.getValue(i));
                    }
                }
            }
            if (qName.toLowerCase().equals("tag")) {
                currentPoint.addDescriptor(attributes.getValue(0), attributes.getValue(1));
            }
            if (qName.toLowerCase().equals("bounds")) {
                //minlat="40.4040000" minlon="-89.1067000" maxlat="40.5439000" maxlon="-88.8372000"
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (attributes.getQName(i).equals("minlat")) {
                        startLat = attributes.getValue(i);
                    } else if (attributes.getQName(i).equals("minlon")) {
                        startLong = attributes.getValue(i);
                    } else if (attributes.getQName(i).equals("maxlat")) {
                        endLat = attributes.getValue(i);
                    } else if (attributes.getQName(i).equals("maxlon")) {
                        endLong = attributes.getValue(i);
                    }
                    //System.out.println(attributes.getValue(i));
                }

            }
        }

        /**
         * End of XML element
         * @param uri url
         * @param localName localName
         * @param qName qName
         * @throws SAXException XML parsing exception
         */
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.toLowerCase().equals("node")) {
                pointsOfInterest.add(currentPoint);
            }
        }

        /**
         * Getter for pointsOfInterest
         * @return pointsOfInterest
         */
        public List<PointOfInterest> getPoints() {
            return pointsOfInterest;
        }
    }

    public String getStartLat() {
        return startLat;
    }

    public String getEndLat() {
        return endLat;
    }

    public String getStartLong() {
        return startLong;
    }

    public String getEndLong() {
        return endLong;
    }
}
